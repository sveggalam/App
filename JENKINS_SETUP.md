# Jenkins Setup Guide - Docker on Local Machine

## Step 1: Start Jenkins with Docker

### Option A: Using the docker-compose file (Recommended)

```bash
cd /Users/subhashveggalam/Documents/FULLSTACK/App/App

# Start Jenkins, SonarQube, and PostgreSQL
docker-compose -f jenkins-docker-compose.yml up -d

# Check status
docker-compose -f jenkins-docker-compose.yml ps

# View Jenkins logs
docker-compose -f jenkins-docker-compose.yml logs -f jenkins
```

### Option B: Using Docker directly (Simple)

```bash
docker run -d \
  --name jenkins \
  -p 8081:8080 \
  -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  -v /var/run/docker.sock:/var/run/docker.sock \
  jenkins/jenkins:lts
```

---

## Step 2: Access Jenkins

1. **Open Jenkins UI:**
   - URL: http://localhost:8085
   - Wait 30-60 seconds for Jenkins to fully start

2. **Get Initial Admin Password:**
   ```bash
   # Option 1: From logs
   docker-compose -f jenkins-docker-compose.yml logs jenkins | grep "Please use the following password"
   
   # Option 2: From Jenkins home
   docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
   ```

3. **First Login:**
   - Copy the password
   - Paste it in Jenkins UI
   - Click "Continue"

---

## Step 3: Initial Jenkins Configuration

### A. Install Suggested Plugins
1. Click "Install suggested plugins"
2. Wait for plugins to install (5-10 minutes)

### B. Create Admin User
1. Username: `admin`
2. Password: `jenkins123` (or your choice)
3. Full name: `Jenkins Admin`
4. Email: `jenkins@example.com`
5. Click "Save and Continue"

### C. Set Jenkins URL
- Jenkins URL: `http://localhost:8081/`
- Click "Save and Finish"

---

## Step 4: Install Additional Plugins

1. Go to **Manage Jenkins** → **Manage Plugins**
2. Click **Available** tab
3. Search and install these plugins:

```
- Docker
- Docker Pipeline
- GitHub
- GitHub Branch Source
- Maven Integration
- JUnit
- Credentials
- Credentials Binding
- Pipeline: Multibranch
- Blue Ocean (Optional, for better UI)
```

Steps:
- Check each plugin checkbox
- Click "Install without restart"
- After installation, restart Jenkins: 
  ```bash
  docker restart jenkins
  ```

---

## Step 5: Configure Docker in Jenkins

### A. Give Jenkins Permission to Use Docker

```bash
# Get into Jenkins container
docker exec -it jenkins bash

# Install Docker CLI inside Jenkins container
apt-get update
apt-get install -y docker.io
exit
```

### B. Verify Docker Works in Jenkins

```bash
docker exec jenkins docker ps
```

---

## Step 6: Add GitHub Credentials to Jenkins

1. Go to **Manage Jenkins** → **Manage Credentials**
2. Click **System** → **Global credentials**
3. Click **Add Credentials**

**For GitHub:**
- Kind: `Username with password`
- Username: `sveggalam`
- Password: Your GitHub **Personal Access Token** (not password!)
  - Create at: https://github.com/settings/tokens
  - Scopes: `repo`, `admin:repo_hook`
- ID: `github-credentials`
- Click **Create**

---

## Step 7: Add Docker Hub Credentials to Jenkins

1. Go to **Manage Jenkins** → **Manage Credentials**
2. Click **System** → **Global credentials**
3. Click **Add Credentials**

**For Docker Hub:**
- Kind: `Username with password`
- Username: `sveggalam`
- Password: Your Docker Hub **Access Token** (not password!)
  - Create at: https://hub.docker.com/settings/security
- ID: `dockerhub-credentials`
- Click **Create**

---

## Step 8: Create a New Jenkins Job

### A. Create a Pipeline Job

1. Click **New Item**
2. Job name: `App-CI-CD-Pipeline`
3. Select **Pipeline**
4. Click **OK**

### B. Configure Pipeline

Go to **Pipeline** section:

```groovy
// Select: Pipeline script from SCM
// SCM: Git
// Repository URL: https://github.com/sveggalam/App.git
// Credentials: github-credentials
// Branch: */main
// Script Path: Jenkinsfile
```

### C. Build Triggers (Optional)

- Check: **GitHub hook trigger for GITScm polling**
  - This triggers the job whenever you push to GitHub

### D. Save

Click **Save**

---

## Step 9: Configure GitHub Webhook (Optional but Recommended)

### A. Get Jenkins Webhook URL

Jenkins Webhook: `http://your-machine-ip:8081/github-webhook/`

### B. Add Webhook to GitHub

1. Go to: https://github.com/sveggalam/App/settings/hooks
2. Click **Add webhook**
3. Payload URL: `http://your-machine-ip:8081/github-webhook/`
4. Content type: `application/json`
5. Events: Select **Just the push event**
6. Click **Add webhook**

---

## Step 10: Run Your First Build

