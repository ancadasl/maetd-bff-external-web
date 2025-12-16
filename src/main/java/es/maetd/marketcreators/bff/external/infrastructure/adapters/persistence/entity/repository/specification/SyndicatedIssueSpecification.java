package es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository.specification;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.SyndicatedIssuesEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class SyndicatedIssueSpecification implements Specification<SyndicatedIssuesEntity> {

    private final Long csb;

    private final Integer month;

    private final Integer year;

    public SyndicatedIssueSpecification(Long csb, Integer month, Integer year) {

        this.csb = csb;
        this.month = month;
        this.year = year;
    }

    @Override
    public Predicate toPredicate(Root<SyndicatedIssuesEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        if (csb != null) {
            predicates.add(builder.equal(root.get("csb"), csb));
        }

        if (month != null) {
            predicates.add(builder.equal(root.get("monthNumeric"), month));
        }

        if (year != null) {
            predicates.add(builder.equal(root.get("year"), year));
        }

        return predicates.isEmpty() ? builder.conjunction() : builder.and(predicates.toArray(new Predicate[0]));
    }
}
