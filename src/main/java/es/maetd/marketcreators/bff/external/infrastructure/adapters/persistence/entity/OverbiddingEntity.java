package es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity;

import es.maetd.marketcreators.bff.external.application.service.dto.types.ProductType;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "v_overbidding")
public class OverbiddingEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "year")
    private Integer year;

    @Column(name = "month")
    private Integer month;

    @Column(name = "csb")
    private String csb;

    @Column(name = "penalty")
    private Double penalty;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Column(name = "average")
    private Double average;
}
