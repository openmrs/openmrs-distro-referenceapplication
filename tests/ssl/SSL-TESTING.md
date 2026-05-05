# SSL Testing Guide

This guide explains how to test the SSL/HTTPS configuration for the OpenMRS Reference Application distribution.

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
docker compose -f docker-compose.yml -f docker-compose.ssl.yml up -d

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
export CERT_WEB_DOMAINS=test.yourdomain.com
export CERT_CONTACT_EMAIL=admin@yourdomain.com

# 2. Start the services
docker compose -f docker-compose.yml -f docker-compose.ssl.yml up -d

# 3. Check certbot logs for any issues
docker compose -f docker-compose.yml -f docker-compose.ssl.yml logs certbot

# 4. Run the production test script
./tests/ssl/test-ssl-prod.sh test.yourdomain.com
```

**Important:** Staging certificates will show warnings in browsers because they're issued by "Fake LE Intermediate X1". This is expected and normal for staging.

### Phase 2: Test from Multiple Locations

Run the test script in "external" mode from a different machine:

```bash
# From a different server or your local machine
./tests/ssl/test-ssl-prod.sh test.yourdomain.com external
```

The `external` flag skips Docker-specific tests and only validates the HTTPS endpoints.

### Phase 3: Move to Production Certificates

Once staging tests pass, switch to production Let's Encrypt:

```bash
# 1. Clean up staging certificates
docker compose -f docker-compose.yml -f docker-compose.ssl.yml down -v

# 2. Use production Let's Encrypt
export SSL_MODE=prod
export SSL_STAGING=false  # or omit this variable
export CERT_WEB_DOMAINS=yourdomain.com,www.yourdomain.com
export CERT_CONTACT_EMAIL=admin@yourdomain.com

# 3. Start the services
docker compose -f docker-compose.yml -f docker-compose.ssl.yml up -d

# 4. Verify with test script
./tests/ssl/test-ssl-prod.sh yourdomain.com
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

### Example Configurations

#### Single Domain (Production)
```bash
export SSL_MODE=prod
export CERT_WEB_DOMAINS=openmrs.example.com
export CERT_CONTACT_EMAIL=admin@example.com
```

#### Multiple Domains (Production)
```bash
export SSL_MODE=prod
export CERT_WEB_DOMAINS=openmrs.example.com,www.openmrs.example.com,demo.openmrs.example.com
export CERT_CONTACT_EMAIL=admin@example.com
```

#### Testing with Staging
```bash
export SSL_MODE=prod
export SSL_STAGING=true
export CERT_WEB_DOMAINS=test.example.com
export CERT_CONTACT_EMAIL=admin@example.com
```

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

## Comprehensive Testing Checklist

### 1. Automated Tests
- [ ] Run `./tests/ssl/test-ssl.sh` for local setup
- [ ] Run `./tests/ssl/test-ssl-prod.sh <domain>` for production
- [ ] Run `./tests/ssl/test-ssl-prod.sh <domain> external` from remote host
- [ ] Verify all tests pass

### 2. Manual Browser Testing
- [ ] Visit `https://yourdomain.com/openmrs/spa` in Chrome
- [ ] Visit in Firefox
- [ ] Visit in Safari
- [ ] Check certificate details (click padlock icon)
- [ ] Verify no certificate warnings
- [ ] Test HTTP redirects to HTTPS

### 3. SSL Analysis Tools
- [ ] Run SSL Labs test: https://www.ssllabs.com/ssltest/analyze.html?d=yourdomain.com
  - Target grade: A or A+
- [ ] Check certificate transparency: https://crt.sh/?q=yourdomain.com
- [ ] Test with SSL Checker: https://www.sslshopper.com/ssl-checker.html

### 4. Security Validation
- [ ] Verify HSTS header is present (31536000 seconds)
- [ ] Check Content-Security-Policy header
- [ ] Verify no mixed content warnings
- [ ] Test with Mozilla Observatory: https://observatory.mozilla.org/

### 5. Certificate Management
- [ ] Verify certificate expiry date (90 days for Let's Encrypt)
- [ ] Check certbot container completed successfully
- [ ] Verify certificate watcher process is running
- [ ] Test certificate renewal (if near expiry)

### 6. Performance Testing
- [ ] Measure response times
- [ ] Verify HTTP/2 is enabled
- [ ] Check OCSP stapling is working
- [ ] Test from multiple geographic locations

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
docker compose -f docker-compose.yml -f docker-compose.ssl.yml logs certbot
```

**Common issues:**
- DNS not pointing to server
- Firewall blocking port 80 or 443
- Domain not registered or expired
- Hit Let's Encrypt rate limit (use staging first)

### Certificate Not Loading

**Check certificate files:**
```bash
docker exec openmrs-distro-referenceapplication-gateway-1 ls -la /etc/letsencrypt/live/yourdomain.com/
```

**Verify nginx configuration:**
```bash
docker exec openmrs-distro-referenceapplication-gateway-1 nginx -t
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
docker compose -f docker-compose.yml -f docker-compose.ssl.yml run --rm certbot renew --dry-run
```

**Force renewal:**
```bash
docker compose -f docker-compose.yml -f docker-compose.ssl.yml run --rm certbot renew --force-renewal
```

## Let's Encrypt Rate Limits

Be aware of these limits when testing:

- **Certificates per Registered Domain**: 50 per week
- **Duplicate Certificates**: 5 per week (exact same set of domains)
- **Failed Validations**: 5 failures per account, per hostname, per hour
- **New Accounts**: 10 per IP address per 3 hours

**Always test with staging first** to avoid hitting these limits.

## Certificate Renewal

Let's Encrypt certificates expire after 90 days. The certbot service automatically attempts renewal.

**Renewal schedule:**
- Certbot runs twice daily
- Certificates are renewed when they have 30 days or less remaining
- Nginx automatically reloads when certificates are updated

**Monitor certificate expiry:**
```bash
# Check expiry date
echo | openssl s_client -servername yourdomain.com -connect yourdomain.com:443 2>/dev/null | openssl x509 -noout -enddate

# Calculate days remaining
./tests/ssl/test-ssl-prod.sh yourdomain.com | grep "valid for"
```

## Security Best Practices

1. **Use strong RSA key size**: Default 4096 bits (configurable via `CERT_RSA_KEY_SIZE`)
2. **Enable HSTS**: Already configured with 1-year max-age
3. **Disable old protocols**: TLS 1.0 and 1.1 are disabled by default
4. **Keep certificates up to date**: Monitor expiry and ensure auto-renewal works
5. **Test regularly**: Run `tests/ssl/test-ssl-prod.sh` weekly or after any configuration changes
6. **Monitor certificate transparency logs**: Subscribe to notifications at https://crt.sh
7. **Set up expiry alerts**: Configure monitoring to alert 30 days before expiry

## Additional Resources

- [Let's Encrypt Documentation](https://letsencrypt.org/docs/)
- [Certbot Documentation](https://eff-certbot.readthedocs.io/)
- [Mozilla SSL Configuration Generator](https://ssl-config.mozilla.org/)
- [SSL Labs Best Practices](https://github.com/ssllabs/research/wiki/SSL-and-TLS-Deployment-Best-Practices)
- [OWASP Transport Layer Protection Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Transport_Layer_Protection_Cheat_Sheet.html)
