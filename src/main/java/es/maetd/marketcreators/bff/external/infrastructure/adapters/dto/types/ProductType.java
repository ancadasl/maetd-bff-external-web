package es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.types;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ProductType {
    LETTER(1),
    BOND(2);

    private final int value;

    ProductType(int value) {
        this.value = value;
    }

    public static ProductType getProductType(int id) {
        return Arrays.stream(ProductType.values())
                .filter(p -> id == p.getValue())
                .findFirst()
                .orElseThrow();
    }

    public static ProductType fromString(String name) {
        return Arrays.stream(ProductType.values())
                .filter(p -> p.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow();
    }
}
