#!/bin/bash
# ------------------------------------------------------------------------------
# Script: restore_dump.sh
# Descripcion: Restaura la DB desde un dump SQL en caliente (sin detener la DB).
#              Compatible con backups de backup_dump.sh
# Uso: ./restore_dump.sh [--container NOMBRE] [--dir DIRECTORIO] [--file ARCHIVO]
# ------------------------------------------------------------------------------

set -euo pipefail

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

CONTAINER_NAME="${CONTAINER_NAME:-peruHCE-db-master}"
BACKUP_DIR="${BACKUP_DIR:-/home/${USER}/peruHCE-dumps}"
BACKUP_FILE=""
DB_NAME="openmrs"
DB_USER="root"
DB_PASSWORD="${MYSQL_ROOT_PASSWORD:-openmrs}"
TEMP_DIR="/tmp/peruHCE-restore-dump-$$"

# Leer credenciales desde Docker secrets si existen
if [ -f /run/secrets/BACKUP_ENCRYPTION_PASSWORD ]; then
    export BACKUP_ENCRYPTION_PASSWORD="$(cat /run/secrets/BACKUP_ENCRYPTION_PASSWORD)"
fi

# Parseo de argumentos
while [[ $# -gt 0 ]]; do
    case $1 in
        --container) CONTAINER_NAME="$2"; shift 2;;
        --dir) BACKUP_DIR="$2"; shift 2;;
        --file) BACKUP_FILE="$2"; shift 2;;
        --help|-h)
            echo "Uso: $0 [--container NOMBRE] [--dir DIRECTORIO] [--file ARCHIVO]"
            echo ""
            echo "Restaura un dump SQL en caliente (DB sigue corriendo)."
            echo "El backend debe estar detenido para evitar escrituras durante el restore."
            exit 0;;
        *) echo "Opcion desconocida: $1"; exit 1;;
    esac
done

cleanup() {
    rm -rf "$TEMP_DIR"
}
trap cleanup EXIT

# --- Seleccion de backup ---

if [ -n "$BACKUP_FILE" ]; then
    if [ ! -f "$BACKUP_FILE" ]; then
        echo -e "${RED}[ERROR] Archivo no encontrado: $BACKUP_FILE${NC}"; exit 1
    fi
    selected_file="$BACKUP_FILE"
else
    if [ ! -d "$BACKUP_DIR" ]; then
        echo -e "${RED}[ERROR] Directorio no encontrado: $BACKUP_DIR${NC}"; exit 1
    fi

    mapfile -t backups < <(ls -t "$BACKUP_DIR"/dump_*.sql.gz* 2>/dev/null)

    if [ ${#backups[@]} -eq 0 ]; then
        echo -e "${RED}[ERROR] No se encontraron dumps en $BACKUP_DIR${NC}"; exit 1
    fi

    echo ""
    echo "Dumps disponibles (mas reciente primero):"
    echo "-----------------------------------------------------------"
    for ((i=0; i<${#backups[@]}; i++)); do
        fname=$(basename "${backups[i]}")
        fsize=$(du -h "${backups[i]}" | cut -f1)
        fdate=$(stat -c '%y' "${backups[i]}" 2>/dev/null | cut -d'.' -f1)
        printf "  %2d) %-45s %6s  %s\n" "$((i+1))" "$fname" "$fsize" "$fdate"
    done
    echo "-----------------------------------------------------------"

    while true; do
        read -p "Selecciona el dump a restaurar (1-${#backups[@]}): " selection
        if [[ "$selection" =~ ^[0-9]+$ ]] && [ "$selection" -ge 1 ] && [ "$selection" -le ${#backups[@]} ]; then
            selected_file="${backups[$((selection-1))]}"
            break
        fi
        echo "Seleccion invalida."
    done
fi

echo -e "[INFO] Dump seleccionado: ${GREEN}$(basename "$selected_file")${NC}"

# --- Confirmacion ---

echo ""
echo -e "${YELLOW}ATENCION: Esto reemplazara la base de datos '$DB_NAME'.${NC}"
echo -e "${YELLOW}El backend deberia estar detenido para evitar conflictos.${NC}"
read -p "Continuar? (s/N): " resp
[[ "$resp" =~ ^[sS]$ ]] || { echo "Cancelado."; exit 0; }

# --- Detener backend (pero NO la DB) ---

echo "[INFO] Deteniendo backend para evitar escrituras..."
docker compose stop backend 2>/dev/null || true
sleep 2

# --- Descifrar si es necesario ---

mkdir -p "$TEMP_DIR"
sql_source="$selected_file"

if [[ "$selected_file" == *.enc ]]; then
    echo "[INFO] Descifrando dump..."

    if [ -z "${BACKUP_ENCRYPTION_PASSWORD:-}" ]; then
        read -sp "Ingresa la clave de cifrado: " BACKUP_ENCRYPTION_PASSWORD
        echo ""
        export BACKUP_ENCRYPTION_PASSWORD
    fi

    decrypted_file="$TEMP_DIR/$(basename "${selected_file%.enc}")"
    if ! openssl enc -aes-256-cbc -d -salt -pbkdf2 \
        -pass env:BACKUP_ENCRYPTION_PASSWORD \
        -in "$selected_file" \
        -out "$decrypted_file"; then
        echo -e "${RED}[ERROR] Fallo al descifrar. Clave incorrecta?${NC}"; exit 1
    fi
    sql_source="$decrypted_file"
    echo -e "${GREEN}[OK] Descifrado exitoso${NC}"
fi

# --- Restaurar en caliente ---

echo "[INFO] Restaurando dump en la base de datos (en caliente)..."
echo "[INFO] Esto puede tomar varios minutos segun el tamano..."

# Drop y recrear la DB, luego importar
docker exec -i "$CONTAINER_NAME" mariadb \
    --user="$DB_USER" \
    --password="$DB_PASSWORD" \
    -e "DROP DATABASE IF EXISTS ${DB_NAME}; CREATE DATABASE ${DB_NAME} CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"

# Importar el dump
gunzip -c "$sql_source" | docker exec -i "$CONTAINER_NAME" mariadb \
    --user="$DB_USER" \
    --password="$DB_PASSWORD" \
    "$DB_NAME"

echo -e "${GREEN}[OK] Dump restaurado exitosamente${NC}"

# --- Reiniciar backend ---

echo "[INFO] Reiniciando backend..."
docker compose up -d

echo ""
echo -e "${GREEN}============================================${NC}"
echo -e "${GREEN}  Restauracion en caliente completada${NC}"
echo -e "${GREEN}============================================${NC}"
echo ""
echo "[INFO] Verifica que el backend inicie correctamente:"
echo "  docker compose logs -f backend"
