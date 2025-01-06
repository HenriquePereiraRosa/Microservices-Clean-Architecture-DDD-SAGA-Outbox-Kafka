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

echo "Checking Schema Registry status..."
docker-compose -f ./infrastructure/docker-files/common.yml -f ./infrastructure/docker-files/kafka_cluster.yml up -d schema-registry

echo "Waiting for Schema Registry to be ready..."
# Add a delay to allow Schema Registry to initialize properly
sleep 20

echo "Checking Schema Registry logs for debugging information..."
# Display the last few lines of Schema Registry logs to verify it's running correctly
docker logs $(docker ps --filter "name=schema-registry" --format "{{.ID}}") | tail -n 20

echo "Validating Schema Registry connectivity..."
# Attempt to fetch the list of subjects from Schema Registry
SCHEMA_REGISTRY_PORT=8081 # Adjust if necessary
SCHEMA_REGISTRY_HOST="localhost"

curl -X GET http://$SCHEMA_REGISTRY_HOST:$SCHEMA_REGISTRY_PORT/subjects || {
    echo "Schema Registry is not reachable at http://$SCHEMA_REGISTRY_HOST:$SCHEMA_REGISTRY_PORT"
    exit 1
}

echo "Schema Registry is running and accessible at http://$SCHEMA_REGISTRY_HOST:$SCHEMA_REGISTRY_PORT"
