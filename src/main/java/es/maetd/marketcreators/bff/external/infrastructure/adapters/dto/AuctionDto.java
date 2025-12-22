package es.maetd.marketcreators.bff.external.infrastructure.adapters.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class AuctionDto implements Serializable {

    private String csb;

    private Long year;

    private Long month;

    private Long computableSale;

    private Long totalSale;

    private Boolean penalty;

    private Double basket;
}
