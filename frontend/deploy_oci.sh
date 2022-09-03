#!/bin/bash

./mvnw package -Pnative
podman build -t gcr.io/sandbox-svc-dev-8rra/frontend -f src/main/docker/Dockerfile.native-micro . 
podman push gcr.io/sandbox-svc-dev-8rra/frontend
gcloud run deploy frontend --image gcr.io/sandbox-svc-dev-8rra/frontend --region asia-northeast3 --allow-unauthenticated