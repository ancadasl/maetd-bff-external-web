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
@Table(name = "v_secondary_auctions")
public class SecondaryAuctionEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(name = "csb")
    private String csb;

    @Column(name = "evaluation_year_month")
    private LocalDateTime evaluationYearMonth;

    @Column(name = "basket")
    private Double basket;

    @Column(name = "penalty")
    private Boolean penalty;

    @Column(name = "product_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType productType;
}
