package es.maetd.marketcreators.bff.external.infrastructure.adapters.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class SyndicatedIssuesDto implements Serializable {

    private String csb;

    private Long year;

    private Long month;

    private Long letter;

    private Integer bonds;
}
