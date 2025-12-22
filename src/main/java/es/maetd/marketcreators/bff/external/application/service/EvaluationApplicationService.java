package es.maetd.marketcreators.bff.external.application.service;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.*;

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
     * @return {@link EvaluationResponseDto} containing the information of individual evaluations.
     */
    EvaluationResponseDto getEvaluations(GetEvaluationsIDTO idto);

    /**
     * Retrieves joint evaluations for a given set of identifiers.
     * <p>
     * This method may combine multiple evaluations into a single response object,
     * providing an aggregated or joint view of the evaluations.
     * </p>
     *
     * @param idto Input DTO containing identifiers or criteria to filter the evaluations.
     * @return {@link EvaluationJointResponseDto} containing the aggregated evaluation information.
     */
    EvaluationJointResponseDto getEvaluationsJoint(GetEvaluationsIDTO idto);
}
