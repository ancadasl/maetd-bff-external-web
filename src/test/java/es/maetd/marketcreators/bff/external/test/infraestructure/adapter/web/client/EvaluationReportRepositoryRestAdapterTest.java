package es.maetd.marketcreators.bff.external.test.infraestructure.adapter.web.client;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import es.maetd.marketcreators.bff.external.application.service.dto.*;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.web.client.EvaluationReportRepositoryRestAdapter;
import es.maetd.marketcreators.bff.external.infrastructure.mappers.ReportInfrastructureMapper;
import es.maetd.marketcreators.bff.external.infrastructure.mappers.ReportInfrastructureMapperImpl;
import es.maetd.marketcreators.clients.rest.domains.evaluations.model.ReportData;
import es.maetd.marketcreators.clients.rest.domains.evaluations.model.ReportList;
import es.maetd.marketcreators.clients.rest.service.base.RestClient;
import es.maetd.marketcreators.clients.rest.service.dto.input.RequestIDTO;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class EvaluationReportRepositoryRestAdapterTest {

    @InjectMocks
    EvaluationReportRepositoryRestAdapter adapter;

    @Mock
    RestClient restClient;

    @Mock
    ReportInfrastructureMapper mapper;

    private static ReportInfrastructureMapper mapperImpl;

    @BeforeAll
    static void setUp() {
        mapperImpl = new ReportInfrastructureMapperImpl();
    }

    @Test
    void getReports_returnsOK() {

        // Given
        ReportList reportList = new ReportList();
        ReportData file = createDummyDocumentReportData();
        reportList.setReports(List.of(file));
        ResponseEntity<ReportList> response = new ResponseEntity<>(reportList, HttpStatus.OK);

        // When
        when(restClient.execute(any(RequestIDTO.class))).thenReturn(response);
        when(mapper.mapAsList(anyList())).thenReturn(mapperImpl.mapAsList(reportList.getReports()));

        GetReportsIDTO input =
                GetReportsIDTO.builder().creatorId(1).page(0).size(10).build();

        ReportResponseDTO result = adapter.getReports(input);

        // Then
        Assertions.assertEquals(file.getFileName(), result.getList().get(0).getFileName());

        verify(restClient, times(1)).execute(any(RequestIDTO.class));
        verify(mapper, times(1)).mapAsList(anyList());
    }

    private ReportData createDummyDocumentReportData() {

        ReportData fileData = new ReportData();
        fileData.setFileName("report_2024_01.csv");
        fileData.setFilePath("/reports/2024/01/");
        fileData.setCreator(123);
        fileData.setSize(new BigDecimal("1024.50"));
        return fileData;
    }
}
