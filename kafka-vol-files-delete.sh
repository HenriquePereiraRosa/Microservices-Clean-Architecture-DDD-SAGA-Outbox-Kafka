#!/bin/bash

# Stops the script in case of errors
set -o errexit -o pipefail
trap 'echo "Error occurred at line $LINENO during $BASH_COMMAND"' ERR


# Remove all files inside docker-files/volumes/* but not the folders
find ./infrastructure/docker-files/volumes/ -type f -delete
