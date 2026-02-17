package es.maetd.marketcreators.bff.external.infrastructure.adapters.web;

import es.maetd.marketcreators.bff.external.application.mappers.ReportApplicationMapper;
import es.maetd.marketcreators.bff.external.application.service.dto.ReportDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.sftp.ReportTransferSftpService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.ReportsApi;
import org.openapitools.model.ReportsDataListWDTO;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ReportControllerWebAdapter extends BaseController implements ReportsApi {

    private final ReportTransferSftpService service;

    private final ReportApplicationMapper mapper;

    public ReportControllerWebAdapter(ReportTransferSftpService service, ReportApplicationMapper mapper) {

        this.service = service;
        this.mapper = mapper;
    }

    @Override
    @SneakyThrows
    @CrossOrigin(exposedHeaders = {"Content-Disposition", "Content-Type", "Content-Length", "Authorization"})
    public ResponseEntity<Resource> getReportData(String filename) {

        log.info("Recovering file binary data with id : {}", filename);

        ReportDTO dto = service.downloadReport(filename);

        log.info("Returning report: {}", dto.getName());

        ByteArrayResource resource = new ByteArrayResource(dto.getData());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dto.getName() + "\"")
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(dto.getData().length)
                .body(resource);
    }

    @Override
    public ResponseEntity<ReportsDataListWDTO> getReports() {

        ReportsDataListWDTO response = new ReportsDataListWDTO();
        response.setReports(mapper.map(service.getReports()));
        response.setTotal(response.getReports().size());
        return ResponseEntity.ok(response);
    }
}
