# need-help-ext-ui
main ui service.

### Common description
This service is built with using reactive library `webflux`. For authentication and registration users used `Keycloak`. If authorization is successful `Keycloak` returns to user a JWT token is then used in the headers.

example basic config `Keycloak`:
```yaml
version: "3.8"

services:
  postgres:
    container_name: postgres
    image: library/postgres
    environment:
      POSTGRES_USER: ${POSTGRES_USER:-postgres}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD:-postgres}
      POSTGRES_DB: keycloak_db
    ports:
      - "5000:5432"
    restart: unless-stopped

  keycloak:
    image: jboss/keycloak
    container_name: keycloak
    environment:
      DB_VENDOR: POSTGRES
      DB_ADDR: postgres
      DB_DATABASE: keycloak_db
      DB_USER: ${POSTGRES_USER:-postgres}
      DB_PASSWORD: ${POSTGRES_PASSWORD:-postgres}
      KEYCLOAK_USER: 1
      KEYCLOAK_PASSWORD: 1
      KEYCLOAK_LOGLEVEL: ERROR
    ports:
      - "8484:8080"
    depends_on:
      - postgres
    links:
      - "postgres:postgres"
```

example application.yaml:
```yaml
keycloak:
  realm: ${KC_REALM:need-help}
  auth-server-url: ${KEYCLOAK_URL:http://localhost:8484/auth}
  default-group: "your default group when created user"
  admin-group: "your admin group"
  credentials:
    manager:
      client-id: ${KC_USER_MANAGER_ID:need_help_ext-ui_manager}
      secret: ${KC_USER_MANAGER_SECRET:ENi7c3ADZCfUzQOukPxHhGDDTKMiYhlb}
```
