package es.maetd.marketcreators.bff.external.infrastructure.adapters.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetDocumentsIDTO {

    private Integer page;

    private Integer size;

    private String category;

    private String csb;

    private LocalDate creationDate;

    private String language;
}
