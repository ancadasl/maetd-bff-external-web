package es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity;

import es.maetd.marketcreators.bff.external.application.service.dto.types.ProductType;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "v_secondary_chart")
public class SecondaryChartEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(name = "csb")
    private String csb;

    @Column(name = "evaluation_year_month")
    private LocalDateTime evaluationYearMonth;

    @Column(name = "total_points")
    private String totalPoints;

    @Column(name = "media")
    private Double media;

    @Column(name = "product_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType productType;
}
