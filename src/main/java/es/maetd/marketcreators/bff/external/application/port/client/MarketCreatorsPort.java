package es.maetd.marketcreators.bff.external.application.port.client;

import es.maetd.marketcreators.bff.external.application.service.dto.EntityDTO;
import java.util.List;

/**
 * Outbound Port interface for communication with the services
 * corresponding to the "ms-market-creators" microservice.
 *
 * <p>
 * Part of a hexagonal architecture, decoupling application logic
 * from external service calls.
 * </p>
 */
public interface MarketCreatorsPort {

    /**
     * Retrieves all market creators from the external microservice.
     *
     * @return a {@link List} of {@link EntityDTO} representing all creators.
     */
    List<EntityDTO> getCreators();
}
