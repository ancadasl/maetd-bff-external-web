package es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.types.EvaluationStatus;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.types.ProductType;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository.converter.ProductTypeConverter;
import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "v_vista_creadores")
public class EvaluationEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(name = "evaluation_id")
    private Long evaluationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EvaluationStatus status;

    @Column(name = "csb")
    private Long csb;

    @Column(name = "year")
    private Long year;

    @Column(name = "month")
    private String month;

    @Column(name = "month_numeric")
    private Long monthNumeric;

    @Column(name = "joint_monthly")
    private Long jointMonthly;

    @Column(name = "cumulative")
    private Long cumulative;

    @Column(name = "letter")
    private Long letter;

    @Column(name = "bonds")
    private Long bonds;

    @Column(name = "subscription")
    private Long subscription;

    @Column(name = "quoting")
    private Long quoting;

    @Column(name = "trading")
    private Long trading;

    @Column(name = "electronic")
    private Long electronic;

    @Column(name = "tier")
    private Long tier;

    @Column(name = "clients")
    private Long clients;

    @Column(name = "strips")
    private Long strips;

    @Column(name = "qualitative")
    private Long qualitative;

    @Column(name = "second_round")
    private Double secondRound;

    @Column(name = "product_type", nullable = false)
    @Convert(converter = ProductTypeConverter.class)
    private ProductType productType;
}
