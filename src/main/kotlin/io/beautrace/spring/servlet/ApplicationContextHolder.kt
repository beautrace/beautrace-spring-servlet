package io.beautrace.spring.servlet

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
open class ApplicationContextHolder : ApplicationContextAware {

    companion object {
        private lateinit var applicationContext: ApplicationContext

        fun <T> getBean(type: Class<T>): T = applicationContext.getBean(type)
    }

    override fun setApplicationContext(context: ApplicationContext) {
        applicationContext = context
    }
}