# -*- coding: utf-8 -*-

import pandas as pd
import requests
import base64
import json
from datetime import datetime

# 📌 Configuración de OpenMRS
BASE_URL = "http://hii1sc-qlty.inf.pucp.edu.pe"
USER_API = "/openmrs/ws/rest/v1/user"
PROVIDER_API = "/openmrs/ws/rest/v1/provider"
ROLE_API = "/openmrs/ws/rest/v1/role"

OPENMRS_USER_URL = BASE_URL + USER_API
OPENMRS_PROVIDER_URL = BASE_URL + PROVIDER_API
OPENMRS_ROLE_URL = BASE_URL + ROLE_API

USERNAME = "admin"
PASSWORD = "Admin123"

# 📌 Mapeo de atributos de Provider en OpenMRS cambiar el uuid generado
ATTRIBUTE_MAPPING = {
    "Labor": "e1a56450-9f54-4bb3-a1d2-e3d00fbf7027",
    "Profession": "2ea7075a-55b8-4365-952e-c346631724e3"
}


# 📌 Función para generar encabezados con autenticación
def get_auth_headers():
    credentials = f"{USERNAME}:{PASSWORD}"
    encoded_credentials = base64.b64encode(credentials.encode("utf-8")).decode("utf-8")
    return {
        "Authorization": f"Basic {encoded_credentials}",
        "Content-Type": "application/json; charset=utf-8"
    }

# 📌 Función para limpiar caracteres no compatibles con LATIN-1
def clean_text(text):
    if isinstance(text, str):
        return text.encode("latin-1", "ignore").decode("latin-1")  # Reemplaza caracteres no compatibles
    return text

# 📌 Obtener roles existentes en OpenMRS
def get_roles_mapping():
    headers = get_auth_headers()
    try:
        response = requests.get(f"{OPENMRS_ROLE_URL}?limit=100&v=default", headers=headers, timeout=10)
        response.raise_for_status()
        roles_data = response.json()

        # Crear lista de roles con name y uuid
        roles_list = [
            {
                "uuid": role["uuid"],
                "name": clean_text(role.get("name", role.get("display", "Desconocido")))
            }
            for role in roles_data.get("results", [])
        ]

        # Construcción del JSON final
        roles_json = {"roles": roles_list}

        # 📌 Mostrar el JSON estructurado para comparación
        print("\n📌 JSON de roles estructurado para comparación:")
        print(json.dumps(roles_json, indent=4))

        return roles_json

    except requests.exceptions.RequestException as e:
        print(f"Error obteniendo roles de OpenMRS: {str(e)}")
        return {"roles": []}




# 📌 Crear un nuevo rol en OpenMRS si no existe
def create_role(rol_name, rol_description):
    headers = get_auth_headers()
    payload = {
        "name": clean_text(rol_name),
        "description": clean_text(rol_description)
    }

    try:
        response = requests.post(OPENMRS_ROLE_URL, headers=headers, json=payload, timeout=10)
        response.raise_for_status()
        role_response = response.json()
        return role_response.get("uuid")
    except requests.exceptions.RequestException as e:
        print(f"Error creando rol '{rol_name}': {str(e)}")
        return None

def update_existing_user(user_uuid,person_uuid, user_data, roles_list, row_index):
    headers = get_auth_headers()

    payload = {
        "person": {
            "names": [{"givenName": user_data["givenName"], "familyName": user_data["familyName"]}],
            "gender": user_data["gender"],
            "birthdate": str(user_data["birthdate"]),
        },
        "roles": roles_list
    }

    # 📌 Mostrar el JSON de actualización antes de enviarlo
    print("\n📌 JSON ENVIADO PARA ACTUALIZACIÓN DE USUARIO:")
    print(json.dumps(payload, indent=4))

    try:
        response = requests.post(f"{OPENMRS_USER_URL}/{user_uuid}", headers=headers, json=payload, timeout=10)
        response.raise_for_status()
        return {
            "row_index": row_index,
            "status": "Éxito",
            "message": "Usuario actualizado",
            "user_uuid": user_uuid,
            "person_uuid": person_uuid,
            "rol_usado": "rol actualizado"
        }
    except requests.exceptions.RequestException as e:
        return {"row_index": row_index, "status": "Error", "message": f"Error al actualizar usuario: {str(e)}" , "rol_usado": "rol NO actualizado"}


