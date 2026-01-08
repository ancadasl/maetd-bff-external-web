package es.maetd.marketcreators.bff.external.infrastructure.adapters.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
public class DocumentDataResponseDTO {

    private List<DocumentDTO> list;

    private Integer count;
}
