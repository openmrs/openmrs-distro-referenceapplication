import pandas as pd
import requests
import base64
from datetime import datetime

# 📌 Configuración de la API de OpenMRS
BASE_PATH = "http://hii1sc-qlty.inf.pucp.edu.pe"
END_URL = "/openmrs/ws/rest/v1/providerattributetype"
OPENMRS_URL = BASE_PATH + END_URL

USERNAME = "admin"
PASSWORD = "Admin123"

# 📌 Función para codificar credenciales en Base64
def get_auth_header(username, password):
    credentials = f"{username}:{password}"
    encoded_credentials = base64.b64encode(credentials.encode()).decode()
    return {"Authorization": f"Basic {encoded_credentials}"}

# 📌 Cargar datos desde un archivo Excel
def load_excel_data(filename):
    df = pd.read_excel(filename, dtype=str)  # Leer el archivo como texto
    df.fillna("", inplace=True)  # Reemplazar valores NaN con cadenas vacías
    return df

# 📌 Registrar un atributo en OpenMRS
def register_provider_attribute(attr_data, row_index):
    headers = get_auth_header(USERNAME, PASSWORD)
    headers.update({"Content-Type": "application/json"})

    # 📌 Mapear "Texto Libre" al tipo de dato correcto en OpenMRS
    datatype_mapping = {
        "Texto Libre": "org.openmrs.customdatatype.datatype.FreeTextDatatype"
    }
    
    # 📌 Verificar que los campos requeridos estén presentes
    required_fields = ["name", "description", "datatypeClassname", "minOccurs", "maxOccurs"]
    missing_fields = [field for field in required_fields if not attr_data.get(field)]

    if missing_fields:
        return {"row_index": row_index, "status": "Error", "message": f"Faltan los siguientes datos: {', '.join(missing_fields)}"}

    # 📌 Construir el payload JSON
    payload = {
        "name": attr_data["name"],
        "description": attr_data["description"],
        "datatypeClassname": datatype_mapping.get(attr_data["datatypeClassname"], attr_data["datatypeClassname"]),
        "minOccurs": int(attr_data["minOccurs"]),
        "maxOccurs": int(attr_data["maxOccurs"])
    }

    # 📌 Enviar la solicitud POST a OpenMRS
    try:
        response = requests.post(OPENMRS_URL, headers=headers, json=payload, timeout=10)
        response.raise_for_status()  # Lanza un error si la solicitud HTTP falla
        return {"row_index": row_index, "status": "Éxito", "message": "Atributo registrado correctamente"}
    except requests.exceptions.RequestException as e:
        return {"row_index": row_index, "status": "Error", "message": f"Error en la solicitud: {str(e)}"}

# 📌 Procesar los atributos desde el archivo Excel y generar un log
def process_attributes_from_excel(filename, log_filename):
    df = load_excel_data(filename)
    log_data = []  # Lista para almacenar el log de cada atributo

    for row_index, row in df.iterrows():
        attr_data = row.to_dict()
        response = register_provider_attribute(attr_data, row_index)

        # 📌 Agregar información al log
        log_entry = {
            **attr_data,
            "Estado": response["status"],
            "Mensaje": response["message"],
            "Fecha": datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        }
        log_data.append(log_entry)

    # 📌 Guardar el log en un archivo Excel
    log_df = pd.DataFrame(log_data)
    log_df.to_excel(log_filename, index=False)
    print(f"Log generado: {log_filename}")

# 📌 Archivos de entrada y log
EXCEL_FILE = "atributos_proveedores.xlsx"  # Asegúrate de que el archivo tiene el formato correcto
LOG_FILE = "atributos_log.xlsx"

# 📌 Ejecutar el proceso
def main():
    process_attributes_from_excel(EXCEL_FILE, LOG_FILE)

if __name__ == "__main__":
    main()
