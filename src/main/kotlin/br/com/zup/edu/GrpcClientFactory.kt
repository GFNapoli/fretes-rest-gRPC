package br.com.zup.edu

import io.grpc.ManagedChannel
import io.grpc.stub.AbstractBlockingStub
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory {

    @Singleton
    fun freteClientStub(@GrpcChannel("fretes") channel: ManagedChannel): FretesServiceGrpc.FretesServiceBlockingStub?{

        return FretesServiceGrpc.newBlockingStub(channel)
    }
}