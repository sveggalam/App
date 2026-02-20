# GitHub Webhook Configuration for Jenkins CI/CD Pipeline

This guide walks you through setting up GitHub webhooks to automatically trigger your Jenkins pipeline whenever code is pushed to the repository.

---

## Prerequisites

- Jenkins running on `http://localhost:8085` (or your Jenkins URL)
- GitHub account with access to the `sveggalam/App` repository
- Admin access to the repository
- Jenkins pipeline job already created (named `app-deployment`)
- Jenkins GitHub plugin installed

---

## Step 1: Install GitHub Plugin in Jenkins

### 1.1 Access Jenkins Manage Plugins

1. Go to **http://localhost:8085**
2. Click **Manage Jenkins** (left sidebar)
3. Click **Manage Plugins**
4. Go to the **Available plugins** tab
5. Search for **GitHub plugin**

### 1.2 Install the Plugin

1. Check the box next to **GitHub Integration** plugin
2. Click **Install without restart**
3. Check **Restart Jenkins when installation is complete and no jobs are running**
4. Wait for Jenkins to restart (30-60 seconds)

---

## Step 2: Create GitHub Personal Access Token

GitHub webhooks need authentication to trigger Jenkins builds.

### 2.1 Generate Token

1. Go to **GitHub Settings:**
   ```
   https://github.com/settings/profile
   ```

2. Click **Developer settings** (bottom of left sidebar)

3. Click **Personal access tokens** → **Tokens (classic)**

4. Click **Generate new token** → **Generate new token (classic)**

5. Fill in the details:
   - **Note:** `Jenkins CI Webhook`
   - **Expiration:** 90 days (or per your policy)
   - **Select scopes:**
     - ✅ `repo` (Full control of private repositories)
     - ✅ `admin:repo_hook` (Full control of repository hooks)
     - ✅ `admin:org_hook` (Full control of organization hooks)

6. Click **Generate token**

