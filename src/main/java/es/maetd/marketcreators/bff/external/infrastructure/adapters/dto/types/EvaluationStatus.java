package es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.types;

import lombok.Getter;

@Getter
public enum EvaluationStatus {
    FINISH_PRE_FINISHED,
    FINISH_DRAFT_FINISHED,
    FINISH_CLOSED_FINISHED,
    FINISH_CONSOLIDATED_FINISHED
}
