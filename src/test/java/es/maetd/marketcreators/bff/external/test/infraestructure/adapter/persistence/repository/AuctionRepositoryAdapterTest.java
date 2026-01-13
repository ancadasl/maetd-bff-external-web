package es.maetd.marketcreators.bff.external.test.infraestructure.adapter.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import es.maetd.marketcreators.bff.external.application.service.dto.AuctionResponseDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.GetAuctionsIDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.types.ProductType;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.PrimaryAuctionEntity;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.SecondaryAuctionEntity;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository.*;
import es.maetd.marketcreators.bff.external.infrastructure.mappers.AuctionInfrastructureMapper;
import es.maetd.marketcreators.bff.external.infrastructure.mappers.AuctionInfrastructureMapperImpl;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class AuctionRepositoryAdapterTest {

    public static final String CSB_123 = "CSB123";

    private static final int YEAR = 2022;

    private static final int MONTH = 2;

    @InjectMocks
    AuctionRepositoryAdapter adapter;

    @Mock
    PrimaryAuctionsRepository primaryAuctionsRepository;

    @Mock
    SecondaryAuctionsRepository secondaryAuctionsRepository;

    @Mock
    AuctionInfrastructureMapper mapper;

    private static AuctionInfrastructureMapperImpl mapperImpl;

    @BeforeAll
    static void setUp() {
        mapperImpl = new AuctionInfrastructureMapperImpl();
    }

    @Test
    void getAuctionsShould_return_a_list_of_auctions() {

        // Given
        PrimaryAuctionEntity primaryAuctionEntity = createDummyPrimaryAuctionEntity();
        SecondaryAuctionEntity secondaryAuctionEntity = createDummySecondaryAuctionEntity();

        // When
        when(primaryAuctionsRepository.findAll(any(Specification.class))).thenReturn(List.of(primaryAuctionEntity));
        when(secondaryAuctionsRepository.findAll(any(Specification.class))).thenReturn(List.of(secondaryAuctionEntity));
        when(mapper.map(any(PrimaryAuctionEntity.class))).thenReturn(mapperImpl.map(primaryAuctionEntity));
        when(mapper.map(any(SecondaryAuctionEntity.class))).thenReturn(mapperImpl.map(secondaryAuctionEntity));

        GetAuctionsIDTO idtoMock =
                GetAuctionsIDTO.builder().year(YEAR).month(MONTH).csb(CSB_123).build();

        AuctionResponseDTO result = adapter.getAuctions(idtoMock);

        // Then
        assertEquals(result.getPrimary().getCsb(), primaryAuctionEntity.getCsb());
        assertEquals(result.getSecondary().getBasket(), secondaryAuctionEntity.getBasket());

        verify(primaryAuctionsRepository, times(1)).findAll(any(Specification.class));
        verify(secondaryAuctionsRepository, times(1)).findAll(any(Specification.class));
        verify(mapper, times(1)).map(any(PrimaryAuctionEntity.class));
        verify(mapper, times(1)).map(any(SecondaryAuctionEntity.class));
    }

    public static PrimaryAuctionEntity createDummyPrimaryAuctionEntity() {
        PrimaryAuctionEntity dto = new PrimaryAuctionEntity();

        dto.setId(1L);
        dto.setCsb(CSB_123);
        dto.setEvaluationYearMonth(LocalDateTime.of(2024, 1, 1, 0, 0));
        dto.setComputableSale(1_000_000L);
        dto.setTotalSale(1_200_000L);
        dto.setPenalty(Boolean.FALSE);
        dto.setProductType(ProductType.BOND);
        return dto;
    }

    public static SecondaryAuctionEntity createDummySecondaryAuctionEntity() {
        SecondaryAuctionEntity dto = new SecondaryAuctionEntity();
        dto.setId(1L);
        dto.setCsb(CSB_123);
        dto.setEvaluationYearMonth(LocalDateTime.of(2024, 3, 1, 0, 0));
        dto.setBasket(1500.75);
        dto.setPenalty(Boolean.FALSE);
        dto.setProductType(ProductType.BOND);
        return dto;
    }
}
