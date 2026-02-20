# Fixing Maven Not Found Error in Jenkins Pipeline

## Problem

Jenkins pipeline failed with error:
```
mvn: not found
```

This occurs because Maven is not installed in the default Jenkins LTS Docker image.

---

## Solution: Rebuild Jenkins with Maven Included

### Step 1: Stop Current Jenkins

```bash
cd /Users/subhashveggalam/Documents/FULLSTACK/App/App
docker-compose -f jenkins-docker-compose.yml down
```

### Step 2: Rebuild and Start Jenkins

```bash
docker-compose -f jenkins-docker-compose.yml up -d --build
```

This will:
- Build a custom Jenkins image with Maven, Java 17, and Docker CLI
- Start all services (Jenkins, SonarQube, PostgreSQL)
- Mount the jenkins_home volume for persistent data

### Step 3: Verify Jenkins is Ready

```bash
# Check if Jenkins container is running
docker ps | grep jenkins

# Check logs for successful startup
docker logs jenkins | tail -20
```

### Step 4: Access Jenkins

Open your browser:
```
http://localhost:8085
```

### Step 5: Re-run Your Pipeline

1. Navigate to your pipeline job: **app-deployment**
2. Click **Build Now**
3. Watch the build progress - it should now find `mvn` correctly

---

## What Changed

### 1. Created Custom Jenkins Dockerfile

**File:** `jenkins.Dockerfile`

Installs:
- ✅ Java 17 (OpenJDK)
- ✅ Maven 3.9.x
- ✅ Docker CLI
- ✅ Git and other utilities

Environment variables set:
- `JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64`
- `MAVEN_HOME=/usr/share/maven`
- `PATH` includes both Java and Maven bin directories

### 2. Updated docker-compose.yml

Changed from using pre-built image to building from custom Dockerfile:

```yaml
# Before:
services:
  jenkins:
    image: jenkins/jenkins:lts

# After:
services:
  jenkins:
    build:
      context: .
      dockerfile: jenkins.Dockerfile
```

### 3. Updated Jenkinsfile

**Changes:**
- Fixed `JAVA_HOME` path: `/usr/lib/jvm/java-17-openjdk-amd64` (not just `java-17-openjdk`)
- Added `PATH` variable to include Maven and Java bin directories
- Updated all `mvn` commands to use full path: `/usr/share/maven/bin/mvn`

This ensures Maven can be found even if PATH is not set correctly.

---

## Build Times

First build will take longer (5-10 minutes) because Docker needs to:
1. Pull Jenkins LTS base image
2. Install Java 17, Maven, and dependencies
3. Build the custom image

Subsequent builds will use cached layers and be much faster.

---

## Troubleshooting

### Jenkins won't start

Check logs:
```bash
docker logs jenkins
```

### Build fails with "mvn: not found" again

Verify Maven is installed in the container:
```bash
docker exec jenkins mvn -version
```

If not found, rebuild with:
```bash
docker-compose -f jenkins-docker-compose.yml up -d --build --no-cache
```

### Docker socket permission issues

If you see Docker build errors, ensure the socket is accessible:
```bash
docker exec jenkins ls -la /var/run/docker.sock
```

---

## Next Steps

After rebuilding Jenkins:

1. ✅ **Re-run the pipeline** to verify Maven works
2. ✅ **Set up GitHub webhooks** to auto-trigger builds on push
3. ✅ **Configure Docker Hub credentials** for pushing images
4. ✅ **Create Kubernetes manifests** for ArgoCD deployment

---

## Complete Rebuild Command

One-liner to stop, rebuild, and restart everything:

```bash
cd /Users/subhashveggalam/Documents/FULLSTACK/App/App && \
docker-compose -f jenkins-docker-compose.yml down && \
docker-compose -f jenkins-docker-compose.yml up -d --build && \
echo "Waiting for Jenkins to start..." && \
sleep 30 && \
docker logs jenkins | tail -20
```

This will:
1. Stop current containers
2. Build new image with Maven
3. Start all services
4. Wait 30 seconds for startup
5. Show last 20 logs to confirm it's running

---

## Custom Jenkinsfile Configuration

Your Jenkinsfile now includes:

```groovy
environment {
    DOCKER_HUB_REPO = 'sveggalam/app'
    MAVEN_HOME = '/usr/share/maven'
    JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
    PATH = "${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${PATH}"
}
```

And all Maven commands use full path:
```groovy
/usr/share/maven/bin/mvn clean package -DskipTests
```

This guarantees Maven will be found regardless of PATH configuration.

