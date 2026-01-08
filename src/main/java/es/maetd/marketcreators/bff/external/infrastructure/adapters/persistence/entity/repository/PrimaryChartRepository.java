package es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.PrimaryChartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PrimaryChartRepository
        extends JpaRepository<PrimaryChartEntity, Long>, JpaSpecificationExecutor<PrimaryChartEntity> {}
