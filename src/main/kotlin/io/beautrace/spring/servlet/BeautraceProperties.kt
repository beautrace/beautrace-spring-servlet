package io.beautrace.spring.servlet

import org.springframework.boot.context.properties.ConfigurationProperties

internal const val BEAUTRACE_CONFIGURATION_PREFIX = "beautrace"

@ConfigurationProperties(prefix = BEAUTRACE_CONFIGURATION_PREFIX)
open class BeautraceProperties {
    var rootPackage: String? = null
    var outputFile: String? = null
}