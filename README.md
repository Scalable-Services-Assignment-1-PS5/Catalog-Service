# Catalog Service

Event and venue catalog management microservice for the Event Ticketing System.

## Overview

The Catalog Service manages events and venues. It provides APIs for browsing events, searching by various criteria, and retrieving venue information. All catalog data is publicly viewable but modifications require authentication.

## Tech Stack

### Core Framework

- **Java**: 17
- **Spring Boot**: 3.2.0
- **Build Tool**: Maven 3.9

### Database

- **MySQL Connector** - Database connectivity
- **Hibernate** - ORM with DDL auto-update

### Security & Documentation

- **JWT (jjwt)**: 0.12.3 - Token validation
- **SpringDoc OpenAPI**: 2.2.0 - API documentation (Swagger UI)

## Build & Run

### Prerequisites

- Java 17 or higher
- Maven 3.9+
- MySQL database (local or remote)

### 1. Build with Maven

```bash
# Clean and build
mvn clean package

# Skip tests (if needed)
mvn clean package -DskipTests
```

The JAR file will be created at: `target/catalog-service-1.0.0.jar`

### 2. Run Locally

```bash
# Run with Java
java -jar target/catalog-service-1.0.0.jar

# Or use Maven
mvn spring-boot:run
```

### 3. Build & Run with Docker

```bash
# Build Docker image
docker build -t catalog-service:1.0.0 .

# Run container
docker run -d \
  -p 8082:8082 \
  -e DB_URL="jdbc:mysql://your-db-host:3306/catalog_service_db" \
  -e DB_USERNAME="your-username" \
  -e DB_PASSWORD="your-password" \
  -e JWT_SECRET="your-secret-key-at-least-32-characters-long" \
  --name catalog-service \
  catalog-service:1.0.0
```

### 4. Run with Docker Compose

```bash
# From project root
docker-compose up catalog-service
```

## Configuration

### Environment Variables

| Variable      | Description                   | Default             |
| ------------- | ----------------------------- | ------------------- |
| `DB_URL`      | MySQL database connection URL | See application.yml |
| `DB_USERNAME` | Database username             | `avnadmin`          |
| `DB_PASSWORD` | Database password             | -                   |
| `JWT_SECRET`  | Secret key for JWT validation | Required            |
| `SERVER_PORT` | Application port              | `8082`              |

### application.yml

```yaml
spring:
  application:
    name: catalog-service
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
server:
  port: 8082
jwt:
  secret: ${JWT_SECRET}
```

## Exposed APIs

### Base URL

- **Local**: `http://localhost:8082`
- **Container**: `http://catalog-service:8082`

### Authentication

All endpoints require JWT authentication via `Authorization: Bearer <token>` header.

---

### 1. **Get All Events**

```http
GET /api/v1/events
```

**Headers:**

- `Authorization: Bearer <JWT_TOKEN>`

**Query Parameters:**

- `city` (optional): Filter by city (e.g., `Pune`, `Mumbai`)
- `type` (optional): Filter by event type (e.g., `CONCERT`, `SPORTS`)
- `status` (optional): Filter by status (e.g., `ON_SALE`, `SOLD_OUT`)

**Response (200 OK):**

```json
[
  {
    "id": 1,
    "title": "Rock Concert 2025",
    "description": "Amazing rock concert",
    "eventType": "Concert",
    "type": "CONCERT",
    "venueId": 1,
    "venueName": "Madison Square Garden",
    "eventDate": "2025-12-31T20:00:00",
    "basePrice": 2500.00,
    "status": "ON_SALE",
    "totalSeats": 500,
    "availableSeats": 450
  }
]
```

**cURL Examples:**

```bash
# Get all events
curl -X GET http://localhost:8082/api/v1/events \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Filter by city
curl -X GET "http://localhost:8082/api/v1/events?city=Pune" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Filter by type
curl -X GET "http://localhost:8082/api/v1/events?type=CONCERT" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Filter by status
curl -X GET "http://localhost:8082/api/v1/events?status=ON_SALE" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

### 2. **Get Event by ID**

```http
GET /api/v1/events/{id}
```

**Headers:**

- `Authorization: Bearer <JWT_TOKEN>`

**Response (200 OK):**

```json
{
  "id": 1,
  "title": "Rock Concert 2025",
  "description": "Amazing rock concert",
  "eventType": "Concert",
  "type": "CONCERT",
  "venueId": 1,
  "venueName": "Madison Square Garden",
  "eventDate": "2025-12-31T20:00:00",
  "basePrice": 2500.00,
  "status": "ON_SALE",
  "totalSeats": 500,
  "availableSeats": 450
}
```

**cURL Example:**

```bash
curl -X GET http://localhost:8082/api/v1/events/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

### 3. **Create Event**

```http
POST /api/v1/events
```

**Headers:**

- `Authorization: Bearer <JWT_TOKEN>`
- `Content-Type: application/json`

**Request Body:**

```json
{
  "title": "Summer Music Festival",
  "description": "Three-day music festival",
  "venueId": 1,
  "eventDate": "2025-07-15T18:00:00",
  "type": "FESTIVAL",
  "basePrice": 3000.00,
  "totalSeats": 1000
}
```

**Response (200 OK):**

