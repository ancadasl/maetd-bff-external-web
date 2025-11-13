package es.maetd.bff.external.infrastructure.adapters.dto;

import es.maetd.marketcreators.ddd.core.events.domains.files.CategoryType;
import es.maetd.marketcreators.ddd.core.events.domains.files.FileType;
import es.maetd.marketcreators.ddd.core.events.domains.files.OriginSystemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDocumentIDTO {

    private String documentName;

    private byte[] documentData;

    private FileType fileType;

    private CategoryType categoryType;

    private OriginSystemType originSystem;
}