1. Go to your Jenkins job: `App-CI-CD-Pipeline`
2. Click **Build Now**
3. Watch the build in real-time:
   - Click on the build number
   - Click **Console Output**

### Expected Build Steps:
1. ✅ Fetch Code from GitHub
2. ✅ Build All Microservices (parallel)
3. ✅ Create Docker Images
4. ✅ Push to Docker Hub
5. ✅ Update Kubernetes Manifests

---

## Troubleshooting

### Issue 1: Jenkins Won't Start

```bash
# Check logs
docker-compose -f jenkins-docker-compose.yml logs jenkins

# Restart Jenkins
docker-compose -f jenkins-docker-compose.yml restart jenkins

# Increase memory if needed
# Edit jenkins-docker-compose.yml and add:
# environment:
#   - JAVA_OPTS=-Xmx512m
```

### Issue 2: Can't Connect to Docker

```bash
# Verify Docker socket is mounted
docker exec jenkins ls -la /var/run/docker.sock

# Give Jenkins user permission
docker exec jenkins usermod -aG docker jenkins
docker restart jenkins
```

### Issue 3: GitHub Credentials Not Working

```bash
# Make sure you're using Personal Access Token, not password
# Create token at: https://github.com/settings/tokens
# Required scopes: repo, admin:repo_hook
```

### Issue 4: Docker Hub Push Fails

```bash
# Verify credentials are correct
# Test manually inside container:
docker exec jenkins docker login -u sveggalam -p YOUR_TOKEN
```

### Issue 5: Build Fails at Docker Build Step

```bash
# Check if Docker is accessible in Jenkins container
docker exec jenkins docker ps

# If not, reinstall Docker CLI:
docker exec jenkins apt-get update && apt-get install -y docker.io
```

---

## Useful Commands

```bash
# View Jenkins logs
docker-compose -f jenkins-docker-compose.yml logs -f jenkins

# Restart Jenkins
docker-compose -f jenkins-docker-compose.yml restart jenkins

# Stop Jenkins
docker-compose -f jenkins-docker-compose.yml down

# Clean up volumes (WARNING: deletes all Jenkins data)
docker-compose -f jenkins-docker-compose.yml down -v

# Access Jenkins container shell
docker exec -it jenkins bash

# Check Jenkins version
docker exec jenkins java -jar /usr/share/jenkins/jenkins.war --version
```

---

## Verify Everything is Working

### 1. Check Jenkins is Running
```bash
curl http://localhost:8081
```

### 2. Check GitHub Connection
- Go to Jenkins → Manage Credentials
- You should see `github-credentials`

### 3. Check Docker Hub Connection
- Go to Jenkins → Manage Credentials
- You should see `dockerhub-credentials`

### 4. Run a Test Build
- Click your job → **Build Now**
- Check **Console Output**

### 5. Verify Docker Images are Created
```bash
# After first successful build
docker images | grep sveggalam/app
```

### 6. Verify Images are Pushed to Docker Hub
```bash
# Go to https://hub.docker.com/r/sveggalam/app
# You should see your images there
```

---

## Next Steps

1. ✅ Start Jenkins with Docker
2. ✅ Complete initial configuration
3. ✅ Install plugins
4. ✅ Add credentials (GitHub & Docker Hub)
5. ✅ Create pipeline job from Jenkinsfile
6. ✅ Run first build
7. → Now create Kubernetes manifests
8. → Set up ArgoCD for CD

---

## Quick Start Command

```bash
# One-liner to start everything:
cd /Users/subhashveggalam/Documents/FULLSTACK/App/App && \
docker-compose -f jenkins-docker-compose.yml up -d && \
echo "Jenkins starting at http://localhost:8081"
```

Wait 30-60 seconds, then open http://localhost:8081 in your browser.

---

## Jenkins Pipeline Stages (What Your Job Will Do)

When you trigger the build:

```
1. Fetch Code from GitHub
   ├─ Clone repo
   ├─ Get commit details
   └─ Load Jenkinsfile

2. Build All Microservices (Parallel)
   ├─ Build auth-service
   ├─ Build studentMicroservice
   ├─ Build libraryMicroservice
   ├─ Build messMicroservice
   └─ Build apiGateway

3. Create Docker Images
   ├─ Build auth-service Docker image
   ├─ Build studentMicroservice Docker image
   ├─ Build libraryMicroservice Docker image
   ├─ Build messMicroservice Docker image
   └─ Build apiGateway Docker image

4. Push to Docker Hub
   ├─ Login to Docker Hub
   ├─ Push auth-service image
   ├─ Push studentMicroservice image
   ├─ Push libraryMicroservice image
   ├─ Push messMicroservice image
   ├─ Push apiGateway image
   └─ Logout from Docker Hub

5. Update Kubernetes Manifests
   └─ Update image tags in k8s deployment files

6. Deploy to Kubernetes
   └─ Ready for manual kubectl apply or ArgoCD
```

---

**You're all set! Ready to run the pipeline?** 🚀
