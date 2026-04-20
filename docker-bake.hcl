// ===========================================
// peruHCE - Docker Bake Build Definitions
// ===========================================
//
// USAGE:
//   docker buildx bake                 # Build core (backend, gateway, frontend, frontend-init)
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

variable "REGISTRY" {
  default = ""
}

// ---- Shared base ----

target "_base" {
  pull = true
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
  inherits   = ["_base"]
  context    = "."
  dockerfile = "backend/Dockerfile"
  tags       = ["${REGISTRY}openmrs/openmrs-reference-application-3-backend:${TAG}"]
  args = {
    GHP_USERNAME = GHP_USERNAME
    GHP_PASSWORD = GHP_PASSWORD
  }
}

target "gateway" {
  inherits   = ["_base"]
  context    = "./gateway"
  dockerfile = "Dockerfile"
  tags       = ["${REGISTRY}peruhce-gateway:${TAG}"]
}

target "frontend-init" {
  inherits   = ["_base"]
  context    = "../sihsalus-esm"
  dockerfile = "Dockerfile"
  target     = "init"
  tags       = ["${REGISTRY}peruhce-frontend-init:${TAG}"]
}

// ---- Optional Targets ----

target "keycloak" {
  inherits   = ["_base"]
  context    = "./keycloak"
  dockerfile = "Dockerfile"
  tags       = ["${REGISTRY}peruhce-keycloak:${TAG}"]
}

target "certbot" {
  inherits   = ["_base"]
  context    = "./certbot"
  dockerfile = "Dockerfile"
  tags       = ["${REGISTRY}peruhce-certbot:${TAG}"]
}
