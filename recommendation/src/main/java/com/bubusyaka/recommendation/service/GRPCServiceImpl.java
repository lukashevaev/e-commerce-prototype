package com.bubusyaka.recommendation.service;

import com.bubusyaka.recommendation.comp.grpc.GrpcServiceGrpc;

import com.bubusyaka.recommendation.comp.grpc.GRPCResponse;
import com.bubusyaka.recommendation.comp.grpc.GRPCRequest;
import com.bubusyaka.recommendation.service.GigaChat.GigaChatDialog;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

@RequiredArgsConstructor
@GrpcService
public class GRPCServiceImpl extends GrpcServiceGrpc.GrpcServiceImplBase {
private final GigaChatDialog gigaChatDialog;
private final FullInformationAboutOrdersService fullInformationAboutOrdersService;
//    @Override
//    public void processRequestBlocking2(GRPCRequest request, StreamObserver<GRPCResponse> responseObserver) {
//        System.out.println("processRequestBlocking2 zashol govno is jopi");
//    }
    @Override
    public StreamObserver<GRPCRequest> processRequest(StreamObserver<GRPCResponse> responseObserver) {
        return new GRPCGigachatStreamObserver(responseObserver, gigaChatDialog, fullInformationAboutOrdersService);
    }

}
