package es.maetd.marketcreators.bff.external.test.application.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import es.maetd.marketcreators.bff.external.application.port.AuctionRepositoryPort;
import es.maetd.marketcreators.bff.external.application.service.impl.AuctionApplicationServiceImpl;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.AuctionDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.AuctionResponseDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetAuctionsIDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuctionApplicationServiceImplTest {

    public static final String CSB_123 = "CSB123";

    private static final int YEAR = 2022;

    private static final int MONTH = 2;

    @Mock
    private AuctionRepositoryPort port;

    private AuctionApplicationServiceImpl service;

    @BeforeEach
    void init() {
        service = new AuctionApplicationServiceImpl(port);
    }

    @Test
    void getAuctionShould_return_a_list_of_auctions() {
        // Given
        AuctionDTO auctionDTO = createDummyAuctionDTO();
        AuctionResponseDTO responseDTO = AuctionResponseDTO.builder()
                .primary(auctionDTO)
                .secondary(auctionDTO)
                .build();

        GetAuctionsIDTO getAuctionsIDTO =
                GetAuctionsIDTO.builder().year(YEAR).month(MONTH).csb(CSB_123).build();

        // When
        when(port.getAuctions(any(GetAuctionsIDTO.class))).thenReturn(responseDTO);

        AuctionResponseDTO result = service.getAuctions(getAuctionsIDTO);

        // Then
        assertEquals(result.getPrimary().getCsb(), auctionDTO.getCsb());
        assertEquals(result.getSecondary().getCsb(), auctionDTO.getCsb());

        verify(port, times(1)).getAuctions(any(GetAuctionsIDTO.class));
    }

    private AuctionDTO createDummyAuctionDTO() {
        AuctionDTO dto = new AuctionDTO();
        dto.setCsb("CSB001");
        dto.setYear(2024L);
        dto.setMonth(1L);
        dto.setComputableSale(500L);
        dto.setTotalSale(600L);
        dto.setPenalty(false);
        dto.setBasket(1200.0);
        return dto;
    }
}
