package es.maetd.marketcreators.bff.external.infrastructure.adapters.dto;

import es.maetd.marketcreators.ddd.core.events.domains.files.CategoryType;
import es.maetd.marketcreators.ddd.core.events.domains.files.FileType;
import es.maetd.marketcreators.ddd.core.events.domains.files.OriginSystemType;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Builder
public class DocumentDTO {

    private UUID id;
    private byte[] data;

    @NotNull
    private String documentName;

    @NotNull
    private FileType fileType;

    @NotNull
    private CategoryType categoryType;

    @NotNull
    private String csb;

    private URI path;
    private LocalDateTime creationDate;
    private String hashcode;
    private OriginSystemType originSystem;
    private String description;

    // Método auxiliar para establecer la fecha de creación automáticamente si no se provee

    /**
     *
     * @return
     */
    public LocalDateTime getCreationDate() {
        if (creationDate == null) {
            creationDate = LocalDateTime.now();
        }
        return creationDate;
    }
}
