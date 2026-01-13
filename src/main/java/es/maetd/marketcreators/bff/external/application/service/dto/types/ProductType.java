package es.maetd.marketcreators.bff.external.application.service.dto.types;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum ProductType {
    LETTER,
    BOND;

    public static ProductType fromString(String name) {
        return Arrays.stream(ProductType.values())
                .filter(p -> p.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow();
    }
}
