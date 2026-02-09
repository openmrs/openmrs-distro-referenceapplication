#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test counter
TESTS_PASSED=0
TESTS_FAILED=0

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

print_info() {
    echo -e "  $1"
}

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
print_test "Checking certificate files..."
if docker exec openmrs-distro-referenceapplication-gateway-1 test -f /etc/letsencrypt/live/localhost/fullchain.pem; then
    print_success "Certificate file exists: fullchain.pem"
else
    print_failure "Certificate file missing: fullchain.pem"
fi

if docker exec openmrs-distro-referenceapplication-gateway-1 test -f /etc/letsencrypt/live/localhost/privkey.pem; then
    print_success "Certificate file exists: privkey.pem"
else
    print_failure "Certificate file missing: privkey.pem"
fi

# Test 3: Test HTTPS connection
print_test "Testing HTTPS connection..."
if curl -k -s -o /dev/null -w "%{http_code}" https://localhost/openmrs/spa/home | grep -q "200"; then
    print_success "HTTPS connection successful (HTTP 200)"
else
    print_failure "HTTPS connection failed"
fi

# Test 4: Test HTTP/2 support
print_test "Testing HTTP/2 support..."
if curl -k -s -I https://localhost/openmrs/spa/home 2>&1 | grep -q "HTTP/2"; then
    print_success "HTTP/2 is enabled"
else
    print_failure "HTTP/2 is not enabled"
fi

# Test 5: Test HTTP→HTTPS redirect
print_test "Testing HTTP→HTTPS redirect..."
REDIRECT_LOCATION=$(curl -s -I http://localhost/openmrs/spa/home 2>&1 | grep -i "location:" | awk '{print $2}' | tr -d '\r')
if echo "$REDIRECT_LOCATION" | grep -q "https://"; then
    print_success "HTTP redirects to HTTPS"
    print_info "Redirect location: $REDIRECT_LOCATION"
else
    print_failure "HTTP does not redirect to HTTPS"
fi

# Test 6: Check HSTS header
print_test "Checking HSTS header..."
if curl -k -s -I https://localhost/openmrs/spa/home 2>&1 | grep -i "strict-transport-security" | grep -q "max-age=31536000"; then
    print_success "HSTS header present with correct max-age"
    HSTS_HEADER=$(curl -k -s -I https://localhost/openmrs/spa/home 2>&1 | grep -i "strict-transport-security" | tr -d '\r')
    print_info "$HSTS_HEADER"
else
    print_failure "HSTS header missing or incorrect"
fi

# Test 7: Check security headers
print_test "Checking security headers..."
HEADERS=$(curl -k -s -I https://localhost/openmrs/spa/home 2>&1)

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

# Test 8: Test IPv6 support
print_test "Testing IPv6 HTTPS access..."
if curl -k -s -o /dev/null -w "%{http_code}" "https://[::1]/openmrs/spa/home" 2>&1 | grep -q "200"; then
    print_success "IPv6 HTTPS access working"
else
    print_failure "IPv6 HTTPS access failed"
fi

# Test 9: Test backend connectivity through HTTPS
print_test "Testing backend connectivity through HTTPS..."
BACKEND_STATUS=$(curl -k -s -o /dev/null -w "%{http_code}" https://localhost/openmrs/ws/rest/v1/session)
if [ "$BACKEND_STATUS" = "302" ] || [ "$BACKEND_STATUS" = "200" ]; then
    print_success "Backend accessible via HTTPS (HTTP $BACKEND_STATUS)"
else
    print_failure "Backend not accessible via HTTPS (HTTP $BACKEND_STATUS)"
fi

# Test 10: Check certificate watcher process
print_test "Checking certificate watcher process..."
if docker exec openmrs-distro-referenceapplication-gateway-1 ps aux 2>/dev/null | grep -q "watch-certs.sh"; then
    print_success "Certificate watcher process is running"
    WATCHER_PID=$(docker exec openmrs-distro-referenceapplication-gateway-1 ps aux 2>/dev/null | grep "watch-certs.sh" | grep -v grep | awk '{print $1}')
    print_info "Process ID: $WATCHER_PID"
else
    print_failure "Certificate watcher process is not running"
fi

# Test 11: Verify nginx is listening on correct ports
print_test "Checking nginx ports..."
if docker compose -f docker-compose.yml -f docker-compose.ssl.yml ps gateway 2>/dev/null | grep -q "443->443"; then
    print_success "Nginx listening on port 443 (HTTPS)"
else
    print_failure "Nginx not listening on port 443"
fi

if docker compose -f docker-compose.yml -f docker-compose.ssl.yml ps gateway 2>/dev/null | grep -q "80->80"; then
    print_success "Nginx listening on port 80 (HTTP)"
else
    print_failure "Nginx not listening on port 80"
fi

# Test 12: Check certbot container completed successfully
print_test "Checking certbot container status..."
CERTBOT_STATUS=$(docker compose -f docker-compose.yml -f docker-compose.ssl.yml ps -a certbot 2>/dev/null | grep certbot 2>/dev/null)
if echo "$CERTBOT_STATUS" | grep -q "Exited (0)"; then
    print_success "Certbot container completed successfully"
else
    print_failure "Certbot container did not complete successfully"
    print_info "$CERTBOT_STATUS"
fi

# Summary
echo -e "\n${YELLOW}================================${NC}"
echo -e "${YELLOW}Test Summary${NC}"
echo -e "${YELLOW}================================${NC}"
echo -e "${GREEN}Passed:${NC} $TESTS_PASSED"
echo -e "${RED}Failed:${NC} $TESTS_FAILED"

if [ $TESTS_FAILED -eq 0 ]; then
    echo -e "\n${GREEN}All tests passed! SSL setup is working correctly.${NC}"
    exit 0
else
    echo -e "\n${RED}Some tests failed. Please review the output above.${NC}"
    exit 1
fi
