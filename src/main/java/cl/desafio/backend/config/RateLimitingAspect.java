package cl.desafio.backend.config;

import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Aspect
@Component
public class RateLimitingAspect {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitingAspect.class);
    private final RateLimiter rateLimiter = RateLimiter.create(3.0 / 60.0); // 3 requests per minute

    /**
     * Valida  numero de reintentos y manejo de excepciones
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* cl.desafio.backend.controller.*.*(..))")
    public Object rateLimiter(ProceedingJoinPoint joinPoint) throws Throwable {
        for (int i = 0; i < 3; i++) { // Try up to 3 times
            if (rateLimiter.tryAcquire()) {
                logger.info("Rate limit acquired for method: {} on attempt {}", joinPoint.getSignature(), (i + 1));
                return joinPoint.proceed();
            } else {
                logger.warn("Rate limit exceeded for method: {}. Retrying in 2 seconds...", joinPoint.getSignature());
                Thread.sleep(2000);
            }
        }
        logger.error("Rate limit exceeded after 3 retries for method: {}", joinPoint.getSignature());
        throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests. Please try again later.");
    }
}