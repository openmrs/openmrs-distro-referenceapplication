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
- [Configuración SSL/HTTPS](#configuración-sslhttps)
- [Credenciales de GitHub Packages](#credenciales-de-github-packages)
- [Políticas de Seguridad](#políticas-de-seguridad-cifrado-de-backups-y-retención-de-logs)

---

## Inicio Rápido

### 1. Configurar variables de entorno

Crear un archivo `.env` en la raíz del proyecto con las siguientes variables obligatorias:

```env
# Credenciales de GitHub Packages (obligatorio para construir el backend)
GHP_USERNAME=<tu_usuario_github>
GHP_PASSWORD=<tu_token_github_con_read:packages>

# Token de OCL para importar conceptos médicos
OMRS_OCL_TOKEN=<tu_token_de_ocl>
```

### 2. Construir e iniciar

#### Sin SSL (desarrollo local)

```bash
docker compose build
docker compose up -d

# http://localhost/openmrs/spa
```

#### Con SSL (producción / red hospitalaria)

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

## Configuración SSL/HTTPS

SIHSALUS genera certificados SSL auto-firmados, pensados para redes hospitalarias internas sin acceso a internet.

No se requiere un dominio público ni una autoridad certificadora externa. El sistema genera su propio certificado al iniciar por primera vez.

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

## Políticas de Seguridad: Cifrado de Backups y Retención de Logs

Este proyecto implementa:
- **Cifrado automático de backups**: Los archivos de respaldo se cifran con AES-256 usando openssl. La clave se provee vía la variable de entorno `BACKUP_ENCRYPTION_PASSWORD`. El backup sin cifrar se elimina tras el cifrado exitoso.
- **Rotación y retención de logs**: Los scripts de backup mantienen solo los últimos 5 archivos de log, eliminando los más antiguos automáticamente.
