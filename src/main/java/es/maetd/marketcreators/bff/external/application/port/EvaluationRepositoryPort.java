package es.maetd.marketcreators.bff.external.application.port;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.EvaluationDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.EvaluationJointDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetEvaluationsIDTO;
import java.util.List;

public interface EvaluationRepositoryPort {

    List<EvaluationDTO> getEvaluations(GetEvaluationsIDTO idto);

    List<EvaluationJointDTO> getEvaluationsJoint(GetEvaluationsIDTO idto);
}
