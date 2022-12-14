# syntax=docker/dockerfile:1.4.1

# See here for image contents: https://github.com/microsoft/vscode-dev-containers/blob/main/containers/java/.devcontainer/base.Dockerfile
# [Choice] Java version (use -bullseye variants on local arm64/Apple Silicon): 8, 11, 17, 8-bullseye, 11-bullseye, 17-bullseye, 8-buster, 11-buster, 17-buster
ARG VARIANT=11-bullseye
FROM mcr.microsoft.com/vscode/devcontainers/java:${VARIANT}

# [Option] Install Maven
ARG INSTALL_MAVEN="false"
ARG MAVEN_VERSION=""
# [Option] Install Gradle
ARG INSTALL_GRADLE="false"
ARG GRADLE_VERSION=""
RUN if [ "${INSTALL_MAVEN}" = "true" ]; then su vscode -c "umask 0002 && . /usr/local/sdkman/bin/sdkman-init.sh && sdk install maven \"${MAVEN_VERSION}\""; fi \
    && if [ "${INSTALL_GRADLE}" = "true" ]; then su vscode -c "umask 0002 && . /usr/local/sdkman/bin/sdkman-init.sh && sdk install gradle \"${GRADLE_VERSION}\""; fi

# Install Google Cloud SDK
RUN echo "deb [signed-by=/usr/share/keyrings/cloud.google.gpg] http://packages.cloud.google.com/apt cloud-sdk main" | tee -a /etc/apt/sources.list.d/google-cloud-sdk.list && curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | apt-key --keyring /usr/share/keyrings/cloud.google.gpg  add - && apt-get update -y && apt-get install google-cloud-sdk -y
## Gen Dummy Service Account
RUN <<EOF
mkdir -p /secret/ && openssl genpkey -out /secret/dummy.pem -algorithm RSA -pkeyopt rsa_keygen_bits:2048
cat <<- _DOC_ > /secret/credential.json 
  {
    "type": "service_account",
    "project_id": "dummy",
    "private_key_id": "dummy",
    "private_key": "$(cat /secret/dummy.pem | perl -pe 's/\n/\\n/g')",
    "client_email": "dummy",
    "client_id": "dummy",
    "auth_uri": "https://accounts.google.com/o/oauth2/auth",
    "token_uri": "https://oauth2.googleapis.com/token",
    "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
    "client_x509_cert_url": "dummy"
  }
_DOC_
EOF

# Install Quarkus
RUN su vscode -c "umask 0002 && . /usr/local/sdkman/bin/sdkman-init.sh && sdk install quarkus 2>&1"

# Podman Install
RUN <<EOF
apt -y install podman
mkdir -p /home/vscode/.config/containers
cat << _DOC_ > /home/vscode/.config/containers/containers.conf
[engine]
events_logger="file"
_DOC_
chown -R vscode:vscode /home/vscode/.config/containers
EOF