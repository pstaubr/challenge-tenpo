package cl.desafio.backend.config;

import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

/**
 * @author Pablo Staub Ramirez
 */
public class RateLimitingAspectTest {

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private RateLimiter rateLimiter;

    @InjectMocks
    private RateLimitingAspect rateLimitingAspect;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRateLimiter_Success() throws Throwable {
        when(rateLimiter.tryAcquire()).thenReturn(true);

        Object result = rateLimitingAspect.rateLimiter(proceedingJoinPoint);
        verify(proceedingJoinPoint, times(1)).proceed();
    }
}
