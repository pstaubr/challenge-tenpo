package cl.desafio.backend.service;

import cl.desafio.backend.repository.CalculationHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Pablo Staub Ramirez
 */
@Service
public class DynamicCalculationService {

    @Autowired
    private ExternalService externalService;

    @Autowired
    private CalculationHistoryRepository historyRepository;

    @Autowired
    private CacheManager cacheManager;

    /**
     * @param num1
     * @param num2
     * @return
     */
    @Retryable(value = RuntimeException.class, maxAttempts = 3)
    public double calculate(double num1, double num2) {
        double sum = num1 + num2;
        double percentage = externalService.getPercentage();
        return sum + (sum * percentage);
    }

    /**
     * @param e
     * @param num1
     * @param num2
     * @return
     */
    @Recover
    public double recover(RuntimeException e, double num1, double num2) {
        Double cachedPercentage = (Double) cacheManager.getCache("percentage").get("percentage").get();
        if (cachedPercentage != null) {
            double sum = num1 + num2;
            return sum + (sum * cachedPercentage);
        } else {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "External service unavailable and no cached value.");
        }
    }
}