package es.maetd.marketcreators.bff.external.application.service;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.*;

public interface EvaluationApplicationService {

    EvaluationResponseDto getEvaluations(GetEvaluationsIDTO idto);
}