def get_existing_user_uuid(username):
    headers = get_auth_headers()
    try:
        response = requests.get(f"{OPENMRS_USER_URL}?q={username}&v=default", headers=headers, timeout=10)
        response.raise_for_status()
        users_data = response.json()

        if users_data.get("results"):
            user_uuid = users_data["results"][0]["uuid"]
            person_uuid = users_data["results"][0]["person"]["uuid"]

            return user_uuid, person_uuid
        
        return None
    except requests.exceptions.RequestException as e:
        print(f"Error buscando UUID del usuario '{username}': {str(e)}")
        return None


# 📌 Registrar un usuario en OpenMRS y obtener su UUID de persona
def register_user(user_data, roles_json, row_index):
    headers = get_auth_headers()
    user_data = {key: clean_text(value) for key, value in user_data.items()}

    required_fields = ["username", "password", "givenName", "familyName", "gender", "birthdate"]
    missing_fields = [field for field in required_fields if not user_data.get(field)]

    if missing_fields:
        return {"row_index": row_index, "status": "Error", "message": f"Faltan datos: {', '.join(missing_fields)}", "rol_usado": "Datos incompletos"}

    # 📌 Obtener el UUID del rol desde roles_json
    rol_name = user_data.get("role_name", "").strip()
    rol_description = user_data.get("role_description", "")
    rol_uuid = None
    rol_mensaje = "No asignado"

    if rol_name:
        # 🔍 Buscar el rol en la lista de roles obtenida de OpenMRS
        rol_uuid = next((role["uuid"] for role in roles_json["roles"] if role["name"] == rol_name), None)

        if rol_uuid:
            rol_mensaje = f"Usado: {rol_name}"
        else:
            print(f"⚠️ El rol '{rol_name}' no existe en OpenMRS. Creándolo...")
            rol_uuid = create_role(rol_name, rol_description)
            if rol_uuid:
                roles_json["roles"].append({"uuid": rol_uuid, "name": rol_name})  # Agregar nuevo rol al JSON de roles
                rol_mensaje = f"Creado: {rol_name}"
            else:
                rol_mensaje = f"Error creando: {rol_name}"

    roles_list = [{"uuid": rol_uuid}] if rol_uuid else []

    # 📌 Construcción del JSON para la solicitud
    payload = {
        "username": user_data["username"],
        "password": user_data["password"],
        "person": {
            "names": [{"givenName": user_data["givenName"], "familyName": user_data["familyName"]}],
            "gender": user_data["gender"],
            "birthdate": str(user_data["birthdate"]),
        },
        "roles": roles_list
    }

    # 📌 Mostrar el JSON que se enviará antes de hacer la solicitud
    #print("\n📌 JSON ENVIADO A OPENMRS (CREAR/ACTUALIZAR USUARIO):")
    #print(json.dumps(payload, indent=4))

    try:
        # Intentar crear el usuario
        response = requests.post(OPENMRS_USER_URL, headers=headers, json=payload, timeout=10)
        response.raise_for_status()
        user_response = response.json()
        person_uuid = user_response.get("person", {}).get("uuid", None)

        if not person_uuid:
            return {"row_index": row_index, "status": "Error", "message": "No se obtuvo UUID de la persona", "rol_usado": rol_mensaje}

        return {
            "row_index": row_index,
            "status": "Éxito",
            "message": "Usuario registrado",
            "person_uuid": person_uuid,
            "rol_usado": rol_mensaje
        }

    except requests.exceptions.HTTPError as e:
        # 📌 Verificar si el error es porque el usuario ya existe
        try:
            error_response = response.json()
            error_message = error_response.get("error", {}).get("message", "")

            if "Username" in error_message and "is already in use" in error_message:
                print(f"⚠️ El usuario '{user_data['username']}' ya existe. Buscando su UUID para actualizar...")

                # Buscar el UUID del usuario existente
                user_uuid, person_uuid = get_existing_user_uuid(user_data["username"])
                if user_uuid and person_uuid:
                    return update_existing_user(user_uuid,person_uuid, user_data, roles_list, row_index)
                else:
                    return {"row_index": row_index, "status": "Error", "message": "No se pudo encontrar el usuario existente para actualizar", "rol_usado": rol_mensaje}
        except Exception as parse_error:
            print(f"Error procesando la respuesta de error de OpenMRS: {parse_error}")

        return {"row_index": row_index, "status": "Error", "message": f"Error en la solicitud: {str(e)}", "rol_usado": "Error general"}

