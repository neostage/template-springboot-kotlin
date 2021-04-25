package com.dasrida.template.springboot.kotlin.websocket

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(
    @Value("\${websocket.stomp.message.size.limit}")
    private val messageSizeLimit: Int,
    @Value("\${websocket.stomp.send.buffer.limit}")
    private val sendBufferLimit: Int,
    @Value("\${websocket.stomp.send.time.limit}")
    private val sendTimeLimit: Int
) : WebSocketMessageBrokerConfigurer {
    @Value("\${websocket.stomp.endpoint}")
    lateinit var endpoint: String

    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.setApplicationDestinationPrefixes("/app")
        config.enableSimpleBroker("/topic", "/queue")
    }

    override fun configureWebSocketTransport(registry: WebSocketTransportRegistration) {
        super.configureWebSocketTransport(registry)
        registry.setMessageSizeLimit(messageSizeLimit)
        registry.setSendBufferSizeLimit(sendBufferLimit)
        registry.setSendTimeLimit(sendTimeLimit)
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint(endpoint).setAllowedOriginPatterns("*")
    }
}
