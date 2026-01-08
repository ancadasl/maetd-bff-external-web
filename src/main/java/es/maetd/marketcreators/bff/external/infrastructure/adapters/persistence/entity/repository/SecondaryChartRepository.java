package es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.SecondaryChartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SecondaryChartRepository
        extends JpaRepository<SecondaryChartEntity, Long>, JpaSpecificationExecutor<SecondaryChartEntity> {}
