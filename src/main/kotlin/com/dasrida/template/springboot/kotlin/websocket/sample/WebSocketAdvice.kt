package com.dasrida.template.springboot.kotlin.websocket.sample

import com.dasrida.template.springboot.kotlin.websocket.SessionData
import com.dasrida.template.springboot.kotlin.websocket.sample.controller.StompSampleController
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.ObjectFactory
import org.springframework.messaging.handler.annotation.MessageExceptionHandler
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ControllerAdvice

@Component
@ControllerAdvice(assignableTypes = [StompSampleController::class])
class WebSocketAdvice(
    private val sessionDataFactory: ObjectFactory<SessionData>
) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    @MessageExceptionHandler
    @SendToUser("/")
    fun exceptionHandler(e: Exception): ErrorResponse =
        try {
            throw e
        } catch (e: RuntimeException) {
            ErrorResponse("${e.message}")
        } catch (e: Exception) {
            ErrorResponse("${e.message}")
        }

    data class ErrorResponse(
        val message: String
    )
}
