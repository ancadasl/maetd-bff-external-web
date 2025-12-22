package es.maetd.marketcreators.bff.external.application.service;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.AuctionsResponseDto;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetAuctionsIDTO;

/**
 * Application service responsible for handling auction-related use cases.
 * <p>
 * This service acts as the entry point for the application layer, orchestrating
 * the required domain logic and interactions with external systems or adapters
 * to retrieve auction information.
 * </p>
 */
public interface AuctionApplicationService {

    /**
     * Retrieves auctions based on the provided input data.
     *
     * @param idto input data transfer object containing the criteria
     *             required to obtain the auctions
     * @return a {@link AuctionsResponseDto} containing the list of auctions
     *         and related metadata
     */
    AuctionsResponseDto getAuctions(GetAuctionsIDTO idto);
}
