# SIHSALUS

![OpenMRS 3.x](https://img.shields.io/badge/OpenMRS-3.6.0-f26522?style=flat-square)
![Docker](https://img.shields.io/badge/Docker-compose-2496ED?style=flat-square&logo=docker&logoColor=white)
![MariaDB](https://img.shields.io/badge/MariaDB-10.11-003545?style=flat-square&logo=mariadb&logoColor=white)
![Nginx](https://img.shields.io/badge/Nginx-SSL-009639?style=flat-square&logo=nginx&logoColor=white)
![License](https://img.shields.io/badge/MPL_2.0-brightgreen?style=flat-square&label=License)

> Distribución OpenMRS 3.x para establecimientos de salud del Perú.
> Certificados SSL auto-firmados, despliegue offline, backups cifrados.

---

## Tabla de Contenidos

- [Inicio Rápido](#inicio-rápido)
- [Profiles](#profiles)
- [Docker Bake (Build)](#docker-bake-build)
- [Configuración SSL/HTTPS](#configuración-sslhttps)
- [Credenciales de GitHub Packages](#credenciales-de-github-packages)
- [Backup y Restore](#backup-y-restore)
- [Políticas de Seguridad](#políticas-de-seguridad-cifrado-de-backups-y-retención-de-logs)

---

## Inicio Rápido

### 1. Configurar variables de entorno

```bash
cp .env.template .env
# Editar .env con tus valores
```

Variables obligatorias en `.env`:

```env
# GitHub Packages (obligatorio para build)
GHP_USERNAME=<tu_usuario_github>
GHP_PASSWORD=<tu_token_github_con_read:packages>

# Base de datos OpenMRS
MYSQL_OPENMRS_PASSWORD=<password_seguro>
MYSQL_ROOT_PASSWORD=<password_seguro>

# Token OCL para importar conceptos médicos
OMRS_OCL_TOKEN=<tu_token_de_ocl>
```

### 2. Construir e iniciar

```bash
# Core (gateway, frontend, backend, db)
docker compose up -d

# http://localhost/openmrs/spa
```

## Profiles

La infraestructura se organiza en **profiles** opcionales. Solo los servicios core (gateway, frontend, backend, db) se inician por defecto.

```bash
# Core solamente
docker compose up -d

# Core + FUA Generator
docker compose --profile fua up -d

# Core + HAPI FHIR
docker compose --profile hapi up -d

# Core + Medical Imaging (OHIF/Orthanc)
docker compose --profile imaging up -d

# Core + Keycloak Auth
docker compose --profile keycloak up -d

# Core + Observabilidad (Grafana/Prometheus/Loki)
docker compose --profile monitoring up -d

# Combinar profiles
docker compose --profile fua --profile hapi --profile monitoring up -d

# Core + SSL/HTTPS (override especial)
docker compose -f docker-compose.yml -f compose/ssl.yml up -d
```

Cada profile requiere sus variables en `.env`. Ver `.env.template` para la lista completa.

### Estructura de archivos

```
docker-compose.yml              # Entry point (include + profiles + volumes + networks)
docker-compose-no-volumes.yml   # CI/testing (standalone)
docker-bake.hcl                 # Build definitions
compose/
  core.yml                      # gateway, frontend, backend, db
  fua.yml                       # profile: fua
  hapi.yml                      # profile: hapi
  imaging.yml                   # profile: imaging
  keycloak.yml                  # profile: keycloak
  monitoring.yml                # profile: monitoring
  ssl.yml                       # override con -f (modifica gateway)
```

## Docker Bake (Build)

Para construir imágenes se puede usar `docker buildx bake`, que paraleliza y cachea builds:

```bash
# Build core (backend, gateway, frontend) en paralelo
docker buildx bake

# Build un target específico
docker buildx bake backend

# Build todos (+ keycloak, certbot)
docker buildx bake all

# Dry-run (ver config sin ejecutar)
docker buildx bake --print
```

Alternativamente, `docker compose build` sigue funcionando.

## Configuración SSL/HTTPS

SIHSALUS genera certificados SSL auto-firmados, pensados para redes hospitalarias internas sin acceso a internet.

No se requiere un dominio público ni una autoridad certificadora externa. El sistema genera su propio certificado al iniciar por primera vez.

### Iniciar con SSL

Agregar las variables SSL al `.env`:

```env
CERT_WEB_DOMAIN_COMMON_NAME=192.168.10.5
CERT_WEB_DOMAINS=192.168.10.5,localhost,127.0.0.1
```

```bash
docker compose -f docker-compose.yml -f compose/ssl.yml build
docker compose -f docker-compose.yml -f compose/ssl.yml up -d

# https://192.168.10.5/openmrs/spa
```

### Variables SSL en `.env`

| Variable | Descripción | Default |
|----------|-------------|---------|
| `SSL_MODE` | `dev` (genera una vez y termina) o `prod` (renueva automáticamente) | `dev` |
| `CERT_WEB_DOMAINS` | Todas las direcciones por las que se accederá al servidor, separadas por coma (IPs y/o nombres) | `localhost,127.0.0.1` |
| `CERT_WEB_DOMAIN_COMMON_NAME` | La dirección principal del servidor (IP o nombre) | `localhost` |

### Ejemplo para despliegue en hospital

Si el servidor tiene IP `192.168.10.5` en la red del hospital y los equipos acceden por esa IP:

```env
CERT_WEB_DOMAIN_COMMON_NAME=192.168.10.5
CERT_WEB_DOMAINS=192.168.10.5,localhost,127.0.0.1
```

Si el hospital tiene varias VLANs y el servidor tiene más de una IP, incluirlas todas:

```env
CERT_WEB_DOMAIN_COMMON_NAME=192.168.10.5
CERT_WEB_DOMAINS=192.168.10.5,192.168.20.5,172.16.0.5,localhost,127.0.0.1
```

### Instalar el certificado en los equipos del hospital

Al ser un certificado auto-firmado, los navegadores mostrarán una advertencia de seguridad la primera vez. Para evitarlo, instalar el certificado en cada equipo cliente:

1. Copiar el archivo `fullchain.pem` del servidor (se encuentra en el volumen Docker `peruHCE-letsencrypt-data`)
2. **Windows**: Importar en "Entidades de certificación raíz de confianza"
3. **Linux**: Copiar a `/usr/local/share/ca-certificates/` y ejecutar `sudo update-ca-certificates`

## Credenciales de GitHub Packages

El backend necesita credenciales de GitHub para descargar módulos privados desde GitHub Packages durante el build.

Las credenciales se configuran en el archivo `.env`:

```env
GHP_USERNAME=<tu_usuario_github>
GHP_PASSWORD=<tu_token_github_con_read:packages>
```

Estas se pasan como **build args** al Dockerfile, que las exporta como variables de entorno para que Maven las use en `credentials/settings.xml.template` (`${env.GHP_USERNAME}`, `${env.GHP_PASSWORD}`).

> **Nota:** Este proyecto NO usa Docker secrets. Las credenciales se manejan mediante variables de entorno en el archivo `.env`.

## Backup y Restore

Los scripts se encuentran en `scripts/backup/`. Hay dos métodos:

### Dump SQL (en caliente, sin downtime)

```bash
# Backup - DB sigue corriendo, sin interrupciones
./scripts/backup/backup_dump.sh

# Restore - solo detiene el backend, DB sigue corriendo
./scripts/backup/restore_dump.sh
```

### Backup binario (en frío, más rápido)

```bash
# Backup con mariadb-backup
./scripts/backup/backup_full.sh

# Restore - detiene DB, crea snapshot de seguridad, restaura
./scripts/backup/restore_full.sh

# Especificar archivo directamente
./scripts/backup/restore_full.sh --file ~/peruHCE-fullBackups/backup_2026-03-01.tar.gz.enc
```

| | Dump SQL (caliente) | Binario (frío) |
|---|---|---|
| Downtime | No (solo backend) | Sí (detiene DB) |
| Velocidad | Más lento | Rápido |
| Formato | `.sql.gz` | `.tar.gz` (mariadb-backup) |
| Cifrado | Opcional (AES-256) | Obligatorio (AES-256) |
| Idempotente | Sí | Sí (snapshot pre-restore) |

> Los backups cifrados requieren la variable `BACKUP_ENCRYPTION_PASSWORD`.

## Políticas de Seguridad: Cifrado de Backups y Retención de Logs

Este proyecto implementa:
- **Cifrado automático de backups**: Los archivos de respaldo se cifran con AES-256 usando openssl. La clave se provee vía la variable de entorno `BACKUP_ENCRYPTION_PASSWORD`. El backup sin cifrar se elimina tras el cifrado exitoso.
- **Rotación y retención de logs**: Los scripts de backup mantienen solo los últimos 5 archivos de log, eliminando los más antiguos automáticamente.
