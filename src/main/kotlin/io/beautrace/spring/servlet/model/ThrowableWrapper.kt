package io.beautrace.spring.servlet.model

class ThrowableWrapper(ex: Throwable) {
    val localizedMessage: String
    val cause: Throwable?
    val stackTrace: Array<StackTraceElement>

    init {
        localizedMessage = ex.localizedMessage
        cause = ex.cause
        stackTrace = ex.stackTrace
    }
}