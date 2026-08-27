# 🎓 SiLearn Backend - E-learning Platform
Project completed as a part of an internship in an ItSilesia.

#### An E-learning platform which allows to create your own courses, learn from them as a student, in different spoken language. Main feature is an automatic code check, teacher can give various correct answers with different input which, check the student solution(code).

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)
![GitLab](https://img.shields.io/badge/GitLab-330F63?style=for-the-badge&logo=gitlab&logoColor=white)

## ✨ Key Features
- **Role-Based Access Control :**
- **👨‍🏫 Teachers:** Can create, edit, and delete courses, manage test scenarios, and manually review student submissions.
- **👨‍🎓 Students:** Can browse courses, learn from the provided materials, solve programming tasks, and track their progress.
- **Course & Task Management:** Full CRUD operations for educational content, allowing instructors to build their own course with structured learning paths and specific tasks.
- **Code Execution Engine :** Remote compilation of sent code, automated task verification of submitted code in various programming languages.
  - **Progress Tracking :** Real-time tracking of student results, task statuses, and manual grading integrations.
  - **Webhooks :** Asynchronous communication and event handling.
  - **Automated cleaning :** Scheduled tasks to secretly remove temporary files and free up resources after code compilation.

## 🏗️ Tech Stack
- **FrameWork :** Java, Spring Boot(backend), TypeScript, React(frontend)
- **DataBase :** PostgreSql
- **DataBase Migrations :** LiquiBase
- **Infrastructure :** Docker, Docker Compose
- **Tools :** Postman, DBeaver, Swagger UI

## 🚀 Getting Started

Follow these steps to set up the backend environment local.

1. **Clone the repository**
- git clone https://gitlab.gliwice.itsilesia.com/itsilesia-www/Trainings/2026/silearn/silearn-backend.git
- cd silearn-backend
2. **Set up Environment Variables :**
The project requires specific environment variables to run properly. 
You will find a template file named .env_template in the root directory.
Create your own .env file by copying the template:

\# Database Connection Components
- DB_HOST=localhost
- DB_PORT=5432
- DB_NAME=example

\# Database Credentials
- DB_USERNAME=user
- DB_PASSWORD=pass

\# liquibase
- LIQUIBASE_ENABLED=false

\# JWT (32+ characters long)
- JWT_SECRET=

\# JWT time = days + hours + minutes
- JWT_EXPIRATION_DAYS=1
- JWT_EXPIRATION_HOURS=0
- JWT_EXPIRATION_MINUTES=0

\# Online compiler
- ONLINE_COMPILER_KEY=
- ONLINE_COMPILER_URL=https://api.onlinecompiler.io
- CLEANUP_INTERVAL_MINUTES=20

**Bash :**
- cp .env_template .env
- ⚠️ Important open the newly created .env file in your text editor and fill the missing values. Pay special attention to the empty fields:
- **- JWT_SECRET :** Must be a secure, random string (at least 32 characters long).
- **- ONLINE_COMPILER_KEY :** You need to provide a valid API key for the online compiler service to enable code execution features.
- **- DB_ :** Default database credentials (user/pass) are provided, but ensure they match your local PostgreSql or Docker configuration.

**Start the Infrastructure (Docker) :**
- Run the PostgreSQL database and other required services using Docker Compose
- docker-compose up -d

**Run the application :**
Start the Spring Boot backend using Maven:
- ./mvnw spring-boot:run

3. **Make a LiquiBase migration :**
- initialize liquibase:diff -f pom.xml

