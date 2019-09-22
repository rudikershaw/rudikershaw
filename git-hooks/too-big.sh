#!/bin/sh

bytez=$(cat "$(git rev-parse --show-toplevel)/$1" | wc -c)
if [ "$bytez" -gt 1000000 ]
then
   cat <<EOF
Error: Attempting to commit a file larger than approximately 1mb.

Commiting large files slows jenkins builds, clones, and other operations we'd rather not slow down.
Consider generating, downloading, zipping, etc these files.

Offending file - $1
EOF
   exit 1
fi
