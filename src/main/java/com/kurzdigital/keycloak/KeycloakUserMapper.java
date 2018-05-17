package com.kurzdigital.keycloak;

import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class KeycloakUserMapper {

    public static KeycloakUser map(UserRepresentation userRepresentation, List<GroupRepresentation> groups, List<RoleRepresentation> roles) {
        KeycloakUser.Builder builder = KeycloakUser.builder();
        builder.id(userRepresentation.getId());
        builder.userName(userRepresentation.getUsername());
        builder.firstName(userRepresentation.getFirstName());
        builder.lastName(userRepresentation.getLastName());
        builder.email(userRepresentation.getEmail());
        if (userRepresentation.getAttributes() != null && userRepresentation.getAttributes().containsKey("locale")) {
            builder.locale(userRepresentation.getAttributes().get("locale").get(0));
        }
        if (groups != null) {
            builder.groups(groups.stream().map(GroupRepresentation::getName).collect(Collectors.toList()));
        }
        if (roles != null) {
            builder.roles(roles.stream().map(RoleRepresentation::getName).collect(Collectors.toList()));
        }
        return builder.build();
    }

    public static UserRepresentation map(KeycloakUser user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(user.getId());
        userRepresentation.setUsername(user.getUserName());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());
        addLocaleToUserRepresentation(user, userRepresentation);
        userRepresentation.setGroups(user.getGroups());
        return userRepresentation;
    }

    public static void addLocaleToUserRepresentation(KeycloakUser source, UserRepresentation target) {
        if (source.getLocale() != null) {
            Map<String, List<String>> locale = new HashMap<>();
            locale.put("locale", Collections.singletonList(source.getLocale()));

            target.setAttributes(locale);
        }
    }

}
