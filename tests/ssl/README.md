# SSL Testing

This directory contains scripts and documentation for testing SSL/HTTPS configuration in the OpenMRS Reference Application distribution.

## Files

- **`test-ssl.sh`** - Development/local SSL testing script for self-signed certificates (localhost)
- **`test-ssl-prod.sh`** - Production SSL testing script for Let's Encrypt certificates (real domains)
- **`SSL-TESTING.md`** - Comprehensive SSL testing guide with usage instructions and troubleshooting

## Quick Start

### Local Testing (Development)
```bash
# Start services with SSL
docker compose -f docker-compose.yml -f docker-compose.ssl.yml up -d

# Run tests
./tests/ssl/test-ssl.sh
```

### Production Testing
```bash
# Test on server (with Docker checks)
./tests/ssl/test-ssl-prod.sh yourdomain.com

# Test from external host (without Docker checks)
./tests/ssl/test-ssl-prod.sh yourdomain.com external
```

## Documentation

See [SSL-TESTING.md](./SSL-TESTING.md) for complete documentation including:
- Configuration options
- Three-phase testing approach (staging → external → production)
- Troubleshooting guide
- Security best practices
- Certificate management
