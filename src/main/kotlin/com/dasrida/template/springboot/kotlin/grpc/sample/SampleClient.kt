package com.dasrida.template.springboot.kotlin.grpc.sample

import com.dasrida.template.springboot.kotlin.util.DslLogger
import com.dasrida.template.springboot.proto.Sample.Data
import com.dasrida.template.springboot.proto.Sample.Position
import com.dasrida.template.springboot.proto.SampleServiceGrpc
import com.dasrida.template.springboot.proto.SampleServiceGrpcKt
import com.google.protobuf.kotlin.toByteStringUtf8
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.slf4j.LoggerFactory

class SampleClient(
    private val serverIp: String,
    private val serverPort: Int
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val log = DslLogger(logger)

    private val channel: ManagedChannel
    private var blockingStub: SampleServiceGrpc.SampleServiceBlockingStub
    private var asyncStub: SampleServiceGrpc.SampleServiceStub
    private var coroutineStub: SampleServiceGrpcKt.SampleServiceCoroutineStub

    init {
        @Suppress("TooGenericExceptionCaught", "TooGenericExceptionThrown", "SwallowedException")
        try {
            val channelBuilder = ManagedChannelBuilder.forAddress(serverIp, serverPort).usePlaintext()
            channel = channelBuilder.build()
            blockingStub = SampleServiceGrpc.newBlockingStub(channel)
            asyncStub = SampleServiceGrpc.newStub(channel)
            coroutineStub = SampleServiceGrpcKt.SampleServiceCoroutineStub(channel)
        } catch (e: Exception) {
            throw e
        }
    }

    @Suppress("LongParameterList")
    private suspend fun bidirectional() {
        val requests = generateDataFlow()
        coroutineStub.bidirectional(requests).collect { data ->
            log debug "position: ${data.position.x}, ${data.position.y}; image: ${data.image}; active: ${data.active}"
        }
        log debug("done")
    }

    private fun generateDataFlow(): Flow<Data> = flow {
        val dataset = listOf(
            Data.newBuilder().setPosition(
                Position.newBuilder().setX(0).setY(0).build()
            )
                .setImage("foo".toByteStringUtf8())
                .setActive(true)
                .build(),
            Data.newBuilder().setPosition(
                Position.newBuilder().setX(0).setY(0).build()
            )
                .setImage("foo".toByteStringUtf8())
                .setActive(true)
                .build()
        )

        for (data in dataset) {
            log debug ("emitting ${data.position.x}, ${data.position.y}")
            emit(data)
        }
    }
}
