package es.maetd.marketcreators.bff.external.application.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetReportsIDTO {

    private Integer page;

    private Integer size;

    private Integer creatorId;

    private Integer month;

    private Integer year;
}
