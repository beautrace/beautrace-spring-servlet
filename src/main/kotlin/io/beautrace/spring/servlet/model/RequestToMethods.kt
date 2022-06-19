package io.beautrace.spring.servlet.model

data class RequestToMethods(
    val request: String,
    val methodCalls: ArrayDeque<MethodIO>,
)