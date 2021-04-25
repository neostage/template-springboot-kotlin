package com.dasrida.template.springboot.kotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinApplication

fun main(args: Array<String>) {
    @Suppress("SpreadOperator")
    runApplication<KotlinApplication>(*args)
}
