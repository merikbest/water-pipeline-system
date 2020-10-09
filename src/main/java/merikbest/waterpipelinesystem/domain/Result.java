package merikbest.waterpipelinesystem.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Result {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @CsvBindByName(column = "ROUTE EXISTS")
    @Column(name = "ROUTE_EXISTS")
    private String routeExists;

    @NonNull
    @CsvBindByName(column = "MIN LENGTH")
    @Column(name = "MIN_LENGTH")
    private String minLength;
}
