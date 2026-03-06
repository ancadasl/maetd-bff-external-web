package es.maetd.marketcreators.bff.external.application.port.client;

import es.maetd.marketcreators.bff.external.application.service.dto.DocumentDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.DocumentDataResponseDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.GetDocumentsIDTO;
import java.util.UUID;

/**
 * Outbound Port interface that defines the contract for
 * managing documents in external systems.
 *
 * <p>
 * Provides operations to list documents based on search criteria
 * and to retrieve the binary data of a specific document identified
 * by its UUID.
 * </p>
 *
 * <p>
 * Part of a hexagonal architecture, decoupling business logic
 * from data access or remote service implementation details.
 * </p>
 */
public interface DocumentRepositoryPort {

    /**
     * Retrieves a list of documents filtered according to the criteria
     * specified in {@link GetDocumentsIDTO}.
     *
     * @param idto DTO containing search criteria (page, size, category, etc.).
     *             Must not be {@code null}.
     * @return {@link DocumentDataResponseDTO} containing the list of documents
     *         and pagination or total count information.
     */
    DocumentDataResponseDTO getDocuments(GetDocumentsIDTO idto);

    /**
     * Retrieves the binary data of a specific document identified by its UUID.
     *
     * @param fileId UUID of the document to retrieve. Must not be {@code null}.
     * @return {@link DocumentDTO} containing the document name and binary content.
     * @throws RuntimeException if the document cannot be retrieved or
     *                          if an error occurs in the remote service.
     */
    DocumentDTO getDocumentData(UUID fileId);
}
