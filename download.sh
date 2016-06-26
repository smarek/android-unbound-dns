#!/bin/bash

source _setenv_android.bash

check_signature() {
    local file="$1.tar.gz.asc" fprint="$2" out=
    if test -n "$_NO_CHECK_SIGNATURE"; then
        echo "[?] signature check for $file skipped, unset _NO_CHECK_SIGNATURE env variable to check signature $_NO_CHECK_SIGNATURE"
        return 0
    fi
    echo "[?] checking $file for valid fingerprint (should be $fprint), set _NO_CHECK_SIGNATURE env variable to skip checking"
    if out=$(gpg --status-fd 1 --keyserver hkp://keys.gnupg.net --verify "$file" 2>/dev/null) &&
        echo "$out" | grep -qs "^\[GNUPG:\] VALIDSIG $fprint "; then
        echo "[+] signature is valid and expected"
        return 0
    else
        echo "$out" >&2
        echo "[-] signature is incorrect or the file is damaged"
        return 1
   fi
}

echo "Downloading Expat from $_EXPAT_URL"
curl -s -L $_EXPAT_URL -o $_EXPAT_NAME.tar.gz || error "Could not download Expat from $_EXPAT_URL"
echo "Downloading OpenSSL from $_OPENSSL_URL"
curl -s -L $_OPENSSL_URL -o $_OPENSSL_NAME.tar.gz || error "Could not download OpenSSL from $_OPENSSL_URL"
curl -s -L $_OPENSSL_URL_SIGNATURE -o $_OPENSSL_NAME.tar.gz.asc || error "Could not download OpenSSL PGP Signature from $_OPENSSL_URL_SIGNATURE"
check_signature $_OPENSSL_NAME $_OPENSSL_URL_SIGNATURE_FINGERPRINT || error "Could not verify signature of $_OPENSSL_NAME.tar.gz"
echo "Downloading LibEvent from $_LIBEVENT_URL"
test -d $_LIBEVENT_NAME || git clone $_LIBEVENT_URL $_LIBEVENT_NAME || error "Could not clone LibEvent repository from $_LIBEVENT_URL"
cd $_LIBEVENT_NAME
git reset --hard
git checkout release-2.0.22-stable
git apply < ../libevent-2.0.22-stable.patch
cd ..
echo "Downloading Unbound from $_UNBOUND_URL"
curl -s -L $_UNBOUND_URL -o $_UNBOUND_NAME.tar.gz || error "Could not download Unbound from $_UNBOUND_URL"
curl -s -L $_UNBOUND_URL_SIGNATURE -o $_UNBOUND_NAME.tar.gz.asc || error "Could not download Unbound PGP Signature from $_UNBOUND_URL_SIGNATURE"
check_signature $_UNBOUND_NAME $_UNBOUND_URL_SIGNATURE_FINGERPRINT || error "Could not verify signature of $_UNBOUND_NAME.tar.gz"

echo "All Downloads finished successfully"
