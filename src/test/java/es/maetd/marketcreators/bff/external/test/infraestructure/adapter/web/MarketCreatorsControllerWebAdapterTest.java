package es.maetd.marketcreators.bff.external.test.infraestructure.adapter.web;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import es.maetd.marketcreators.bff.external.application.mappers.MarketCreatorApplicationMapper;
import es.maetd.marketcreators.bff.external.application.mappers.MarketCreatorApplicationMapperImpl;
import es.maetd.marketcreators.bff.external.application.service.MarketCreatorApplicationService;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.EntityDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.web.MarketCreatorsControllerWebAdapter;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.MarketCreatorDataWDTO;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class MarketCreatorsControllerWebAdapterTest {

    @InjectMocks
    private MarketCreatorsControllerWebAdapter adapter;

    @Mock
    private MarketCreatorApplicationService service;

    @Mock
    private MarketCreatorApplicationMapper mapper;

    private static final Integer MARKET_CREATOR1_CSB = 3851;

    private static final String MARKET_CREATOR1_DESCRIPTION = "CREDIT AGRICOLE";

    private static MarketCreatorApplicationMapper mapperImpl;

    @BeforeAll
    static void setup() {
        mapperImpl = new MarketCreatorApplicationMapperImpl();
    }

    @Test
    void getMarketCreatorsShould_return_a_list_of_creators() {
        // Given
        List<EntityDTO> list = List.of(createDummyEntityModel());
        // When
        when(service.getMarketCreators()).thenReturn(list);

        when(mapper.mapAsList(anyList())).thenReturn(mapperImpl.mapAsList(list));

        ResponseEntity<List<MarketCreatorDataWDTO>> result = adapter.getMarketCreators();

        // Then
        assertAll(
                () -> assertEquals(list.get(0).getCsb(), result.getBody().get(0).getCsb()),
                () -> verify(service, times(1)).getMarketCreators(),
                () -> verify(mapper, times(1)).mapAsList(anyList()));
    }

    private EntityDTO createDummyEntityModel() {
        EntityDTO model = new EntityDTO();
        model.setName(MARKET_CREATOR1_DESCRIPTION);
        model.setCsb(MARKET_CREATOR1_CSB.toString());
        return model;
    }
}
