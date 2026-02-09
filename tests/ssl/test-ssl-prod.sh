#!/bin/bash

# Production SSL Testing Script
# Usage: ./test-ssl-prod.sh <domain> [external]
# - domain: The domain to test (required)
# - external: Add "external" flag to skip Docker-specific tests (optional)

# Check if domain parameter is provided
if [ -z "$1" ]; then
    echo "Usage: $0 <domain> [external]"
    echo "Example: $0 test.example.com"
    echo "Example: $0 test.example.com external  # For testing from external host"
    exit 1
fi

DOMAIN="$1"
EXTERNAL_MODE="${2:-}"

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Test counter
TESTS_PASSED=0
TESTS_FAILED=0
TESTS_SKIPPED=0

# Helper functions
print_test() {
    echo -e "\n${YELLOW}[TEST]${NC} $1"
}

print_success() {
    echo -e "${GREEN}✓${NC} $1"
    ((TESTS_PASSED++))
}

print_failure() {
    echo -e "${RED}✗${NC} $1"
    ((TESTS_FAILED++))
}

print_skip() {
    echo -e "${BLUE}⊘${NC} $1"
    ((TESTS_SKIPPED++))
}

print_info() {
    echo -e "  $1"
}

echo -e "${BLUE}================================${NC}"
echo -e "${BLUE}Production SSL Test Suite${NC}"
echo -e "${BLUE}Domain: $DOMAIN${NC}"
if [ "$EXTERNAL_MODE" = "external" ]; then
    echo -e "${BLUE}Mode: External (Docker tests skipped)${NC}"
else
    echo -e "${BLUE}Mode: Local (includes Docker tests)${NC}"
fi
echo -e "${BLUE}================================${NC}"

# Docker-specific tests (skip in external mode)
if [ "$EXTERNAL_MODE" != "external" ]; then
    # Test 1: Check if containers are running
    print_test "Checking if containers are running..."
    if docker compose -f docker-compose.yml -f docker-compose.ssl.yml ps 2>/dev/null | grep -q "gateway.*Up"; then
        print_success "Gateway container is running"
    else
        print_failure "Gateway container is not running"
    fi

    if docker compose -f docker-compose.yml -f docker-compose.ssl.yml ps 2>/dev/null | grep -q "backend.*Up"; then
        print_success "Backend container is running"
    else
        print_failure "Backend container is not running"
    fi

    if docker compose -f docker-compose.yml -f docker-compose.ssl.yml ps 2>/dev/null | grep -q "frontend.*Up"; then
        print_success "Frontend container is running"
    else
        print_failure "Frontend container is not running"
    fi

    # Test 2: Check certificate files exist
    print_test "Checking certificate files in container..."
    if docker exec openmrs-distro-referenceapplication-gateway-1 test -f /etc/letsencrypt/live/$DOMAIN/fullchain.pem 2>/dev/null; then
        print_success "Certificate file exists: fullchain.pem"
    else
        print_failure "Certificate file missing: fullchain.pem"
    fi

    if docker exec openmrs-distro-referenceapplication-gateway-1 test -f /etc/letsencrypt/live/$DOMAIN/privkey.pem 2>/dev/null; then
        print_success "Certificate file exists: privkey.pem"
    else
        print_failure "Certificate file missing: privkey.pem"
    fi

    # Test 3: Check certbot container completed successfully
    print_test "Checking certbot container status..."
    CERTBOT_STATUS=$(docker compose -f docker-compose.yml -f docker-compose.ssl.yml ps -a certbot 2>/dev/null | grep certbot 2>/dev/null)
    if echo "$CERTBOT_STATUS" | grep -q "Exited (0)"; then
        print_success "Certbot container completed successfully"
    else
        print_failure "Certbot container did not complete successfully"
        print_info "$CERTBOT_STATUS"
    fi

    # Test 4: Check certificate watcher process
    print_test "Checking certificate watcher process..."
    if docker exec openmrs-distro-referenceapplication-gateway-1 ps aux 2>/dev/null | grep -q "watch-certs.sh"; then
        print_success "Certificate watcher process is running"
        WATCHER_PID=$(docker exec openmrs-distro-referenceapplication-gateway-1 ps aux 2>/dev/null | grep "watch-certs.sh" | grep -v grep | awk '{print $1}')
        print_info "Process ID: $WATCHER_PID"
    else
        print_failure "Certificate watcher process is not running"
    fi

    # Test 5: Verify nginx is listening on correct ports
    print_test "Checking nginx ports..."
    if docker compose -f docker-compose.yml -f docker-compose.ssl.yml ps gateway 2>/dev/null | grep -q "443"; then
        print_success "Nginx listening on port 443 (HTTPS)"
    else
        print_failure "Nginx not listening on port 443"
    fi

    if docker compose -f docker-compose.yml -f docker-compose.ssl.yml ps gateway 2>/dev/null | grep -q "80"; then
        print_success "Nginx listening on port 80 (HTTP)"
    else
        print_failure "Nginx not listening on port 80"
    fi
