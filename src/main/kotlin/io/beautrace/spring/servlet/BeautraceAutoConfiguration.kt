package io.beautrace.spring.servlet

import io.beautrace.spring.servlet.aspect.TracingAdvice
import org.springframework.aop.Advisor
import org.springframework.aop.aspectj.AspectJExpressionPointcut
import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan


@ComponentScan
// @SpringBootConfiguration can be used as an alternative to the Spring's standard @Configuration annotation
// so that configuration can be found automatically (for example in tests).
// From: https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/SpringBootConfiguration.html
@SpringBootConfiguration
@EnableConfigurationProperties(BeautraceProperties::class)
open class BeautraceAutoConfiguration {

    @Value("\${$BEAUTRACE_CONFIGURATION_PREFIX.root-package}")
    val rootPackage: String? = null

    @Bean
    open fun advisorBean(): Advisor {
        val pointcut = AspectJExpressionPointcut()
        pointcut.expression = "within($rootPackage..*)"
        return DefaultPointcutAdvisor(pointcut, TracingAdvice())
    }
}