package com.cvetkov.fedor.laboratoryworkmicro.users.keycloak;

import com.cvetkov.fedor.laboratoryworkmicro.entities.model.User;
import com.cvetkov.fedor.laboratoryworkmicro.users.dtoKecloak.RequestUserKeycloak;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class KeycloakUtils {

    //считываем из удаленного конфига kc
    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    private static Keycloak keycloak;
    private static RealmResource realmResource;
    private static UsersResource usersResource;

    @PostConstruct
    public void initKeycloak() {
        if (keycloak == null) {
            keycloak = KeycloakBuilder.builder()
                    .realm(realm)
                    .serverUrl(serverUrl)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .build();

            realmResource = keycloak.realm(realm);
            usersResource = realmResource.users();
        }
    }

    public Response createKeycloakUser(RequestUserKeycloak user) {

        CredentialRepresentation credentialRepresentation = createPasswordCredential(user.getPassword());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(user.getUsername());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setEmail(user.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);

        Response response = usersResource.create(kcUser);

        return response;
    }

    private CredentialRepresentation createPasswordCredential(String password) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(password);
        return credentialRepresentation;
    }


    public void addRoles(String userId, List<String> roles) {

        List<RoleRepresentation> kcRoles = new ArrayList<>();

        for (String role : roles) {
            RoleRepresentation roleRepresentation = realmResource.roles().get(role).toRepresentation();
            kcRoles.add(roleRepresentation);
        }

        UserResource uniqueUserResource = usersResource.get(userId);

        uniqueUserResource.roles().realmLevel().add(kcRoles);
    }

    public void deleteKeycloakUser(String userId) {
        UserResource userResource = usersResource.get(userId);
        userResource.remove();
    }

    public UserRepresentation findUserById(String userId) {
        return usersResource.get(userId).toRepresentation();
    }

    public List<UserRepresentation> searchKeycloakUsers(String text) {
        return usersResource.searchByAttributes("email:" + text);
    }

    public void updateKeycloakUser(RequestUserKeycloak user) {
        CredentialRepresentation credentialRepresentation = createPasswordCredential(user.getPassword());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(user.getUsername());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setEmail(user.getEmail());

        UserResource userResource = usersResource.get(user.getId());
        userResource.update(kcUser);
    }
}
