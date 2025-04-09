package com.bubusyaka.demo.configuration;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class MinioConfig {

    @Value("${minio.access.name}")
    String accessKey;
    @Value("${minio.access.secret}")
    String accessSecret;
    @Value("${minio.url}")
    String minioUrl;

//    @Bean
//    public MinioClient minioClient() {
//        MinioClient minioClient = MinioClient.builder().credentials(accessKey, secretKey)
//                .endpoint(minioUrl, 9000, minioSecure).build();
//        return minioClient;
//    }

//    @Bean
//    public MinioClient minioClient() {
//        try {
//            MinioClient client = new MinioClient(minioUrl, accessKey, accessSecret);
//            return client;
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage());
//        }
//
//    }

    @Bean
    public MinioClient generateMinioClient() {
        try {

            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.MINUTES)
                    .writeTimeout(10, TimeUnit.MINUTES)
                    .readTimeout(30, TimeUnit.MINUTES)
                    .build();

            MinioClient client = MinioClient.builder()
                    .endpoint(minioUrl)
                    .httpClient(httpClient)
                    .credentials(accessKey, accessSecret)
                    .build();
            return client;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

}
