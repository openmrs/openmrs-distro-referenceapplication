# -*- coding: utf-8 -*-
"""
Carga masiva de formularios a OpenMRS

Este script se encarga de procesar los archivos JSON en un directorio especificado,
verificar si los formularios ya existen en OpenMRS y, si no existen, crear los formularios
y asociar los archivos JSON correspondientes.

Los parámetros de entrada deben ser proporcionados por línea de comandos.
"""

import os
import json
import requests
from base64 import b64encode
import argparse

def get_existing_forms(base_url, auth_header):
    """
    Obtiene la lista de formularios existentes en OpenMRS.

    Devuelve un conjunto con los nombres de los formularios existentes.
    """
    url = f"{base_url}/form?v=custom:(name)"
    try:
        response = requests.get(url, headers=auth_header)
        response.raise_for_status()  # Lanza un error si el código de estado no es 200
        data = response.json()
        return {form["name"] for form in data.get("results", [])}
    except requests.exceptions.RequestException as e:
        print(f"Error al obtener formularios: {e}")
        return set()
    except json.JSONDecodeError:
        print("Error: La respuesta de la API no es un JSON válido.")
        return set()

def create_form(base_url, form_data, auth_header):
    """
    Crea un formulario en OpenMRS si no existe previamente.

    Parámetros:
        form_data (dict): Datos del formulario a crear.
    
    Devuelve el UUID del formulario creado si la creación es exitosa, de lo contrario, devuelve None.
    """
    if form_data["name"] in get_existing_forms(base_url, auth_header):
        print(f"✖ Formulario '{form_data['name']}' ya existe. Omitiendo.")
        return None

    url = f"{base_url}/form"
    try:
        response = requests.post(url, headers=auth_header, json=form_data)
        response.raise_for_status()  # Lanza un error si el código de estado no es 201
        return response.json()["uuid"]
    except requests.exceptions.RequestException as e:
        print(f"Error creando formulario: {e}")
        return None

def upload_clobdata(file_path, base_url, auth_header):
    """
    Subir un archivo JSON a clobdata en OpenMRS.

    Parámetros:
        file_path (str): Ruta del archivo JSON a subir.

    Devuelve el UUID del archivo JSON subido si la subida es exitosa, de lo contrario, devuelve None.
    """
    url = f"{base_url}/clobdata"
    try:
        with open(file_path, "rb") as file:
            files = {"file": (os.path.basename(file_path), file, "application/json")}
            response = requests.post(url, headers={"Authorization": auth_header["Authorization"]}, files=files)
            response.raise_for_status()  # Lanza un error si el código de estado no es 201
            return response.text.strip()  # La respuesta es un UUID en texto plano
    except requests.exceptions.RequestException as e:
        print(f"Error subiendo JSON: {e}")
        return None

def link_json_to_form(form_uuid, json_uuid, base_url, auth_header):
    """
    Asocia un archivo JSON subido al formulario especificado.

    Parámetros:
        form_uuid (str): UUID del formulario.
        json_uuid (str): UUID del archivo JSON subido.
    """
    url = f"{base_url}/form/{form_uuid}/resource"
    data = {
        "name": "JSON schema",
        "dataType": "AmpathJsonSchema",
        "valueReference": json_uuid
    }
    try:
        response = requests.post(url, headers=auth_header, json=data)
        response.raise_for_status()  # Lanza un error si el código de estado no es 201
        print(f"JSON asociado al formulario {form_uuid}")
    except requests.exceptions.RequestException as e:
        print(f"Error asociando JSON: {e}")

def process_json_files(folder_path, base_url, auth_header, depth=0, max_depth=3):
    """
    Procesa todos los archivos JSON en la carpeta especificada hasta una profundidad máxima.

    Parámetros:
        folder_path (str): Ruta de la carpeta que contiene los archivos JSON.
        base_url (str): URL base de la API de OpenMRS.
        auth_header (dict): Cabecera de autenticación para las solicitudes HTTP.
        depth (int): Profundidad actual del directorio.
        max_depth (int): Profundidad máxima permitida.
    """
    if depth > max_depth:
        return

    existing_forms = get_existing_forms(base_url, auth_header)

    # Recorremos directorios y archivos
    for root, dirs, files in os.walk(folder_path):
        current_depth = root.count(os.sep) - folder_path.count(os.sep)
        if current_depth >= max_depth:
            dirs[:] = []  # Evitar que os.walk descienda a subdirectorios

        for file_name in files:
            if file_name.endswith(".json"):
                file_path = os.path.join(root, file_name)
                with open(file_path, "r", encoding="utf-8") as file:
                    try:
                        json_data = json.load(file)
                    except json.JSONDecodeError:
                        print(f"Advertencia: El archivo {file_name} no es un JSON válido. Omitiendo.")
                        continue
                
                form_name = json_data.get("name")
                if not form_name:
                    print(f"Advertencia: El archivo {file_name} no tiene un 'name' definido.")
                    continue

                if form_name in existing_forms:
                    print(f"X Formulario '{form_name}' ya existe. Omitiendo.")
                    continue
                
                form_data = {
                    "name": form_name,
                    "version": json_data.get("version", "1.0"),
                    "published": True,
                    "description": json_data.get("description", "Genero por Script"),
                    "encounterType": json_data["encounterType"]
                }

                form_uuid = create_form(base_url, form_data, auth_header)
                if form_uuid:
                    json_uuid = upload_clobdata(file_path, base_url, auth_header)
                    if json_uuid:
                        link_json_to_form(form_uuid, json_uuid, base_url, auth_header)

def main():
    """
    Función principal para manejar los parámetros de entrada y ejecutar el proceso de carga masiva.
    """
    # Configuración de los parámetros de línea de comandos
    parser = argparse.ArgumentParser(description="Carga masiva de formularios a OpenMRS")
    parser.add_argument("base_url", help="URL base del servidor OpenMRS")
    parser.add_argument("username", help="Nombre de usuario para autenticación")
    parser.add_argument("password", help="Contraseña para autenticación")
    parser.add_argument("folder_path", help="Ruta de la carpeta que contiene los archivos JSON")
    parser.add_argument("--max_depth", type=int, default=3, help="Profundidad máxima de los directorios a procesar (por defecto 3)")

    args = parser.parse_args()

    # Generamos la cabecera de autenticación
    auth_header = {
        "Authorization": f"Basic {b64encode(f'{args.username}:{args.password}'.encode()).decode()}",
        "Content-Type": "application/json"
    }

    # Asegurarse de que la carpeta de destino exista
    os.makedirs(args.folder_path, exist_ok=True)
    print(f"Coloca tus archivos JSON en: {args.folder_path}")

    # Ejecutar el proceso
    process_json_files(args.folder_path, args.base_url, auth_header, max_depth=args.max_depth)

if __name__ == "__main__":
    main()
