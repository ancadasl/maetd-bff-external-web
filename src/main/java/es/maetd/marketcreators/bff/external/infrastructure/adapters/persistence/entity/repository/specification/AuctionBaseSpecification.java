package es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository.specification;

import es.maetd.marketcreators.bff.external.application.service.dto.types.ProductType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public abstract class AuctionBaseSpecification<T> implements Specification<T> {

    protected final ProductType product;
    protected final String csb;
    protected final LocalDate date;

    protected AuctionBaseSpecification(ProductType product, String csb, Integer year, Integer month) {
        this.product = product;
        this.csb = csb;
        this.date = LocalDate.of(year, month, 1);
    }

    protected void addCommonPredicates(Root<T> root, CriteriaBuilder builder, List<Predicate> predicates) {

        if (product != null) {
            predicates.add(builder.equal(root.get("productType"), product));
        }

        if (csb != null) {
            predicates.add(builder.equal(root.get("csb"), csb));
        }

        if (date != null) {
            LocalDateTime start = date.withDayOfMonth(1).atStartOfDay();
            LocalDateTime end = start.plusMonths(1);

            predicates.add(builder.between(root.get("evaluationYearMonth"), start, end));
        }
    }
}
