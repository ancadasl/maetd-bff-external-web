package es.maetd.bff.external.infrastructure.adapters.persistence.entity.datamart;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "v_datos_letras_anual")
public class LetterAnnualDataEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private LetterAnnualDataPk id;

    @Column(name = "month_numeric")
    private Integer monthNumeric;

    @Column(name = "joint_monthly")
    private Double jointMonthly;

    @Column(name = "acumulado")
    private Double cumulative;

    @Column(name = "letras")
    private Double letters;

    @Column(name = "suscripcion")
    private Double subscription;

    @Column(name = "cotizacion")
    private Double quoting;

    @Column(name = "negociacion")
    private Double trading;

    @Column(name = "electronic")
    private Double electronic;

    @Column(name = "segundo_escalon")
    private Double secondTier;

    @Column(name = "terceros")
    private Double clients;

    @Column(name = "cualitativo")
    private Double qualitative;

    @Column(name = "segundas_vueltas")
    private Double secondRounds;
}
