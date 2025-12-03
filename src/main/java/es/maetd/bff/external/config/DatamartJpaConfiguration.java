package es.maetd.bff.external.config;

import es.maetd.marketcreators.clients.rest.configuration.base.RestClientsConfigurator;
import es.maetd.marketcreators.common.security.jwks.validator.spring.conf.SecurityInfoConfigurator;
import es.maetd.marketcreators.common.web.cors.CorsConfigurator;
import java.util.Objects;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {
            "es.maetd.bff.external.infrastructure.adapters.persistence.entity.datamart",
            "es.maetd.bff.external.infrastructure.adapters.persistence.repository.datamart"
        },
        entityManagerFactoryRef = "datamartEntityManagerFactory",
        transactionManagerRef = "datamartTransactionManager")
public class DatamartJpaConfiguration {

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean datamartEntityManagerFactory(
            @Qualifier("datamartDataSource") DataSource dataSource, EntityManagerFactoryBuilder builder) {
        return builder.dataSource(dataSource)
                .packages(
                        "es.maetd.bff.external.infrastructure.adapters.persistence.entity.datamart",
                        "es.maetd.bff.external.infrastructure.adapters.persistence.repository.datamart")
                .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager datamartTransactionManager(
            @Qualifier("datamartEntityManagerFactory")
                    LocalContainerEntityManagerFactoryBean datamartEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(datamartEntityManagerFactory.getObject()));
    }
}
