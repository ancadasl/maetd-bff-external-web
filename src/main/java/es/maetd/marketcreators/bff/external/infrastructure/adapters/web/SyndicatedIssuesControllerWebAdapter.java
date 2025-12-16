package es.maetd.marketcreators.bff.external.infrastructure.adapters.web;

import es.maetd.marketcreators.bff.external.application.mappers.SyndicatedIssuesApplicationMapper;
import es.maetd.marketcreators.bff.external.application.service.SyndicatedIssuesApplicationService;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetSyndicatedIssuesIDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.SyndicatedIssuesResponseDto;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.SyndicatedIssuesApi;
import org.openapitools.model.SyndicatedIssuesDataListWDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SyndicatedIssuesControllerWebAdapter extends BaseController implements SyndicatedIssuesApi {

    private final SyndicatedIssuesApplicationService service;

    private final SyndicatedIssuesApplicationMapper mapper;

    public SyndicatedIssuesControllerWebAdapter(
            SyndicatedIssuesApplicationService service, SyndicatedIssuesApplicationMapper mapper) {

        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<SyndicatedIssuesDataListWDTO> getSyndicatedIssues(
            Optional<String> csb, Optional<Integer> month, Optional<Integer> year) {
        log.info(
                "Get evaluations for csb: {} , month: {} , year: {}.",
                csb.orElse(null),
                month.orElse(null),
                year.orElse(null));

        doValidateCsbPermission(csb.orElse(null));

        GetSyndicatedIssuesIDTO idto = new GetSyndicatedIssuesIDTO();

        csb.ifPresent(value -> idto.setCsb(Long.valueOf(value)));

        idto.setMonth(month.orElse(null));
        idto.setYear(year.orElse(null));

        SyndicatedIssuesResponseDto dto = service.getSyndicatedIssues(idto);

        SyndicatedIssuesDataListWDTO response = new SyndicatedIssuesDataListWDTO();
        response.setSyndicatedIssues(mapper.map(dto.getList()));

        return ResponseEntity.ok(response);
    }
}
