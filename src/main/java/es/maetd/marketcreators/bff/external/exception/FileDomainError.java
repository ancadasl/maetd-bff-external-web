package es.maetd.marketcreators.bff.external.exception;

import es.maetd.marketcreators.ddd.core.errors.DomainError;
import es.maetd.marketcreators.ddd.core.events.domains.status.FileStatusType;
import java.util.Objects;
import lombok.Getter;
import org.hibernate.Hibernate;

@Getter
public class FileDomainError extends DomainError {

    private final String fileId;

    private final FileStatusType fileStatus;

    public FileDomainError(String errorMessage, String fileId, FileStatusType status) {
        super(errorMessage);
        this.fileId = fileId;
        this.fileStatus = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FileDomainError that = (FileDomainError) o;
        return getFileId() != null && Objects.equals(getFileId(), that.getFileId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
