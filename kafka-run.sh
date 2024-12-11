#!/bin/bash

set -e #stops in case of errors

docker-compose -f ./infrastructure/docker-files/common.yml -f ./infrastructure/docker-files/zookeeper.yml up -d
docker-compose -f ./infrastructure/docker-files/common.yml -f ./infrastructure/docker-files/kafka_cluster.yml up -d
docker-compose -f ./infrastructure/docker-files/common.yml -f ./infrastructure/docker-files/kafka_create_topics.yml up -d
