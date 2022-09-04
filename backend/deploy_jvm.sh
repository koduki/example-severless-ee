#!/bin/bash

mvn package
podman build -t gcr.io/sandbox-svc-dev-8rra/backend -f src/main/docker/Dockerfile.jvm .
gcloud auth print-access-token | podman login -u oauth2accesstoken --password-stdin gcr.io
podman push gcr.io/sandbox-svc-dev-8rra/backend
gcloud run deploy backend --image gcr.io/sandbox-svc-dev-8rra/backend --region asia-northeast3 --allow-unauthenticated