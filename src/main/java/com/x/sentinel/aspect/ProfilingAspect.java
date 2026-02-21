package com.x.sentinel.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

@Aspect
@Component
@ConditionalOnProperty(
        name = {"sentinel.enabled", "sentinel.profiling.enabled"},
        havingValue = "true"
)
public class ProfilingAspect {

    // On utilise une structure légère pour éviter les concaténations de chaînes répétitives
    private static final ThreadLocal<Deque<Long>> startTimeStack = ThreadLocal.withInitial(ArrayDeque::new);
    private static final ThreadLocal<String> currentTraceId = new ThreadLocal<>();
    private static final ThreadLocal<Integer> depthLevel = ThreadLocal.withInitial(() -> 0);

    @Around("execution(public * com.x.sentinel.services..*.*(..)) || " +
            "execution(public * com.x.sentinel.repositories..*.*(..))")
    public Object profile(ProceedingJoinPoint jp) throws Throwable {

        // 1. Initialisation de la trace si c'est l'appel racine [cite: 49, 234]
        if (currentTraceId.get() == null) {
            currentTraceId.set(UUID.randomUUID().toString().substring(0, 8)); // Plus lisible
            System.out.println("\n[Sentinel-X] Profiling trace_id=" + currentTraceId.get()); //[cite: 225]
        }

        // 2. Préparation du contexte
        int currentDepth = depthLevel.get();
        depthLevel.set(currentDepth + 1);
        startTimeStack.get().push(System.nanoTime()); // nanoTime est plus précis pour le profiling

        try {
            return jp.proceed();
        } finally {
            // 3. Calcul de la durée en millisecondes [cite: 51, 166]
            long end = System.nanoTime();
            long start = startTimeStack.get().pop();
            long durationMs = (end - start) / 1_000_000;

            // 4. Affichage optimisé avec indentation [cite: 219, 224]
            String indent = "  ".repeat(currentDepth);
            System.out.printf("%s%s [%d ms]%n", indent, jp.getSignature().toShortString(), durationMs);// [cite: 226, 227]

            // 5. Nettoyage strict pour éviter les fuites mémoire (ThreadLocal)
            depthLevel.set(currentDepth);
            if (currentDepth == 0) {
                cleanUp();
            }
        }
    }

    private void cleanUp() {
        startTimeStack.remove();
        currentTraceId.remove();
        depthLevel.remove();
    }
}