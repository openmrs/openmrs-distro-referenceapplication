# Onprem deployment via webhook

CI/CD for deploying this repo's `backend`/`frontend`/`gateway` images to an
onprem VM, triggered by a signed webhook instead of SSH or a self-hosted
runner. Nothing on the VM needs to be reachable by GitHub except a single
authenticated HTTPS endpoint, and no deploy credentials for the VM are ever
stored in GitHub.

## Architecture

```
 push to main
      |
      v
 GitHub Actions: deploy-onprem.yml
      |  1. build backend/frontend/gateway images
      |  2. docker push -> ghcr.io/phcc-openmrs/*
      |  3. HMAC-sign a JSON payload, POST it to ONPREM_WEBHOOK_URL
      v
 Onprem VM (reverse proxy) --/hooks/deploy-distro--> webhook (127.0.0.1:9000)
      |  verifies X-Hub-Signature-256 against the shared secret
      |  on match, runs deploy.sh
      v
 deploy.sh
      |  cd's into the repo checkout, so docker compose picks up COMPOSE_FILE
      |  and TAG from .env / the exported TAG env var
      |  docker compose pull
      |  docker compose up -d
      v
 running stack updated
```

Key properties:
- **Pull-based trust boundary**: GitHub only ever gets a 200/4xx/5xx response
  from your VM; it has no SSH key, no docker socket access, no shell on your
  infrastructure.
- **`webhook` never sits directly on the public interface** — it binds to
  `127.0.0.1:9000` and is reverse-proxied, so whatever already terminates TLS
  for the VM's domain controls what's exposed.
- **The image build and the deploy trigger are decoupled**: `deploy-onprem.yml`
  only calls the webhook after both image pushes succeed, so a broken build
  never triggers a deploy.

## Files in this directory

| File | Purpose |
|---|---|
| `hooks.json.example` | Template config for the `webhook` listener — defines the `deploy-distro` hook and its HMAC trigger rule. Copy to `hooks.json` on the VM and fill in the real secret; `hooks.json` is gitignored. |
| `deploy.sh` | What the webhook actually executes: pulls the new images and rolls the stack over via `docker compose up -d`. |
| `webhook.service` | systemd unit that runs the `webhook` listener as the `deploy` user. |

The CI side lives at [`../../.github/workflows/deploy-onprem.yml`](../../.github/workflows/deploy-onprem.yml); the compose override it targets is [`../../docker-compose.prod.yml`](../../docker-compose.prod.yml).

## Prerequisites

- Docker + Docker Compose v2 already installed and working on the VM (the
  stack itself runs the same way it does today via `docker-compose.yml`).
