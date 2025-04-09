package com.bubusyaka.demo.service;

import com.bubusyaka.demo.model.entity.NewOrderEntity;
import com.bubusyaka.demo.repository.jpa.NewOrderRepository;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewOrdersService {
    private final NewOrderRepository newOrderRepository;
    private String isProcedureDone;
    private final MinioClient minioClient;
    private final MinioAdapter minioAdapter;

    @Value("${force.run.procedure}")
    Boolean forceRunProcedure;

    @Transactional
    //@PostConstruct
    public void callNewOrdersProcedure() throws IOException {

        String response = minioAdapter.getFile("isProcedureDone");

        if (forceRunProcedure || response == null || response.equals("false")) {
            newOrderRepository.callInsertNonCompletedOrdersProcedure();
            response = "true";

            BufferedWriter writer = new BufferedWriter(new FileWriter("isProcedureDone"));
            writer.write(response);
            writer.close();

            minioAdapter.uploadFile("isProcedureDone", response);
        }
        else {
            log.info("This procedure is already done");
        }

    };
}
