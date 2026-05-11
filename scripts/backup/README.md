# Backup y Restore

Scripts para respaldar y restaurar la base de datos MariaDB de sihsalus.

## Scripts disponibles

| Script | Tipo | Downtime | Descripcion |
|--------|------|----------|-------------|
| `backup_dump.sh` | SQL (caliente) | No | Dump SQL con `--single-transaction` |
| `restore_dump.sh` | SQL (caliente) | Solo backend | Restaura dump `.sql.gz` sin detener DB |
| `backup_full.sh` | Binario (frio) | No | Backup con `mariadb-backup` |
| `restore_full.sh` | Binario (frio) | Si | Restaura backup binario, con snapshot de seguridad |

## Uso rapido

### Dump SQL (en caliente, sin downtime)

```bash
# Backup
./scripts/backup/backup_dump.sh

# Restore interactivo (lista dumps disponibles)
./scripts/backup/restore_dump.sh

# Restore directo
./scripts/backup/restore_dump.sh --file ~/sihsalus-dumps/dump_2026-03-09.sql.gz
```

### Backup binario (en frio, mas rapido)

```bash
# Backup
./scripts/backup/backup_full.sh

# Restore interactivo
./scripts/backup/restore_full.sh

# Restore directo
./scripts/backup/restore_full.sh --file ~/sihsalus-fullBackups/backup_2026-03-01.tar.gz.enc
```

## Opciones comunes

```
--container NOMBRE    Contenedor DB (default: sihsalus-db-master)
--dir DIRECTORIO      Directorio de backups
--file ARCHIVO        Archivo especifico (omitir para seleccion interactiva)
--max N               Maximo de backups a retener (solo backup scripts)
```

## Cifrado

Los backups se cifran con AES-256 si la variable `BACKUP_ENCRYPTION_PASSWORD` esta definida.
El restore descifra automaticamente archivos `.enc` (pide clave si no esta en env).

## Seguridad del restore

- `restore_dump.sh`: idempotente, solo detiene el backend
- `restore_full.sh`: crea un snapshot del volumen antes de limpiar. Si el restore falla, restaura automaticamente el snapshot anterior
