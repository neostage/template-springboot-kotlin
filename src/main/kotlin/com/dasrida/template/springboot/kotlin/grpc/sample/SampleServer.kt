package com.dasrida.template.springboot.kotlin.grpc.sample

import com.dasrida.template.springboot.proto.Sample.Data
import com.dasrida.template.springboot.proto.SampleServiceGrpcKt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SampleServer(
    val dataset: Collection<Data>
): SampleServiceGrpcKt.SampleServiceCoroutineImplBase() {
    override suspend fun bidirectional(requests: Flow<Data>): Flow<Data> =
        flow {
            requests.collect {data ->
                val list = computeIfAbasent
            }
        }
}
