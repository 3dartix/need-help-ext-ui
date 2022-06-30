package ru.pugart.ext.api.ui.service.keycloak;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtKeycloakDataDto {
    private Integer exp;
    private Integer iat;
    private String jti;
    private String iss;
    private String aud;
    private String sub;
    private String typ;
    private String azp;
    @JsonProperty("session_state")
    private String sessionState;
    private String acr;
    @JsonProperty("realm_access")
    private Realm realmAccess;
    @JsonProperty("resource_access")
    private ResourceAccess resourceAccess;
    private String scope;
    private String sid;
    @JsonProperty("email_verified")
    private Boolean emailVerified;
    private String name;
    @JsonProperty("preferred_username")
    private String preferredUsername;
    @JsonProperty("given_name")
    private String givenName;
    @JsonProperty("family_name")
    private String familyName;
    private String email;

    @Data
    public static class ResourceAccess{
        private Realm account;
    }

    @Data
    public static class Realm {
        private ArrayList<String> roles;
    }
}

