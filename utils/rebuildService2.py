# Importing libraries
import json
import os
import subprocess
import sys
from pathlib import Path

# Variables:
COMPOSE_DIR = ".."                     # Define the directory containing the docker-compose.yml file
DOCKER_FILE = "docker-compose.yml"      #File of docker compose

# ANSI Colors
GREEN =     "\033[0;32m"
RED =       "\033[0;31m"
NC =        "\033[0m"  # No Color

# Define which service need to erase volumes and their name
servicesIndications = [
    {
        "service":          "portainer",
        "name":             "peruHCE-portainer",
        "dependantOn":      None,
        "deleteVolumes":    True,
        "volumesToDelete":  [
            { "name": "portainer-data" },
        ]
    },
    {
        "service":          "dns",
        "name":             "peruHCE-dns",
        "dependantOn":      None,
        "deleteVolumes":    True,
        "volumesToDelete":  [
            { "name": "pihole-data" },
            { "name": "pihole-dnsmaq" }
        ]
    },
    {
        "service":          "gateway",
        "name":             "peruHCE-gateway",
        "dependantOn":      None,
        "deleteVolumes":    False
    },
    {
        "service":          "frontend",
        "name":          "peruHCE-frontend",
        "dependantOn":      None,
        "deleteVolumes":    True,
        "volumesToDelete":  [
            { "name": "openmrs-frontend" },
        ]
    },
    {
        "service":          "backend",
        "name":          "peruHCE-backend",
        "dependantOn":      "db",
        "deleteVolumes":    True,
        "volumesToDelete":  [
            { "name": "opemrs-data" },
        ]
    },
    {
        "service":          "db",
        "name":          "peruHCE-db-master",
        "dependantOn":      "db-replic",
        "deleteVolumes":    True,
        "volumesToDelete":  [
            { "name": "db-data" },
            { "name": "db-backup" }
        ]
    },
    {
        "service":          "db-replic",
        "name":             "peruHCE-db-replic",
        "dependantOn":      "db",
        "deleteVolumes":    True,
        "volumesToDelete":  [
            { "name": "db-data-r" },
            { "name": "db-backup-r" }
        ]
    }
]

##############################
# BEGIN OF PROMPT ############
##############################

# Go to compose directory
try:
    os.chdir(COMPOSE_DIR)
except FileNotFoundError:
    print("ERROR: DIRECTORIO DE DOCKER COMPOSE NO EXISTE. ")
    sys.exit(1)

# Check if docker compose file exists
if not os.path.isfile(DOCKER_FILE):
    print(f"{RED}ERROR: docker-compose.yml not found in {COMPOSE_DIR}{NC}")
    sys.exit(1)

# List service to rebuild, back and database should be one
print()
print("LISTA DE SERVICIOS DISPONIBLES:")
for index, indication in enumerate(servicesIndications) :
    print(f"{index+1:02d} Servicio: {indication['service']:15s} Nombre: {indication['name']}" )
user_input = input("\nIngresa el numero o nombre del servicio a reconstruir: ")
# Check type of input
if user_input.isdigit():
    # Check if input number is okay
    if not ( int(user_input) <= len(list(enumerate(servicesIndications))) and int(user_input) > 0 ):
        print("Numero ingresado incorrecto")
        exit(1)
    else:
        # Get service name by number
        serviceToRebuild = list(servicesIndications)[int(user_input) - 1]['service']
else:
    # Check if input service name is okay
    if not ( any( user_input in serviceInfo['service'] for serviceInfo in list(servicesIndications) ) ):
        print("Nombre de servicio ingresado incorrecto")
        exit(1)
    else:
        # Get service name by name
        serviceToRebuild = user_input

#print(serviceToRebuild)

# Extra question if one of the databases services were selected
if( serviceToRebuild == "db" or serviceToRebuild == "db-replic" ):
    user_input = input("Esta seguro de reconstruir un servicio de base de datos? (La sincronizacion de las replicas fallará) (si/no) ")
    rebuild = None
    if(user_input in ["si","s","SI","yes","YES","y"]):
        rebuild = True
    if(user_input in ["no","n","NO"]):
        rebuild = False
    # Invalid answer
    if(rebuild == None):
        print("Respuesta inválida.")
        exit(1)


# Extra question if backend was selected:
backendRebuild = None
if( serviceToRebuild == "backend" ):
    user_input = input("Desea reconstruir el servicio de backend y reiniciar la base de datos? (si/no) ")
    if(user_input in ["si","s","SI","yes","YES","y"]):
        backendRebuild = True
    if(user_input in ["no","n","NO"]):
        backendRebuild = False
    # Invalid answer
    if(backendRebuild == None):
        print("Respuesta inválida.")
        exit(1)

# Extra question if user wants to use cache:
cacheRebuild = None
if( serviceToRebuild == "backend" ):
    user_input = input("Desea reconstruir el servicio de backend y reiniciar la base de datos? (si/no) ")
    if(user_input in ["si","s","SI","yes","YES","y"]):
        cacheRebuild = True
    if(user_input in ["no","n","NO"]):
        cacheRebuild = False
    # Invalid answer
    if(cacheRebuild == None):
        print("Respuesta inválida.")
        exit(1)

