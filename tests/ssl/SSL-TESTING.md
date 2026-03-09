# SSL Testing Guide

This guide explains how to test the SSL/HTTPS configuration for the SIHSALUS (PeruHCE) distribution.

## Overview

The distribution supports two SSL modes:

1. **Development Mode** (`SSL_MODE=dev`): Self-signed certificates for localhost testing
2. **Production Mode** (`SSL_MODE=prod`): Real Let's Encrypt certificates for production domains

## Prerequisites

### For Local/Development Testing
- Docker and Docker Compose installed
- The distribution running locally
- `tests/ssl/test-ssl.sh` script

### For Production Testing
- A registered domain name
- DNS A/AAAA records pointing to your server
- Port 80 and 443 accessible from the internet
- Valid email address for Let's Encrypt notifications
- `tests/ssl/test-ssl-prod.sh` script

## Development/Local Testing

Test the SSL setup with self-signed certificates for localhost:

```bash
# 1. Start the services with SSL
docker compose -f docker-compose.yml -f compose/ssl.yml up -d

# 2. Run the test script
./tests/ssl/test-ssl.sh
```

The script will verify:
- Container status
- Certificate file existence
- HTTPS connectivity
- HTTP to HTTPS redirection
- Security headers (HSTS, CSP, etc.)
- HTTP/2 support
- IPv6 support
- Backend API accessibility

## Production Testing

### Phase 1: Test with Let's Encrypt Staging (Recommended)

**Why staging first?** Let's Encrypt has rate limits (5 certificates per domain per week). Testing with staging avoids hitting these limits.

```bash
# 1. Set up environment variables
export SSL_MODE=prod
export SSL_STAGING=true
export CERT_WEB_DOMAINS=sihsalus.example.com
export CERT_CONTACT_EMAIL=admin@example.com

# 2. Start the services
docker compose -f docker-compose.yml -f compose/ssl.yml up -d

# 3. Check certbot logs for any issues
docker compose -f docker-compose.yml -f compose/ssl.yml logs certbot

# 4. Run the production test script
./tests/ssl/test-ssl-prod.sh sihsalus.example.com
```

**Important:** Staging certificates will show warnings in browsers because they're issued by "Fake LE Intermediate X1". This is expected and normal for staging.

### Phase 2: Test from Multiple Locations

Run the test script in "external" mode from a different machine:

```bash
# From a different server or your local machine
./tests/ssl/test-ssl-prod.sh sihsalus.example.com external
```

The `external` flag skips Docker-specific tests and only validates the HTTPS endpoints.

### Phase 3: Move to Production Certificates

Once staging tests pass, switch to production Let's Encrypt:

```bash
# 1. Clean up staging certificates
docker compose -f docker-compose.yml -f compose/ssl.yml down -v

# 2. Use production Let's Encrypt
export SSL_MODE=prod
export SSL_STAGING=false  # or omit this variable
export CERT_WEB_DOMAINS=sihsalus.example.com,www.sihsalus.example.com
export CERT_CONTACT_EMAIL=admin@example.com

# 3. Start the services
docker compose -f docker-compose.yml -f compose/ssl.yml up -d

# 4. Verify with test script
./tests/ssl/test-ssl-prod.sh sihsalus.example.com
```

## Environment Variables Reference

### SSL Configuration Variables

| Variable | Values | Default | Description |
|----------|--------|---------|-------------|
| `SSL_MODE` | `dev`, `prod` | `dev` | Certificate mode: self-signed (dev) or Let's Encrypt (prod) |
| `SSL_STAGING` | `true`, `false` | `false` | Use Let's Encrypt staging for testing (prod mode only) |
| `CERT_WEB_DOMAINS` | Comma-separated domains | `localhost,127.0.0.1` | Domains for the certificate |
| `CERT_WEB_DOMAIN_COMMON_NAME` | Domain string | First domain in list | Override primary domain (CN) |
| `CERT_CONTACT_EMAIL` | Email address | (empty) | Email for Let's Encrypt notifications (required for prod) |
| `CERT_RSA_KEY_SIZE` | Number | `4096` | RSA key size for certificates |

