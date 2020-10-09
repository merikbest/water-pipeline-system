package merikbest.waterpipelinesystem.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class WaterPipeline {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @CsvBindByName
    private int idX;

    @NonNull
    @CsvBindByName
    private int idY;

    @NonNull
    @CsvBindByName
    private int length;
}