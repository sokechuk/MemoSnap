# 📸 MemoSnap — Mobile Security Capstone Project

**Course**: Mobile Security  
**Project Type**: Group Capstone  
**Semester**: Summer 2025  
**Team Members**: Samuel Okechukwu, ...

---

## 📋 Project Description

**MemoSnap** is a proof-of-concept Android application developed for educational purposes to explore mobile application security. The app allows users to capture and store photo memos on their device.

The project is structured into two major phases:
1. An intentionally insecure version that demonstrates common mobile vulnerabilities.
2. A secured version where these issues are addressed using Android best practices and security features.

This hands-on approach allows us to analyze real vulnerabilities using tools like MobSF and demonstrate how to fix them.

---

## 🗂️ Deliverables

- ✅ Final App Repository (Insecure and Secure Versions)
- ✅ Final Report (Architecture, Threat Models, Fixes, Reflection)
- ✅ Video Demos (Insecure Walkthrough, Secure Version Walkthrough)

---

## 🚀 Project Phases & Checklist

### 🔧 Phase 1: Project Setup

- [x] Create Android Studio project (language: Kotlin)
- [x] Set up GitHub repository
- [ ] Define app architecture (MVVM + local storage)
- [ ] Define UI mockups (main screen, memo detail screen, add photo memo)

---

### 🐛 Phase 2: Intentionally Insecure Version

Implement core features **without** applying security best practices.

- [ ] Capture and store memos (photo + optional text)
- [ ] Save images unencrypted to external storage
- [ ] Use `SharedPreferences` to store memo metadata (in plaintext)
- [ ] Use insecure logging (e.g., `Log.d()` with file paths or sensitive data)
- [ ] Hardcode file paths and keys (e.g., encryption keys)
- [ ] Disable code obfuscation (no ProGuard/R8)
- [ ] Enable `android:debuggable` in the manifest
- [ ] Add unnecessary or overly broad permissions (e.g., `READ_EXTERNAL_STORAGE`)
- [ ] No authentication (no login or PIN protection)

---

### 🔍 Phase 3: Vulnerability Analysis

Use static and dynamic tools to identify vulnerabilities.

- [ ] Run **MobSF** scan on APK (static and dynamic analysis)
- [ ] Use **JADX** to inspect decompiled code
- [ ] Document discovered vulnerabilities
- [ ] (Optional) Use **Frida/Objection** to simulate attacks
- [ ] Record **Video Demo 1**: Explaining vulnerabilities

---

### 🔐 Phase 4: Secure Version

Fix identified vulnerabilities and demonstrate at least 2 security features.

- [ ] Encrypt photo files using AndroidX `EncryptedFile` or AES + KeyStore
- [ ] Use `EncryptedSharedPreferences` or SQLite with SQLCipher
- [ ] Remove all sensitive logging
- [ ] Replace hardcoded values with secure retrieval mechanisms
- [ ] Restrict permissions (only request what's needed)
- [ ] Disable `debuggable` in manifest
- [ ] Enable R8/ProGuard for code obfuscation
- [ ] Add basic authentication (biometrics or PIN)
- [ ] (Optional) Add root detection logic
- [ ] Record **Video Demo 2**: Secure version walkthrough

---

### 📄 Phase 5: Final Report

Create a comprehensive report to summarize findings and improvements.

- [ ] Project architecture diagram
- [ ] Before/After **Threat Model** (e.g., STRIDE table or DFD)
- [ ] Table of vulnerabilities (Insecure vs Secured)
- [ ] Reflections from each group member:
    - What you learned
    - Challenges you faced
    - How tools helped
- [ ] Link to GitHub repo and APKs (vulnerable and secure)

---

## 💡 Tools & Frameworks

| Tool | Purpose |
|------|---------|
| **MobSF** | Static & dynamic vulnerability scanning |
| **JADX** | Decompiled code inspection |
| **Android Studio** | App development |
| **Frida / Objection** | (Optional) Dynamic hooking and runtime tampering |
| **Git / GitHub** | Version control and collaboration |
| **R8 / ProGuard** | Code obfuscation and shrinkage |
| **EncryptedFile / EncryptedSharedPreferences** | Secure data storage |
| **Android Keystore** | Secure key management |

---

## 📂 Repo Structure (Suggested)

```
MemoSnap/
├── insecure/               # Insecure app version
├── secure/                 # Secure app version
├── screenshots/            # Screenshots for README or report
├── demos/                  # Recorded demo videos
├── report/                 # Final report files (PDF, diagrams)
├── README.md
```

---

## 🙋 Task Assignments (To Be Discussed)

| Team Member | Responsibilities |
|-------------|------------------|
| [Name] | Android dev (UI + insecure features) |
| [Name] | Security testing (MobSF, Frida) |
| [Name] | Secure feature implementation |
| [Name] | Report writing + video production |

---

## ✅ Progress Tracking

Use GitHub Issues and PRs to break down tasks and track individual contributions.

---

## 🧠 License & Disclaimer

This app is for **educational purposes only**. The insecure version is intentionally vulnerable and should **not** be used in production environments or installed on personal devices without caution.

---
