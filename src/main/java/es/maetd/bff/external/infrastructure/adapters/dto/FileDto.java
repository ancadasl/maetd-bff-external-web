package es.maetd.bff.external.infrastructure.adapters.dto;

import es.maetd.marketcreators.ddd.core.events.domains.files.FileType;
import es.maetd.marketcreators.ddd.core.events.domains.files.OriginSystemType;
import es.maetd.marketcreators.ddd.core.events.domains.status.FileStatusType;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * Represents a file in the system with information such as its ID, filename, type, status,
 * and associated metadata like creation date, origin, and hash code.
 *
 * <p>This model is used to handle and process file-related data, including the computation of file hashes,
 * file status management, and reprocessing logic.</p>
 */
@Slf4j
@Getter
@Setter
public class FileDto {

    /**
     * The unique identifier for the file.
     */
    private UUID id;

    /**
     * The raw byte data of the file.
     * This can represent the contents of the file.
     */
    // @NotEmpty  // Validation can be applied here if needed
    private byte[] data;

    /**
     * The name of the file.
     * This field is mandatory and should not be null.
     */
    @NotNull
    private String filename;

    /**
     * The type of the file (e.g., CSV, PDF, etc.).
     * This field is mandatory and should not be null.
     */
    @NotNull
    private FileType fileType;

    /**
     * The URI pointing to the file's storage location.
     * This can be a file system path or an external URL.
     */
    private URI path;

    /**
     * The date and time when the file was created.
     * This is automatically set during file creation.
     */
    private LocalDateTime creationDate;

    /**
     * The hash code of the file, calculated from its byte data.
     * The hash is generated using the SHA3-256 algorithm.
     */
    private String hashCode;

    /**
     * The origin system type from which the file was sourced.
     */
    private OriginSystemType originSystem;

    /**
     * The current status of the file (e.g., FORMAT_KO, PROCESSED).
     */
    private FileStatusType status;

    /**
     * A description of the file's status.
     * This can provide additional information about the current file state.
     */
    private String descriptionStatus;

    /**
     * Constructs a new FileModel with the specified details.
     * The creation date is set automatically, and the hash code is computed from the file data.
     *
     * @param data the byte data of the file
     * @param path the URI pointing to the file location
     * @param filename the name of the file
     * @param fileType the type of the file
     * @param originSystem the origin system type
     * @param status the status of the file
     * @param descriptionStatus a description of the file's status
     */
    public FileDto(
            byte[] data,
            URI path,
            String filename,
            FileType fileType,
            OriginSystemType originSystem,
            FileStatusType status,
            String descriptionStatus) {
        this.data = data;
        this.path = path;
        this.filename = filename;
        this.fileType = fileType;
        this.originSystem = originSystem;
        this.creationDate = LocalDateTime.now();
        this.status = status;
        this.descriptionStatus = descriptionStatus;
        this.computeHashcode();
    }

    /**
     * Computes the hash code of the file based on its byte data.
     * This uses the SHA3-256 algorithm to generate the hash.
     * If the data is null or empty, a warning is logged.
     */
    private void computeHashcode() {
        if (Objects.nonNull(data) && data.length > 0) {
            this.hashCode = new DigestUtils("SHA3-256").digestAsHex(data);
        } else {
            log.warn("No se puede calcular el hashcode si los datos del fichero son nulos o vacíos.");
        }
    }

    /**
     * Determines if the file is reprocessable based on its status.
     *
     * <p>Files with statuses such as FORMAT_KO, CREATORS_KO, or PERSISTED are considered reprocessable.</p>
     *
     * @return true if the file status is FORMAT_KO, CREATORS_KO, or PERSISTED; false otherwise
     */
    public boolean isReprocessable() {
        return status == FileStatusType.CREATORS_KO
                || status == FileStatusType.PERSISTED
                || status == FileStatusType.EXTERNAL_KO;
    }

    /**
     * Determines if the file can be deleted based on its status.
     *
     * <p>Files with statuses such as FORMAT_KO, NOT_PERSISTED, DUPLICATED, and PERSISTED can be deleted.</p>
     *
     * @return true if the file status is one of the deletable statuses; false otherwise
     */
    public boolean isDeletable() {
        return status == FileStatusType.FORMAT_KO
                || status == FileStatusType.NOT_PERSISTED
                || status == FileStatusType.DUPLICATED
                || status == FileStatusType.PERSISTED
                || status == FileStatusType.PROCESSED_KO;
    }

    /**
     * Checks if the file model has a status of DELETED.
     *
     * @return true if the file status is DELETED, false otherwise
     */
    public boolean isFileDeleted() {
        return status == FileStatusType.DELETED;
    }
}
