package es.maetd.marketcreators.bff.external.application.port;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.EvaluationDto;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetEvaluationsIDTO;
import java.util.List;

public interface EvaluationRepositoryPort {

    List<EvaluationDto> getEvaluations(GetEvaluationsIDTO idto);
}
