package ru.pugart.ext.api.ui.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "keycloak")
@Data
public class KeycloakConfig {
    private String realm;
    private String authServerUrl;
    private String sslRequired;
    private String resource;
    private Map<String, Credentials> credentials;
    private Integer confidentialPort;
    private String defaultGroup;
    private String adminGroup;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Credentials {
        private String secret;
        private String clientId;
    }

    @Getter
    @AllArgsConstructor
    public enum KeycloakUserType{
        USER_MANAGER("user-manager"),
        AUTH("auth");

        private String name;
    }
}
