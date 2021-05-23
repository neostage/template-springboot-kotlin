package com.dasrida.template.springboot.kotlin.websocket.sample.exception

import com.dasrida.template.springboot.kotlin.websocket.WebSocketException

sealed class SampleException : WebSocketException {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
