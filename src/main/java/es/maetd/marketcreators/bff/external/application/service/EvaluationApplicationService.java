package es.maetd.marketcreators.bff.external.application.service;

import es.maetd.marketcreators.bff.external.application.service.dto.EvaluationJointResponseDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.EvaluationResponseDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.GetEvaluationsIDTO;

/**
 * Application service interface for retrieving evaluations.
 * <p>
 * Provides methods to fetch individual evaluations as well as joint evaluations
 * based on the criteria defined in the input DTO {@link GetEvaluationsIDTO}.
 * </p>
 */
public interface EvaluationApplicationService {

    /**
     * Retrieves individual evaluations for a given set of identifiers.
     *
     * @param idto Input DTO containing identifiers or criteria to filter the evaluations.
     * @return {@link EvaluationResponseDTO} containing the information of individual evaluations.
     */
    EvaluationResponseDTO getEvaluations(GetEvaluationsIDTO idto);

    /**
     * Retrieves joint evaluations for a given set of identifiers.
     * <p>
     * This method may combine multiple evaluations into a single response object,
     * providing an aggregated or joint view of the evaluations.
     * </p>
     *
     * @param idto Input DTO containing identifiers or criteria to filter the evaluations.
     * @return {@link EvaluationJointResponseDTO} containing the aggregated evaluation information.
     */
    EvaluationJointResponseDTO getEvaluationsJoint(GetEvaluationsIDTO idto);
}
