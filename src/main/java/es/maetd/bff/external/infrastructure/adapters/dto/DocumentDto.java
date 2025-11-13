package es.maetd.bff.external.infrastructure.adapters.dto;

import es.maetd.marketcreators.ddd.core.events.domains.files.CategoryType;
import es.maetd.marketcreators.ddd.core.events.domains.files.FileType;
import es.maetd.marketcreators.ddd.core.events.domains.files.OriginSystemType;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Data Transfer Object (DTO) that represents a document within the system.
 * <p>
 * This class encapsulates information about a document or file, including its
 * identifier, name, type, category, origin, creation date, storage path, and
 * optional binary content.
 * </p>
 *
 * <p>
 * The {@code DocumentDto} is primarily used for transferring file-related
 * metadata and content between different system layers or services. It may be
 * used to create, update, or retrieve document data from external systems.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * DocumentDto document = new DocumentDto(
 *     fileBytes,
 *     URI.create("https://storage.server.com/docs/file.pdf"),
 *     "file.pdf",
 *     FileType.PDF,
 *     OriginSystemType.EXTERNAL,
 *     FileStatusType.PROCESSED,
 *     "File successfully processed"
 * );
 * }</pre>
 *
 * <p>
 * The {@code creationDate} is automatically set at instantiation time.
 * </p>
 *
 * @author
 * @version 1.1
 */
@Slf4j
@Getter
@Setter
public class DocumentDto {

    /**
     * Unique identifier of the document.
     */
    private UUID id;

    /**
     * Binary data of the document (optional).
     * <p>
     * Represents the raw byte content of the file, which may be stored locally
     * or externally.
     * </p>
     */
    private byte[] data;

    /**
     * Name of the document (e.g., "report.pdf").
     * <p>
     * This field is mandatory.
     * </p>
     */
    @NotNull
    private String documentName;

    /**
     * Type of file (e.g., CSV, PDF, XML).
     * <p>
     * This field is mandatory.
     * </p>
     */
    @NotNull
    private FileType fileType;

    /**
     * Category type associated with the file (e.g., NORMATIVE, ACTAS_LETTER).
     * <p>
     * This field is mandatory.
     * </p>
     */
    @NotNull
    private CategoryType categoryType;

    /**
     * CSB code of the document creator.
     * <p>
     * This field is mandatory and identifies the user or system responsible
     * for document creation.
     * </p>
     */
    @NotNull
    private String csb;

    /**
     * URI of the file storage location.
     * <p>
     * It may represent a file system path or a remote URL.
     * </p>
     */
    private URI path;

    /**
     * Date and time when the document was created.
     * <p>
     * Automatically set when the object is instantiated.
     * </p>
     */
    private LocalDateTime creationDate;

    /**
     * SHA3-256 hash code computed from the file data, used to verify file integrity.
     */
    private String hashcode;

    /**
     * System of origin for the document (e.g., INTERNAL, EXTERNAL).
     */
    private OriginSystemType originSystem;

    /**
     * Short description about files content
     */
    private String description;

    /**
     * Constructs a new {@code DocumentDto} with the specified details.
     * <p>
     * The creation date is automatically set to the current timestamp.
     * </p>
     *
     * @param data           the binary data of the file
     * @param path           the URI pointing to the file's storage location
     * @param documentName       the name of the file
     * @param fileType       the file type
     * @param originSystem   the origin system type
     * @param description   Short description
     */
    public DocumentDto(
            byte[] data,
            URI path,
            String documentName,
            FileType fileType,
            OriginSystemType originSystem,
            String description) {
        this.data = data;
        this.path = path;
        this.documentName = documentName;
        this.fileType = fileType;
        this.originSystem = originSystem;
        this.creationDate = LocalDateTime.now();
        this.description = description;
    }
}
