#!/bin/bash

./mvnw package -Pnative
podman build -t gcr.io/sandbox-svc-dev-8rra/frontend -f src/main/docker/Dockerfile.native-micro . 
gcloud auth print-access-token | podman login -u oauth2accesstoken --password-stdin gcr.io
podman push gcr.io/sandbox-svc-dev-8rra/frontend
gcloud run deploy frontend --image gcr.io/sandbox-svc-dev-8rra/frontend --region asia-northeast3 --allow-unauthenticated