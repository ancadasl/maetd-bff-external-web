package es.maetd.marketcreators.bff.external.application.mappers;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.EntityDTO;
import es.maetd.marketcreators.common.mappers.BaseMapper;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.openapitools.model.MarketCreatorDataWDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MarketCreatorApplicationMapper extends BaseMapper {

    MarketCreatorDataWDTO map(EntityDTO marketCreatorModel);

    List<MarketCreatorDataWDTO> mapAsList(List<EntityDTO> list);
}
