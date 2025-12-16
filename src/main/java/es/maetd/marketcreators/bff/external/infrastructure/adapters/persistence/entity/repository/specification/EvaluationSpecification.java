package es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository.specification;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.types.EvaluationStatus;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.types.ProductType;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.EvaluationEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class EvaluationSpecification implements Specification<EvaluationEntity> {

    private final ProductType product;

    private final Long csb;

    private final Integer month;

    private final Integer year;

    private List<EvaluationStatus> status;

    public EvaluationSpecification(
            ProductType productType, Long csb, Integer month, Integer year, List<EvaluationStatus> status) {

        this.product = productType;
        this.csb = csb;
        this.month = month;
        this.year = year;
        this.status = status;
    }

    @Override
    public Predicate toPredicate(Root<EvaluationEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        if (product != null) {
            predicates.add(root.get("productType").in(product));
        }

        if (csb != null) {
            predicates.add(builder.equal(root.get("csb"), csb));
        }

        if (month != null) {
            predicates.add(builder.equal(root.get("monthNumeric"), month));
        }

        if (year != null) {
            predicates.add(builder.equal(root.get("year"), year));
        }

        if (status != null && !status.isEmpty()) {
            predicates.add(root.get("status").in(status));
        }
        return predicates.isEmpty() ? builder.conjunction() : builder.and(predicates.toArray(new Predicate[0]));
    }
}
