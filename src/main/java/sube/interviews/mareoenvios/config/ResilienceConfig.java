package sube.interviews.mareoenvios.config;

import io.github.resilience4j.common.retry.configuration.RetryConfigCustomizer;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.LockAcquisitionException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.web.client.HttpServerErrorException;
import sube.interviews.mareoenvios.exception.RetryableIntegrationException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.sql.SQLTransientConnectionException;
import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class ResilienceConfig {

    private final RetryRegistry retryRegistry;

    @PostConstruct
    public void setUp() {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofSeconds(5))
                .retryExceptions(
                        RetryableIntegrationException.class,
                        CannotAcquireLockException.class,
                        LockAcquisitionException.class,
                        QueryTimeoutException.class,
                        SQLTransientConnectionException.class,
                        SocketTimeoutException.class,
                        ConnectException.class,
                        HttpServerErrorException.ServiceUnavailable.class,
                        HttpServerErrorException.GatewayTimeout.class,
                        HttpServerErrorException.BadGateway.class
                )
                .build();

        retryRegistry.retry("shippingRetry", config);
    }
}
