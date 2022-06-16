package io.beautrace.spring.servlet.aspect

import io.beautrace.spring.servlet.ApplicationContextHolder
import io.beautrace.spring.servlet.model.RequestTraceState
import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import java.lang.reflect.Method

open class TracingAdvice: MethodInterceptor {

    override fun invoke(invocation: MethodInvocation): Any? {
        val method: Method = invocation.method
        val requestTraceState = ApplicationContextHolder.getBean(RequestTraceState::class.java)
        requestTraceState.methodCalls.add(method.toGenericString())
        return invocation.proceed()
    }
}