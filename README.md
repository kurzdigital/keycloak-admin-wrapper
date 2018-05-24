# Keycloak Admin Wrapper

This is a wrapper around the official (REST - based) Keycloak Admin API. See www.keycloak.org for more details about keycloak and the Admin API.

Supported Keycloak version is currently 3.4.3. Older versions probably wont work, newer versions of the keycloak server will probably work. 

[![Build Status](https://travis-ci.org/kurzdigital/keycloak-admin-wrapper.svg?branch=master)](https://travis-ci.org/kurzdigital/keycloak-admin-wrapper)

## Usage

### Required Configuration

For initializing the Keycloak APIs you need the AdapterConfig and a username / password in keycloak.

In a spring environment the `AdapterConfig` can be retrieved from the context since you probably already use the keycloak spring adapter. 
Otherwise the adapter config can be retrieved from the keycloak client config. 

The user needed for the API classes must have the following permissions:
- `realm-admin` im Client `realm-management`

### Maven

```xml
<dependency>
    <groupId>com.kurzdigital</groupId>
    <artifactId>keycloak-admin-wrapper</artifactId>
    <version>0.9.10</version>
</dependency>
```

### Example with User API

The `KeycloakUserAPI` is an easy API to manage users in keycloak.

For a independent user model we have custom class `User`:
```java
public final class KeycloakUser {
    private final String id;
    private final String userName;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String locale;
    private final List<String> groups;
    private final List<String> roles;
// ...    
}
```

#### Create User

```java
KeycloakUser user = KeycloakUser.builder()
		 .firstName("Mister")
		 .lastName("Test")
		 .email("test@sample.org")
		 .locale("en")
		 .userName("test")
		 .groups(Arrays.asList("USER"))
		 .build();
		 
KeycloakUserApi userApi = new KeycloakUserApi(adapterConfig, "admin", "12345678");
userApi.createUser(user, "password");
```

This will create a user in the group `USER` and with password `password`. Normally this would mean 3 requests to keycloak admin API.
If you don't specify a password, the user will be initialised without and retrieve an email to update its password first.

### Group API

The `KeycloakGroupAPI` is an easy API to manage groups in keycloak. 
You can for example query for all members of a group.
