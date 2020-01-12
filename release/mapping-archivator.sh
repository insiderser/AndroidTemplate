#!/usr/bin/env bash

# Zip mapping
zip -r -j app/build/outputs/mapping/release.zip app/build/outputs/mapping/release/

#Encrypt zipped mapping
openssl enc -e -aes-256-cbc -in app/build/outputs/mapping/release.zip \
    -out app/build/outputs/mapping/release.zip.enc -v -iter 1000000 \
    -k "$MAPPING_PASSPHRASE" -md sha256

# Remove mapping to make sure that we don't leak it anywhere
rm -rf app/build/outputs/mapping/release/ app/build/outputs/mapping/release.zip
