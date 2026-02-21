package com.x.sentinel.aspect;

import com.x.sentinel.annotation.SecureAudit;
import com.x.sentinel.entities.AuditEvent;
import com.x.sentinel.enums.AuditStatus;
import com.x.sentinel.repositories.AuditEventRepository;
import com.x.sentinel.utils.SimulatedUserContext;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

@Aspect
@Component
@ConditionalOnProperty(name = "sentinel.audit.enabled", havingValue = "true")
@RequiredArgsConstructor
public class AuditAspect {
    private final AuditEventRepository repo;

    @Around("@annotation(secureAudit)")
    public Object audit(ProceedingJoinPoint jp, SecureAudit secureAudit) throws Throwable {
        Instant start = Instant.now();
        String username = SimulatedUserContext.getCurrentUser().username();
        String traceId = UUID.randomUUID().toString();
        AuditEvent event = AuditEvent.builder()
                .action(secureAudit.action())
                .username(username)
                .methodSignature(jp.getSignature().toString())
                .traceId(traceId)
                .build();

        try {
            Object result = jp.proceed();
            event.setStatus(AuditStatus.SUCCESS);
            event.setDurationMs(Duration.between(start, Instant.now()).toMillis());
            if (secureAudit.logArgs()) {
                event.setArgsLogged(true);
                event.setArgsMasked(secureAudit.sensitive());
                event.setArgsSummary(secureAudit.sensitive() ? "[MASKED]" : Arrays.toString(jp.getArgs()));
            }
            if (secureAudit.logResult()) {
                event.setResultSummary(result != null ? result.toString() : "null");
            }
            return result;
        } catch (Throwable e) {
            event.setStatus(AuditStatus.FAILURE);
            event.setExceptionClass(e.getClass().getName());
            event.setExceptionMessage(e.getMessage());
            throw e;
        } finally {
            event.setDurationMs(Duration.between(start, Instant.now()).toMillis());
            logAudit(jp, secureAudit, event.getUsername(), String.valueOf(event.getStatus()), event.getDurationMs());
            System.out.println("event avant save "+event);
            repo.saveAndFlush(event);
            System.out.println(event);
        }
    }

    private void logAudit(ProceedingJoinPoint jp, SecureAudit audit, String user, String status, long duration) {
        String args = audit.sensitive() ? "[MASKED]" : Arrays.toString(jp.getArgs());
        System.out.printf("AUDIT | action=%s | user=%s | status=%s | duration=%dms | args=%s%n",
                audit.action(), user, status, duration, args);
    }
}
//mvn spring-boot:run -Dspring-boot.run.jvmArguments="-javaagent:C:\libs\aspectjweaver-1.9.22.jar"