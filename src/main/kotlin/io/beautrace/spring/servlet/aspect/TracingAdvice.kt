package io.beautrace.spring.servlet.aspect

import com.fasterxml.jackson.databind.ObjectMapper
import io.beautrace.spring.servlet.ApplicationContextHolder
import io.beautrace.spring.servlet.model.MethodIO
import io.beautrace.spring.servlet.model.RequestTraceState
import io.beautrace.spring.servlet.model.ThrowableWrapper
import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.reflect.Parameter
import java.nio.file.Paths

open class TracingAdvice : MethodInterceptor {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun invoke(invocation: MethodInvocation): Any? {
        val method = invocation.method
        val arguments = invocation.arguments
        val parameters = method.parameters
        val methodIO = if (parameters.isNotEmpty()) {
            val paramsToArgs = parameters.map(Parameter::toString).zip(arguments).toList()
            MethodIO(method.toGenericString(), paramsToArgs)
        } else
            MethodIO(method.toGenericString(), rawArguments = arguments.toList())
        val result: Any?
        try {
            result = invocation.proceed()
            methodIO.result = result
        } catch (ex: Throwable) {
            methodIO.result = null
            methodIO.exception = ThrowableWrapper(ex)
            throw ex
        } finally {
            val requestTraceState = ApplicationContextHolder.getBean(RequestTraceState::class.java)
            requestTraceState.methodCalls.addFirst(methodIO)
            val mapper = ApplicationContextHolder.getBean(ObjectMapper::class.java)
            mapper.writeValue(Paths.get("beautrace.json").toFile(), requestTraceState.methodCalls)
            if (log.isDebugEnabled)
                log.debug("requestTraceState.methodCalls: {}", mapper.writeValueAsString(requestTraceState.methodCalls))
        }
        return result
    }
}