package es.maetd.marketcreators.bff.external.application.service.dto;

import es.maetd.marketcreators.bff.external.application.service.dto.types.EvaluationStatus;
import es.maetd.marketcreators.bff.external.application.service.dto.types.ProductType;
import java.io.Serial;
import java.io.Serializable;
import lombok.*;

@Data
public class EvaluationDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long evaluationId;

    private EvaluationStatus status;

    private String csb;

    private Long year;

    private String month;

    private Long monthNumeric;

    private Long jointMonthly;

    private Long cumulative;

    private Long letter;

    private Long bonds;

    private Long subscription;

    private Long quoting;

    private Long trading;

    private Long electronic;

    private Long tier;

    private Long clients;

    private Long strips;

    private Long qualitative;

    private Double secondRound;

    private ProductType productType;
}
