package com.kurzdigital.keycloak;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.internal.LocalResteasyProviderFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.keycloak.admin.client.Keycloak;

/**
 * To support smooth upgrade we support a forward compatibility pattern:
 *
 * You can upgrade the Keycloak Server in your local and dev environments without having tom upgrade the keycloak libraries
 * in your clients (here). The jackson is configured to ignore unknown properties.
 *
 * Once the productive keycloak instances are upgraded, you can upgrade the core libraries here as well.
 * Until then they stay at the same version as we use in prod.
 */
public class KeycloakInstanceBuilder {

    public Keycloak build(String userName, String password, String serverUrl, String resource, String realm) {
        ResteasyJackson2Provider provider = new ResteasyJackson2Provider();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        provider.setMapper(objectMapper);
        ResteasyProviderFactory providerFactory = new LocalResteasyProviderFactory(new ResteasyProviderFactory());
        providerFactory.registerProviderInstance(provider);
        RegisterBuiltin.register(providerFactory);

        ResteasyClient resteasyClient = new ResteasyClientBuilder()
                .connectionPoolSize(10)
                .providerFactory(providerFactory)
                .build();

        return org.keycloak.admin.client.KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .username(userName)
                .password(password)
                .clientId(resource)
                .resteasyClient(resteasyClient)
                .build();
    }

}
