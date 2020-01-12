#!/usr/bin/env bash

if [[ -n $RELEASE_KEYSTORE_ENCRYPT_KEY && -n $RELEASE_KEYSTORE_IV ]]; then
    openssl aes-256-cbc -md sha256 -d -in release/release.jks.aes \
        -out release/release.jks -K "$RELEASE_KEYSTORE_ENCRYPT_KEY" \
        -iv "$RELEASE_KEYSTORE_IV" -iter 1000000
else
    echo Keys are empty
    exit 1
fi