7. **Copy the token** (you won't be able to see it again!)
   ```
   ghp_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
   ```

---

## Step 3: Add GitHub Credentials to Jenkins

### 3.1 Navigate to Credentials

1. Go to **http://localhost:8085**
2. Click **Manage Jenkins**
3. Click **Credentials**
4. Click **System** (on the left)
5. Click **Global credentials (unrestricted)**

### 3.2 Add New Credentials

1. Click **+ Add Credentials** (top right or left sidebar)

2. Fill in the form:
   - **Kind:** `Username with password`
   - **Username:** `github-webhook` (or your GitHub username)
   - **Password:** Paste your GitHub Personal Access Token
   - **ID:** `github-webhook-token` (for reference in Jenkinsfile)
   - **Description:** `GitHub Webhook Token for CI/CD`

3. Click **Create**

---

## Step 4: Configure Jenkins Job for GitHub Integration

### 4.1 Open Your Pipeline Job

1. Go to **http://localhost:8085**
2. Click on your pipeline job: **app-deployment**
3. Click **Configure** (left sidebar)

### 4.2 Enable GitHub Integration

1. Scroll to the **Build Triggers** section

2. Check the box: **GitHub hook trigger for GITScm polling**
   - This enables Jenkins to listen for GitHub webhooks

3. Scroll to **Pipeline** section (if not already there)

4. Ensure your pipeline configuration is set to:
   - **Definition:** Pipeline script from SCM
   - **SCM:** Git
   - **Repository URL:** `https://github.com/sveggalam/App.git`
   - **Credentials:** Select `github-webhook-token` (or your GitHub credentials)
   - **Branch Specifier:** `*/main`

5. Click **Save**

---

## Step 5: Configure Webhook in GitHub Repository

### 5.1 Navigate to Repository Settings

1. Go to: `https://github.com/sveggalam/App/settings`
2. Click **Webhooks** (left sidebar)
3. Click **Add webhook**

### 5.2 Configure Webhook Details

Fill in the following:

| Field | Value |
|-------|-------|
| **Payload URL** | `http://localhost:8085/github-webhook/` |
| **Content type** | `application/json` |
| **Secret** | Leave empty (optional for local setup) |
| **Which events?** | ✅ Push events ✅ Pull requests |
| **Active** | ✅ Checked |

**Important:** 
- For **LOCAL Jenkins (localhost):** GitHub cannot reach it from the internet
- For **PUBLIC Jenkins:** Use your actual domain: `https://your-jenkins-domain.com/github-webhook/`

### 5.3 Create Webhook

1. Click **Add webhook**
2. You'll see a **green checkmark** if the webhook was created successfully
3. GitHub will attempt to deliver a test payload

---

## Step 6: Handle Local Network Issue (Important!)

### Problem: GitHub Cannot Reach Localhost

Since Jenkins is running locally (`http://localhost:8085`), GitHub's servers on the internet **cannot reach your local machine**. 

### Solutions:

#### Solution A: Use ngrok (Recommended for Development) 🌟

Ngrok creates a public tunnel to your local Jenkins.

**1. Install ngrok:**
```bash
# Using Homebrew on macOS
brew install ngrok
```

**2. Start ngrok tunnel:**
```bash
ngrok http 8085
```

**Output:**
```
Session Status                online
Account                       <your-account>
Version                       3.x.x
Region                        United States (us)
Forwarding                    https://xxxx-xxx-xx-xx.ngrok.io -> http://localhost:8085
```

**3. Update GitHub Webhook Payload URL:**
```
https://xxxx-xxx-xx-xx.ngrok.io/github-webhook/
```

**4. Keep ngrok running** while using the webhook (it closes when you stop the process)

---

#### Solution B: Use GitHub Actions Instead

If you can't use ngrok, consider using **GitHub Actions** instead:
```yaml
name: Trigger Jenkins
on: [push]
jobs:
  trigger:
    runs-on: ubuntu-latest
    steps:
      - name: Trigger Jenkins Pipeline
        run: |
          curl -X POST http://your-jenkins-server/job/app-deployment/build \
            --user admin:YOUR_API_TOKEN
```

---

#### Solution C: Deploy Jenkins to Cloud

Deploy Jenkins to a public cloud provider (AWS, Azure, Google Cloud) that GitHub can reach.

---

## Step 7: Test the Webhook

### 7.1 Make a Test Git Push

1. Make a small change to your repo:
   ```bash
   cd /Users/subhashveggalam/Documents/FULLSTACK/App/App
   echo "# Webhook Test" >> README.md
   git add README.md
   git commit -m "test: webhook trigger"
   git push origin main
   ```

2. GitHub will immediately send a webhook payload to your configured URL

### 7.2 Check Jenkins Build

1. Go to **http://localhost:8085/job/app-deployment/**
2. Look for a new build in the **Build History** (left sidebar)
3. Click on the build number to see the console output

### 7.3 Verify in GitHub

1. Go to `https://github.com/sveggalam/App/settings/hooks`
2. Click on your webhook
3. Scroll to **Recent Deliveries**
4. Check the status:
   - ✅ **Green checkmark** = Webhook payload delivered successfully
   - ❌ **Red X** = Jenkins URL unreachable (common for localhost)

---

## Troubleshooting

### Issue: Webhook shows "Couldn't connect" in GitHub

**Cause:** GitHub cannot reach your local Jenkins

**Solutions:**
- Use **ngrok** to create a public tunnel (recommended)
- Deploy Jenkins to a public URL
- Use GitHub Actions as an alternative

---

### Issue: Webhook delivers but Jenkins doesn't build

**Cause:** GitHub hook trigger not enabled or credentials misconfigured

**Solutions:**
1. Verify **Build Triggers** → **GitHub hook trigger for GITScm polling** is checked
2. Verify Git repository credentials in the job configuration
3. Check Jenkins logs:
   ```bash
   docker logs $(docker ps | grep jenkins | awk '{print $1}') | grep -i github
   ```

---

### Issue: "Invalid crumb" errors

**Cause:** CSRF protection blocking webhook

**Solution:** Jenkins should automatically handle GitHub webhook requests. If errors persist:
1. Go to **Manage Jenkins** → **Configure System**
2. Scroll to **CSRF Protection** section
3. Check if webhook URL is in **Proxy Compatibility** whitelist

---

### Issue: Pipeline not using latest code

**Cause:** Checkout using old branch information

**Solution:**
1. Edit job configuration
2. Under **Pipeline** → **Definition**
3. Add branch specifier: `${GIT_BRANCH}` or `*/main`

---

## Advanced: Using GitHub Organizations and Webhooks

For organization-level webhooks that trigger multiple repositories:

1. Go to **Organization Settings** → **Webhooks**
2. Configure the same way as repository webhooks
3. In Jenkins, use **Organization Folder** plugin for automatic job creation

---

## Full Webhook Flow Diagram

```
┌─────────────────┐
│  Developer      │
│  git push       │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  GitHub         │
│  Repository     │
└────────┬────────┘
         │ (webhook POST)
         ▼
┌─────────────────┐
│  Jenkins        │
│  localhost:8085 │  (with ngrok: public URL)
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Pipeline Job   │
│  Triggers Build │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Build Steps    │
│  • Build Maven  │
│  • Docker Image │
│  • Push to Hub  │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  Docker Hub     │
│  sveggalam/app  │
└─────────────────┘
```

---

## Complete Quick Reference

```bash
# 1. Install ngrok (one-time)
brew install ngrok

# 2. Start ngrok tunnel (in one terminal)
ngrok http 8085
# Copy the HTTPS URL: https://xxxx-xxx-xx-xx.ngrok.io

# 3. GitHub Steps:
#    - Settings → Developer settings → Personal access tokens
#    - Select scopes: repo, admin:repo_hook
#    - Copy token

# 4. Jenkins Steps:
#    - Manage Jenkins → Credentials → Add GitHub token
#    - Job Configuration → Build Triggers → GitHub hook trigger
#    - Job Configuration → Pipeline → Repository credentials

# 5. GitHub Repository Steps:
#    - Settings → Webhooks → Add webhook
#    - Payload URL: https://xxxx-xxx-xx-xx.ngrok.io/github-webhook/
#    - Content type: application/json
#    - Events: Push events, Pull requests
#    - Active: ✅

# 6. Test
git push origin main
# Check Jenkins → app-deployment → Build History
```

---

## Next Steps

1. **Install ngrok** and start the tunnel
2. **Create GitHub Personal Access Token**
3. **Configure webhook in GitHub** using ngrok URL
4. **Test with a git push**
5. **Verify build in Jenkins**

Once verified, you have a fully automated CI/CD pipeline! 🎉

---

## Security Notes

- ⚠️ **Never commit API tokens or credentials** to your repository
- ✅ Store tokens in Jenkins Credentials Manager
- ✅ Use HTTPS for webhook URLs (ngrok provides this automatically)
- ✅ Consider IP whitelisting if deploying to production
- ✅ Review webhook deliveries regularly in GitHub

