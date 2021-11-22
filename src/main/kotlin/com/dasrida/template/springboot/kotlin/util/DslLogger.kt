package com.dasrida.template.springboot.kotlin.util

import org.slf4j.Logger

class DslLogger(private val logger: Logger) {
    infix fun trace(message: String) {
        logger.trace(message)
    }

    infix fun debug(message: String) {
        logger.debug(message)
    }

    infix fun info(message: String) {
        logger.info(message)
    }

    infix fun warn(message: String) {
        logger.warn(message)
    }

    infix fun error(message: String) {
        logger.error(message)
    }
}
