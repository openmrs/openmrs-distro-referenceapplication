# keycloak/Dockerfile
FROM quay.io/keycloak/keycloak:26.3.2

# Variables por defecto (sobreescribibles desde docker-compose)
ENV KEYCLOAK_ADMIN=admin
ENV KEYCLOAK_ADMIN_PASSWORD=admin123
ENV KC_DB=postgres
ENV KC_DB_URL=jdbc:postgresql://keycloak-db:5432/keycloak
ENV KC_DB_USERNAME=keycloak
ENV KC_DB_PASSWORD=keycloak123
ENV KC_HOSTNAME=localhost
ENV KC_HOSTNAME_STRICT=false

# Import automático de realm
ENV KEYCLOAK_IMPORT=/opt/keycloak/data/import/realm-export.json

# Copiamos el realm duro y el tema custom
COPY realm-export.json /opt/keycloak/data/import/realm-export.json
COPY themes/ /opt/keycloak/themes/

# Ajustamos permisos para el usuario 1000
USER root
RUN chown -R 1000:0 /opt/keycloak/data /opt/keycloak/themes
USER 1000

# Pre-compilación para PostgreSQL
RUN /opt/keycloak/bin/kc.sh build --db=${KC_DB}

# Arranca en modo dev e importa el realm
ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev", "--import-realm"]
