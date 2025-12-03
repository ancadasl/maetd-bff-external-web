package es.maetd.bff.external.infrastructure.adapters.persistence.entity.datamart;

import java.io.Serializable;
import javax.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LetterAnnualDataPk implements Serializable {

    private Long id;

    @Column(name = "csb")
    private String csb;

    @Column(name = "año")
    private Integer year;

    @Column(name = "mes")
    private String month;
}
