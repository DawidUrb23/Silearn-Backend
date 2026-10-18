# 🎓 SiLearn Backend - E-learning Platform
Project completed as a part of an internship at itSilesia.

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)
![GitLab](https://img.shields.io/badge/GitLab-330F63?style=for-the-badge&logo=gitlab&logoColor=white)

The core API for the SiLearn educational platform. The system provides a comprehensive environment for both learning and teaching programming, featuring role-based access, course management, and an automated code execution engine.

## ✨ Key Features

- **Role-Based Access Control (RBAC):**
  - **👨‍🏫 Teachers:** Can create, edit, and delete courses, manage test scenarios, and manually review student submissions.
  - **👨‍🎓 Students:** Can browse courses, learn from the provided materials, solve programming tasks, and track their progress.
- **Course & Task Management:** Full CRUD operations for educational content, allowing instructors to build their own courses with structured learning paths and specific tasks.
- **Code Execution Engine:** Remote compilation of sent code, automated task verification of submitted code in various programming languages.
- **Progress Tracking:** Real-time tracking of student results, task statuses, and manual grading integrations.
- **Webhooks:** Asynchronous communication and event handling.
- **Automated Cleaning:** Scheduled tasks to securely remove temporary files and free up resources after code compilation.

## 🏗️ Tech Stack

- **Framework:** Java, Spring Boot
- **Database:** PostgreSQL
- **Database Migrations:** Liquibase
- **Infrastructure:** Docker, Docker Compose
- **Tools:** Postman, DBeaver, Swagger UI

## 🚀 Getting Started

Follow these steps to set up the backend environment locally.

1. **Clone the repository:**
   ```bash
   git clone [https://gitlab.gliwice.itsilesia.com/itsilesia-www/Trainings/2026/silearn/silearn-backend.git](https://gitlab.gliwice.itsilesia.com/itsilesia-www/Trainings/2026/silearn/silearn-backend.git)
   cd silearn-backend
