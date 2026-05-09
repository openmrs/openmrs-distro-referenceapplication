#!/bin/bash
# Script para generar contraseñas seguras y configurar Docker Secrets
#
# Uso: ./generate-secrets.sh
# IMPORTANTE: Ejecutar ANTES de docker-compose up en producción

set -e

echo "========================================="
echo "SIHSALUS Security Setup"
echo "Generando contraseñas seguras..."
echo "========================================="
echo ""

# Crear directorio de secrets
mkdir -p secrets
chmod 700 secrets

# Función para generar password seguro
generate_password() {
    openssl rand -base64 32 | tr -d "=+/" | cut -c1-32
}

# Generar contraseñas
echo "Generando contraseñas..."

# Keycloak
echo -n "$(generate_password)" > secrets/keycloak_admin_password.txt
echo -n "$(generate_password)" > secrets/keycloak_db_password.txt

# Grafana
echo -n "$(generate_password)" > secrets/grafana_admin_password.txt

# OpenMRS Database
echo -n "$(generate_password)" > secrets/mysql_root_password.txt
echo -n "$(generate_password)" > secrets/mysql_openmrs_password.txt
echo -n "$(generate_password)" > secrets/mysql_repl_password.txt
echo -n "$(generate_password)" > secrets/mysql_backup_password.txt

# Pi-hole
echo -n "$(generate_password)" > secrets/pihole_password.txt

# FUA Generator
echo -n "$(generate_password)" > secrets/fua_db_password.txt
echo -n "$(generate_password)" > secrets/fua_token.txt

# Ajustar permisos
chmod 600 secrets/*

echo "✅ Contraseñas generadas en ./secrets/"
echo ""

# Mostrar resumen (sin mostrar las contraseñas)
echo "========================================="
echo "Archivos de secrets creados:"
echo "========================================="
ls -lh secrets/
echo ""

# Crear archivo .env.production como plantilla
cat > .env.production << 'EOF'
# .env.production
# IMPORTANTE: Este archivo contiene referencias a secrets
# Las contraseñas reales están en ./secrets/ (NO en Git)

# Keycloak
KEYCLOAK_ADMIN=admin
KEYCLOAK_ADMIN_PASSWORD_FILE=/run/secrets/keycloak_admin_password
KC_DB_DATABASE=keycloak
KC_DB_USERNAME=keycloak
KC_DB_PASSWORD_FILE=/run/secrets/keycloak_db_password
KC_HOSTNAME=openmrs.hospital.local
KEYCLOAK_PORT=8180
KEYCLOAK_REALM=openmrs
KEYCLOAK_CLIENT_ID=openmrs
KEYCLOAK_CLIENT_SECRET=433b9e20-549e-42e7-a05a-6f83dfffa8ba

# Grafana
GRAFANA_ADMIN_USER=admin
GRAFANA_ADMIN_PASSWORD_FILE=/run/secrets/grafana_admin_password

# MySQL/MariaDB
MYSQL_ROOT_PASSWORD_FILE=/run/secrets/mysql_root_password
OMRS_DB_USER=openmrs
OMRS_DB_PASSWORD_FILE=/run/secrets/mysql_openmrs_password
OMRS_DB_REPL_USER=openmrs_repl
OMRS_DB_REPL_PASSWORD_FILE=/run/secrets/mysql_repl_password
OMRS_DB_BACKUP_USER=openmrs_backup
OMRS_DB_BACKUP_PASSWORD_FILE=/run/secrets/mysql_backup_password

# Pi-hole DNS
DNS_PASSWORD_FILE=/run/secrets/pihole_password
HOSPITAL_GATEWAY=192.168.1.1
HOSPITAL_NETWORK=192.168.1.0/24

# FUA Generator
SIHSALUS_FUA_GEN_DB_USER=fuagenerator
SIHSALUS_FUA_GEN_DB_PASSWORD_FILE=/run/secrets/fua_db_password
SIHSALUS_FUA_GEN_DB=fuagenerator
SIHSALUS_FUA_GEN_TOKEN_FILE=/run/secrets/fua_token
EOF

echo "✅ Plantilla .env.production creada"
echo ""

# Crear .gitignore para secrets
cat > secrets/.gitignore << 'EOF'
# NO commitear secrets a Git
*
!.gitignore
EOF

echo "✅ .gitignore configurado para secrets/"
echo ""

# Instrucciones
echo "========================================="
echo "✅ Setup completado!"
echo "========================================="
echo ""
echo "PRÓXIMOS PASOS:"
echo ""
echo "1. Revisar y ajustar .env.production según tu entorno"
echo ""
echo "2. Para usar secrets en docker-compose.yml, agregar:"
echo "   secrets:"
echo "     keycloak_admin_password:"
echo "       file: ./secrets/keycloak_admin_password.txt"
echo ""
echo "3. En cada servicio, referenciar secrets:"
echo "   keycloak:"
echo "     secrets:"
echo "       - keycloak_admin_password"
echo "     environment:"
echo "       KEYCLOAK_ADMIN_PASSWORD_FILE: /run/secrets/keycloak_admin_password"
echo ""
echo "4. IMPORTANTE: Hacer backup de ./secrets/ en ubicación segura"
echo ""
echo "5. Documentar contraseñas en gestor de passwords (1Password, Bitwarden, etc.)"
echo ""
echo "========================================="
echo "⚠️  ADVERTENCIA DE SEGURIDAD"
echo "========================================="
echo ""
echo "- NO commitear ./secrets/ a Git"
echo "- NO compartir contraseñas por email/chat"
echo "- Usar gestor de passwords corporativo"
echo "- Cambiar contraseñas periódicamente"
echo "- Revocar acceso cuando empleados dejen el hospital"
echo ""
echo "========================================="
