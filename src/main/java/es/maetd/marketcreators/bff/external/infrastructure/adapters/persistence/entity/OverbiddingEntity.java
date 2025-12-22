package es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "v_overbidding")
public class OverbiddingEntity implements Serializable {

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

    @Column(name = "type")
    private String type;

    @Column(name = "average")
    private Double average;
}
