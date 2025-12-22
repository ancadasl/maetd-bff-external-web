package es.maetd.marketcreators.bff.external.application.service.impl;

import es.maetd.marketcreators.bff.external.application.port.EvaluationRepositoryPort;
import es.maetd.marketcreators.bff.external.application.service.EvaluationApplicationService;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.EvaluationJointResponseDto;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.EvaluationResponseDto;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetEvaluationsIDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class EvaluationApplicationServiceImpl implements EvaluationApplicationService {

    private final EvaluationRepositoryPort port;

    @Override
    public EvaluationResponseDto getEvaluations(GetEvaluationsIDTO idto) {

        return EvaluationResponseDto.builder().list(port.getEvaluations(idto)).build();
    }

    @Override
    public EvaluationJointResponseDto getEvaluationsJoint(GetEvaluationsIDTO idto) {
        return EvaluationJointResponseDto.builder()
                .list(port.getEvaluationsJoint(idto))
                .build();
    }
}
