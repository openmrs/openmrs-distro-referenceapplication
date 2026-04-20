# frontend/Dockerfile
#
# Init container: downloads a pre-built frontend release from sihsalus-esm,
# then assembles the SPA into SPA_OUTPUT_DIR at deployment time.
#
# Required release asset at:
#   https://github.com/sihsalus/sihsalus-esm/releases/download/<TAG>/frontend-dist.tar.gz
#
# Tarball must contain (same layout as the build output):
#   node_modules/
#   packages/apps/
#   packages/tooling/scripts/assemble-importmap.js

FROM node:24-alpine AS init
WORKDIR /app

ARG GHP_TOKEN
ARG FRONTEND_TAG

RUN apk add --no-cache curl && \
    curl -fsSL \
      -H "Authorization: Bearer ${GHP_TOKEN}" \
      "https://github.com/sihsalus/sihsalus-esm/releases/download/${FRONTEND_TAG}/frontend-dist.tar.gz" \
      | tar -xz && \
    apk del curl

COPY config/ ./config/
COPY assets/ ./assets/

ENV NODE_ENV=production
ENV SPA_OUTPUT_DIR=/spa

CMD ["node", "packages/tooling/scripts/assemble-importmap.js"]
