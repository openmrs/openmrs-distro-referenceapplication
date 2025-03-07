# OpenMRS 3.0 Reference Application - SIH SALUS

Este repositorio contiene la configuración y despliegue de la distribución **SIH SALUS** basada en OpenMRS 3.0 Reference Application.

## Configuración y Despliegue

Se ha creado un archivo `template-enviromentVariables.env` para facilitar la configuración y gestión de variables de entorno en el despliegue con Docker.

### Pasos para la Configuración
1. Copiar el archivo de variables de entorno:
   ```bash
   cp template-enviromentVariables.env .env
   ```
2. Editar `.env` y actualizar las variables necesarias según el entorno de despliegue.
3. Construir y levantar los servicios con Docker Compose:
   ```bash
   docker compose up -d
   ```

## Servicios Disponibles
Los siguientes servicios están disponibles en la distribución **SIH SALUS**:
- `db`: Base de datos MySQL para OpenMRS.
- `backend`: Servidor de OpenMRS.
- `frontend`: Interfaz de usuario basada en OpenMRS 3.0.
- `gateway`: Servidor API Gateway para la gestión de microservicios.

## Administración y Mantenimiento

### Reconstrucción de Servicios
Si es necesario reconstruir un servicio específico, ejecutar:
```bash
docker compose build --no-cache <servicio>
docker compose up -d <servicio>
```
Ejemplo para el frontend:
```bash
docker compose build --no-cache frontend
docker compose up -d frontend
```

### Eliminación de Caché y Recursos No Utilizados
Para limpiar la caché de Docker y liberar espacio en disco:
```bash
docker system prune -a --volumes
```
⚠️ **Advertencia:** Esto eliminará imágenes, contenedores detenidos y volúmenes no utilizados.

## Contribución
Si deseas contribuir a la mejora de **SIH SALUS**, por favor sigue las normas de desarrollo establecidas en este repositorio y realiza pull requests con los cambios propuestos.

---
Distribución personalizada **SIH SALUS** basada en OpenMRS 3.0.

