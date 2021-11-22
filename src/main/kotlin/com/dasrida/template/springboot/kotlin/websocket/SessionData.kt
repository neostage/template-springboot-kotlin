package com.dasrida.template.springboot.kotlin.websocket

import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component

@Component
@Scope(scopeName = "websocket", proxyMode = ScopedProxyMode.TARGET_CLASS)
data class SessionData(
    var sessionId: String? = null
)
