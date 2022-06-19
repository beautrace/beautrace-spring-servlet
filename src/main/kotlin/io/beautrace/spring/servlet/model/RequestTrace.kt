package io.beautrace.spring.servlet.model

import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

@Component
@RequestScope
open class RequestTrace(
    open var requestToMethods: RequestToMethods? = null,
)