package cl.desafio.backend.controller;

import cl.desafio.backend.entity.DynamicCalculationHistoryEntity;
import cl.desafio.backend.repository.CalculationHistoryRepository;
import cl.desafio.backend.service.DynamicCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

/**
 * @author Pablo Staub Ramirez
 */
@RestController
@RequestMapping("/api")
public class DynamicCalculationController {

    @Autowired
    private DynamicCalculationService dynamicCalculationService;

    @Autowired
    private CalculationHistoryRepository historyRepository;

    /**
     * @param num1
     * @param num2
     * @return
     */
    @PostMapping("/dynamicCalculation")
    public ResponseEntity<?> calculate(@RequestParam double num1, @RequestParam double num2) {
        try {
            double result = dynamicCalculationService.calculate(num1, num2);
            saveHistory("/DynamicCalculation", "num1=" + num1 + "&num2=" + num2, String.valueOf(result), null);
            return ResponseEntity.ok(result);
        } catch (ResponseStatusException e) {
            saveHistory("/DynamicCalculation", "num1=" + num1 + "&num2=" + num2, null, e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    /**
     * @param pageable
     * @return
     */
    @GetMapping("/callHistory")
    public ResponseEntity<?> getHistory(Pageable pageable) {
        Page<DynamicCalculationHistoryEntity> page = historyRepository.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * Información a insertar en la tabla
     *
     * @param endpoint
     * @param parameters
     * @param response
     * @param error
     */
    private void saveHistory(String endpoint, String parameters, String response, String error) {
        DynamicCalculationHistoryEntity historyEntity = new DynamicCalculationHistoryEntity();
        historyEntity.setTimestamp(LocalDateTime.now());
        historyEntity.setEndpoint(endpoint);
        historyEntity.setParameters(parameters);
        historyEntity.setResponse(response);
        historyEntity.setError(error);
        historyRepository.save(historyEntity);
    }
}
