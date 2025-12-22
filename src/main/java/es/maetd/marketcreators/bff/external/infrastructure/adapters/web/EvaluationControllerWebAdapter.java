package es.maetd.marketcreators.bff.external.infrastructure.adapters.web;

import es.maetd.marketcreators.bff.external.application.mappers.EvaluationApplicationMapper;
import es.maetd.marketcreators.bff.external.application.mappers.EvaluationJointApplicationMapper;
import es.maetd.marketcreators.bff.external.application.service.EvaluationApplicationService;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.*;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.types.EvaluationStatus;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.types.ProductType;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.EvaluationsApi;
import org.openapitools.model.EvaluationDataListWDTO;
import org.openapitools.model.EvaluationJoinDataListWDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class EvaluationControllerWebAdapter extends BaseController implements EvaluationsApi {

    private final EvaluationApplicationService service;

    private final EvaluationApplicationMapper mapper;

    private final EvaluationJointApplicationMapper mapperJoint;

    public EvaluationControllerWebAdapter(
            EvaluationApplicationService service,
            EvaluationApplicationMapper mapper,
            EvaluationJointApplicationMapper mapperJoint) {

        this.service = service;
        this.mapper = mapper;
        this.mapperJoint = mapperJoint;
    }

    @Override
    public ResponseEntity<EvaluationDataListWDTO> getEvaluations(
            Optional<String> productType,
            Optional<String> csb,
            Optional<Integer> month,
            Optional<Integer> year,
            Optional<List<String>> status) {
        log.info(
                "Get evaluations  for productType: {} , csb: {} , month: {} , year: {}, status: {}.",
                productType.orElse(null),
                csb.orElse(null),
                month.orElse(null),
                year.orElse(null),
                status.orElse(null));

        doValidateCsbPermission(csb.orElse(null));

        GetEvaluationsIDTO idto = new GetEvaluationsIDTO();

        productType.ifPresent(p -> idto.setProductType(ProductType.fromString(p)));
        status.ifPresent(list -> {
            List<EvaluationStatus> enumList =
                    list.stream().map(EvaluationStatus::valueOf).toList();
            idto.setStatus(enumList);
        });
        idto.setCsb(csb.orElse(null));
        idto.setMonth(month.orElse(null));
        idto.setYear(year.orElse(null));

        EvaluationResponseDto dto = service.getEvaluations(idto);

        EvaluationDataListWDTO response = new EvaluationDataListWDTO();
        response.setEvaluations(mapper.map(dto.getList()));

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<EvaluationJoinDataListWDTO> getEvaluationsJoint(
            Optional<String> csb, Optional<Integer> month, Optional<Integer> year, Optional<List<String>> status) {
        log.info(
                "Get evaluations Joint for csb: {} , month: {} , year: {}, status: {}.",
                csb.orElse(null),
                month.orElse(null),
                year.orElse(null),
                status.orElse(null));

        doValidateCsbPermission(csb.orElse(null));

        GetEvaluationsIDTO idto = new GetEvaluationsIDTO();

        status.ifPresent(list -> {
            List<EvaluationStatus> enumList =
                    list.stream().map(EvaluationStatus::valueOf).toList();
            idto.setStatus(enumList);
        });
        idto.setCsb(csb.orElse(null));
        idto.setMonth(month.orElse(null));
        idto.setYear(year.orElse(null));

        EvaluationJointResponseDto dto = service.getEvaluationsJoint(idto);
        EvaluationJoinDataListWDTO response = new EvaluationJoinDataListWDTO();
        response.setEvaluationsJoint(mapperJoint.map(dto.getList()));
        return ResponseEntity.ok(response);
    }
}
