package es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.types.ProductType;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "v_primary_chart")
public class PrimaryChartEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(name = "csb")
    private String csb;

    @Column(name = "evaluation_year_month")
    private LocalDateTime evaluationYearMonth;

    @Column(name = "total_points")
    private Double totalPoints;

    @Column(name = "media")
    private Double media;

    @Column(name = "product_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType productType;
}
