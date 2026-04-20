# syntax=docker/dockerfile:1
# Serves the SPA assembled by the sihsalus-esm init container (spa-data volume).
# cgr.dev/chainguard/nginx: distroless, non-root (UID 65532), port 8080, near-zero CVEs.
FROM cgr.dev/chainguard/nginx:latest

COPY nginx.conf /etc/nginx/nginx.conf
