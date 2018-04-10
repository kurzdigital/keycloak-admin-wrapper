package com.kurzdigital.keycloak;

import java.util.List;

public interface GroupApi {
    void createGroup(String group);

    List<KeycloakUser> getGroupMembers(String groupName);
}
