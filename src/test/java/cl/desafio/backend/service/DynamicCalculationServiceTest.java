package cl.desafio.backend.service;

import cl.desafio.backend.repository.CalculationHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Pablo Staub Ramirez
 */
public class DynamicCalculationServiceTest {

    @Mock
    private ExternalService externalService;

    @Mock
    private CalculationHistoryRepository historyRepository;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @Mock
    private Cache.ValueWrapper valueWrapper;

    @InjectMocks
    private DynamicCalculationService dynamicCalculationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculate_Success() {
        double num1 = 10.0;
        double num2 = 5.0;
        double percentage = 0.1;
        double expectedResult = 16.5;

        when(externalService.getPercentage()).thenReturn(percentage);

        double result = dynamicCalculationService.calculate(num1, num2);
        assertEquals(expectedResult, result);
    }
}
