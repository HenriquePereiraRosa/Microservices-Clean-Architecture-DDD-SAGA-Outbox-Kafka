#!/bin/bash

echo "Starting DataBase..."
docker-compose -f ./infrastructure/docker-files/db.yml up -d