```json
{
  "id": 2,
  "title": "Summer Music Festival",
  "description": "Three-day music festival",
  "eventType": "Festival",
  "type": "FESTIVAL",
  "venueId": 1,
  "venueName": "Madison Square Garden",
  "eventDate": "2025-07-15T18:00:00",
  "basePrice": 3000.00,
  "status": "ON_SALE",
  "totalSeats": 1000,
  "availableSeats": 1000
}
```

**cURL Example:**

```bash
curl -X POST http://localhost:8082/api/v1/events \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Summer Music Festival",
    "description": "Three-day music festival",
    "venueId": 1,
    "eventDate": "2025-07-15T18:00:00",
    "type": "FESTIVAL",
    "basePrice": 3000.00,
    "totalSeats": 1000
  }'
```

---

### 4. **Get All Venues**

```http
GET /api/v1/venues
```

**Headers:**

- `Authorization: Bearer <JWT_TOKEN>`

**Response (200 OK):**

```json
[
  {
    "id": 1,
    "name": "Madison Square Garden",
    "city": "New York",
    "state": "NY",
    "address": "4 Pennsylvania Plaza, New York, NY 10001",
    "capacity": 20000
  }
]
```

**cURL Example:**

```bash
curl -X GET http://localhost:8082/api/v1/venues \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

### 5. **Get Venue by ID**

```http
GET /api/v1/venues/{id}
```

**Headers:**

- `Authorization: Bearer <JWT_TOKEN>`

**Response (200 OK):**

```json
{
  "id": 1,
  "name": "Madison Square Garden",
  "city": "New York",
  "state": "NY",
  "address": "4 Pennsylvania Plaza, New York, NY 10001",
  "capacity": 20000
}
```

**cURL Example:**

```bash
curl -X GET http://localhost:8082/api/v1/venues/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

### 6. **Health Check** (No Auth Required)

```http
GET /actuator/health
```

**Response (200 OK):**

```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP"
    }
  }
}
```

**cURL Example:**

```bash
curl http://localhost:8082/actuator/health
```

---

## Event Types

| Type       | Description                  |
| ---------- | ---------------------------- |
| `CONCERT`  | Music concerts               |
| `PLAY`     | Theater performances         |
| `SPORTS`   | Sporting events              |
| `COMEDY`   | Stand-up comedy shows        |
| `FESTIVAL` | Multi-day festivals          |
| `OTHER`    | Other event types            |

## Event Status

| Status      | Description                      |
| ----------- | -------------------------------- |
| `ON_SALE`   | Tickets available for purchase   |
| `SOLD_OUT`  | All tickets sold                 |
| `CANCELLED` | Event cancelled                  |

## Security Features

- **JWT Authentication**: All endpoints require valid JWT tokens
- **Role-based Access**: Event creation restricted to admins
- **Validation**: Request body validation using Bean Validation

## API Documentation

### Swagger UI

Access interactive API documentation at:

```
http://localhost:8082/swagger-ui.html
```

### OpenAPI Specification

Get the OpenAPI JSON specification at:

```
http://localhost:8082/v3/api-docs
```

## Database Schema

### Events Table

```sql
CREATE TABLE events (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  event_type VARCHAR(50) NOT NULL,
  type ENUM('CONCERT','PLAY','SPORTS','COMEDY','FESTIVAL','OTHER') NOT NULL,
  venue_id BIGINT NOT NULL,
  event_date DATETIME NOT NULL,
  base_price DECIMAL(10,2) NOT NULL,
  status ENUM('ON_SALE','SOLD_OUT','CANCELLED') NOT NULL,
  total_seats INT,
  available_seats INT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (venue_id) REFERENCES venues(id),
  INDEX idx_event_date (event_date),
  INDEX idx_status (status)
);
```

### Venues Table

```sql
CREATE TABLE venues (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  city VARCHAR(100) NOT NULL,
  state VARCHAR(50) NOT NULL,
  address TEXT NOT NULL,
  capacity INT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_city (city)
);
```

## Monitoring

### Health Checks

- **Endpoint**: `/actuator/health`
- **Interval**: 30 seconds (in Docker)
- **Checks**: Database connectivity, disk space, ping

### Logs

Application logs include:

- Event creation/updates
- Search queries and filters
- Venue management operations

## Quick Start Example

```bash
# 1. Build the service
mvn clean package

# 2. Run the service
java -jar target/catalog-service-1.0.0.jar

# 3. Get JWT token from user service
TOKEN=$(curl -s -X POST http://localhost:8081/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password123"}' | jq -r '.token')

# 4. Browse all events
curl -X GET http://localhost:8082/api/v1/events \
  -H "Authorization: Bearer $TOKEN" | jq '.'

# 5. Get events on sale
curl -X GET "http://localhost:8082/api/v1/events?status=ON_SALE" \
  -H "Authorization: Bearer $TOKEN" | jq '.'

# 6. Get all venues
curl -X GET http://localhost:8082/api/v1/venues \
  -H "Authorization: Bearer $TOKEN" | jq '.'
```

## Related Services

- **User Service** (8081): Provides authentication tokens
- **Seating Service** (8083): Manages seats for events
- **Order Service** (8084): Creates orders for events
- **Payment Service** (8085): Processes event ticket payments

---

**Version**: 1.0.0  
**Port**: 8082

