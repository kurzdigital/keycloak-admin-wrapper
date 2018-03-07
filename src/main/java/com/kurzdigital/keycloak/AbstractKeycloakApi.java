package com.kurzdigital.keycloak;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public abstract class AbstractKeycloakApi implements AutoCloseable {

    protected final Keycloak keycloak;
    protected final String realm;
    protected final AdapterConfig keycloakConfiguration;
    protected final String userName;

    /**
     * Creates a new {@link AbstractKeycloakApi} instance. The given user must have realm-management rights on client realm-admin!
     */
    public AbstractKeycloakApi(AdapterConfig keycloakConfiguration, String userName, String password) {
        this.keycloakConfiguration = keycloakConfiguration;
        String serverUrl = keycloakConfiguration.getAuthServerUrl();
        realm = keycloakConfiguration.getRealm();
        String resource = keycloakConfiguration.getResource();
        keycloak = new KeycloakInstanceBuilder().build(userName, password, serverUrl, resource, realm);
        this.userName = userName;
    }

    protected RealmResource getRealmResource() {
        return keycloak.realm(realm);
    }

    @Override
    public void close() throws Exception {
        RealmResource realm = getRealmResource();
        UsersResource usersResource = realm.users();
        List<UserRepresentation> backendUsers = usersResource.search(userName, null, null, null, 0, 1);
        if (backendUsers.size() == 1) {
            UserRepresentation backendUser = backendUsers.get(0);
            String backendUserId = backendUser.getId();
            UserResource userResource = usersResource.get(backendUserId);
            userResource.logout();
        }

    }
}
