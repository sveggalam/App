FROM jenkins/jenkins:2.426.2-jdk17

USER root

# Install Maven, Docker CLI, and other dependencies
RUN apt-get update && apt-get install -y \
    maven \
    curl \
    git \
    apt-transport-https \
    ca-certificates \
    gnupg \
    lsb-release \
    docker.io && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Verify installations
RUN java -version && mvn -version && docker --version

USER jenkins
