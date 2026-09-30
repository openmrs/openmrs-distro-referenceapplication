# OpenMRS 3.0 Reference Application

This project holds the build configuration for the OpenMRS 3.0 reference application, found on
https://dev3.openmrs.org and https://o3.openmrs.org.

## Quick start

### Run the app (using pre-built images)

```bash
docker compose up
```

The OpenMRS 3.x UI is accessible at http://localhost/openmrs/spa

OpenMRS Legacy UI is accessible at http://localhost/openmrs

### ACT Core

The backend image builds the [ACT Core](https://github.com/DIGI-UW/openmrs-module-actcore) module from
source at `ACTCORE_REF` (default `main`), so build it locally:

```bash
docker compose up --build
docker compose build --build-arg ACTCORE_REF=<commit or branch> backend
```

ACT Core re-evaluates the flags daily, keeps a list per flag, answers the flag gap look-up, computes
prophylaxis adherence, and loads the report descriptors at startup, which the reporting module skips on a
fresh database.

ACT's reports are YAML and SQL files in `distro/configuration/reports/reportdescriptors/`. How they are
written and loaded, and what each returns, is in ACT Core's
[docs/report-descriptors.md](https://github.com/DIGI-UW/openmrs-module-actcore/blob/main/docs/report-descriptors.md).

The frontend image assembles the [ACT frontend module](https://github.com/DIGI-UW/openmrs-esm-act-app),
`@mherman22/esm-act-app`, from npm at its `next` tag, alongside the RefApp's modules. It adds ACT home, the
registry, and the workspace that lists the data missing behind a critical data flag. `docker compose up
--build` builds the frontend image with it. Docker reuses the assembled modules until `CACHE_BUST` changes, so
to pick up newer `next` versions, rebuild with `docker compose build --build-arg CACHE_BUST=$(date +%s) frontend`.

ACT home is the landing page. The login app goes to `/home/act-home`, ACT home and Registry come first in
the home page's left nav, and `defaultDashboardPerRole` in `frontend/config-core_demo.json` sends the
RefApp's organizational roles and System Developer there too. ACT home needs View Patient Flags, which
clinician roles get through `Privilege Level: High`.

The RefApp apps ACT does not use are assembled but switched off in `frontend/config-core_demo.json`:
Service Queues, Appointments, Billing, Laboratory, Wards, Bed Management, Stock Management, Dispensing and
Patient lists,
and the patient chart's Orders, Results, Programs, Attachments, Immunizations, Procedures, Conditions and
Medications pages and order basket. Each is hidden by removing its links, widgets and buttons from the slots
they appear in, with an `extensionSlots` `remove` list under the app that owns the slot. Another
implementation that wants one back deletes its entries from those lists, with no rebuild:

| App | Entries to delete |
| --- | --- |
| Service Queues | `service-queues-dashboard-link` (home), `queue-screen-link` (app menu), `visit-form-queue-fields` (start visit form), `queue-patient-info-queue-entry-status` (patient banner), `admin-service-queues-card-link` (system admin) |
| Appointments | `clinical-appointments-dashboard-link`, `home-appointments` (home), `patient-appointments-summary-dashboard` (chart), `patient-upcoming-appointment-widget` (start visit form); set `showUpcomingAppointments` back to `true` |
| Billing | `billing-dashboard-link` (home), `billing-summary-dashboard-link` (chart), `billing-checkin-form` (start visit form), `visit-bills-panel` (visit summary), `patient-banner-billing-tags`, `patient-banner-payment-status-tag` (patient banner), `billable-services-admin-card-link` (system admin) |
| Laboratory | `laboratory-dashboard-link` (home) |
| Wards | `ward-dashboard-link` (home) |
| Bed Management | `bed-management-admin-card-link` (system admin) |
| Stock Management | `stock-management-admin-card-link` (system admin) |
| Dispensing | `dispensing-link` (app menu) |
| Patient lists | `patient-lists-dashboard-link` (home), `add-patient-to-patient-list-button` (patient chart's Actions menu). ACT Core still keeps a list per flag; ACT home's worklist tiles count them and open the registry narrowed to the flag |
| Chart pages | the entry for the page in `patient-chart-dashboard-slot`, and `patient-chart-order-basket` for the order basket |

Hiding an app removes its entry points, not its pages: a user who types an app's address still reaches it.
The backend modules stay in `distro/distro.properties`, because the RefApp's demo roles grant their
privileges.

To work on the module itself, run it from a checkout of it, with this repository beside it, and open
http://localhost:8090/openmrs/spa:

```bash
npx openmrs develop --backend http://localhost --port 8090 \
  --config-file ../openmrs-distro-referenceapplication/frontend/config-core_demo.json
```

Clinician roles get the View Patient Flags privilege from the second start of the backend after it is
first created.

Each critical data flag's criteria return a row per missing answer: the patient, the encounter, and
the question. ACT Core lists those rows as the flag's gaps. When a patient has no encounter to point
at yet, such as no RHD Consultation Visit, the flag still shows and the workspace offers a new form.

Change a flag's criteria in `distro/configuration/flags/rhd_flags.csv` and restart the backend so
Initializer loads it. Saving criteria through the patientflags REST API stores `<` as `&lt;`, which
breaks every criterion that compares dates.

ACT Core also computes each patient's prophylaxis adherence and next due date, daily, with ACT 2.0's
calculation. The registry's BPG status and adherence columns and the care cascade's Adherent step read it.

### Production deployment with SSL

For production deployments with HTTPS/SSL certificates, create a `.env` file in the project root:

```env
COMPOSE_FILE=docker-compose.yml:docker-compose.ssl.yml
SSL_MODE=prod
CERT_WEB_DOMAINS=your-domain.com
CERT_CONTACT_EMAIL=admin@your-domain.com
```

Then start the application as usual:

```bash
docker compose up
```

The `COMPOSE_FILE` variable tells Docker Compose to automatically include the SSL overlay, so you never need to pass `-f` flags. See the [SSL/HTTPS Configuration](#sslhttps-configuration) section for detailed setup instructions.

## Overview

This distribution consists of four images:

- **db** - This is just the standard MariaDB image supplied to use as a database
- **backend** - This image is the OpenMRS backend. It is built from the main Dockerfile included in the root of the project and
  based on the core OpenMRS Docker file. Additional contents for this image are drawn from the `distro` sub-directory which
  includes a full Initializer configuration for the reference application intended as a starting point.
- **frontend** - This image is a simple nginx container that embeds the 3.x frontend, including the modules described in the
  `frontend/spa-assemble-config.json` file.
- **gateway** - This image is an nginx reverse proxy that sits in front of the `backend` and `frontend` containers
  and provides a common interface to both. This helps mitigate CORS issues.

When running with SSL enabled (using `docker-compose.ssl.yml`), an additional service is included:

- **certbot** - This image is used for generating and renewing SSL certificates (Let's Encrypt or self-signed)

## SSL/HTTPS Configuration

The application can be run with SSL/HTTPS support for both development and production environments.

SSL configuration lives in `docker-compose.ssl.yml`, which is included alongside the base `docker-compose.yml`. The recommended way to enable it is via a `.env` file in the project root:

```env
COMPOSE_FILE=docker-compose.yml:docker-compose.ssl.yml
```

This tells Docker Compose to automatically include the SSL overlay, so all commands remain simply `docker compose up`, `docker compose down`, etc. without needing `-f` flags. All SSL-related environment variables (like `SSL_MODE` and `CERT_WEB_DOMAINS`) can also be set in this file.

### Development mode (self-signed certificates)

For local development with HTTPS:

```env
# .env
COMPOSE_FILE=docker-compose.yml:docker-compose.ssl.yml
```

```bash
docker compose up
```

This will:
- Automatically generate self-signed certificates for `localhost` and `127.0.0.1`
- Configure nginx to use HTTPS on port 443
- Redirect HTTP (port 80) to HTTPS

The application will be accessible at:
- https://localhost/openmrs/spa
- https://127.0.0.1/openmrs/spa

**Note**: Your browser will show a security warning for self-signed certificates. This is expected - click "Advanced" and proceed to the site.

### Production mode (Let's Encrypt)

For production deployments with valid SSL certificates from Let's Encrypt, create a `.env` file:

```env
# .env
COMPOSE_FILE=docker-compose.yml:docker-compose.ssl.yml
SSL_MODE=prod
CERT_WEB_DOMAINS=example.com
CERT_CONTACT_EMAIL=admin@example.com
```

```bash
docker compose up
```

**Configuration options**:

- `SSL_MODE=prod` - Use Let's Encrypt certificates (required)
- `CERT_WEB_DOMAINS` - Your domain name(s), comma-separated (e.g., `example.com,www.example.com`). The first domain is used as the primary domain for certificate paths and the nginx `server_name` directive.
- `CERT_CONTACT_EMAIL` - Email for Let's Encrypt notifications
- `CERT_WEB_DOMAIN_COMMON_NAME` - (Optional) Override the primary domain. By default, this is derived from the first domain in `CERT_WEB_DOMAINS`. You only need to set this if you want the certificate's common name to differ from the first domain.
- `SSL_STAGING=true` - (Optional) Use Let's Encrypt staging environment for testing

**The certbot container will**:
1. Create a temporary certificate to allow nginx to start
2. Wait for nginx to be ready
3. Request a real Let's Encrypt certificate via ACME HTTP-01 challenge
4. Reload nginx with the real certificate
5. Run a renewal daemon that checks for renewal every 12 hours

**Important**: Ensure your domain's DNS is correctly configured to point to your server before starting, as Let's Encrypt needs to verify domain ownership via HTTP.

### Testing with Let's Encrypt staging

To test the SSL setup without hitting Let's Encrypt rate limits, add `SSL_STAGING=true` to your `.env` file:

```env
# .env
COMPOSE_FILE=docker-compose.yml:docker-compose.ssl.yml
SSL_MODE=prod
SSL_STAGING=true
CERT_WEB_DOMAINS=example.com
CERT_CONTACT_EMAIL=admin@example.com
```

Staging certificates won't be trusted by browsers but allow you to verify the setup works correctly. Remove `SSL_STAGING` (or set it to `false`) once you've confirmed the setup works.

### Certificate profiles

Let's Encrypt offers different certificate profiles with varying validity periods:

| Profile | Validity | Use Case |
|---------|----------|----------|
| `classic` | 90 days (default) | Standard certificates |
| `tlsserver` | 45 days | Shorter validity for improved security |
| `shortlived` | 6 days | Required for IP address certificates |

To request a specific profile, add `CERT_PROFILE` to your `.env` file:

```env
# .env (additions)
CERT_PROFILE=tlsserver
```

**Note**: Let's Encrypt is transitioning all certificates to 45-day validity by 2028. Using the `tlsserver` profile allows you to opt-in to shorter certificates now.

### IP address certificates

Let's Encrypt now supports issuing certificates for publicly-addressable IP addresses. These certificates must use the `shortlived` profile (6-day validity).

```env
# .env
COMPOSE_FILE=docker-compose.yml:docker-compose.ssl.yml
SSL_MODE=prod
CERT_WEB_DOMAINS=203.0.113.50
CERT_CONTACT_EMAIL=admin@example.com
```

**Important notes for IP address certificates**:
- The IP address must be publicly addressable (not private IPs like 192.168.x.x or 10.x.x.x)
- The `shortlived` profile is automatically selected when an IP address is detected
- Certificates are valid for approximately 6 days and renew automatically
- IPv6 addresses are also supported

You can also mix domain names and IP addresses:

```env
CERT_WEB_DOMAINS=example.com,203.0.113.50
```

When any IP address is included, the shortlived profile is required and will be automatically enforced.

### Manual certificate renewal

While certificates renew automatically in production mode, you can manually force renewal if needed.

**If the certbot container is running** (prod mode):

```bash
# Force renewal
docker compose exec certbot certbot renew --force-renewal --webroot -w /var/www/certbot

# Reload nginx to pick up new certificates
docker compose exec gateway nginx -s reload
```

**If certbot container has stopped or for one-off renewal**:

```bash
# Run certbot in one-off mode (override entrypoint to run certbot directly)
docker compose run --rm --entrypoint certbot certbot \
  renew --force-renewal --webroot -w /var/www/certbot

# Reload nginx
docker compose exec gateway nginx -s reload
```

**Check certificate expiration**:

```bash
# If certbot container is running
docker compose exec certbot certbot certificates

# If certbot container is stopped
docker compose run --rm --entrypoint certbot certbot certificates
```

### Regenerating certificates

If you need to start fresh with certificates (e.g., after changing domains), delete the letsencrypt volume and restart:

```bash
docker compose down

# Remove only this project's letsencrypt volume
docker volume rm "$(docker compose config | awk '/^name:/{print $2}')_letsencrypt-data"

docker compose up
```

The certbot entrypoint skips certificate generation when it finds existing certificates for the configured domain. Removing the volume forces it to go through the full setup process again.

### Running with Grafana

The service can run with Grafana for monitoring logs. You can run it with:
```bash
docker compose -f docker-compose.yml -f docker-compose.grafana.yml up
```
Grafana will be available at http://localhost/grafana. Use admin as username and see docker-compose.grafana.yml for the initial password.

If you would like to use grafana in your distro, you just need to copy over `docker-compose.grafana.yml`.

### Environment variables reference

| Variable | Default | Description |
|----------|---------|-------------|
| `SSL_MODE` | `dev` | `dev` for self-signed certificates, `prod` for Let's Encrypt |
| `SSL_STAGING` | `false` | Use Let's Encrypt staging environment (set to `true` for testing) |
| `CERT_WEB_DOMAINS` | `localhost,127.0.0.1` | Comma-separated list of domain names or IP addresses |
| `CERT_WEB_DOMAIN_COMMON_NAME` | first domain from `CERT_WEB_DOMAINS` | Override the primary domain used for certificate paths and nginx `server_name`. Most users should not set this. |
| `CERT_CONTACT_EMAIL` | (empty) | Email for Let's Encrypt notifications (required in prod mode) |
| `CERT_RSA_KEY_SIZE` | `4096` | RSA key size for certificates |
| `CERT_PROFILE` | (empty) | Certificate profile: `classic` (90 days), `tlsserver` (45 days), or `shortlived` (6 days). Auto-set to `shortlived` for IP addresses |

## Contributing to the configuration

This project uses the [Initializer](https://github.com/mekomsolutions/openmrs-module-initializer) module
to configure metadata for this project. The Initializer configuration can be found in the configuration
subfolder of the distro folder. Any files added to this will be automatically included as part of the
metadata for the RefApp.

Eventually, we would like to split this metadata into two packages:

- `openmrs-core`, which will contain all the metadata necessary to run OpenMRS
- `openmrs-demo`, which will include all of the sample data we use to run the RefApp

The `openmrs-core` package will eventually be a standard part of the distribution, with the `openmrs-demo`
provided as an optional add-on. Most data in this configuration _should_ be regarded as demo data. We
anticipate that implementation-specific metadata will replace data in the `openmrs-demo` package,
though they may use that metadata as a starting point for that customization.

To help us keep track of things, we ask that you suffix any files you add with either
`-core_demo` for files that should be part of the demo package and `-core_data` for
those that should be part of the core package. For example, a form named `test_form.json` would become
`test_core-core_demo.json`.

Frontend configuration can be found in `frontend/config-core_demo.json`.

Thanks!
