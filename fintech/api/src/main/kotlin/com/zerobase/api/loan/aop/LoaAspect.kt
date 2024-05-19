package com.zerobase.api.loan.aop

import mu.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

import org.springframework.stereotype.Component
import org.springframework.util.StopWatch

@Component
@Aspect
class LoaAspect {
    val logger = KotlinLogging.logger {}

    @Pointcut("within(com.zerobase.api..*)")
    fun isApi() {}

    @Around("isApi()")
    private fun loggingAspect(joinPoint: ProceedingJoinPoint): Any {
        val stopWatch = StopWatch()
        stopWatch.start()

        val result = joinPoint.proceed()

        stopWatch.stop()

        logger.debug {
            "${joinPoint.signature.name} " +
                    "${joinPoint.args[0]}" +
                    " ${stopWatch.totalTimeMillis}"
        }
        return result
    }

}