#!/bin/bash
# SIHSALUS Backup Script
# Realiza backup completo de todos los servicios críticos
#
# Uso: ./backup_all.sh
# Cron: 0 2 * * * /path/to/backup_all.sh

set -e

# Configuración
BACKUP_DIR="/backup/sihsalus"
DATE=$(date +%Y%m%d_%H%M%S)
RETENTION_DAYS=30

# Crear directorio de backup con fecha
CURRENT_BACKUP="$BACKUP_DIR/$DATE"
mkdir -p "$CURRENT_BACKUP"

echo "========================================="
echo "SIHSALUS Backup Script"
echo "Fecha: $(date)"
echo "Directorio: $CURRENT_BACKUP"
echo "========================================="

# 1. Backup de Base de Datos MariaDB (OpenMRS)
echo "[1/7] Backing up MariaDB Master..."
docker exec sihsalus-db-master mysqldump \
  --user=root \
  --password=${MYSQL_ROOT_PASSWORD:-openmrs} \
  --all-databases \
  --single-transaction \
  --quick \
  --lock-tables=false \
  --routines \
  --triggers \
  --events \
  | gzip > "$CURRENT_BACKUP/mariadb-master.sql.gz"
echo "✅ MariaDB Master backup completed"

# 2. Backup de Base de Datos Replica (verificación)
echo "[2/7] Backing up MariaDB Replica..."
docker exec sihsalus-db-replic mysqldump \
  --user=root \
  --password=${MYSQL_ROOT_PASSWORD:-openmrs} \
  --all-databases \
  --single-transaction \
  --quick \
  --lock-tables=false \
  | gzip > "$CURRENT_BACKUP/mariadb-replica.sql.gz"
echo "✅ MariaDB Replica backup completed"

# 3. Backup de Keycloak Database
echo "[3/7] Backing up Keycloak Database..."
docker exec sihsalus-keycloak-db pg_dump \
  -U ${KC_DB_USERNAME:-keycloak} \
  -d ${KC_DB_DATABASE:-keycloak} \
  | gzip > "$CURRENT_BACKUP/keycloak-db.sql.gz"
echo "✅ Keycloak DB backup completed"

# 4. Backup de Orthanc DICOM Data
echo "[4/7] Backing up Orthanc DICOM data..."
docker run --rm \
  -v sihsalus-distro-referenceapplication_orthanc-data:/data \
  -v "$CURRENT_BACKUP":/backup \
  alpine tar czf /backup/orthanc-data.tar.gz -C /data .
echo "✅ Orthanc backup completed"

# 5. Backup de Prometheus Data
echo "[5/7] Backing up Prometheus metrics..."
docker run --rm \
  -v sihsalus-distro-referenceapplication_prometheus-data:/data \
  -v "$CURRENT_BACKUP":/backup \
  alpine tar czf /backup/prometheus-data.tar.gz -C /data .
echo "✅ Prometheus backup completed"

# 6. Backup de Grafana Data
echo "[6/7] Backing up Grafana dashboards..."
docker run --rm \
  -v sihsalus-distro-referenceapplication_grafana-data:/data \
  -v "$CURRENT_BACKUP":/backup \
  alpine tar czf /backup/grafana-data.tar.gz -C /data .
echo "✅ Grafana backup completed"

# 7. Backup de Configuraciones
echo "[7/7] Backing up configurations..."
tar czf "$CURRENT_BACKUP/configs.tar.gz" \
  docker-compose.yml \
  .env \
  dns/ \
  monitoring/ \
  scripts/ \
  keycloak/realm-export.json \
  2>/dev/null || echo "⚠️  Some config files not found"
echo "✅ Configurations backup completed"

# Calcular tamaño del backup
BACKUP_SIZE=$(du -sh "$CURRENT_BACKUP" | cut -f1)
echo ""
echo "========================================="
echo "Backup completed successfully!"
echo "Location: $CURRENT_BACKUP"
echo "Size: $BACKUP_SIZE"
echo "========================================="

# Crear symlink al último backup
ln -sfn "$CURRENT_BACKUP" "$BACKUP_DIR/latest"

# Limpiar backups antiguos (retención)
echo ""
echo "Cleaning old backups (retention: $RETENTION_DAYS days)..."
find "$BACKUP_DIR" -maxdepth 1 -type d -mtime +$RETENTION_DAYS -exec rm -rf {} \;
echo "✅ Old backups cleaned"

# Opcional: Copiar backup a ubicación remota
# rsync -avz "$CURRENT_BACKUP" user@backup-server:/backups/sihsalus/

echo ""
echo "========================================="
echo "All done! ✅"
echo "========================================="
