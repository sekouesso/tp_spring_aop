package com.x.sentinel.aspect;

import com.x.sentinel.annotation.RetryOnFailure;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Aspect
@Component
@ConditionalOnProperty(
        name = {"sentinel.enabled", "sentinel.retry.enabled"},
        havingValue = "true"
)
public class RetryAspect {

    @Around("@annotation(retry)")
    public Object retry(ProceedingJoinPoint jp, RetryOnFailure retry) throws Throwable {

        int attempts = 0;
        Throwable lastException = null;

        while (attempts < retry.maxAttempts()) {
            attempts++;

            try {
                Object result = jp.proceed();

                if (retry.logEachAttempt()) {
                    System.out.println("[RETRY SUCCESS] attempt=" + attempts +
                            " method=" + jp.getSignature());
                }

                return result;

            } catch (Throwable ex) {

                lastException = ex;
                if (retry.logEachAttempt()) {
                    System.out.println("[RETRY FAILED] attempt=" + attempts +
                            " method=" + jp.getSignature() +
                            " cause=" + ex.getClass().getSimpleName());
                }
                boolean shouldRetry = false;
                for (Class<? extends Throwable> clazz : retry.retryOn()) {
                    if (clazz.isAssignableFrom(ex.getClass())) {
                        shouldRetry = true;
                        break;
                    }
                }

                if (!shouldRetry) {
                    throw ex;
                }



                Thread.sleep(retry.delayMs());
            }
        }

        throw lastException;
    }
}
