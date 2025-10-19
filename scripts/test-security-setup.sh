#!/bin/bash

# Test Security Automation Setup
# This script validates that the security automation components are properly configured

echo "🔒 Testing Security Automation Setup"
echo "===================================="

# Test 1: Validate workflow files
echo "1. Validating GitHub workflow files..."
if [[ -f ".github/workflows/security-auto-upgrade.yml" ]]; then
    echo "   ✅ Security auto-upgrade workflow exists"
else
    echo "   ❌ Security auto-upgrade workflow missing"
    exit 1
fi

# Test 2: Validate dependabot configuration
echo "2. Validating dependabot configuration..."
if [[ -f ".github/dependabot.yml" ]]; then
    if grep -q "maven" ".github/dependabot.yml"; then
        echo "   ✅ Dependabot configured for Maven"
    else
        echo "   ❌ Dependabot not configured for Maven"
        exit 1
    fi
else
    echo "   ❌ Dependabot configuration missing"
    exit 1
fi

# Test 3: Check Maven plugin configurations
echo "3. Checking Maven plugin configurations..."
if grep -q "dependency-check-maven" pom.xml; then
    echo "   ✅ OWASP Dependency Check plugin configured"
else
    echo "   ❌ OWASP Dependency Check plugin missing"
    exit 1
fi

if grep -q "versions-maven-plugin" pom.xml; then
    echo "   ✅ Maven Versions plugin configured"
else
    echo "   ❌ Maven Versions plugin missing"
    exit 1
fi

# Test 4: Check suppressions file
echo "4. Checking security suppressions file..."
if [[ -f "dependency-check-suppressions.xml" ]]; then
    echo "   ✅ Dependency check suppressions file exists"
else
    echo "   ❌ Dependency check suppressions file missing"
    exit 1
fi

# Test 5: Check documentation
echo "5. Checking security documentation..."
if [[ -f "docs/SECURITY_AUTOMATION.md" ]]; then
    echo "   ✅ Security automation documentation exists"
else
    echo "   ❌ Security automation documentation missing"
    exit 1
fi

# Test 6: Validate Maven configuration syntax
echo "6. Validating Maven configuration..."
if mvn help:effective-pom > /dev/null 2>&1; then
    echo "   ✅ Maven configuration is valid"
else
    echo "   ❌ Maven configuration has syntax errors"
    exit 1
fi

# Test 7: Check for known vulnerable dependencies
echo "7. Checking for known vulnerable dependencies..."
if grep -q "4.13.1" pom.xml; then
    echo "   ⚠️  Found JUnit 4.13.1 (has known vulnerabilities - will be auto-upgraded)"
else
    echo "   ✅ No obvious vulnerable dependencies detected"
fi

echo ""
echo "🎉 Security automation setup validation completed successfully!"
echo ""
echo "Next steps:"
echo "- The security workflow will run daily at 6 AM UTC"
echo "- Dependabot will scan for security updates daily"
echo "- Security PRs will be auto-merged after validation"
echo "- Manual triggers available via GitHub Actions UI"
echo ""
echo "To test manually:"
echo "  mvn org.owasp:dependency-check-maven:check"
echo "  mvn versions:display-dependency-updates"