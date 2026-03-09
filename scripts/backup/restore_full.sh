#!/bin/bash
# ------------------------------------------------------------------------------
# Script: restore_full.sh
# Descripcion: Restaura la base de datos MariaDB desde un backup completo.
# Uso: ./restore_full.sh [--container NOMBRE] [--dir DIRECTORIO] [--file ARCHIVO]
# Autor: Equipo PeruHCE
# ------------------------------------------------------------------------------

set -euo pipefail

# Colores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Configuracion por defecto
CONTAINER_NAME="${CONTAINER_NAME:-peruHCE-db-master}"
BACKUP_DIR="${BACKUP_DIR:-/home/${USER}/peruHCE-fullBackups}"
BACKUP_FILE=""
COMPOSE_PROJECT="sihsalus-distro-referenceapplication"
DB_VOLUME="${COMPOSE_PROJECT}_db-data"
BACKUP_VOLUME="${COMPOSE_PROJECT}_db-backup"
MARIADB_IMAGE="mariadb:10.11.7"
TEMP_DIR="/tmp/peruHCE-restore-$$"

# Leer credenciales desde Docker secrets si existen
if [ -f /run/secrets/BACKUP_ENCRYPTION_PASSWORD ]; then
    export BACKUP_ENCRYPTION_PASSWORD="$(cat /run/secrets/BACKUP_ENCRYPTION_PASSWORD)"
fi

# Parseo de argumentos
while [[ $# -gt 0 ]]; do
    case $1 in
        --container)
            CONTAINER_NAME="$2"; shift 2;;
        --dir)
            BACKUP_DIR="$2"; shift 2;;
        --file)
            BACKUP_FILE="$2"; shift 2;;
        --help|-h)
            echo "Uso: $0 [--container NOMBRE] [--dir DIRECTORIO] [--file ARCHIVO]"
            echo ""
            echo "Opciones:"
            echo "  --container  Nombre del contenedor DB (default: peruHCE-db-master)"
            echo "  --dir        Directorio de backups (default: ~/peruHCE-fullBackups)"
            echo "  --file       Archivo de backup especifico (omitir para seleccion interactiva)"
            exit 0;;
        *)
            echo "Opcion desconocida: $1. Usa --help para ver opciones."; exit 1;;
    esac
done

# Funciones
cleanup() {
    echo -e "\n[INFO] Limpiando archivos temporales..."
    rm -rf "$TEMP_DIR"
}
trap cleanup EXIT

confirm() {
    echo -e "${YELLOW}$1${NC}"
    read -p "Continuar? (s/N): " resp
    [[ "$resp" =~ ^[sS]$ ]] || { echo "Cancelado."; exit 0; }
}

# --- Seleccion de backup ---

if [ -n "$BACKUP_FILE" ]; then
    # Archivo especificado por argumento
    if [ ! -f "$BACKUP_FILE" ]; then
        echo -e "${RED}[ERROR] Archivo no encontrado: $BACKUP_FILE${NC}"; exit 1
    fi
    selected_file="$BACKUP_FILE"
