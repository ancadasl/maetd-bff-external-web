package es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository.specification;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.types.ProductType;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.SecondaryAuctionEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SecondaryAuctionSpecification extends AuctionBaseSpecification<SecondaryAuctionEntity> {

    public SecondaryAuctionSpecification(ProductType product, String csb, Integer year, Integer month) {
        super(product, csb, year, month);
    }

    @Override
    public Predicate toPredicate(Root<SecondaryAuctionEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        List<Predicate> predicates = new ArrayList<>();
        addCommonPredicates(root, builder, predicates);

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
