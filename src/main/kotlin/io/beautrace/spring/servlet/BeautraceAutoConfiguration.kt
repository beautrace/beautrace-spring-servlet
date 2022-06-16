package io.beautrace.spring.servlet

import io.beautrace.spring.servlet.aspect.TracingAdvice
import org.springframework.aop.Advisor
import org.springframework.aop.aspectj.AspectJExpressionPointcut
import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan

@ComponentScan
// @SpringBootConfiguration can be used as an alternative to the Spring's standard @Configuration annotation
// so that configuration can be found automatically (for example in tests).
// From: https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/SpringBootConfiguration.html
@SpringBootConfiguration
@EnableConfigurationProperties(BeautraceProperties::class)
// Enable Beautrace only if root-package property is set
@ConditionalOnExpression("'\${${BEAUTRACE_CONFIGURATION_PREFIX}.root-package}' > ''")
open class BeautraceAutoConfiguration {

    @Autowired
    private lateinit var beautraceProperties: BeautraceProperties

    @Bean
    open fun beautraceAdvisorBean(): Advisor {
        val pointcut = AspectJExpressionPointcut()
        pointcut.expression = "within(${beautraceProperties.rootPackage}..*)"
        return DefaultPointcutAdvisor(pointcut, TracingAdvice())
    }
}