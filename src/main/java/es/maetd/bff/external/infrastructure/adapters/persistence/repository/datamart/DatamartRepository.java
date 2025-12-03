package es.maetd.bff.external.infrastructure.adapters.persistence.repository.datamart;

import es.maetd.bff.external.infrastructure.adapters.persistence.entity.datamart.DatamartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatamartRepository extends JpaRepository<DatamartEntity, Long> {}
