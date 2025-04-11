package com.bubusyaka.demo.controller;

import com.bubusyaka.demo.model.dto.CompositeDTO;
import com.bubusyaka.demo.service.DataSetOfUsersAndItemsService;
import com.bubusyaka.demo.service.GigaChatDialog;
import com.bubusyaka.demo.service.TestServiceGrpc;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/recommend")
@RequiredArgsConstructor
@Slf4j
public class UserItemsInformationController {
    private final TestServiceGrpc testServiceGrpc;
    private final DataSetOfUsersAndItemsService dataSetOfUsersAndItemsService;
    private final GigaChatDialog gigaChatDialog;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    public String processRequest(Long id) throws Exception {
        String messagePart = "Порекомендуй пользователю с введенным идентификатором TOP-3 категории товаров в формате Json, которые он еще не заказывал, но был бы заинтересован или товары, которые заказывал чаще всего, на основе его наиболее частых покупок или на основе сходства этого пользователя с другими. Напиши в ответе только список рекомендованных тобой категорий товаров в формате Json,(пример: Categories: category1, category2,...). Не пиши в ответе ничего кроме данных в Json.";
        String messagePart2 = "Идентификатор пользователя=" + id.toString() + " . Датасет представлен далее, в нем id-идентификатор заказа, itId-идентификатор заказанного товара, n-название товара, uId-идентификатор пользователя, pC-категория товара, aC-возрастная категория товара, uA-возраст пользователя. Датасет: ";
        List<CompositeDTO> orders = dataSetOfUsersAndItemsService.getItemsInfoOrderedByUsers();
        String dataSetPart = dataSetOfUsersAndItemsService.convertToCustomString(orders);
        String message = messagePart + messagePart2 + dataSetPart;
        return gigaChatDialog.getResponse(message);
    }

    @GetMapping("/items")
    public ResponseEntity<String> recommendedItems(@RequestParam Long id) {
        CompletableFuture.runAsync(() -> {
            try {
                log.info("Processed request: {}", processRequest(id));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, executor);

        return ResponseEntity.ok().body("Request received");
    }

    @GetMapping("/process-user-request")
    public String processUserRequest(@RequestBody Long id) {
        testServiceGrpc.sendMessage(id);
        return "Запрос успешно обработан." ;
    }
}