# 📌 Registrar el Provider asociado al usuario
def register_provider(person_uuid, user_data, row_index=0):
    headers = get_auth_headers()
    attributes = []

    print("\n📌 user_data recibido:")
    print(json.dumps(user_data, indent=4, ensure_ascii=False))

    for attr_name, attr_uuid in ATTRIBUTE_MAPPING.items():
        # 📌 Depuración: Mostrar qué clave estamos buscando
        print(f"🔍 Buscando clave '{attr_name}' en user_data...")

        if attr_name in user_data:
            value = user_data[attr_name]

            if value:  # Evita valores vacíos o None
                print(f"?Atributo encontrado: {attr_name} -> {value}")
                attributes.append({"attributeType": attr_uuid, "value": clean_text(value)})
            else:
                print(f"⚠️ Atributo '{attr_name}' está vacío. No se agregará.")
        else:
            print(f"?Clave '{attr_name}' no encontrada en user_data.")

    # 📌 Depuración: Mostrar la lista final de atributos asignados
    print("\n📌 Lista final de atributos asignados:")
    print(json.dumps(attributes, indent=4, ensure_ascii=False))

    payload = {
        "person": person_uuid,
        "identifier": clean_text(user_data["identifier"]),
        "attributes": attributes,
        "retired": False
    }

    try:
        response = requests.post(OPENMRS_PROVIDER_URL, headers=headers, json=payload, timeout=10)
        response.raise_for_status()
        return {"row_index": row_index, "status": "Éxito", "message": "Provider registrado"}
    except requests.exceptions.RequestException as e:
        return {"row_index": row_index, "status": "Error", "message": f"Error en la solicitud: {str(e)}"}

# 📌 Procesar los usuarios desde un archivo Excel y generar un log
def process_users_from_excel(filename, log_filename):
    df = pd.read_excel(filename, dtype=str)
    df.fillna("", inplace=True)
    log_data = []

    roles_mapping = get_roles_mapping()

    for row_index, row in df.iterrows():
        if row.isnull().all():
            continue

        user_data = row.dropna().to_dict()
        user_response = register_user(user_data, roles_mapping, row_index)

        if user_response["status"] == "Éxito":
            provider_response = register_provider(user_response["person_uuid"], user_data, row_index=row_index)
        else:
            provider_response = {"row_index": row_index, "status": "Error", "message": "No se creó el Provider"}

        log_entry = {
            **user_data,
            "Usuario Estado": user_response["status"],
            "Provider Estado": provider_response["status"],
            "Usuario Mensaje": user_response["message"],
            "Rol Usado": user_response.get("rol_usado", "No asignado"),  # ?Evita KeyError
            "Fecha": datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        }
        log_data.append(log_entry)

    if log_data:
        log_df = pd.DataFrame(log_data)

        # 📌 Guardar el log en Excel
        log_df.to_excel(log_filename, index=False)

        # 📌 Guardar el log en CSV para más compatibilidad
        ##csv_filename = log_filename.replace(".xlsx", ".csv")
        #log_df.to_csv(csv_filename, index=False, encoding="utf-8")

        print(f"\n?Log generado y guardado en: {log_filename}")

        # 📌 Mostrar el log en pantalla
        #import ace_tools as tools
        #tools.display_dataframe_to_user(name="Log de Usuarios en OpenMRS", dataframe=log_df)

    else:
        print("\n⚠️ No se generaron registros en el log. Verifica el archivo de entrada.")


# 📌 Archivos de entrada y log
EXCEL_FILE = "usuarios_openmrs.xlsx"
LOG_FILE = "usuarios_providers_log.xlsx"

# 📌 Ejecutar el proceso
def main():
    process_users_from_excel(EXCEL_FILE, LOG_FILE)

if __name__ == "__main__":
    main()