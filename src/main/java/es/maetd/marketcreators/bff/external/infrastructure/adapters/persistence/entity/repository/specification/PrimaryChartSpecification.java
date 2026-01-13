package es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository.specification;

import es.maetd.marketcreators.bff.external.application.service.dto.types.ProductType;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.PrimaryChartEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class PrimaryChartSpecification extends ChartBaseSpecification<PrimaryChartEntity> {

    public PrimaryChartSpecification(ProductType product, String csb, Integer year) {
        super(product, csb, year);
    }

    @Override
    public Predicate toPredicate(Root<PrimaryChartEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        List<Predicate> predicates = new ArrayList<>();
        addCommonPredicates(root, builder, predicates);

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
