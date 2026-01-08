package es.maetd.marketcreators.bff.external.infrastructure.adapters.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityDTO implements Serializable {

    private String name;

    private String csb;
}
