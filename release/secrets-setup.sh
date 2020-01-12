#!/usr/bin/env bash

if [[ -n $RELEASE_KEYSTORE_KEY ]]; then
    openssl aes-256-cbc -md sha256 -d -in release/release.jks.enc \
        -out release/release.jks -k "$RELEASE_KEYSTORE_KEY" -iter 1000000
else
    echo Keys are empty
    exit 1
fi
