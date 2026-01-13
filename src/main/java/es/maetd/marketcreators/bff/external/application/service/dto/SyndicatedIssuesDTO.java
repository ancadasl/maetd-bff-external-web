package es.maetd.marketcreators.bff.external.application.service.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class SyndicatedIssuesDTO implements Serializable {

    private String csb;

    private Long year;

    private Long month;

    private Long letter;

    private Integer bonds;
}
