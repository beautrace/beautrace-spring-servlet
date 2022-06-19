package com.beautrace.spring.servlet.model

data class MethodIO(
    val name: String,
    /**
     * If -parameters (-java-parameters for Kotlin compiler) flag is set for a compiler, then method arguments will be
     * nicely displayed as name-value pairs
     */
    val arguments: List<Pair<String, Any>> = listOf(),
    /**
     * If -parameters (-java-parameters for Kotlin compiler) flag is not set for a compiler, then raw argument values
     * will be displayed
     */
    val rawArguments: List<Any> = listOf(),
    var result: Any? = null,
    var exception: ThrowableWrapper? = null,
)