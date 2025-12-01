
# 📦 Bulk Orchestration Service

A high-performance **batch processing microservice** built with **Spring Boot**, **Spring Batch**, **WebClient**, **MySQL**, and **Docker**.
This service handles large CSV imports, integrates with the [**User Management Service**](https://github.com/sanah-saleem/user-management), and provides **row-level error tracking**, **CSV exporting**, and **multi-threaded processing**.

---

## ✨ Features

### ✅ **1. CSV Import (Spring Batch Multi-threaded)**

* Upload CSV file (email, firstName, lastName)
* Processes records in parallel using **AsyncTaskExecutor**
* Step-scoped components for safe multi-threading

### ✅ **2. REST Integration (WebClient)**

* Each CSV row triggers a request to **User Management Service**
* API calls, timeouts, and error handling
* Retry-ready architecture

### ✅ **3. Row-level Error Tracking**

* Every failed row is stored in `import_job_errors`
* API to view errors per job
* **Export errors as CSV** (`GET /api/imports/{id}/errors/export`)

### ✅ **4. Import Job Metadata**

Tracks at job level:

* totalItems
* successCount
* failureCount
* timestamps (createdAt, startedAt, finishedAt)
* error messages
* status (PENDING/RUNNING/COMPLETED/FAILED)

### ✅ **5. Dockerized + MySQL Ready**

* Dockerfile (multi-stage)
* Docker Compose support
* `.env` based configuration
* Push images to Docker Hub

### ✅ **6. Postman Collection**

A ready-to-use **Postman collection JSON** is included in the project root.

---

## 🛠️ Tech Stack

### **Backend**

* **Java 21**
* **Spring Boot 3**
* **Spring Batch 5**
* **Spring WebFlux WebClient**
* **Spring Data JPA**

### **Infrastructure**

* **MySQL 8**
* **Docker + Docker Compose**
* **Docker Hub Image Deployment**
* **TaskExecutor (Thread Pool)**

### **Testing & Tools**

* Postman Collection
* Lombok
* JPA/Hibernate

---

# 🚀 Installation & Setup Guide

## 1️⃣ Clone the repository

```bash
git clone https://github.com/<your-username>/bulkorchestration.git
cd bulkorchestration
```

---

## 2️⃣ Create your Environment Variables

A template is provided:

```bash
cp .env.example .env
```


## 3️⃣ Run using Docker Compose (recommended)

Make sure Docker is installed.

```bash
docker compose up -d
```

To pull updated images:

```bash
docker compose up --pull always -d
```

To recreate containers:

```bash
docker compose up --force-recreate -d
```

Your service will be available at:

```
http://localhost:8083
```

---

## 4️⃣ Running Locally (Without Docker)

### Install dependencies

You need:

* Java 21
* Maven 3.9+
* MySQL running locally on port 3310 (or adjust `application.yml`)

### Start MySQL locally:

```bash
docker run --name mysql-bulk \
 -e MYSQL_ROOT_PASSWORD=root \
 -e MYSQL_DATABASE=bulk_orchestration \
 -p 3310:3306 \
 -d mysql:8.0
```

### Run the service:

```bash
mvn spring-boot:run
```

---

## 5️⃣ Building & Publishing Docker Image

### Build image

```bash
docker build -t yourdockerhubusername/bulk-orchestration-service:latest .
```

### Push to Docker Hub

```bash
docker push yourdockerhubusername/bulk-orchestration-service:latest
```

Update compose to use it:

```yaml
image: yourdockerhubusername/bulk-orchestration-service:latest
```

---

# 📚 API Endpoints

### Upload CSV File

```
POST /api/imports/upload
Multipart: file, sendWelcomeEmail
```

### Run Import Job

```
POST /api/imports/{id}/run
```

### Get Job Status

```
GET /api/imports/{id}
```

### Get Error Rows

```
GET /api/imports/{id}/errors
```

### Export Error Rows as CSV

```
GET /api/imports/{id}/errors/export
```

---

# 🧪 Postman Collection

A Postman collection JSON file is included at the root of this project:

```
bulk-orchestration-postman.json
```

Import it into Postman to instantly test:

* File upload
* Job run
* Job status
* Error exports

---

# 🔮 Future Enhancements

### 🚀 1. Welcome Email via Notification Service (Kafka)

Trigger a welcome email event after successful user creation.

### 🔄 2. Support more file types

* Excel (XLSX)
* JSON lines
* ND-CSV

### 🚢 3. Scheduled imports

Cron-based batch jobs using Spring Scheduler.

### 📤 4. Export full job report

Success rows + failure rows + summary.

### 📈 5. Dashboard / UI

React or Angular UI to view import history.

---

