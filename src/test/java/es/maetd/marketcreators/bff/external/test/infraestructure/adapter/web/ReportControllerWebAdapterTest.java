package es.maetd.marketcreators.bff.external.test.infraestructure.adapter.web;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import es.maetd.marketcreators.bff.external.application.mappers.ReportApplicationMapper;
import es.maetd.marketcreators.bff.external.application.mappers.ReportApplicationMapperImpl;
import es.maetd.marketcreators.bff.external.application.service.dto.ReportDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.sftp.ReportTransferSftpService;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.web.ReportControllerWebAdapter;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.ReportsDataListWDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ReportControllerWebAdapterTest {

    @Mock
    private ReportTransferSftpService service;

    @Mock
    private ReportApplicationMapper mapper;

    @InjectMocks
    private ReportControllerWebAdapter controller;

    private static ReportApplicationMapper mapperImpl;

    @BeforeAll
    static void setUp() {
        mapperImpl = new ReportApplicationMapperImpl();
    }

    @Test
    void whenGetReportListIsCalled_thenResponseShouldBeOk() {
        // given
        ReportDTO dto = createDummyReportDTO();

        // when
        when(service.getReports()).thenReturn(List.of(dto));
        when(mapper.map(anyList())).thenReturn(mapperImpl.map(List.of(dto)));

        // then
        ResponseEntity<ReportsDataListWDTO> response = controller.getReports();

        Assertions.assertEquals(
                dto.getName(),
                Objects.requireNonNull(response.getBody()).getReports().get(0).getName());

        verify(service, times(1)).getReports();
        verify(mapper, times(1)).map(anyList());
    }

    @Test
    void testGetReportData() {
        // Given
        byte[] data = "contenido-de-prueba".getBytes();

        ReportDTO dto = createDummyReportDTO();

        dto.setData(data);

        when(service.downloadReport(dto.getName())).thenReturn(dto);

        // When
        ResponseEntity<Resource> response = controller.getReportData(dto.getName());

        // Then
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());

        Assertions.assertEquals(
                "attachment; filename=\"fileTest.txt\"",
                response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));

        Assertions.assertEquals(data.length, response.getHeaders().getContentLength());
        Assertions.assertEquals(
                MediaType.APPLICATION_OCTET_STREAM, response.getHeaders().getContentType());

        Assertions.assertNotNull(response.getBody());
        Assertions.assertDoesNotThrow(
                () -> Assertions.assertEquals(data.length, response.getBody().contentLength()));

        verify(service, times(1)).downloadReport(dto.getName());
    }

    private ReportDTO createDummyReportDTO() {
        return ReportDTO.builder()
                .data(new byte[0])
                .name("fileTest.txt")
                .path("/path")
                .build();
    }
}
