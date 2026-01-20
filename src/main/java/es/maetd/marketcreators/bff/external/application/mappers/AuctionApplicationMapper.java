package es.maetd.marketcreators.bff.external.application.mappers;

import es.maetd.marketcreators.bff.external.application.service.dto.AuctionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.openapitools.model.AuctionDataWDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface AuctionApplicationMapper {

    AuctionDataWDTO map(AuctionDTO dto);
}
