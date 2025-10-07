package es.maetd.bff.external.config;

import es.maetd.marketcreators.clients.rest.configuration.base.RestClientsConfigurator;
import es.maetd.marketcreators.common.security.jwks.validator.spring.conf.SecurityInfoConfigurator;
import es.maetd.marketcreators.common.web.cors.CorsConfigurator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({CorsConfigurator.class, RestClientsConfigurator.class, SecurityInfoConfigurator.class})
public class BffExternalWebConfiguration {}
