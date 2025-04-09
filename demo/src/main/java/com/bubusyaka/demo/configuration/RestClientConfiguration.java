package com.bubusyaka.demo.configuration;

import okhttp3.OkHttpClient;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.http.HttpHost;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

@Configuration
public class RestClientConfiguration {

    @Value("${CERT_PATH}")
    private String certificatePath;

    @Bean
    public RestClient restClient() {
        return RestClient.builder().build();
    }

    @Bean
    public RestClient secureRestClient() throws Exception {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(30000);
        factory.setConnectionRequestTimeout(30000);
        factory.setHttpClient(httpClient());
        return RestClient.builder().requestFactory(factory).build();
    }

    public HttpClient httpClient() throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        FileInputStream certInput = new FileInputStream(certificatePath);
        X509Certificate caCert = (X509Certificate) cf.generateCertificate(certInput);

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        keyStore.setCertificateEntry("caCert", caCert);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        SSLContext context = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = new TrustManager[]{new NullTrustManager()};
        context.init(null, trustManagers, null);

        SSLConnectionSocketFactory connectionSocketFactory = SSLConnectionSocketFactoryBuilder.create()
                .setSslContext(context)
                .setHostnameVerifier(new NullHostnameVerifier())
                .build();
        HttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(connectionSocketFactory)
                .build();
        return HttpClients.custom()
                //.setProxy(new HttpHost("localhost", 9008))
                .setConnectionManager(connectionManager)
                .build();
    }

    public static class NullTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
    public static class NullHostnameVerifier implements javax.net.ssl.HostnameVerifier {
        public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
            return true;
        }
    }
}
