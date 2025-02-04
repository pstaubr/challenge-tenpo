package cl.desafio.backend.repository;


import cl.desafio.backend.entity.DynamicCalculationHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Pablo Staub Ramirez
 */
public interface CalculationHistoryRepository extends JpaRepository<DynamicCalculationHistoryEntity, Long> {
}