#######################################
# Rebuild process with selected service
print()

# Extra step if backendRebuild with databases was selected
if serviceToRebuild == "backend" and backendRebuild == True:
    # Stop and erase both databases
    databases = ["db-replic", "db "]
    for dbService in databases:
        dbInfo = next((service for service in servicesIndications if service["service"] == dbService), None)
        if dbInfo == None:
            print("Error al reconstruir backend con bases de datos: Servicio " + dbService + " no encontrado.")
            exit(1)
        print("Deteniendo y borrando contenedor de ",dbInfo["service"])
        eraseDbService = ["docker", "compose", "rm", "-s", "-v", dbInfo["service"]]
        # Print the output
        print(result.stdout)
        if result.returncode != 0:
            print("STDERR:", result.stderr)
            exit(1)
        for volumeToErase in serviceInfo["volumesToDelete"]:
            eraseVolumeCommand = ["docker", "compose", "rm", volumeToErase["name"]]
            print("Borrando volumen: ", volumeToErase["name"])
            print("Executing command:", " ".join(eraseVolumeCommand))
            # Erase volume
            result = subprocess.run(eraseVolumeCommand, capture_output=True, text=True, shell=True)
            # Print the output
            print(result.stdout)
            if result.returncode != 0:
                print("STDERR:", result.stderr)
                exit(1)
        # Build db service
        buildContainerCommand = ["docker", "compose", "build", dbService]
        if cacheRebuild == False:
            buildContainerCommand.append("--no-cache")
        print("Construyendo contenedor de ",dbService)
        print("Executing command:", " ".join(buildContainerCommand))
        result = subprocess.run(buildContainerCommand, capture_output=True, text=True, shell=True)
        # Print the output
        print(result.stdout)
        if result.returncode != 0:
            print("STDERR:", result.stderr)
            exit(1)

        # Up db service
        upContainerCommand = ["docker", "compose", "up", "--no-deps", dbService]
        print("Construyendo contenedor de ",dbService)
        print("Executing command:", " ".join(upContainerCommand))
        result = subprocess.run(upContainerCommand, capture_output=True, text=True, shell=True)
        # Print the output
        print(result.stdout)
        if result.returncode != 0:
            print("STDERR:", result.stderr)
            exit(1)

# Stop and erase container of service and anonymius volumes
eraseContainerCommand = ["docker", "compose", "rm", "-s", "-v", serviceToRebuild]
print("Deteniendo y borrando contenedor de ",serviceToRebuild)
print("Executing command:", " ".join(eraseContainerCommand))
try:
    result = subprocess.run(eraseContainerCommand, capture_output=True, text=True, shell=True)
    # Print the output
    print(result.stdout)
    if result.returncode != 0:
        print("STDERR:", result.stderr)
        exit(1)
except subprocess.TimeoutExpired:
    print("Error: Tiempo expirado de espera.")
    exit(1)

# Erase not anonymous volumes
serviceInfo = next((service for service in servicesIndications if service["service"] == serviceToRebuild), None)
if(serviceInfo == None):
    print("Error al borrar volumenes: Servicio no encontrado.")
    exit(1)
if serviceInfo["deleteVolumes"] == True:
    print(serviceInfo["volumesToDelete"])
    for volumeToErase in serviceInfo["volumesToDelete"]:
        eraseVolumeCommand = ["docker", "compose", "rm", volumeToErase["name"]]
        print("Borrando volumen: ", volumeToErase["name"])
        print("Executing command:", " ".join(eraseVolumeCommand))

        # Erase volume
        result = subprocess.run(eraseVolumeCommand, capture_output=True, text=True, shell=True)
        # Print the output
        print(result.stdout)
        if result.returncode != 0:
            print("STDERR:", result.stderr)
            exit(1)
else:
    print("No volumes to erase")

# Build service
buildContainerCommand = ["docker", "compose", "build", serviceToRebuild]
if cacheRebuild == False:
    buildContainerCommand.append("--no-cache")
print("Construyendo contenedor de ",serviceToRebuild)
print("Executing command:", " ".join(buildContainerCommand))
result = subprocess.run(buildContainerCommand, capture_output=True, text=True, shell=True)
# Print the output
print(result.stdout)
if result.returncode != 0:
    print("STDERR:", result.stderr)
    exit(1)

# Up service
upContainerCommand = ["docker", "compose", "up", "--no-deps", serviceToRebuild]
print("Construyendo contenedor de ",serviceToRebuild)
print("Executing command:", " ".join(upContainerCommand))
result = subprocess.run(upContainerCommand, capture_output=True, text=True, shell=True)
# Print the output
print(result.stdout)
if result.returncode != 0:
    print("STDERR:", result.stderr)
    exit(1)

# End of flow
print("##########################################################")
print("Servicio " + serviceToRebuild + " reconstruido.")
print("##########################################################")