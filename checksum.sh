#!/usr/bin/env bash
set -e

RESULT_FILE="$1"

if [ -f "$RESULT_FILE" ]; then
    rm "$RESULT_FILE"
fi

checksum_file() {
    file="$1"
    openssl md5 "$file" | awk '{print $2}'
}

FILES=()
while read -r -d ''; do
    echo "Found $REPLY"
    FILES+=("$REPLY")
done < <(find . -type f \( -name "build.gradle*" -o -name "Dependencies.kt" \
    -o -name "gradle-wrapper.properties" -o -name "Configuration.kt" \) -print0)

# Loop through files and append their MD5 to result file
for file in "${FILES[@]}"; do
    checksum_file "$file" >>"$RESULT_FILE"
done

# Now sort the file so that we don't have different hashes because of random order
sort "$RESULT_FILE" -o "$RESULT_FILE"
