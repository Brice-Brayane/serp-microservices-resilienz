# SERP Microservice System - Masterarbeit Case Study

Dieses Projekt demonstriert eine resiliente Microservice-Architektur für ein SERP-System.

## Verwendete Muster (Patterns)
- **API Gateway**: Zentrales Routing und Sicherheit.
- **Database per Service**: Isolierte PostgreSQL-Datenbanken.
- **Resilience4j**: Implementierung von Circuit Breaker, Retry und Fallback.

## Voraussetzungen
- Docker und Docker Compose
- Java 17 und Maven (zum Bauen)

## Starten der Anwendung
1. Projekt bauen:
   `mvn clean package -DskipTests`
2. Microservices mit Docker starten:
   `docker compose up -d --build`
