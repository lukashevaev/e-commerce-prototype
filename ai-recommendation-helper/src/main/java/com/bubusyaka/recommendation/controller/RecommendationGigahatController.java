package com.bubusyaka.recommendation.controller;

import com.bubusyaka.recommendation.model.dto.CompositeDTO;
import com.bubusyaka.recommendation.service.FullInformationAboutOrdersService;
import com.bubusyaka.recommendation.service.GigaChat.GigaChatDialog;
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
@RequestMapping("/recommendation")
@RequiredArgsConstructor
@Slf4j
public class RecommendationGigahatController {
    private final FullInformationAboutOrdersService fullInformationAboutOrdersService;
    private final GigaChatDialog gigaChatDialog;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public String processRequest(Long id) throws Exception {
        String messagePart1 = "Порекомендуй пользователю с введенным идентификатором TOP-3 категории товаров в формате Json, которые он еще не заказывал, но был бы заинтересован или товары, которые заказывал чаще всего, на основе его наиболее частых покупок или на основе сходства этого пользователя с другими. Напиши в ответе только список рекомендованных тобой категорий товаров в формате Json,(пример: Categories: category1, category2,...). Не пиши в ответе ничего кроме данных в Json.";
        String messagePart2 = "Идентификатор пользователя=" + id.toString() + " . Датасет представлен далее, в нем id-идентификатор заказа, itId-идентификатор заказанного товара, n-название товара, uId-идентификатор пользователя, pC-категория товара, aC-возрастная категория товара, uA-возраст пользователя. Датасет: ";
        List<CompositeDTO> orders = fullInformationAboutOrdersService.getLast25Orders();
        String dataSetPart = fullInformationAboutOrdersService.formatOutputoString(orders);
        String message = messagePart1 + messagePart2 + dataSetPart;
        System.out.println("message: " + message);
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

    @GetMapping("/test")
    public String test() {
        List<CompositeDTO> orders = fullInformationAboutOrdersService.getLast25Orders();
        return fullInformationAboutOrdersService.formatOutputoString(orders);
    }
}
