# Jenkins Quick Start (5 Minutes)

## 1. Start Jenkins

```bash
cd /Users/subhashveggalam/Documents/FULLSTACK/App/App
docker-compose -f jenkins-docker-compose.yml up -d
```

Wait 30 seconds for Jenkins to start.

## 2. Get Admin Password

```bash
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

Copy this password.

## 3. Open Jenkins

Open browser: **http://localhost:8081**

Paste the password and continue.

## 4. Install Plugins

Click **Install suggested plugins** and wait (5-10 min).

## 5. Create Admin User

- Username: `admin`
- Password: `jenkins123`
- Email: `jenkins@example.com`

Click **Save and Continue** → **Save and Finish**

## 6. Install Additional Plugins

Go to **Manage Jenkins** → **Manage Plugins** → **Available tab**

Search and install:
- Docker
- Docker Pipeline
- GitHub
- Maven Integration

After installing, restart Jenkins:
```bash
docker restart jenkins
```

## 7. Add GitHub Credentials

1. **Manage Jenkins** → **Manage Credentials**
2. **System** → **Global credentials** → **Add Credentials**
3. Create your GitHub Personal Access Token:
   - Go to: https://github.com/settings/tokens
   - Click "Generate new token"
   - Name: `jenkins-token`
   - Scopes: Check `repo` and `admin:repo_hook`
   - Click "Generate token"
4. In Jenkins, enter:
   - Kind: `Username with password`
   - Username: `sveggalam`
   - Password: (paste the token)
   - ID: `github-credentials`
   - Click **Create**

## 8. Add Docker Hub Credentials

1. **Manage Jenkins** → **Manage Credentials**
2. **System** → **Global credentials** → **Add Credentials**
3. Create your Docker Hub Access Token:
   - Go to: https://hub.docker.com/settings/security
   - Click "New Access Token"
   - Name: `jenkins`
   - Click "Generate"
4. In Jenkins, enter:
   - Kind: `Username with password`
   - Username: `sveggalam`
   - Password: (paste the token)
   - ID: `dockerhub-credentials`
   - Click **Create**

## 9. Create Pipeline Job

1. Click **New Item**
2. Name: `App-CI-CD-Pipeline`
3. Select **Pipeline**
4. Click **OK**
5. In **Pipeline** section:
   - Select: **Pipeline script from SCM**
   - SCM: **Git**
   - Repository URL: `https://github.com/sveggalam/App.git`
   - Credentials: `github-credentials`
   - Branch: `*/main`
   - Script Path: `Jenkinsfile`
6. Click **Save**

## 10. Run First Build

1. Click **Build Now**
2. Click the build number (blue ball) to watch progress
3. Check **Console Output**

## Done!

Your Jenkins pipeline is ready! 🎉

Every time you push to GitHub, Jenkins will automatically:
- Build your microservices
- Create Docker images
- Push to Docker Hub
- Update Kubernetes manifests

---

## Troubleshooting

**Jenkins won't start?**
```bash
docker-compose -f jenkins-docker-compose.yml logs jenkins
docker-compose -f jenkins-docker-compose.yml restart jenkins
```

**Can't login?**
- Check password: `docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword`

**Build fails?**
- Check logs in Jenkins console
- Verify credentials are correct

**Docker won't work in Jenkins?**
```bash
docker exec jenkins apt-get update && apt-get install -y docker.io
docker restart jenkins
```

---

## Next: Kubernetes Manifests

Once your builds work, we'll create:
1. Kubernetes deployment files
2. Service files for each microservice
3. ArgoCD setup for continuous deployment

See: `JENKINS_SETUP.md` for full details.
