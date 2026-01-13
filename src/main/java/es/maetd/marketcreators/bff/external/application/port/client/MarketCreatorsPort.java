package es.maetd.marketcreators.bff.external.application.port.client;

import es.maetd.marketcreators.bff.external.application.service.dto.EntityDTO;
import java.util.List;

/**
 * Interfaz para la comunicacion con los servicios correspondientes al micro ms-market-creators.
 */
public interface MarketCreatorsPort {

    /** Recover all creators
     *
     * @return the list of creators
     */
    List<EntityDTO> getCreators();
}
