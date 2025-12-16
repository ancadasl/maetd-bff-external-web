package es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.types;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum Role {
    IS_ADMIN("isAdmin"),
    EVALUATION_ACCESS("evaluation_access");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public static Role fromString(String name) {
        return Arrays.stream(Role.values())
                .filter(p -> p.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow();
    }
}
