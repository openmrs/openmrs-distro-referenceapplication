// ===========================================
// peruHCE - Docker Bake Build Definitions
// ===========================================
//
// USAGE:
//   docker buildx bake                 # Build core (backend, gateway, frontend)
//   docker buildx bake all             # Build all targets
//   docker buildx bake backend         # Build single target
//   docker buildx bake --print         # Show resolved build config (dry-run)

variable "TAG" {
  default = "qa"
}

variable "GHP_USERNAME" {
  default = ""
}

variable "GHP_PASSWORD" {
  default = ""
}

// ---- Groups ----

group "default" {
  targets = ["backend", "gateway", "frontend"]
}

group "all" {
  targets = ["backend", "gateway", "frontend", "keycloak", "certbot"]
}

// ---- Core Targets ----

target "backend" {
  context    = "."
  dockerfile = "Dockerfile"
  tags       = ["openmrs/openmrs-reference-application-3-backend:${TAG}"]
  args = {
    GHP_USERNAME = GHP_USERNAME
    GHP_PASSWORD = GHP_PASSWORD
  }
}

target "gateway" {
  context    = "./gateway"
  dockerfile = "Dockerfile"
  tags       = ["peruhce-gateway:latest"]
}

target "frontend" {
  context    = "./frontend"
  dockerfile = "Dockerfile"
  tags       = ["peruhce-frontend:latest"]
}

// ---- Optional Targets ----

target "keycloak" {
  context    = "./keycloak"
  dockerfile = "Dockerfile"
  tags       = ["peruhce-keycloak:latest"]
}

target "certbot" {
  context    = "./certbot"
  dockerfile = "Dockerfile"
  tags       = ["peruhce-certbot:latest"]
}
