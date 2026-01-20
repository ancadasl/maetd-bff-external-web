package es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "v_syndicated_issues")
public class SyndicatedIssuesEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(name = "csb")
    private String csb;

    @Column(name = "year")
    private Integer year;

    @Column(name = "month")
    private String month;

    @Column(name = "li")
    private Integer li;

    @Column(name = "size")
    private Double size;

    @Column(name = "expiration")
    private Double expiration;

    @Column(name = "currency")
    private String currency;
}
