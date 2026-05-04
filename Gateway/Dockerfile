# STAGE 1: Compilation (Build)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Optimisation : on télécharge les dépendances d'abord
COPY pom.xml .
RUN mvn dependency:go-offline

# Compilation du binaire de la Gateway
COPY src ./src
RUN mvn clean package -DskipTests

# STAGE 2: Image de Production (Runtime)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# On ne récupère que le JAR exécutable
COPY --from=build /app/target/*.jar app.jar

# La Gateway écoute sur le port 8080 (Port d'entrée standard)
EXPOSE 8080

# Exécution avec optimisation de la mémoire pour un conteneur
ENTRYPOINT ["java", "-jar", "app.jar"]