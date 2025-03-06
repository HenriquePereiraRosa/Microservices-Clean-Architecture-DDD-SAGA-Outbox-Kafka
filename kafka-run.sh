#!/bin/bash

# Stops the script in case of errors
set -o errexit -o pipefail
trap 'echo "Error occurred at line $LINENO during $BASH_COMMAND"' ERR

echo "Starting Zookeeper..."
docker-compose -f ./infrastructure/docker-files/common.yml -f ./infrastructure/docker-files/zookeeper.yml up -d

echo "Starting Kafka Cluster..."
docker-compose -f ./infrastructure/docker-files/common.yml -f ./infrastructure/docker-files/kafka_cluster.yml up -d

echo "Starting Kafka Topics Initialization..."
docker-compose -f ./infrastructure/docker-files/common.yml -f ./infrastructure/docker-files/kafka_create_topics.yml up -d
