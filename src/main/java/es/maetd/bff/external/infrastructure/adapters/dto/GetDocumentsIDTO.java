package es.maetd.bff.external.infrastructure.adapters.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetDocumentsIDTO {

    private Integer page;

    private Integer size;

    private String category;

    private String csb;

    private LocalDate creationDate;

    private String language;
}
