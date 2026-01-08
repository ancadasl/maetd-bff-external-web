package es.maetd.marketcreators.bff.external.application.service;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.DocumentDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.DocumentDataResponseDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetDocumentsIDTO;
import java.util.UUID;

/**
 * Application service interface for managing and retrieving document-related information.
 * <p>
 * This service defines the contract for operations involving external document data,
 * typically serving as a bridge between the presentation layer (e.g., REST controllers)
 * and the domain or infrastructure layers.
 * </p>
 *
 * <p>
 * Implementations of this interface are responsible for orchestrating business logic,
 * data access, and integration with external systems or storage services.
 * </p>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * GetDocumentsIDTO request = new GetDocumentsIDTO();
 * request.setDocumentId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
 *
 * DocumentDataResponseDto response = documentApplicationService.getFiles(request);
 * System.out.println("Document name: " + response.getDocumentName());
 * }</pre>
 *
 * @author Oliver Muñoz
 * @version 1.0
 * @since 2025-11
 */
public interface DocumentApplicationService {

    /**
     * Retrieves document data based on the provided input parameters.
     * <p>
     * This method receives an input DTO containing one or more document identifiers or
     * filters, and returns a response DTO containing the corresponding document metadata
     * and content, if available.
     * </p>
     *
     * @param idto the input DTO containing the identifiers or filters for retrieving documents
     * @return a {@link DocumentDataResponseDTO} containing document details and content
     * @throws IllegalArgumentException if the input data is invalid or incomplete
     * @throws RuntimeException if an unexpected error occurs during data retrieval
     */
    DocumentDataResponseDTO getFiles(GetDocumentsIDTO idto);

    /**
     * Retrieves detailed information about a single document identified by its unique ID.
     * <p>
     * This method returns metadata and additional information related to the specified
     * document. It is typically used in scenarios where only a single document needs to be
     * fetched or displayed, such as detail views or validation steps.
     * </p>
     *
     * @param fileId the unique identifier of the document to retrieve
     * @return a {@link DocumentDTO} containing metadata and document-related information
     * @throws IllegalArgumentException if {@code fileId} is {@code null}
     * @throws RuntimeException if the document cannot be retrieved or an unexpected error occurs
     */
    DocumentDTO getDocumentData(UUID fileId);
}
