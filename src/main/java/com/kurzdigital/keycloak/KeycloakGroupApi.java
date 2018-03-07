package com.kurzdigital.keycloak;

import org.keycloak.admin.client.resource.GroupResource;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.keycloak.representations.idm.GroupRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.kurzdigital.keycloak.ResponseHelper.*;

/**
 * An easy to use wrapper around the keycloak admin API group management rest calls.
 * <p>
 * The {@link KeycloakGroupApi} uses a user session and must be closed (=logout for the given admin user) when no longer needed.
 * It is implemented as an {@link AutoCloseable}.
 */
@SuppressWarnings("unused")
public class KeycloakGroupApi extends AbstractKeycloakApi {
    private static final Logger LOG = LoggerFactory.getLogger(KeycloakGroupApi.class);

    /**
     * Creates a new {@link KeycloakGroupApi} instance. The given user must have realm-management rights on client realm-admin!
     */
    public KeycloakGroupApi(AdapterConfig keycloakConfiguration, String userName, String password) {
        super(keycloakConfiguration, userName, password);
    }

    /**
     * Creates a new group inside keycloak.
     */
    public void createGroup(String group) {
        RealmResource realm = getRealmResource();
        GroupsResource groupsResource = realm.groups();
        GroupRepresentation groupRepresentation = new GroupRepresentation();
        groupRepresentation.setName(group);
        Response response = groupsResource.add(groupRepresentation);
        checkCreateResponse(group, response);
        String groupId = getIdFromLocation(response);
    }

    /**
     * retrieves all members of the given group.
     */
    public List<KeycloakUser> getGroupMembers(String groupName) {
        RealmResource realm = getRealmResource();
        GroupsResource groups = realm.groups();
        List<GroupRepresentation> groupRepresentations = retryWithException(groups::groups);
        for (GroupRepresentation groupRepresentation : groupRepresentations) {
            if (groupRepresentation.getName().equals(groupName)) {
                GroupResource group = groups.group(groupRepresentation.getId());
                return group.members().stream().map(user -> KeycloakUserMapper.map(user, Collections.emptyList(), Collections.emptyList())).collect(Collectors.toList());
            }
        }
        LOG.warn("Group " + groupName + " not found in keycloak.");
        return Collections.emptyList();
    }
}
