// ===========================================
// sihsalus - Docker Bake Build Definitions
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
  secret     = ["id=GHP_USERNAME,env=GHP_USERNAME", "id=GHP_PASSWORD,env=GHP_PASSWORD"]
}

target "gateway" {
  inherits   = ["_base"]
  context    = "./gateway"
  dockerfile = "Dockerfile"
  tags       = ["${REGISTRY}sihsalus-gateway:${TAG}"]
}

target "frontend-init" {
  inherits   = ["_base"]
  context    = "./frontend"
  dockerfile = "Dockerfile"
  tags       = ["${REGISTRY}sihsalus-frontend-init:${TAG}"]
  args   = { FRONTEND_TAG = TAG }
  secret = ["id=GHP_TOKEN,env=GHP_PASSWORD"]
}

// ---- Optional Targets ----

target "keycloak" {
  inherits   = ["_base"]
  context    = "./keycloak"
  dockerfile = "Dockerfile"
  tags       = ["${REGISTRY}sihsalus-keycloak:${TAG}"]
}

target "certbot" {
  inherits   = ["_base"]
  context    = "./certbot"
  dockerfile = "Dockerfile"
  tags       = ["${REGISTRY}sihsalus-certbot:${TAG}"]
}
