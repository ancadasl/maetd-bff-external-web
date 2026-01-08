package es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository.specification;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.types.ProductType;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.OverbiddingEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class OverBiddingSpecification implements Specification<OverbiddingEntity> {

    private final String csb;

    private final Integer year;

    protected final ProductType product;

    public OverBiddingSpecification(String csb, Integer year, ProductType product) {

        this.csb = csb;
        this.year = year;
        this.product = product;
    }

    @Override
    public Predicate toPredicate(Root<OverbiddingEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        if (csb != null) {
            predicates.add(builder.equal(root.get("csb"), csb));
        }

        if (year != null) {
            predicates.add(builder.equal(root.get("year"), year));
        }

        if (product != null) {
            predicates.add(builder.equal(root.get("productType"), product));
        }

        return predicates.isEmpty() ? builder.conjunction() : builder.and(predicates.toArray(new Predicate[0]));
    }
}
