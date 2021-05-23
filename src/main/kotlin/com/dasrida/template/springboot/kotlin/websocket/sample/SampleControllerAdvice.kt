package com.dasrida.template.springboot.kotlin.websocket.sample

import com.dasrida.template.springboot.kotlin.http.sample.controller.SampleController
import com.dasrida.template.springboot.kotlin.websocket.sample.exception.SampleConcreteException
import com.dasrida.template.springboot.kotlin.websocket.sample.exception.SampleException
import org.springframework.messaging.handler.annotation.MessageExceptionHandler
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.socket.WebSocketMessage

@Component("SampleControllerAdvice")
@ControllerAdvice(assignableTypes = [SampleController::class])
class SampleControllerAdvice {
    @SendToUser
    @MessageExceptionHandler(SampleException::class)
    fun handle(e: SampleException): WebSocketMessage<Any> {
        return when (e) {
            is SampleConcreteException -> object : WebSocketMessage<Any> {
                override fun getPayload(): Any = Unit
                override fun getPayloadLength(): Int = 0
                override fun isLast(): Boolean = true
            }
        }
    }
}
