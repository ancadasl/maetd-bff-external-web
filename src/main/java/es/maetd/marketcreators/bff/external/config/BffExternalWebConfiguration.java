package es.maetd.marketcreators.bff.external.config;

import es.maetd.marketcreators.clients.rest.configuration.base.RestClientsConfigurator;
import es.maetd.marketcreators.common.security.jwks.validator.spring.conf.SecurityInfoConfigurator;
import es.maetd.marketcreators.common.web.cors.CorsConfigurator;
import es.maetd.marketcreators.sftp.client.configuration.props.FileTransferSFTPConfigurationProps;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
    CorsConfigurator.class,
    RestClientsConfigurator.class,
    SecurityInfoConfigurator.class,
    FileTransferSFTPConfigurationProps.class
})
public class BffExternalWebConfiguration {}
