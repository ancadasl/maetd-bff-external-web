package es.maetd.bff.external.application.port.client;

import es.maetd.bff.external.infrastructure.adapters.dto.FileDto;
import java.util.List;

public interface FilesRepositoryPort {

    List<FileDto> getFiles();
}
