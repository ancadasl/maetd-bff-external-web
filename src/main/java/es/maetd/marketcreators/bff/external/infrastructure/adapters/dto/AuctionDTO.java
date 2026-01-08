package es.maetd.marketcreators.bff.external.infrastructure.adapters.dto;

import lombok.Data;

@Data
public class AuctionDTO {

    private String csb;

    private Long year;

    private Long month;

    private Long computableSale;

    private Long totalSale;

    private Boolean penalty;

    private Double basket;
}
