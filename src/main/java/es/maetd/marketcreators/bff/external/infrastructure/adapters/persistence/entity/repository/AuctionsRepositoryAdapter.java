package es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository;

import es.maetd.marketcreators.bff.external.application.port.AuctionsRepositoryPort;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.*;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.PrimaryAuctionEntity;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.SecondaryAuctionEntity;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository.specification.PrimaryAuctionsSpecification;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository.specification.SecondaryAuctionsSpecification;
import es.maetd.marketcreators.bff.external.infrastructure.mappers.AuctionInfrastructureMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuctionsRepositoryAdapter implements AuctionsRepositoryPort {

    private final PrimaryAuctionsRepository prRepository;

    private final SecondaryAuctionsRepository scRepository;

    private final AuctionInfrastructureMapper mapper;

    @Override
    public AuctionsResponseDto getAuctions(GetAuctionsIDTO idto) {

        Specification<PrimaryAuctionEntity> primaryFilters = setPrimaryAuctionsFilters(idto);

        Specification<SecondaryAuctionEntity> secondaryFilters = setSecondaryAuctionsFilters(idto);

        AuctionDto primary = prRepository.findAll(primaryFilters).stream()
                .map(mapper::map)
                .findFirst()
                .orElse(null);

        AuctionDto secondary = scRepository.findAll(secondaryFilters).stream()
                .map(mapper::map)
                .findFirst()
                .orElse(null);

        return AuctionsResponseDto.builder()
                .primary(primary)
                .secondary(secondary)
                .build();
    }

    private Specification<PrimaryAuctionEntity> setPrimaryAuctionsFilters(GetAuctionsIDTO idto) {
        return new PrimaryAuctionsSpecification(idto.getProductType(), idto.getCsb(), idto.getYear(), idto.getMonth());
    }

    private Specification<SecondaryAuctionEntity> setSecondaryAuctionsFilters(GetAuctionsIDTO idto) {
        return new SecondaryAuctionsSpecification(
                idto.getProductType(), idto.getCsb(), idto.getYear(), idto.getMonth());
    }
}
