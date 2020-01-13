#!/usr/bin/env bash

RELEASE_KEYSTORE_KEY="$1"
RELEASE_KEYSTORE_IV="$2"

if [[ -n $RELEASE_KEYSTORE_KEY && -n $RELEASE_KEYSTORE_IV ]]; then
    openssl enc -aes-256-cbc -md sha256 -d -in release/release.jks.enc \
        -out release/release.jks -K "$RELEASE_KEYSTORE_KEY" -iter 1000000 \
        -iv "$RELEASE_KEYSTORE_IV"
else
    echo RELEASE_KEYSTORE_KEY is empty
    exit 1
fi
