package cl.desafio.backend.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Pablo Staub Ramirez
 */

@Service
public class ExternalService {

    @Autowired
    private CacheManager cacheManager;

    /**
     * @return
     */
    @Cacheable(value = "percentage", key = "'percentage'")
    @Retryable(value = RuntimeException.class, maxAttempts = 3)
    public Double getPercentage() {
        if (Math.random() < 0.5) {
            throw new RuntimeException("Falló servicio simulado...");
        }
        return 0.10;
    }

    @Recover
    public Double recover(RuntimeException e) {
        Double cachedPercentage = (Double) cacheManager.getCache("percentage").get("percentage").get();
        if (cachedPercentage != null) {
            return cachedPercentage;
        } else {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Servicio no disponible y sin valor almacenado en caché...");
        }
    }
}
