package es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository;

import es.maetd.marketcreators.bff.external.application.port.AuctionRepositoryPort;
import es.maetd.marketcreators.bff.external.application.service.dto.AuctionDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.AuctionResponseDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.GetAuctionsIDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.PrimaryAuctionEntity;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.SecondaryAuctionEntity;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository.specification.PrimaryAuctionSpecification;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository.specification.SecondaryAuctionSpecification;
import es.maetd.marketcreators.bff.external.infrastructure.mappers.AuctionInfrastructureMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuctionRepositoryAdapter implements AuctionRepositoryPort {

    private final PrimaryAuctionsRepository prRepository;

    private final SecondaryAuctionsRepository scRepository;

    private final AuctionInfrastructureMapper mapper;

    @Override
    public AuctionResponseDTO getAuctions(GetAuctionsIDTO idto) {

        Specification<PrimaryAuctionEntity> primaryFilters = setPrimaryAuctionsFilters(idto);

        Specification<SecondaryAuctionEntity> secondaryFilters = setSecondaryAuctionsFilters(idto);

        AuctionDTO primary = prRepository.findAll(primaryFilters).stream()
                .map(mapper::map)
                .findFirst()
                .orElse(null);

        AuctionDTO secondary = scRepository.findAll(secondaryFilters).stream()
                .map(mapper::map)
                .findFirst()
                .orElse(null);

        return AuctionResponseDTO.builder()
                .primary(primary)
                .secondary(secondary)
                .build();
    }

    private Specification<PrimaryAuctionEntity> setPrimaryAuctionsFilters(GetAuctionsIDTO idto) {
        return new PrimaryAuctionSpecification(idto.getProductType(), idto.getCsb(), idto.getYear(), idto.getMonth());
    }

    private Specification<SecondaryAuctionEntity> setSecondaryAuctionsFilters(GetAuctionsIDTO idto) {
        return new SecondaryAuctionSpecification(idto.getProductType(), idto.getCsb(), idto.getYear(), idto.getMonth());
    }
}
