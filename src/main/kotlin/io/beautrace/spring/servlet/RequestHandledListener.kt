package io.beautrace.spring.servlet

import com.fasterxml.jackson.databind.ObjectMapper
import io.beautrace.spring.servlet.model.RequestTrace
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.context.support.ServletRequestHandledEvent
import java.io.BufferedWriter
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStreamWriter

@Component
open class RequestHandledListener(
    private val jacksonObjectMapper: ObjectMapper,
    private val requestTrace: RequestTrace,
    private val beautraceProperties: BeautraceProperties,
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @EventListener
    open fun requestHandled(event: ServletRequestHandledEvent) {
        if (requestTrace.requestToMethods!!.request.contains(event.requestUrl)) {
            beautraceProperties.outputFile?.let { outputFileName ->
                val writer: BufferedWriter
                try {
                    writer = BufferedWriter(
                        OutputStreamWriter(FileOutputStream(outputFileName, true), "UTF-8")
                    )
                } catch (ex: FileNotFoundException) {
                    log.error("Cannot open or create a specified output file. Exception: {}", ex.localizedMessage)
                    return
                } catch (ex: SecurityException) {
                    log.error(
                        "Cannot obtain write access to the specified out file. Exception: {}",
                        ex.localizedMessage
                    )
                    return
                }
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
}