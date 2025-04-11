package com.bubusyaka.recommendation.service;

import com.bubusyaka.recommendation.comp.grpc.GRPCRequest;
import com.bubusyaka.recommendation.comp.grpc.GRPCResponse;
import com.bubusyaka.recommendation.model.dto.CompositeDTO;
import com.bubusyaka.recommendation.service.GigaChat.GigaChatDialog;
import io.grpc.stub.StreamObserver;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@RequiredArgsConstructor
public class GRPCGigachatStreamObserver implements StreamObserver<GRPCRequest> {
    private final StreamObserver<GRPCResponse> responseObserver;
    private final GigaChatDialog gigaChatDialog;
    private final FullInformationAboutOrdersService fullInformationAboutOrdersService;

    // используем виртуальные (green) потоки, которые существуют только на уровне java-приложения.
    // эти потоки, в отличие потоков операционной системы, бесплатны по ресурсам и их можно создать в "effectively" неограниченном кол-ве.
    // и когда нужно выпонить какую-то блокирующую операцию, мы блокируем только виртуальный поток, в то время как поток ОС может заниматься другой полезной нагрузкой.
    private final ExecutorService databaseExecutor = Executors.newVirtualThreadPerTaskExecutor();
    private final ExecutorService gigachatExecutor = Executors.newVirtualThreadPerTaskExecutor();


    private String buildMessage(GRPCRequest request) {
        String handledMessage = request.getMessage();
        String messagePart1 = "Порекомендуй пользователю с введенным идентификатором TOP-3 категории товаров в формате Json, которые он еще не заказывал, но был бы заинтересован или товары, которые заказывал чаще всего, на основе его наиболее частых покупок или на основе сходства этого пользователя с другими. Напиши в ответе только список рекомендованных тобой категорий товаров в формате Json,(пример: Categories: category1, category2,...). Не пиши в ответе ничего кроме данных в Json.";
        String messagePart2 = "Идентификатор пользователя=" + handledMessage + " . Датасет представлен далее, в нем id-идентификатор заказа, itId-идентификатор заказанного товара, n-название товара, uId-идентификатор пользователя, pC-категория товара, aC-возрастная категория товара, uA-возраст пользователя. Датасет: ";
        List<CompositeDTO> orders = fullInformationAboutOrdersService.getLast25Orders();
        String dataSetPart = fullInformationAboutOrdersService.formatOutputoString(orders);
        String message = messagePart1 + messagePart2 + dataSetPart;

        return message;
    }

    private GRPCResponse requestToGigaChat(String message) {

        String result = Try.of(() -> gigaChatDialog.getResponse(message))
                .getOrElseThrow(() -> new RuntimeException(message));
        GRPCResponse.Builder response = GRPCResponse.newBuilder()
                .setResponse(result);
        return response.build();
    }

    @Override
    public void onNext(GRPCRequest request) {
        // requestToGigaChat выполняется на отдельном пуле потоков из executor из-за того что запрос гигачат блокирующий
        // а thenAccept нет потому что там только возвращается ответ, вызов thenApply произойдет в вызывающем потоке
        // The methods without async execute their task in the same thread as the previous task
        CompletableFuture.supplyAsync(() -> buildMessage(request), databaseExecutor)
                        .thenApplyAsync(this::requestToGigaChat, gigachatExecutor)
                        .thenAccept(grpcResponse -> {
                            responseObserver.onNext(grpcResponse);
                            responseObserver.onCompleted();
                        });
    }

    @Override
    public void onError(Throwable throwable) {
        // Handle any errors
    }

    @Override
    public void onCompleted() {
        // Complete the response stream
        responseObserver.onCompleted();
    }
}
