package org.example.bookingservice.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
@Aspect
class LoggableAspect {

    val logger: Logger = LoggerFactory.getLogger(LoggableAspect::class.java)

    @Pointcut("@annotation(org.example.bookingservice.aspect.Loggable)")
    fun loggable() {}

    @Around("loggable()")
    fun aroundLoggable(joinPoint: ProceedingJoinPoint): Any {
        val result: Any?
        try {
            result = joinPoint.proceed()
        } catch (e: Exception) {
            logger.error("Method {} was throwing exception {}:{}",
                joinPoint.signature.toLongString(), e.javaClass, e.message)
            throw e
        }
        if (result == null) logger.info("Method {} was finished", joinPoint.signature.toLongString())
        else logger.info("Method {} was finished and return {}", joinPoint.signature.toLongString(), result)

        return result
    }
}
