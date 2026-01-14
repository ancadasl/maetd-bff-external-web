package es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity;

import es.maetd.marketcreators.bff.external.application.service.dto.types.ProductType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationEntityId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private ProductType productType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EvaluationEntityId that = (EvaluationEntityId) o;
        return Objects.equals(id, that.id) && productType == that.productType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productType);
    }
}
