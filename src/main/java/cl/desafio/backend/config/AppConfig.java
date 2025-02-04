package cl.desafio.backend.config;

import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.support.RetryTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author Pablo Staub Ramirez
 */
@Configuration
@EnableCaching
@EnableRetry
public class AppConfig {

    /**
     * valor expiracion de 30 min cache
     */
    @Value("${cache.expiration.minutes:30}")
    private int cacheExpirationMinutes;

    /**
     * @return
     */
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("percentage") {
            @Override
            protected org.springframework.cache.Cache createConcurrentMapCache(final String name) {
                return new ConcurrentMapCache(name, CacheBuilder.newBuilder()
                        .expireAfterWrite(cacheExpirationMinutes, TimeUnit.MINUTES)
                        .build().asMap(), false);
            }
        };
    }

    /**
     * @return
     */
    @Bean
    public RetryTemplate retryTemplate() {
        return new RetryTemplate();
    }
}