package io.beautrace.spring.servlet.aspect

import io.beautrace.spring.servlet.ApplicationContextHolder
import io.beautrace.spring.servlet.model.MethodIO
import io.beautrace.spring.servlet.model.RequestTraceState
import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class TracingAdvice : MethodInterceptor {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun invoke(invocation: MethodInvocation): Any? {
        val method = invocation.method
        val arguments = invocation.arguments
        val result = invocation.proceed()
        val parameters = method.parameters
        val methodIO = if (parameters.isNotEmpty()) {
            val paramsToArgs = parameters.zip(arguments).toList()
            MethodIO(method.toGenericString(), paramsToArgs, result = result)
        } else
            MethodIO(method.toGenericString(), rawArguments = arguments.toList(), result = result)

        val requestTraceState = ApplicationContextHolder.getBean(RequestTraceState::class.java)
        requestTraceState.methodCalls.addFirst(methodIO)

        if (log.isDebugEnabled)
            log.debug("requestTraceState.methodCalls: {}", requestTraceState.methodCalls)
        return result
    }
}