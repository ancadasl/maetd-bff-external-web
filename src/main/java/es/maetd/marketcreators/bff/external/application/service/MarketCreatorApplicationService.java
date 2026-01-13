package es.maetd.marketcreators.bff.external.application.service;

import es.maetd.marketcreators.bff.external.application.service.dto.EntityDTO;
import java.util.List;

/**
 * Service interface for retrieving Market Creator entities.
 * <p>
 * This interface defines the contract for application services that handle
 * operations related to Market Creators in the system.
 * </p>
 */
public interface MarketCreatorApplicationService {

    /**
     * Retrieves a list of all Market Creator entities.
     *
     * @return a {@link List} of {@link EntityDTO} representing all Market Creators
     */
    List<EntityDTO> getMarketCreators();
}
