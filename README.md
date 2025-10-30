# Microservice Employee Management System

This repository contains the complete codebase for a distributed **Employee Management System** built on a Spring Cloud microservice architecture.

It demonstrates modern enterprise patterns including **Service Discovery (Eureka)**, **Centralized Configuration (Config Server)**, **API Gateway (Spring Cloud Gateway)**, and **stateless authentication using JWT**.

## Project Overview

This system is divided into five distinct services, designed to be highly scalable, resilient, and decoupled. All services communicate internally using service names resolved via Eureka and are secured behind the API Gateway.

| Service | Port | Description | Technologies |
| :--- | :--- | :--- | :--- |
| **Config Server** | `8888` | Centralized external configuration management for all services. | Spring Cloud Config |
| **Discovery Service** | `8761` | Service registry and discovery (Eureka). Required for inter-service communication. | Spring Cloud Netflix Eureka |
| **API Gateway** | `9090` | Single entry point. Handles **JWT validation**, **security enforcement**, and **request routing**. | Spring Cloud Gateway |
| **Auth Service** | `8081` | Manages user registration, login, and issues **stateless JWTs** containing user roles. | Spring Security, JJWT, PostgreSQL |
| **Employee Service** | `8080` | Core HR logic (CRUD operations for employees and departments). Secured via **Role-Based Access Control (RBAC)** based on headers injected by the Gateway. | Spring Data JPA, PostgreSQL, Flyway |

---

## 🛠️ Prerequisites

Before running the services, you must have the following installed and running:

* **Java 17+**
* **Maven**
* **PostgreSQL Database**
* **Git**

### Database Setup (PostgreSQL)

The `auth-service` and `employee-service` rely on a shared PostgreSQL instance. You can run one easily using Docker:

```bash
docker run --name my-postgres -e POSTGRES_PASSWORD=admin123 -e POSTGRES_USER=postgres -e POSTGRES_DB=employee_db -p 5432:5432 -d postgres