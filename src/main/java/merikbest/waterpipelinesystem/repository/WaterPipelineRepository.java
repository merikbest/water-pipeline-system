package merikbest.waterpipelinesystem.repository;

import merikbest.waterpipelinesystem.domain.WaterPipeline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaterPipelineRepository extends JpaRepository<WaterPipeline, Long> {
}
