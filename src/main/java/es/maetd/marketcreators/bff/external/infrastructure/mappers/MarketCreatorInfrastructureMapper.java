package es.maetd.marketcreators.bff.external.infrastructure.mappers;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.EntityDTO;
import es.maetd.marketcreators.clients.rest.domains.creators.model.MarketCreatorData;
import java.util.List;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface MarketCreatorInfrastructureMapper {

    @InheritInverseConfiguration
    MarketCreatorData map(EntityDTO model);

    List<EntityDTO> mapAsList(List<MarketCreatorData> list);
}
