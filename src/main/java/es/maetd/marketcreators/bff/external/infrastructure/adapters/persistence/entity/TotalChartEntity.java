package es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "v_total_chart")
public class TotalChartEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(name = "csb")
    private String csb;

    @Column(name = "evaluation_year_month")
    private LocalDateTime evaluationYearMonth;

    @Column(name = "total_evaluation")
    private Double totalPoints;

    @Column(name = "media")
    private Double media;
}
