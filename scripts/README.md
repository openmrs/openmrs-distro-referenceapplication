# Scripts Directory

Este directorio centraliza todos los scripts utilizados en el proyecto peruHCE Distribution.

## Estructura

```
scripts/
├── backup/          # Scripts relacionados con backups de base de datos
├── database/        # Scripts de inicialización y configuración de base de datos
├── frontend/        # Scripts para el frontend (nginx, SPA)
└── utils/           # Scripts de utilidades generales
```

## Contenido por Carpeta

### backup/
Scripts para realizar backups completos e incrementales de las bases de datos master y réplica:
- `backup_full.sh` - Backup completo
- `backup_incremental.sh` - Backup incremental
- `backup_full_generate.sh` - Generación de backup completo
- `backup_incremental_generate.sh` - Generación de backup incremental
- `backup_full_generate_replica.sh` - Backup completo de réplica
- `backup_incremental_generate_replica.sh` - Backup incremental de réplica
- `replica_reset.sh` - Script bash para resetear la replicación de la base de datos
- `backup_all.sh` - Backup de todo el sistema

### database/
Scripts de inicialización de las bases de datos:
- `db_init_master.sh` - Inicialización de la base de datos master
- `db_init_slave.sh` - Inicialización de la base de datos slave/réplica
- `init-master-sql.sql` - (futuro) Script SQL para master
- `init-slave-sql.sql` - (futuro) Script SQL para slave

### frontend/
Scripts relacionados con el frontend:
- `frontend_startup.sh` - Script de inicio del contenedor nginx con el SPA

### utils/
Scripts de utilidades generales:
- `init_full.sh` - Inicialización completa del sistema desde cero
- `logs_creation.sh` - Obtención de logs del módulo initializer
- `generateCertificate.sh` - Generación de certificados SSL autofirmados
- `rebuildService.sh` - Reconstrucción del servicio
- `bulk_form_uploader.py` - Carga masiva de formularios
- `insertar_usuarios.py` - Inserción de usuarios en el sistema
- `rebuildScriptPython.py` - Script de reconstrucción en Python
- `docker-compose-app.service` - Archivo de servicio systemd


## Políticas de Seguridad y Cumplimiento

### Cifrado de Backups
Todos los scripts de backup generan archivos cifrados automáticamente usando AES-256 (openssl). La clave de cifrado debe ser provista mediante la variable de entorno `BACKUP_ENCRYPTION_PASSWORD` (recomendado: usar Docker secrets). El archivo .tar.gz sin cifrar se elimina tras el cifrado exitoso. El backup final tiene extensión `.tar.gz.enc`.

**Ejemplo de uso:**
```bash
export BACKUP_ENCRYPTION_PASSWORD="<clave-segura>"
./backup_full.sh
```

### Rotación y Retención de Logs
Cada script de backup mantiene solo los últimos 5 archivos de log (por ejemplo, `fullBackup_log.txt`, `fullBackup_log.txt.20240501120000`, ...). Los logs más antiguos se eliminan automáticamente para limitar el almacenamiento y cumplir políticas de retención.

**Importante:** Nunca almacenes la clave de cifrado en el código ni en archivos versionados. Usa Docker secrets o variables de entorno seguras.

---
## Uso

Los scripts están referenciados en:
- `docker-compose.yml` - Para entornos de desarrollo
- `compose/ssl.yml` - Para entornos con SSL/HTTPS
- `frontend/Dockerfile` - Para la construcción del contenedor frontend

## Migración

Esta estructura fue creada para centralizar scripts que anteriormente estaban dispersos en:
- `backupScripts/` → `scripts/backup/`
- `db-config/` → `scripts/database/`
- `utils/` → `scripts/utils/`
- `frontend/startup.sh` → `scripts/frontend/frontend_startup.sh`

Todas las referencias en los archivos de configuración han sido actualizadas para reflejar la nueva ubicación.
