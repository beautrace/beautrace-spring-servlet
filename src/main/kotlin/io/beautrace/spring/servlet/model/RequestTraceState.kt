package io.beautrace.spring.servlet.model

import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

@Component
@RequestScope
open class RequestTraceState(
    open var methodCalls: MutableSet<String> = mutableSetOf(),
)