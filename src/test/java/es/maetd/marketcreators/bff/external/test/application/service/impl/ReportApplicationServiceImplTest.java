package es.maetd.marketcreators.bff.external.test.application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import es.maetd.marketcreators.bff.external.application.port.client.BlindMarketPort;
import es.maetd.marketcreators.bff.external.application.service.dto.GetReportsIDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.ReportDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.ReportResponseDTO;
import es.maetd.marketcreators.bff.external.application.service.impl.ReportApplicationServiceImpl;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.web.client.ReportTransferSftpRestAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportApplicationServiceImplTest {

    @Mock
    private BlindMarketPort repositoryPort;

    @Mock
    private ReportTransferSftpRestAdapter sftpService;

    @InjectMocks
    private ReportApplicationServiceImpl service;

    @Test
    void shouldDelegateGetReportsToRepositoryPort() {
        // Given
        GetReportsIDTO input = mock(GetReportsIDTO.class);
        ReportResponseDTO expectedResponse = mock(ReportResponseDTO.class);

        // When
        when(repositoryPort.getReports(input)).thenReturn(expectedResponse);

        ReportResponseDTO result = service.getReports(input);

        // Then
        assertEquals(expectedResponse, result);
        verify(repositoryPort, times(1)).getReports(input);
        verifyNoInteractions(sftpService);
    }

    @Test
    void shouldDelegateDownloadReportToSftpService() {
        // Given
        String filename = "report.csv";
        ReportDTO expectedReport = mock(ReportDTO.class);

        // When
        when(sftpService.downloadReport(filename)).thenReturn(expectedReport);

        ReportDTO result = service.downloadReport(filename);

        // Then
        assertEquals(expectedReport, result);
        verify(sftpService, times(1)).downloadReport(filename);
        verifyNoInteractions(repositoryPort);
    }
}
