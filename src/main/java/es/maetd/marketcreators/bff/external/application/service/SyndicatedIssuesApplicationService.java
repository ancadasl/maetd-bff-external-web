package es.maetd.marketcreators.bff.external.application.service;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetSyndicatedIssuesIDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.SyndicatedIssuesResponseDTO;

/**
 * Service interface for retrieving Syndicated Issues.
 * <p>
 * This interface defines the contract for application services that handle
 * operations related to Syndicated Issues in the system.
 * </p>
 */
public interface SyndicatedIssuesApplicationService {

    /**
     * Retrieves Syndicated Issues based on the specified criteria.
     *
     * @param idto the {@link GetSyndicatedIssuesIDTO} containing filter criteria such as CSB, year, and month
     * @return a {@link SyndicatedIssuesResponseDTO} containing the requested Syndicated Issues data
     */
    SyndicatedIssuesResponseDTO getSyndicatedIssues(GetSyndicatedIssuesIDTO idto);
}