## Test Script Details

### test-ssl.sh (Development)

Tests local SSL setup with self-signed certificates:

- **Container checks**: Verifies all services are running
- **Certificate existence**: Confirms certificate files are present
- **HTTPS connectivity**: Tests encrypted connections
- **HTTP/2 support**: Verifies modern protocol support
- **Security headers**: Validates HSTS, CSP, XSS protection, etc.
- **Certificate watcher**: Confirms auto-reload process is running

### test-ssl-prod.sh (Production)

Tests production SSL setup with real certificates:

```bash
# Local testing (with Docker checks)
./tests/ssl/test-ssl-prod.sh yourdomain.com

# External testing (without Docker checks)
./tests/ssl/test-ssl-prod.sh yourdomain.com external
```

Additional tests compared to development script:
- **DNS resolution**: Verifies domain resolves correctly
- **Certificate validation**: Checks issuer, expiry, SAN, chain
- **TLS protocol tests**: Verifies TLS 1.2/1.3 enabled, old protocols disabled
- **OCSP stapling**: Tests certificate revocation checking
- **Response time**: Measures performance

## Troubleshooting

### Certbot Fails to Issue Certificate

**Check DNS propagation:**
```bash
nslookup yourdomain.com
dig yourdomain.com
```

**Verify ports are accessible:**
```bash
nc -zv yourdomain.com 80
nc -zv yourdomain.com 443
```

**Check certbot logs:**
```bash
docker compose -f docker-compose.yml -f compose/ssl.yml logs certbot
```

**Common issues:**
- DNS not pointing to server
- Firewall blocking port 80 or 443
- Domain not registered or expired
- Hit Let's Encrypt rate limit (use staging first)

### Certificate Not Loading

**Check certificate files:**
```bash
docker exec peruHCE-gateway ls -la /etc/letsencrypt/live/yourdomain.com/
```

**Verify nginx configuration:**
```bash
docker exec peruHCE-gateway nginx -t
```

**Check nginx logs:**
```bash
docker compose logs gateway
```

### Browser Shows Certificate Warning

**Staging certificate:** If you see "Fake LE Intermediate X1", you're using staging. Switch to production mode.

**Wrong domain:** Verify `CERT_WEB_DOMAINS` matches the domain you're accessing.

**Expired certificate:** Check expiry date and renew if needed.

### Certificate Renewal Fails

**Manual renewal test:**
```bash
docker compose -f docker-compose.yml -f compose/ssl.yml run --rm certbot renew --dry-run
```

**Force renewal:**
```bash
docker compose -f docker-compose.yml -f compose/ssl.yml run --rm certbot renew --force-renewal
```

## Let's Encrypt Rate Limits

Be aware of these limits when testing:

- **Certificates per Registered Domain**: 50 per week
- **Duplicate Certificates**: 5 per week (exact same set of domains)
- **Failed Validations**: 5 failures per account, per hostname, per hour
- **New Accounts**: 10 per IP address per 3 hours

**Always test with staging first** to avoid hitting these limits.

## Security Best Practices

1. **Use strong RSA key size**: Default 4096 bits (configurable via `CERT_RSA_KEY_SIZE`)
2. **Enable HSTS**: Already configured with 1-year max-age
3. **Disable old protocols**: TLS 1.0 and 1.1 are disabled by default
4. **Keep certificates up to date**: Monitor expiry and ensure auto-renewal works
5. **Test regularly**: Run `tests/ssl/test-ssl-prod.sh` weekly or after any configuration changes
6. **Monitor certificate transparency logs**: Subscribe to notifications at https://crt.sh
7. **Set up expiry alerts**: Configure monitoring to alert 30 days before expiry
