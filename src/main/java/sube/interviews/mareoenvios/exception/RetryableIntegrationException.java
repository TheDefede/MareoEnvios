package sube.interviews.mareoenvios.exception;

public class RetryableIntegrationException extends RuntimeException {
    public RetryableIntegrationException(String message) {
        super(message);
    }
}
