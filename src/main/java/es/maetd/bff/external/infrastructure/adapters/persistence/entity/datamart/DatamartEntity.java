package es.maetd.bff.external.infrastructure.adapters.persistence.entity.datamart;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DatamartEntity implements Serializable {

    @Id
    private Long id;
}