else
    print_test "Skipping Docker-specific tests (external mode)..."
    print_skip "Container status checks"
    print_skip "Certificate file checks in container"
    print_skip "Certbot container status"
    print_skip "Certificate watcher process"
    print_skip "Nginx port checks"
fi

# DNS Resolution Test
print_test "Testing DNS resolution..."
if host $DOMAIN >/dev/null 2>&1; then
    print_success "DNS resolution successful"
    IP_ADDRESSES=$(host $DOMAIN | grep "has address" | awk '{print $4}')
    if [ -n "$IP_ADDRESSES" ]; then
        print_info "IP addresses: $(echo $IP_ADDRESSES | tr '\n' ', ' | sed 's/,$//')"
    fi
else
    print_failure "DNS resolution failed"
fi

# Test HTTPS connection
print_test "Testing HTTPS connection..."
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" https://$DOMAIN/openmrs/spa/home 2>&1)
if [ "$HTTP_CODE" = "200" ]; then
    print_success "HTTPS connection successful (HTTP 200)"
else
    print_failure "HTTPS connection failed (HTTP $HTTP_CODE)"
fi

# Test HTTP/2 support
print_test "Testing HTTP/2 support..."
if curl -s -I https://$DOMAIN/openmrs/spa/home 2>&1 | grep -q "HTTP/2"; then
    print_success "HTTP/2 is enabled"
else
    print_failure "HTTP/2 is not enabled"
fi

