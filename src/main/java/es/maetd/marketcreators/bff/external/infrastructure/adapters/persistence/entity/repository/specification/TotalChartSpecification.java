package es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository.specification;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.TotalChartEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class TotalChartSpecification extends ChartBaseSpecification<TotalChartEntity> {

    public TotalChartSpecification(String csb, Integer year) {
        super(null, csb, year);
    }

    @Override
    public Predicate toPredicate(Root<TotalChartEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        List<Predicate> predicates = new ArrayList<>();
        addCommonPredicates(root, builder, predicates);

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
