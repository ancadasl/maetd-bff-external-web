package es.maetd.marketcreators.bff.external.application.mappers;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.AuctionDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.openapitools.model.AuctionDataWDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface AuctionApplicationMapper {

    List<AuctionDataWDTO> map(List<AuctionDto> dto);

    AuctionDataWDTO map(AuctionDto dto);
}
