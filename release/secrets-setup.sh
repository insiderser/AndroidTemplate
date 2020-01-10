#!/usr/bin/env bash

if [[ -n $RELEASE_KEYSTORE_ENCRYPT_KEY ]]; then
    openssl aes-256-cbc -md sha256 -d -in release/release.jks.aes \
        -out release/release.jks -K "$RELEASE_KEYSTORE_ENCRYPT_KEY" -v -iter 1000000
else
    echo The RELEASE_KEYSTORE_ENCRYPT_KEY is empty
fi
