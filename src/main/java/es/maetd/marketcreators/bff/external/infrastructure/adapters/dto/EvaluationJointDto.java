package es.maetd.marketcreators.bff.external.infrastructure.adapters.dto;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.types.EvaluationStatus;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EvaluationJointDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private EvaluationStatus status;

    private String csb;

    private Long year;

    private Long monthNumeric;

    private Long jointMonthly;

    private Long cumulative;

    private Long letter;

    private Long bonds;
}
