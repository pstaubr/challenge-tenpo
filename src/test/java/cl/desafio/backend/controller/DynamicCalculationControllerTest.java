package cl.desafio.backend.controller;

import cl.desafio.backend.entity.DynamicCalculationHistoryEntity;
import cl.desafio.backend.repository.CalculationHistoryRepository;
import cl.desafio.backend.service.DynamicCalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Pablo Staub Ramirez
 */
public class DynamicCalculationControllerTest {

    @Mock
    private DynamicCalculationService dynamicCalculationService;

    @Mock
    private CalculationHistoryRepository historyRepository;

    @InjectMocks
    private DynamicCalculationController dynamicCalculationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculate_Success() {
        double num1 = 10.0;
        double num2 = 5.0;
        double expectedResult = 15.0;

        when(dynamicCalculationService.calculate(num1, num2)).thenReturn(expectedResult);

        ResponseEntity<?> response = dynamicCalculationController.calculate(num1, num2);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
        verify(historyRepository, times(1)).save(any(DynamicCalculationHistoryEntity.class));
    }

    @Test
    void testCalculate_Failure() {
        double num1 = 10.0;
        double num2 = 0.0;
        String errorMessage = "Division by zero";

        when(dynamicCalculationService.calculate(num1, num2)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage));

        ResponseEntity<?> response = dynamicCalculationController.calculate(num1, num2);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(historyRepository, times(1)).save(any(DynamicCalculationHistoryEntity.class));
    }

    @Test
    void testGetHistory() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<DynamicCalculationHistoryEntity> page = new PageImpl<>(Collections.emptyList());

        when(historyRepository.findAll(pageable)).thenReturn(page);

        ResponseEntity<?> response = dynamicCalculationController.getHistory(pageable);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
    }
}
