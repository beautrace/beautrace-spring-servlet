package io.beautrace.spring.servlet

import com.fasterxml.jackson.databind.ObjectMapper
import io.beautrace.spring.servlet.model.RequestTrace
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.context.support.ServletRequestHandledEvent
import java.io.BufferedWriter
import java.io.FileOutputStream
import java.io.OutputStreamWriter

@Component
open class RequestHandledListener(
    private val jacksonObjectMapper: ObjectMapper,
    private val requestTrace: RequestTrace,
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @EventListener
    open fun requestHandled(event: ServletRequestHandledEvent) {
        if (requestTrace.requestToMethods!!.request.contains(event.requestUrl)) {
            val writer = BufferedWriter(
                OutputStreamWriter(FileOutputStream("beautrace.json", true), "UTF-8")
            )
            try {
                writer.newLine()
                jacksonObjectMapper.writeValue(writer, requestTrace.requestToMethods)
            } catch (ex: Exception) {
                log.error(
                    "Exception occurred when trying to write to an output file. Exception: {}",
                    ex.localizedMessage
                )
            } finally {
                writer.close()
            }
        }
    }
}