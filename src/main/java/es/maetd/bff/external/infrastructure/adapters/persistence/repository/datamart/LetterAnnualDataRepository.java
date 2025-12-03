package es.maetd.bff.external.infrastructure.adapters.persistence.repository.datamart;

import es.maetd.bff.external.infrastructure.adapters.persistence.entity.datamart.LetterAnnualDataEntity;
import es.maetd.bff.external.infrastructure.adapters.persistence.entity.datamart.LetterAnnualDataPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LetterAnnualDataRepository extends JpaRepository<LetterAnnualDataEntity, LetterAnnualDataPk> {}
