package es.maetd.marketcreators.bff.external.infrastructure.adapters.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetSyndicatedIssuesIDTO {

    private Long csb;

    private Integer month;

    private Integer year;
}
