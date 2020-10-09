package merikbest.waterpipelinesystem.repository;

import merikbest.waterpipelinesystem.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
}
