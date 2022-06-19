package io.beautrace.spring.servlet.common

import org.springframework.web.filter.AbstractRequestLoggingFilter
import javax.servlet.http.HttpServletRequest

object RequestLogger : AbstractRequestLoggingFilter() {

    init {
        super.setIncludeClientInfo(true)
        super.setIncludeQueryString(true)
        super.setIncludePayload(true)
        super.setMaxPayloadLength(64000)
    }

    override fun beforeRequest(request: HttpServletRequest, message: String) {
        throw NotImplementedError("Not supported")
    }

    override fun afterRequest(request: HttpServletRequest, message: String) {
        throw NotImplementedError("Not supported")
    }

    fun logRequest(request: HttpServletRequest): String {
        return super.createMessage(request, "", "")
    }
}