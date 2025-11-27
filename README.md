# 📘 slUniversity -- Full Stack University Management System

*Backend:* Spring Boot + MySQL\
*Frontend:* Next.js\
*Infra:* Docker · Docker Compose · GitHub Actions · AWS EC2

------------------------------------------------------------------------

## ⭐ Overview

slUniversity is a full-stack university management system built with:

-   *Spring Boot (Backend REST API)*
-   *Next.js (Frontend UI)*
-   *MySQL Database*
-   *Docker & Docker Compose*
-   *GitHub Actions CI/CD*
-   *AWS EC2 for Deployment*

This README explains *how to run locally with Docker*, **how CI/CD
works*, and **how to deploy to AWS EC2*.

------------------------------------------------------------------------

# 📂 Project Structure

    slUniversity/
    ├── slUniversityBackend/
    │   ├── src/
    │   ├── pom.xml
    │   └── Dockerfile
    ├── slUniversityFrontend/
    │   ├── pages/
    │   ├── package.json
    │   └── Dockerfile
    └── docker-compose.yml

------------------------------------------------------------------------

# 🐳 Docker Setup

## ⿡ Backend Dockerfile

 dockerfile
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]


## ⿢ Frontend Dockerfile

 dockerfile
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build
FROM node:20-alpine AS runner
WORKDIR /app
ENV NODE_ENV=production
COPY --from=build /app/package*.json ./
COPY --from=build /app/.next ./.next
COPY --from=build /app/public ./public
RUN npm install --omit=dev
EXPOSE 3000
CMD ["npm", "start"]


------------------------------------------------------------------------

# 🛠 Local Development (Using Docker Compose)

 yaml
version: "3.9"
services:
  mysql:
    image: mysql:8.0
    container_name: sluni-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: sluniversity
      MYSQL_USER: sluser
      MYSQL_PASSWORD: slpass
    ports: ["3306:3306"]
    volumes: [db_data:/var/lib/mysql]
    networks: [sluni-net]

  backend:
    build: ./slUniversityBackend
    container_name: sluni-backend
    depends_on: [mysql]
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/sluniversity
      SPRING_DATASOURCE_USERNAME: sluser
      SPRING_DATASOURCE_PASSWORD: slpass
      SPRING_PROFILES_ACTIVE: prod
    ports: ["8080:8080"]
    networks: [sluni-net]

  frontend:
    build: ./slUniversityFrontend
    container_name: sluni-frontend
    depends_on: [backend]
    environment:
      NEXT_PUBLIC_API_BASE_URL: http://localhost:8080
    ports: ["3000:3000"]
    networks: [sluni-net]

networks:
  sluni-net:
volumes:
  db_data:


Run locally:

    docker compose up -d

------------------------------------------------------------------------

# 🔄 CI/CD With GitHub Actions

Workflow file:

 yaml
name: CI/CD
on:
  push:
    branches: [main]
jobs:
  build-and-push:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - run: cd slUniversityBackend && docker build -t ${{ secrets.DOCKERHUB_USERNAME }}/sl-university-backend:latest .
      - run: cd slUniversityFrontend && docker build -t ${{ secrets.DOCKERHUB_USERNAME }}/sl-university-frontend:latest .
      - uses: docker/login-action@v3
        with:
          username: ${{ secrets.DOCKERHUB_USERNAME }}
          password: ${{ secrets.DOCKERHUB_TOKEN }}
      - run: |
          docker push ${{ secrets.DOCKERHUB_USERNAME }}/sl-university-backend:latest
          docker push ${{ secrets.DOCKERHUB_USERNAME }}/sl-university-frontend:latest


------------------------------------------------------------------------

# ☁ AWS EC2 Deployment

Install Docker:

    sudo apt update
    sudo apt install -y docker.io docker-compose-plugin
    sudo usermod -aG docker ubuntu

Production compose:

 yaml
version: "3.9"
services:
  mysql:
    image: mysql:8.0
    container_name: sluni-mysql
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: sluniversity
      MYSQL_USER: sluser
      MYSQL_PASSWORD: slpass
    volumes: [db_data:/var/lib/mysql]

  backend:
    image: akila/sl-university-backend:latest
    container_name: sluni-backend
    restart: always
    depends_on: [mysql]
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/sluniversity
      SPRING_DATASOURCE_USERNAME: sluser
      SPRING_DATASOURCE_PASSWORD: slpass
    ports: ["8080:8080"]

  frontend:
    image: akila/sl-university-frontend:latest
    container_name: sluni-frontend
    restart: always
    depends_on: [backend]
    environment:
      NEXT_PUBLIC_API_BASE_URL: http://<EC2-IP>:8080
    ports: ["80:3000"]

volumes:
  db_data:


Deploy:

    docker compose -f docker-compose.prod.yml pull
    docker compose -f docker-compose.prod.yml up -d

------------------------------------------------------------------------

# 🎯 Summary

✔ Fully dockerized backend & frontend\
✔ Local compose environment\
✔ GitHub Actions CI/CD\
✔ EC2 deployment ready
