package es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository.converter;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.types.ProductType;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ProductTypeConverter implements AttributeConverter<ProductType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ProductType attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public ProductType convertToEntityAttribute(Integer dbData) {
        return dbData != null ? ProductType.getProductType(dbData) : null;
    }
}
