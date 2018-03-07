package com.kurzdigital.keycloak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A user in keycloak. This is the model class to be used when managing users in keycloak with this wrapper class.
 * It contains more fields than the normal UserRepresentation class from keycloak, like roles and groups.
 */
public final class KeycloakUser {

    private final String id;
    private final String userName;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final List<String> groups;
    private final List<String> roles;

    private KeycloakUser(Builder builder) {
        this.id = builder.id;
        this.userName = builder.userName;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.groups = new ArrayList<>(builder.groups);
        this.roles = new ArrayList<>(builder.roles);
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getGroups() {
        return groups;
    }

    public List<String> getRoles() {
        return roles;
    }

    public Builder toBuilder() {
        return builder()
                .id(id)
                .userName(userName)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .groups(groups)
                .roles(roles);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String id;
        private String userName;
        private String firstName;
        private String lastName;
        private String email;
        private List<String> groups = Collections.emptyList();
        private List<String> roles = Collections.emptyList();

        public Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder groups(List<String> groups) {
            this.groups = groups;
            return this;
        }

        public Builder roles(List<String> roles) {
            this.roles = roles;
            return this;
        }

        public KeycloakUser build() {
            return new KeycloakUser(this);
        }
    }
}
