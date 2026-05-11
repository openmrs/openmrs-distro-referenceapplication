// ===========================================
// sihsalus - Docker Bake Build Definitions
// ===========================================
//
// USAGE:
//   docker buildx bake                 # Build core (backend, gateway)
//   docker buildx bake all             # Build all targets
//   docker buildx bake backend         # Build single target
//   docker buildx bake --print         # Show resolved build config (dry-run)

variable "TAG" {
  default = "qa"
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
  targets = ["backend", "gateway"]
}

group "all" {
  targets = ["backend", "gateway", "keycloak", "certbot"]
}

// ---- Core Targets ----

target "backend" {
  inherits   = ["_base"]
  context    = "."
  dockerfile = "backend/Dockerfile"
  tags       = ["${REGISTRY}openmrs/openmrs-reference-application-3-backend:${TAG}"]
}

target "gateway" {
  inherits   = ["_base"]
  context    = "./gateway"
  dockerfile = "Dockerfile"
  tags       = ["${REGISTRY}sihsalus-gateway:${TAG}"]
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