# Test HTTP→HTTPS redirect
print_test "Testing HTTP→HTTPS redirect..."
REDIRECT_LOCATION=$(curl -s -I http://$DOMAIN/openmrs/spa/home 2>&1 | grep -i "location:" | awk '{print $2}' | tr -d '\r')
if echo "$REDIRECT_LOCATION" | grep -q "https://"; then
    print_success "HTTP redirects to HTTPS"
    print_info "Redirect location: $REDIRECT_LOCATION"
else
    print_failure "HTTP does not redirect to HTTPS"
fi

# Check HSTS header
print_test "Checking HSTS header..."
HSTS_HEADER=$(curl -s -I https://$DOMAIN/openmrs/spa/home 2>&1 | grep -i "strict-transport-security" | tr -d '\r')
if echo "$HSTS_HEADER" | grep -q "max-age=31536000"; then
    print_success "HSTS header present with correct max-age"
    print_info "$HSTS_HEADER"
else
    print_failure "HSTS header missing or incorrect"
fi

# Check security headers
print_test "Checking security headers..."
HEADERS=$(curl -s -I https://$DOMAIN/openmrs/spa/home 2>&1)

if echo "$HEADERS" | grep -qi "x-xss-protection"; then
    print_success "X-XSS-Protection header present"
else
    print_failure "X-XSS-Protection header missing"
fi

if echo "$HEADERS" | grep -qi "content-security-policy"; then
    print_success "Content-Security-Policy header present"
else
    print_failure "Content-Security-Policy header missing"
fi

if echo "$HEADERS" | grep -qi "x-content-type-options"; then
    print_success "X-Content-Type-Options header present"
else
    print_failure "X-Content-Type-Options header missing"
fi

# Test backend connectivity through HTTPS
print_test "Testing backend connectivity through HTTPS..."
BACKEND_STATUS=$(curl -s -o /dev/null -w "%{http_code}" https://$DOMAIN/openmrs/ws/rest/v1/session)
if [ "$BACKEND_STATUS" = "302" ] || [ "$BACKEND_STATUS" = "200" ]; then
    print_success "Backend accessible via HTTPS (HTTP $BACKEND_STATUS)"
else
    print_failure "Backend not accessible via HTTPS (HTTP $BACKEND_STATUS)"
fi

# Certificate validation tests
print_test "Validating SSL certificate..."

# Check if openssl is available
if ! command -v openssl &> /dev/null; then
    print_skip "OpenSSL not available, skipping detailed certificate checks"
else
    # Get certificate details
    CERT_INFO=$(echo | openssl s_client -servername $DOMAIN -connect $DOMAIN:443 2>/dev/null | openssl x509 -noout -text 2>/dev/null)

    if [ $? -eq 0 ]; then
        # Check issuer
        ISSUER=$(echo "$CERT_INFO" | grep "Issuer:" | sed 's/.*Issuer: //')
        if echo "$ISSUER" | grep -qi "Let's Encrypt"; then
            print_success "Certificate issued by Let's Encrypt"
            print_info "Issuer: $ISSUER"
        else
            print_info "Certificate issuer: $ISSUER"
            if echo "$ISSUER" | grep -qi "Staging"; then
                print_info "⚠️  Warning: This appears to be a Let's Encrypt STAGING certificate"
            fi
        fi

        # Check subject matches domain
        SUBJECT=$(echo "$CERT_INFO" | grep "Subject:" | sed 's/.*CN = //' | sed 's/,.*//')
        if [ "$SUBJECT" = "$DOMAIN" ]; then
            print_success "Certificate subject matches domain"
        else
            print_info "Certificate subject: $SUBJECT"
        fi

        # Check expiry date
        EXPIRY=$(echo | openssl s_client -servername $DOMAIN -connect $DOMAIN:443 2>/dev/null | openssl x509 -noout -enddate 2>/dev/null | sed 's/notAfter=//')
        if [ -n "$EXPIRY" ]; then
            print_success "Certificate expiry date retrieved"
            print_info "Expires: $EXPIRY"

            # Calculate days until expiry
            EXPIRY_EPOCH=$(date -j -f "%b %d %H:%M:%S %Y %Z" "$EXPIRY" "+%s" 2>/dev/null || date -d "$EXPIRY" +%s 2>/dev/null)
            CURRENT_EPOCH=$(date +%s)
            DAYS_UNTIL_EXPIRY=$(( ($EXPIRY_EPOCH - $CURRENT_EPOCH) / 86400 ))

            if [ $DAYS_UNTIL_EXPIRY -lt 0 ]; then
                print_failure "Certificate has EXPIRED"
            elif [ $DAYS_UNTIL_EXPIRY -lt 30 ]; then
                print_info "⚠️  Warning: Certificate expires in $DAYS_UNTIL_EXPIRY days (renewal recommended)"
            else
                print_info "Certificate valid for $DAYS_UNTIL_EXPIRY more days"
            fi
        fi

        # Check SAN (Subject Alternative Names)
        SAN=$(echo "$CERT_INFO" | grep -A1 "Subject Alternative Name:" | tail -1 | sed 's/^ *//')
        if [ -n "$SAN" ]; then
            print_success "Subject Alternative Names present"
            print_info "$SAN"
        fi

        # Verify certificate chain
        print_test "Verifying certificate chain..."
        CHAIN_VERIFY=$(echo | openssl s_client -servername $DOMAIN -connect $DOMAIN:443 2>&1 | grep "Verify return code:")
        if echo "$CHAIN_VERIFY" | grep -q "ok"; then
            print_success "Certificate chain is valid"
            print_info "$CHAIN_VERIFY"
        else
            print_failure "Certificate chain validation failed"
            print_info "$CHAIN_VERIFY"
        fi
    else
        print_failure "Could not retrieve certificate information"
    fi
fi

# SSL Protocol and Cipher tests
print_test "Testing SSL protocols and ciphers..."

# Test TLS 1.2 support
if echo | openssl s_client -tls1_2 -servername $DOMAIN -connect $DOMAIN:443 2>&1 | grep -q "Protocol.*TLSv1.2"; then
    print_success "TLS 1.2 supported"
else
    print_info "TLS 1.2 not supported or connection failed"
fi

# Test TLS 1.3 support
if echo | openssl s_client -tls1_3 -servername $DOMAIN -connect $DOMAIN:443 2>&1 | grep -q "Protocol.*TLSv1.3"; then
    print_success "TLS 1.3 supported"
else
    print_info "TLS 1.3 not supported"
fi

# Test that old protocols are disabled
if echo | openssl s_client -ssl3 -servername $DOMAIN -connect $DOMAIN:443 2>&1 | grep -q "Protocol.*SSLv3"; then
    print_failure "SSLv3 is enabled (security risk!)"
else
    print_success "SSLv3 is disabled (as expected)"
fi

if echo | openssl s_client -tls1 -servername $DOMAIN -connect $DOMAIN:443 2>&1 | grep -q "Protocol.*TLSv1[^.]"; then
    print_failure "TLS 1.0 is enabled (should be disabled)"
else
    print_success "TLS 1.0 is disabled (as expected)"
fi

if echo | openssl s_client -tls1_1 -servername $DOMAIN -connect $DOMAIN:443 2>&1 | grep -q "Protocol.*TLSv1.1"; then
    print_failure "TLS 1.1 is enabled (should be disabled)"
else
    print_success "TLS 1.1 is disabled (as expected)"
fi

# Test OCSP stapling
print_test "Testing OCSP stapling..."
OCSP_RESPONSE=$(echo | openssl s_client -servername $DOMAIN -connect $DOMAIN:443 -status 2>&1 | grep "OCSP Response Status:")
if echo "$OCSP_RESPONSE" | grep -q "successful"; then
    print_success "OCSP stapling is working"
    print_info "$OCSP_RESPONSE"
else
    print_info "OCSP stapling not detected or not configured"
fi

# Performance test
print_test "Testing response time..."
RESPONSE_TIME=$(curl -s -o /dev/null -w "%{time_total}" https://$DOMAIN/openmrs/spa/home 2>&1)
if [ $? -eq 0 ]; then
    print_success "Response time: ${RESPONSE_TIME}s"
    # Warn if response is slow
    RESPONSE_TIME_MS=$(echo "$RESPONSE_TIME * 1000" | bc 2>/dev/null)
    if [ -n "$RESPONSE_TIME_MS" ]; then
        RESPONSE_TIME_INT=$(echo "$RESPONSE_TIME_MS" | cut -d. -f1)
        if [ "$RESPONSE_TIME_INT" -gt 3000 ]; then
            print_info "⚠️  Warning: Response time is slow (>3s)"
        fi
    fi
else
    print_failure "Could not measure response time"
fi

# Summary
echo -e "\n${YELLOW}================================${NC}"
echo -e "${YELLOW}Test Summary${NC}"
echo -e "${YELLOW}================================${NC}"
echo -e "${GREEN}Passed:${NC} $TESTS_PASSED"
echo -e "${RED}Failed:${NC} $TESTS_FAILED"
echo -e "${BLUE}Skipped:${NC} $TESTS_SKIPPED"

# Additional recommendations
echo -e "\n${BLUE}Recommendations:${NC}"
echo -e "  1. Test from multiple geographic locations"
echo -e "  2. Run SSL Labs test: https://www.ssllabs.com/ssltest/analyze.html?d=$DOMAIN"
echo -e "  3. Verify certificate appears in Certificate Transparency logs"
echo -e "  4. Test auto-renewal before 30 days before expiry"
echo -e "  5. Set up monitoring for certificate expiry"

if [ $TESTS_FAILED -eq 0 ]; then
    echo -e "\n${GREEN}All tests passed! Production SSL setup is working correctly.${NC}"
    exit 0
else
    echo -e "\n${RED}Some tests failed. Please review the output above.${NC}"
    exit 1
fi
