package com.bubusyaka.demo.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.messages.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MinioAdapter {

    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    String defaultBucketName;

    @Value("${minio.default.folder}")
    String defaultBaseFolder;

    public List<Bucket> getAllBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //public void uploadFile(String name, byte[] content) {
    public void uploadFile(String name, String contentInString) {
        byte[] content = contentInString.getBytes(Charset.forName("UTF-8"));

        File file = new File("/tmp/" + name);
        file.canWrite();
        file.canRead();
        try {
            FileOutputStream iofs = new FileOutputStream(file);
            iofs.write(content);

            UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                    .bucket(defaultBucketName)
                    .object(defaultBaseFolder + name)
                    .filename(file.getAbsolutePath())
                    .build();

            minioClient.uploadObject(uploadObjectArgs);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //public byte[] getFile(String name) {
    public String getFile(String name) {
        try {
            //InputStream obj = minioClient.getObject(defaultBucketName, defaultBaseFolder + "/" + key);
            InputStream obj = minioClient.getObject(GetObjectArgs.builder().bucket(defaultBucketName).object(name).build());
            byte[] content = IOUtils.toByteArray(obj);
            obj.close();
            String newContent = new String(content, StandardCharsets.UTF_8);
            log.info("newContent: " + newContent);
            return newContent;
            //return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
