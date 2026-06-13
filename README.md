# Verdant Flow - Service Server

Welcome to the **Verdant Flow** service server repository. This project serves as the backbone for the Verdant Flow ecosystem, providing robust microservices to handle application logic and hardware orchestration.

## Project Overview

Verdant Flow is a smart system focused on environmental management and automation. This repository contains the backend services that facilitate data flow from sensors to the end-user application and back to control units.

## Repository Structure

The backend is composed of the following services:

- **`application-service/`**: This service manages the core application logic, user data, and serves the main API for client applications.
- **`microcontroller-service/`**: This service acts as the gateway for hardware communication, processing incoming sensor data and dispatching commands to microcontrollers.

## Prerequisites

- **Java Development Kit (JDK) 17** or newer.
- **Connectivity** to any required external databases or message brokers (check service-specific configurations).

## Getting Started

### Installation

Each service is built using Maven. You can use the provided Maven Wrapper (`mvnw` or `mvnw.cmd`) to ensure a consistent build environment.

1. Clone the repository.
2. Navigate to the desired service directory.

### Building the Services

```bash
# Example for application-service
cd application-service
./mvnw clean install
```

### Running the Services

```bash
./mvnw spring-boot:run
```

## Development

- **Build Tool**: Apache Maven
- **Framework**: Spring Boot
- **Language**: Java

## Configuration

Configuration for each service is managed via `src/main/resources/application.properties` or `application.yml` files within their respective directories. Ensure that environment-specific variables are properly set before deployment.