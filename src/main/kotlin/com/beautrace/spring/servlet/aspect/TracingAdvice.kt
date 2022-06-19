package com.beautrace.spring.servlet.aspect

import com.fasterxml.jackson.databind.ObjectMapper
import com.beautrace.spring.servlet.ApplicationContextHolder
import com.beautrace.spring.servlet.model.MethodIO
import com.beautrace.spring.servlet.common.RequestLogger
import com.beautrace.spring.servlet.model.RequestToMethods
import com.beautrace.spring.servlet.model.RequestTrace
import com.beautrace.spring.servlet.model.ThrowableWrapper
import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.lang.reflect.Parameter

open class TracingAdvice : MethodInterceptor {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun invoke(invocation: MethodInvocation): Any? {
        val method = invocation.method
        val arguments = invocation.arguments
        val parameters = method.parameters
        val methodIO = if (parameters.isNotEmpty()) {
            val paramsToArgs = parameters.map(Parameter::toString).zip(arguments).toList()
            MethodIO(method.toGenericString(), paramsToArgs)
        } else MethodIO(method.toGenericString(), rawArguments = arguments.toList())
        val result: Any?
        try {
            result = invocation.proceed()
            methodIO.result = result
        } catch (ex: Throwable) {
            methodIO.result = null
            methodIO.exception = ThrowableWrapper(ex)
            throw ex
        } finally {
            val requestTrace = ApplicationContextHolder.getBean(RequestTrace::class.java)
            if (requestTrace.requestToMethods == null) {
                val httpRequest = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
                requestTrace.requestToMethods = RequestToMethods(RequestLogger.logRequest(httpRequest), ArrayDeque())
            }
            requestTrace.requestToMethods!!.methodCalls.addFirst(methodIO)
            if (log.isDebugEnabled) {
                val mapper = ApplicationContextHolder.getBean(ObjectMapper::class.java)
                log.debug("requestTraceState.methodCalls: {}", mapper.writeValueAsString(requestTrace.requestToMethods))
            }
        }
        return result
    }
}