else
    # Seleccion interactiva
    if [ ! -d "$BACKUP_DIR" ]; then
        echo -e "${RED}[ERROR] Directorio de backups no encontrado: $BACKUP_DIR${NC}"; exit 1
    fi

    mapfile -t backups < <(ls -t "$BACKUP_DIR"/*.tar.gz.enc "$BACKUP_DIR"/*.tar.gz 2>/dev/null)

    if [ ${#backups[@]} -eq 0 ]; then
        echo -e "${RED}[ERROR] No se encontraron backups en $BACKUP_DIR${NC}"; exit 1
    fi

    echo ""
    echo "Backups disponibles (mas reciente primero):"
    echo "-----------------------------------------------------------"
    for ((i=0; i<${#backups[@]}; i++)); do
        fname=$(basename "${backups[i]}")
        fsize=$(du -h "${backups[i]}" | cut -f1)
        fdate=$(stat -c '%y' "${backups[i]}" 2>/dev/null | cut -d'.' -f1)
        printf "  %2d) %-45s %6s  %s\n" "$((i+1))" "$fname" "$fsize" "$fdate"
    done
    echo "-----------------------------------------------------------"

    while true; do
        read -p "Selecciona el backup a restaurar (1-${#backups[@]}): " selection
        if [[ "$selection" =~ ^[0-9]+$ ]] && [ "$selection" -ge 1 ] && [ "$selection" -le ${#backups[@]} ]; then
            selected_file="${backups[$((selection-1))]}"
            break
        fi
        echo "Seleccion invalida."
    done
fi

echo ""
echo -e "[INFO] Backup seleccionado: ${GREEN}$(basename "$selected_file")${NC}"

# --- Verificaciones previas ---

# Verificar que el volumen de datos existe
if ! docker volume inspect "$DB_VOLUME" &>/dev/null; then
    echo -e "${RED}[ERROR] Volumen '$DB_VOLUME' no encontrado.${NC}"
    echo "[INFO] Volumenes disponibles:"
    docker volume ls --format '  {{.Name}}' | grep -i "db-data" || true
    exit 1
fi

confirm "ATENCION: Esto reemplazara TODA la base de datos actual. Los datos actuales se perderan."

# --- Descifrar si es necesario ---

mkdir -p "$TEMP_DIR"
archive_file="$selected_file"

if [[ "$selected_file" == *.enc ]]; then
    echo "[INFO] Backup cifrado detectado. Descifrando..."

    if [ -z "${BACKUP_ENCRYPTION_PASSWORD:-}" ]; then
        read -sp "Ingresa la clave de cifrado: " BACKUP_ENCRYPTION_PASSWORD
        echo ""
        export BACKUP_ENCRYPTION_PASSWORD
    fi

    decrypted_file="$TEMP_DIR/$(basename "${selected_file%.enc}")"
    openssl enc -aes-256-cbc -d -salt -pbkdf2 \
        -pass env:BACKUP_ENCRYPTION_PASSWORD \
        -in "$selected_file" \
        -out "$decrypted_file"

    if [ $? -ne 0 ]; then
        echo -e "${RED}[ERROR] Fallo al descifrar. Clave incorrecta?${NC}"; exit 1
    fi
    echo -e "${GREEN}[OK] Descifrado exitoso${NC}"
    archive_file="$decrypted_file"
fi

# --- Descomprimir ---

echo "[INFO] Descomprimiendo backup..."
restore_dir="$TEMP_DIR/restore"
mkdir -p "$restore_dir"
tar -xzf "$archive_file" -C "$restore_dir"

# Encontrar el directorio del backup (puede estar anidado)
backup_content=$(find "$restore_dir" -name "xtrabackup_checkpoints" -o -name "backup-my.cnf" | head -1)
if [ -z "$backup_content" ]; then
    echo -e "${RED}[ERROR] No se encontro un backup MariaDB valido en el archivo${NC}"; exit 1
fi
backup_data_dir=$(dirname "$backup_content")
echo -e "${GREEN}[OK] Backup descomprimido: $backup_data_dir${NC}"

# --- Preparar backup ---

echo "[INFO] Preparando backup (mariadb-backup --prepare)..."
docker run --rm \
    -v "$backup_data_dir:/backup/full" \
    --user root \
    "$MARIADB_IMAGE" \
    mariadb-backup --prepare --target-dir=/backup/full

echo -e "${GREEN}[OK] Backup preparado${NC}"

# --- Detener servicios ---

echo "[INFO] Deteniendo servicios de base de datos..."
docker compose stop db 2>/dev/null || docker stop "$CONTAINER_NAME" 2>/dev/null || true

# Esperar a que el contenedor se detenga
sleep 2

# --- Snapshot del volumen actual (safety net) ---

SNAPSHOT_VOLUME="${DB_VOLUME}-pre-restore"
echo "[INFO] Creando snapshot del volumen actual en '$SNAPSHOT_VOLUME'..."

# Eliminar snapshot anterior si existe
docker volume rm "$SNAPSHOT_VOLUME" 2>/dev/null || true

# Copiar volumen actual a snapshot
docker volume create "$SNAPSHOT_VOLUME" >/dev/null
docker run --rm \
    -v "$DB_VOLUME:/source:ro" \
    -v "$SNAPSHOT_VOLUME:/dest" \
    busybox sh -c "cp -a /source/. /dest/"

echo -e "${GREEN}[OK] Snapshot creado. En caso de fallo, restaurar con:${NC}"
echo -e "  docker run --rm -v $SNAPSHOT_VOLUME:/source:ro -v $DB_VOLUME:/dest busybox sh -c 'rm -rf /dest/* /dest/.* 2>/dev/null; cp -a /source/. /dest/'"

# --- Limpiar volumen de datos ---

echo "[INFO] Limpiando volumen de datos actual..."
docker run --rm \
    -v "$DB_VOLUME:/var/lib/mysql" \
    busybox sh -c "rm -rf /var/lib/mysql/* /var/lib/mysql/.*" 2>/dev/null || true

echo -e "${GREEN}[OK] Volumen limpiado${NC}"

# --- Restaurar ---

echo "[INFO] Restaurando backup (mariadb-backup --copy-back)..."
if ! docker run --rm \
    --name peruHCE-db-restore \
    -v "$DB_VOLUME:/var/lib/mysql" \
    -v "$backup_data_dir:/backup/full:ro" \
    --user root \
    "$MARIADB_IMAGE" \
    mariadb-backup --copy-back --target-dir=/backup/full; then

    echo -e "${RED}[ERROR] Fallo en copy-back. Restaurando snapshot anterior...${NC}"
    docker run --rm \
        -v "$DB_VOLUME:/var/lib/mysql" \
        busybox sh -c "rm -rf /var/lib/mysql/* /var/lib/mysql/.*" 2>/dev/null || true
    docker run --rm \
        -v "$SNAPSHOT_VOLUME:/source:ro" \
        -v "$DB_VOLUME:/dest" \
        busybox sh -c "cp -a /source/. /dest/"
    echo -e "${YELLOW}[INFO] Snapshot restaurado. Reiniciando con datos anteriores...${NC}"
    docker compose up -d
    exit 1
fi

echo -e "${GREEN}[OK] Datos restaurados${NC}"

# --- Reiniciar servicios ---

echo "[INFO] Reiniciando servicios..."
docker compose up -d

# Limpiar snapshot despues de exito
echo "[INFO] Eliminando snapshot (restore exitoso)..."
docker volume rm "$SNAPSHOT_VOLUME" 2>/dev/null || true

echo ""
echo -e "${GREEN}============================================${NC}"
echo -e "${GREEN}  Restauracion completada exitosamente${NC}"
echo -e "${GREEN}============================================${NC}"
echo ""
echo "[INFO] Verifica que el backend inicie correctamente:"
echo "  docker compose logs -f backend"
