// ===========================================
// peruHCE - Docker Bake Build Definitions
// ===========================================
//
// USAGE:
//   docker buildx bake                 # Build core (backend, gateway, frontend-init)
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
  targets = ["backend", "gateway", "frontend-init"]
}

group "all" {
  targets = ["backend", "gateway", "frontend-init", "keycloak", "certbot"]
}

// ---- Core Targets ----

target "backend" {
  context    = "."
  dockerfile = "backend/Dockerfile"
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

target "frontend-init" {
  context    = "../sihsalus-esm"
  dockerfile = "Dockerfile"
  tags       = ["peruhce-frontend-init:latest"]
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
