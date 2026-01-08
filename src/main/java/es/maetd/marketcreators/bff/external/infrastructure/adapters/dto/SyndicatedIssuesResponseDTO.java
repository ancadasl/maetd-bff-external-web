package es.maetd.marketcreators.bff.external.infrastructure.adapters.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
@Getter
@Setter
public class SyndicatedIssuesResponseDTO {

    private List<SyndicatedIssuesDTO> list;
}
