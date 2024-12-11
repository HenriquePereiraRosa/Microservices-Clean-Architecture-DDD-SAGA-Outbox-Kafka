#!/bin/bash

set -e #stops in case of errors


# Remove all files inside docker-files/volumes/* but not the folders
find ./infrastructure/docker-files/volumes/ -type f -delete
