package es.maetd.marketcreators.bff.external.test.infraestructure.adapter.web.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.EntityDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.web.client.MarketCreatorsRepositoryRestAdapter;
import es.maetd.marketcreators.bff.external.infrastructure.mappers.MarketCreatorInfrastructureMapper;
import es.maetd.marketcreators.bff.external.infrastructure.mappers.MarketCreatorInfrastructureMapperImpl;
import es.maetd.marketcreators.clients.rest.domains.creators.model.MarketCreatorData;
import es.maetd.marketcreators.clients.rest.domains.creators.model.MarketCreatorsList;
import es.maetd.marketcreators.clients.rest.service.base.RestClient;
import es.maetd.marketcreators.clients.rest.service.dto.input.RequestIDTO;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class MarketCreatorRepositoryRestTest {

    private static final String DEFAULT_CSB = "000867";

    private static final String DEFAULT_NAME = "CaixaBank Investment Group";

    @InjectMocks
    MarketCreatorsRepositoryRestAdapter adapter;

    @Mock
    RestClient restClient;

    @Mock
    MarketCreatorInfrastructureMapper mapper;

    private static MarketCreatorInfrastructureMapper mapperImpl;

    @BeforeAll
    static void setup() {
        mapperImpl = new MarketCreatorInfrastructureMapperImpl();
    }

    @Test
    void getCreatorsShould_return_a_list_of_creators() {
        // Given
        MarketCreatorsList list = createDummyMarketCreatorList();
        ResponseEntity<MarketCreatorsList> response = new ResponseEntity(list, HttpStatus.OK);

        // When
        when(restClient.execute(any(RequestIDTO.class))).thenReturn(response);
        when(mapper.mapAsList(anyList())).thenReturn(mapperImpl.mapAsList(list.getData()));

        List<EntityDTO> result = adapter.getCreators();

        // Then
        assertEquals(list.getData().get(0).getCsb(), result.get(0).getCsb());

        verify(restClient, times(1)).execute(any(RequestIDTO.class));
        verify(mapper, times(1)).mapAsList(anyList());
    }

    private MarketCreatorsList createDummyMarketCreatorList() {
        MarketCreatorsList list = new MarketCreatorsList();
        list.data(List.of(createDummyMarketCreatorData()));
        return list;
    }

    private MarketCreatorData createDummyMarketCreatorData() {
        MarketCreatorData data = new MarketCreatorData();
        data.setCsb(DEFAULT_CSB);
        data.setName(DEFAULT_NAME);
        return data;
    }
}
