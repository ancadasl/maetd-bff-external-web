package es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.SyndicatedIssuesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SyndicatedIssuesRepository
        extends JpaRepository<SyndicatedIssuesEntity, Long>, JpaSpecificationExecutor<SyndicatedIssuesEntity> {}
