package com.bubusyaka.demo.service;

import com.bubusyaka.demo.comp.grpc.GRPCRequest;
import com.bubusyaka.demo.comp.grpc.GRPCResponse;
import com.bubusyaka.demo.comp.grpc.GrpcServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestServiceGrpc {
    private final DataSetOfUsersAndItemsService dataSetOfUsersAndItemsService;

    public void sendMessage(Long id) {
        String request = id.toString();

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8090).usePlaintext().build();
        GrpcServiceGrpc.GrpcServiceStub asyncStub = GrpcServiceGrpc.newStub(channel);

        StreamObserver<GRPCResponse> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(GRPCResponse response) {
                // Handle the server's response
                System.out.println("Response: " + response.getResponse());
            }

            @Override
            public void onError(Throwable throwable) {
                // Handle any errors
            }

            @Override
            public void onCompleted() {
                // Complete the communication
                channel.shutdown();
            }
        };
        // Create the ChatMessage
        GRPCRequest message = GRPCRequest.newBuilder().setMessage(request).build();
        //GRPCResponse response = grpcServiceBlockingStub.processRequestBlocking2(message);
        // Send the chat messages to the server
        asyncStub
                .processRequest(responseObserver)
                .onNext(message);
        String result = responseObserver.toString();
    }

}

