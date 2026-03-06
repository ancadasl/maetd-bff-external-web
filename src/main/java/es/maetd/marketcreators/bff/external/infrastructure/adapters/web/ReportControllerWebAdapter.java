package es.maetd.marketcreators.bff.external.infrastructure.adapters.web;

import es.maetd.marketcreators.bff.external.application.mappers.ReportApplicationMapper;
import es.maetd.marketcreators.bff.external.application.service.ReportApplicationService;
import es.maetd.marketcreators.bff.external.application.service.dto.GetReportsIDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.ReportDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.ReportResponseDTO;
import java.util.Optional;
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

    private final ReportApplicationService service;

    private final ReportApplicationMapper mapper;

    public ReportControllerWebAdapter(ReportApplicationService service, ReportApplicationMapper mapper) {

        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<ReportsDataListWDTO> getReports(
            Integer page, Integer size, Integer id, Optional<Integer> year, Optional<Integer> month) {
        log.info(
                "Get reports for creator id: {} , year: {}, month: {}, page: {} , size: {}.",
                id,
                year.orElse(null),
                month.orElse(null),
                page,
                size);

        GetReportsIDTO idto = GetReportsIDTO.builder()
                .page(page)
                .size(size)
                .creatorId(id)
                .year(year.orElse(null))
                .month(month.orElse(null))
                .build();

        ReportsDataListWDTO response = new ReportsDataListWDTO();
        ReportResponseDTO dto = service.getReports(idto);
        response.setReports(mapper.mapAsList(dto.getList()));
        response.setTotal(response.getReports().size());
        return ResponseEntity.ok(response);
    }

    @Override
    @SneakyThrows
    @CrossOrigin(exposedHeaders = {"Content-Disposition", "Content-Type", "Content-Length", "Authorization"})
    public ResponseEntity<Resource> getReportData(String filePath) {

        log.info("Recovering file binary data with id : {}", filePath);

        ReportDTO dto = service.downloadReport(filePath);

        log.info("Returning report: {}", dto.getFileName());

        ByteArrayResource resource = new ByteArrayResource(dto.getData());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dto.getFileName() + "\"")
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(dto.getData().length)
                .body(resource);
    }
}
