# Étape 1 : Build (Utilise Maven pour compiler)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# 1. On copie d'abord le pom.xml pour mettre en cache les dépendances Maven
COPY pom.xml .

# 2. IMPORTANT : On copie le dossier api-specs AVANT la compilation
COPY api-specs ./api-specs

# 3. On copie le code source
COPY src ./src

# 4. On compile et on génère le code
RUN mvn clean package -DskipTests

# Étape 2 : Runtime (Image finale légère pour l'exécution)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# On récupère uniquement le JAR généré à l'étape précédente
COPY --from=build /app/target/*.jar app.jar

# Port exposé (doit correspondre à ton server.port dans application.properties)
EXPOSE 8081

# Commande de lancement
ENTRYPOINT ["java", "-jar", "app.jar"]