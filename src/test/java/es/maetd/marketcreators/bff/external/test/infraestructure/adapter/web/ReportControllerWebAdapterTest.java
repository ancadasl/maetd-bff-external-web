package es.maetd.marketcreators.bff.external.test.infraestructure.adapter.web;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import es.maetd.marketcreators.bff.external.application.mappers.ReportApplicationMapper;
import es.maetd.marketcreators.bff.external.application.mappers.ReportApplicationMapperImpl;
import es.maetd.marketcreators.bff.external.application.service.ReportApplicationService;
import es.maetd.marketcreators.bff.external.application.service.dto.*;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.sftp.ReportTransferSftpService;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.web.ReportControllerWebAdapter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    public static final int BANKINTER = 15;

    @Mock
    private ReportTransferSftpService sftpService;

    @Mock
    private ReportApplicationService service;

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
        ReportResponseDTO resDocument =
                ReportResponseDTO.builder().list(List.of(dto)).build();

        Integer page = 0;
        Integer size = 15;
        Integer creationId = BANKINTER;
        Optional<Integer> year = Optional.of(2025);
        Optional<Integer> month = Optional.of(8);

        // when
        when(service.getReports(any(GetReportsIDTO.class))).thenReturn(resDocument);
        when(mapper.map(anyList())).thenReturn(mapperImpl.map(List.of(dto)));

        // then
        ResponseEntity<ReportsDataListWDTO> response = controller.getReports(page, size, creationId, year, month);

        Assertions.assertEquals(
                dto.getFileName(),
                Objects.requireNonNull(response.getBody()).getReports().get(0).getFileName());

        verify(service, times(1)).getReports(any(GetReportsIDTO.class));
        verify(mapper, times(1)).map(anyList());
    }

    @Test
    void testGetReportData() {
        // Given
        byte[] data = "contenido-de-prueba".getBytes();

        ReportDTO dto = createDummyReportDTO();

        dto.setData(data);

        when(sftpService.downloadReport(dto.getFileName())).thenReturn(dto);

        // When
        ResponseEntity<Resource> response = controller.getReportData(dto.getFileName());

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

        verify(sftpService, times(1)).downloadReport(dto.getFileName());
    }

    private ReportDTO createDummyReportDTO() {
        return ReportDTO.builder()
                .data(new byte[0])
                .fileName("fileTest.txt")
                .filePath("/path")
                .build();
    }
}