- The VM has a domain/IP reachable over HTTPS from the internet, with some
  reverse proxy (this repo's own `gateway`, or another one) already
  terminating TLS in front of it.
- You can create GitHub Actions repo secrets (Settings → Secrets and
  variables → Actions) on `PHCC-Openmrs/openmrs-distro-referenceapplication`.

## One-time VM setup

1. **Install `webhook`** (https://github.com/adnanh/webhook):
   ```
   sudo apt-get install webhook   # Debian/Ubuntu; or grab a release binary
   ```

2. **Check out this repo on the VM**, e.g. at
   `/home/openmrsdev/openmrsdev/openmrs-distro-referenceapplication`. The
   paths baked into `webhook.service` and the example hook config assume this
   location — edit both files if you use a different path.

3. **Generate a shared secret** — this is what proves a webhook call really
   came from your CI, since the endpoint itself is public:
   ```
   openssl rand -hex 32
   ```
   You'll use this same value in two places (step 4 and step 8) and nowhere
   else. Treat it like a password; it's the only thing standing between the
   public internet and a deploy on your VM.

4. **Create the real `hooks.json`** from the template:
   ```
   cd /home/openmrsdev/openmrsdev/openmrs-distro-referenceapplication/ops/onprem-webhook
   cp hooks.json.example hooks.json
   # edit hooks.json: replace REPLACE_WITH_YOUR_ONPREM_WEBHOOK_SECRET with the
   # value from step 3
   chmod 600 hooks.json
   ```
   `hooks.json` is gitignored on purpose — never commit the real secret.

5. **Confirm the `openmrsdev` user owns the checkout and can run Docker.**
   `webhook.service` runs as `openmrsdev` — if that user already owns
   `/home/openmrsdev/openmrsdev/openmrs-distro-referenceapplication` and can
   already run `docker compose` there (which it does, per the existing manual
   deploy flow), there's nothing to do here. Otherwise:
   ```
   sudo usermod -aG docker openmrsdev
   sudo chown -R openmrsdev:openmrsdev /home/openmrsdev/openmrsdev/openmrs-distro-referenceapplication
   ```

6. **Install and start the systemd unit**:
   ```
   sudo cp webhook.service /etc/systemd/system/webhook.service
   sudo systemctl daemon-reload
   sudo systemctl enable --now webhook
   sudo systemctl status webhook   # confirm it's active and listening
   ```

7. **Reverse-proxy `/hooks/deploy-distro` to `127.0.0.1:9000`.** Example nginx
   `location` block, added to whatever vhost already serves the VM's domain:
   ```nginx
   location /hooks/ {
       proxy_pass http://127.0.0.1:9000/hooks/;
       proxy_set_header Host $host;
   }
   ```
   Reload/restart that proxy after adding it.

8. **Set the GitHub Actions secrets** on the repo:
   | Secret | Value |
   |---|---|
   | `ONPREM_WEBHOOK_URL` | `https://openmrsdev.beehyv.com/hooks/deploy-distro` |
   | `ONPREM_WEBHOOK_SECRET` | the value generated in step 3 |

9. **Make the GHCR images pullable from the VM.** Either:
   - Mark the three `ghcr.io/phcc-openmrs/openmrs-reference-application-3-*`
     packages public (package Settings → Change visibility), or
   - `docker login ghcr.io` once on the VM as the `openmrsdev` user, using a
     GitHub PAT scoped to `read:packages`.

10. **Set `COMPOSE_FILE` in `.env`** to include `docker-compose.prod.yml` —
    that's the file that points the stack at the `ghcr.io/phcc-openmrs/*`
    images this workflow builds, instead of the upstream Docker Hub images
    the base `docker-compose.yml` defaults to:
    ```
    COMPOSE_FILE=docker-compose.yml:docker-compose.prod.yml:docker-compose.grafana.yml
    ```

## Testing it

**End to end**: push to `main` (or run the `Deploy to Onprem VM` workflow
manually from the Actions tab), then watch both sides:
```
# on the VM
journalctl -u webhook -f
```
You should see the incoming POST logged, followed by `deploy.sh`'s
`[deploy] ...` output ending in `[deploy] done`.

**VM side only**, without touching CI — useful for confirming `deploy.sh` and
the compose override work before wiring up the webhook:
```
cd /home/openmrsdev/openmrsdev/openmrs-distro-referenceapplication
sudo -u openmrsdev ../ops/onprem-webhook/deploy.sh main
```

**Webhook auth only**, without a real deploy — confirms the reverse proxy and
signature check work, using a throwaway payload:
```
secret=<the ONPREM_WEBHOOK_SECRET value>
payload='{"tag":"test","sha":"test","ref":"test"}'
sig="sha256=$(printf '%s' "$payload" | openssl dgst -sha256 -hmac "$secret" | sed 's/^.* //')"
curl -i -X POST https://openmrsdev.beehyv.com/hooks/deploy-distro \
  -H "Content-Type: application/json" \
  -H "X-Hub-Signature-256: $sig" \
  --data "$payload"
```
A `200` means the signature matched and `deploy.sh` ran (it will actually
deploy `test`, which does not exist as an image tag and will fail the
`docker compose pull`); a `404`/`golang` error from `webhook` usually means
signature mismatch or the hook ID doesn't match `hooks.json`.

## Rollback

Images are tagged both `<branch>` (e.g. `main` — a floating tag that always
points at the latest build for that branch, and is what gets deployed
automatically) and `<branch>-<full-sha>` (immutable), so you can pin to a
known-good build without waiting on CI:
```
cd /home/openmrsdev/openmrsdev/openmrs-distro-referenceapplication
sudo -u openmrsdev env TAG=main-<good-full-sha> docker compose up -d
```

## Troubleshooting

| Symptom | Likely cause |
|---|---|
| CI step "Send signed deploy webhook" fails with a connection error | Reverse proxy isn't forwarding `/hooks/` yet, or `ONPREM_WEBHOOK_URL` is wrong/unreachable. |
| CI gets a non-2xx HTTP response | `webhook` rejected the signature (secret mismatch between GitHub secret and VM's `hooks.json`), or the hook `id` in the URL doesn't match `hooks.json`. |
| Webhook call succeeds (200) but nothing changes on the VM | Check `journalctl -u webhook`, then run `deploy.sh` manually (see Testing) to see the actual `docker compose` error — usually a pull auth failure (see step 9) or the `deploy` user isn't in the `docker` group yet (needs re-login/`newgrp`). |
| `docker compose pull` fails with `unauthorized` | GHCR packages are private and the VM hasn't logged in — see step 9. |
| Systemd unit won't start | `sudo journalctl -u webhook -n 50 --no-pager` — usually a bad path in `webhook.service`/`hooks.json`, or the `webhook` binary isn't on `PATH` for the unit's `ExecStart`. |

## Security notes

- The webhook secret is the sole authentication for triggering a deploy —
  rotate it (regenerate, update both the GitHub secret and `hooks.json`,
  restart `webhook`) if you suspect it leaked, e.g. via a CI log or a
  compromised laptop.
- `webhook` binding to `127.0.0.1` only, with the reverse proxy as the single
  public entry point, means TLS, rate limiting, and IP allowlisting (if
  desired) are handled in one place — the proxy config — not duplicated in
  `webhook` itself.
- `deploy.sh` only ever runs `docker compose pull`/`up`/`image prune` — it
  does not evaluate or interpolate any part of the webhook payload as a shell
  command, so a forged-but-unsigned payload can't do anything even if it
  somehow reached `deploy.sh` directly.
