#!/usr/bin/env bash

# Zip mapping
zip -r -j app/build/outputs/mapping/release.zip app/build/outputs/mapping/release/

#Encrypt zipped mapping
openssl aes-256-cbc -e -md sha256 -in app/build/outputs/mapping/release.zip \
    -out app/build/outputs/mapping/release.zip.aes -v -iter 1000000 -K "$MAPPING_KEY"

# Remove mapping to make sure that we don't leak it anywhere
rm -rf app/build/outputs/mapping/release/ app/build/outputs/mapping/release.zip
