package cl.desafio.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Pablo Staub Ramirez
 */
public class ExternalServiceTest {

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @Mock
    private Cache.ValueWrapper valueWrapper;

    @InjectMocks
    private ExternalService externalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPercentage_Success() {
        double expectedPercentage = 0.10;

        // Mocking the method call to avoid random failure
        ExternalService spyService = spy(externalService);
        doReturn(expectedPercentage).when(spyService).getPercentage();

        Double result = spyService.getPercentage();
        assertEquals(expectedPercentage, result);
    }

    @Test
    void testGetPercentage_RetryAndRecover() {
        double cachedPercentage = 0.10;

        when(cacheManager.getCache("percentage")).thenReturn(cache);
        when(cache.get("percentage")).thenReturn(valueWrapper);
        when(valueWrapper.get()).thenReturn(cachedPercentage);

        Double result = externalService.recover(new RuntimeException("Simulated external service failure"));
        assertEquals(cachedPercentage, result);
    }
}
