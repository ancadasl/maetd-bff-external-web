package es.maetd.bff.external.infrastructure.adapters.persistence.entity.datamart;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "v_datos_bonos_anual")
public class BondAnnualDataEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private BondAnnualDataPk id;

    @Column(name = "month_numeric")
    private Integer monthNumeric;

    @Column(name = "acumulado")
    private Double cumulative;

    @Column(name = "bonos")
    private Double bonds;

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

    @Column(name = "strips")
    private Double strips;

    @Column(name = "cualitativo")
    private Double qualitative;

    @Column(name = "segundas_vueltas")
    private Double secondRounds;
}
