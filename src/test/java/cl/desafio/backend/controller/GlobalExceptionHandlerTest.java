package cl.desafio.backend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Pablo Staub Ramirez
 */
public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleResponseStatusException() {
        String errorMessage = "Service unavailable";
        HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
        ResponseStatusException exception = new ResponseStatusException(status, errorMessage);

        ResponseEntity<String> response = globalExceptionHandler.handleResponseStatusException(exception);

        assertEquals(status, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }
}
