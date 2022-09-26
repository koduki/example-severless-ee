#!/bin/bash

./mvnw package -Pnative
podman build -t gcr.io/sandbox-svc-dev-8rra/backend -f src/main/docker/Dockerfile.native-micro . 
podman push gcr.io/sandbox-svc-dev-8rra/backend
gcloud run deploy backend --image gcr.io/sandbox-svc-dev-8rra/backend --region asia-northeast3 --allow-unauthenticated