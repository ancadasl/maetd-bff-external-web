package es.maetd.marketcreators.bff.external.infrastructure.mappers;

import es.maetd.marketcreators.bff.external.application.service.dto.AuctionDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.PrimaryAuctionEntity;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.SecondaryAuctionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface AuctionInfrastructureMapper {

    AuctionDTO map(PrimaryAuctionEntity primaryAuction);

    AuctionDTO map(SecondaryAuctionEntity primaryAuction);
}
