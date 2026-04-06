#!/bin/bash
# ------------------------------------------------------------------------------
# Script: backup_dump.sh
# Descripcion: Backup SQL logico en caliente (sin detener la DB).
#              Genera un .sql.gz que se puede restaurar con restore_dump.sh
# Uso: ./backup_dump.sh [--container NOMBRE] [--dir DIRECTORIO] [--max N]
# ------------------------------------------------------------------------------

set -euo pipefail

CONTAINER_NAME="${CONTAINER_NAME:-peruHCE-db-master}"
BACKUP_DIR="${BACKUP_DIR:-/home/${USER}/peruHCE-dumps}"
MAX_BACKUPS="${MAX_BACKUPS:-10}"
DB_NAME="openmrs"
DB_USER="root"
DB_PASSWORD="${MYSQL_ROOT_PASSWORD:-openmrs}"
TIMESTAMP=$(date +"%Y-%m-%d_%H-%M-%S")
DUMP_FILE="dump_${TIMESTAMP}.sql.gz"

# Parseo de argumentos
while [[ $# -gt 0 ]]; do
    case $1 in
        --container) CONTAINER_NAME="$2"; shift 2;;
        --dir) BACKUP_DIR="$2"; shift 2;;
        --max) MAX_BACKUPS="$2"; shift 2;;
        --help|-h)
            echo "Uso: $0 [--container NOMBRE] [--dir DIRECTORIO] [--max N]"
            exit 0;;
        *) echo "Opcion desconocida: $1"; exit 1;;
    esac
done

mkdir -p "$BACKUP_DIR"

echo "[INFO] Iniciando dump SQL en caliente de '$DB_NAME' ($TIMESTAMP)"

# Dump en caliente con --single-transaction (consistente, sin bloqueo)
docker exec "$CONTAINER_NAME" \
    mariadb-dump \
    --user="$DB_USER" \
    --password="$DB_PASSWORD" \
    --single-transaction \
    --routines \
    --triggers \
    --events \
    --quick \
    --lock-tables=false \
    "$DB_NAME" | gzip > "$BACKUP_DIR/$DUMP_FILE"

DUMP_SIZE=$(du -h "$BACKUP_DIR/$DUMP_FILE" | cut -f1)
echo "[OK] Dump creado: $BACKUP_DIR/$DUMP_FILE ($DUMP_SIZE)"

# Cifrar si hay clave disponible
if [ -n "${BACKUP_ENCRYPTION_PASSWORD:-}" ]; then
    openssl enc -aes-256-cbc -salt -pbkdf2 \
        -pass env:BACKUP_ENCRYPTION_PASSWORD \
        -in "$BACKUP_DIR/$DUMP_FILE" \
        -out "$BACKUP_DIR/$DUMP_FILE.enc"
    rm -f "$BACKUP_DIR/$DUMP_FILE"
    echo "[OK] Cifrado: $BACKUP_DIR/$DUMP_FILE.enc"
    DUMP_FILE="$DUMP_FILE.enc"
fi

# Rotar backups antiguos
BACKUP_COUNT=$(ls -1 "$BACKUP_DIR"/dump_*.sql.gz* 2>/dev/null | wc -l)
if [ "$BACKUP_COUNT" -gt "$MAX_BACKUPS" ]; then
    ls -1t "$BACKUP_DIR"/dump_*.sql.gz* | tail -n +$((MAX_BACKUPS+1)) | xargs rm -f
    echo "[INFO] Rotados, manteniendo los ultimos $MAX_BACKUPS"
fi

echo "[OK] Backup SQL completado sin downtime"
