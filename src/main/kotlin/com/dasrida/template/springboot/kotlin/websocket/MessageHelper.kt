package com.dasrida.template.springboot.kotlin.websocket

import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessageType

class MessageHelper private constructor() {
    companion object {
        fun createHeaders(sessionId: String): MessageHeaders {
            val headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE)
            headerAccessor.sessionId = sessionId
            headerAccessor.setLeaveMutable(true)
            return headerAccessor.messageHeaders
        }
    }
}
