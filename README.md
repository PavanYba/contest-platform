# Contest Platform

A Spring Boot-based competitive programming platform that allows users to participate in coding contests, submit solutions, and view leaderboards.

## 🚀 Features

- **Contest Management**: Create and manage programming contests with multiple problems
- **Code Submission**: Submit code solutions with automated judging
- **Leaderboards**: Real-time contest leaderboards and rankings
- **Problem Management**: Support for problems with test cases and descriptions
- **RESTful APIs**: Clean REST API design for frontend integration
- **Asynchronous Processing**: Background processing for code judging

## 🛠 Technology Stack

- **Backend**: Spring Boot 3.5.5
- **Java Version**: 21
- **Database**: MySQL
- **ORM**: Spring Data JPA with Hibernate
- **Build Tool**: Maven
- **Containerization**: Docker
- **Additional Libraries**:
  - Lombok for boilerplate code reduction
  - MySQL Connector for database connectivity

## 📋 Prerequisites

- Java 21 or higher
- Maven 3.6+
- MySQL 8.0+
- Docker

## 🚀 Getting Started

### Database Setup

1. Install and start MySQL server
2. Create a database named `contest_db` (or it will be created automatically)
3. Update database credentials in `application.properties` if needed

### Running the Application

#### Option 1: Using Maven
```bash
# Clone the repository
git clone <repository-url>
cd contest-platform

# Run the application
./mvnw spring-boot:run
```

#### Option 2: Using Docker
```bash
# Build and run with Docker
docker build -f docker/Dockerfile -t contest-platform .
docker run -p 8080:8080 contest-platform
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

### Contest APIs

#### Get Contest Details
```http
GET /api/contests/{contestId}
```
Returns contest information including associated problems.

**Response:**
```json
{
  "id": 1,
  "name": "Contest Name",
  "description": "Contest description",
  "problems": [...]
}
```

#### Get Contest Leaderboard
```http
GET /api/contests/{contestId}/leaderboard
```
Returns the current leaderboard for the specified contest.

### Submission APIs

#### Submit Code
```http
POST /api/submissions
Content-Type: application/json

{
  "problemId": 1,
  "userId": 1,
  "code": "solution code here",
  "language": "java"
}
```

#### Get Submission Status
```http
GET /api/submissions/{submissionId}
```
Returns the status and result of a code submission.

## 🗄 Database Schema

### Core Entities

- **Contest**: Represents a programming contest
- **Problem**: Individual problems within contests
- **User**: Platform users/participants
- **Submission**: Code submissions by users for problems

### Entity Relationships

- Contest → Problems (One-to-Many)
- Problem → Contest (Many-to-One)
- User → Submissions (One-to-Many)
- Problem → Submissions (One-to-Many)

## 🏗 Project Structure

```
src/main/java/com/shodh/contestplatform/
├── ContestPlatformApplication.java     # Main application class
├── config/
│   ├── AsyncConfig.java               # Async processing configuration
│   └── DataInitializer.java           # Initial data setup
├── controller/
│   ├── ContestController.java         # Contest-related endpoints
│   └── SubmissionController.java      # Submission-related endpoints
├── dto/
│   ├── ContestResponse.java           # Contest response DTO
│   ├── LeaderboardEntry.java          # Leaderboard entry DTO
│   ├── ProblemDto.java               # Problem data transfer object
│   ├── SubmissionRequest.java         # Submission request DTO
│   └── SubmissionResponse.java        # Submission response DTO
├── model/
│   ├── Contest.java                   # Contest entity
│   ├── Problem.java                   # Problem entity
│   ├── Submission.java               # Submission entity
│   └── User.java                     # User entity
├── repository/
│   ├── ContestRepository.java         # Contest data access
│   ├── ProblemRepository.java         # Problem data access
│   ├── SubmissionRepository.java      # Submission data access
│   └── UserRepository.java           # User data access
└── service/
    ├── ContestService.java           # Contest business logic
    ├── JudgeService.java             # Code judging logic
    └── SubmissionService.java        # Submission processing
```

## ⚙ Configuration

### Application Properties

Key configuration options in `application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/contest_db
spring.datasource.username=root
spring.datasource.password=your_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Server Configuration
server.port=8080
```

### CORS Configuration

The application is configured to accept cross-origin requests from any origin (`*`). For production, update this to specific domains.

## 🧪 Testing

Run the test suite:
```bash
./mvnw test
```

## 🚀 Deployment

### Production Considerations

1. **Security**: Update CORS configuration for production
2. **Database**: Use connection pooling for better performance
3. **Environment Variables**: Externalize sensitive configuration
4. **Logging**: Configure appropriate logging levels
5. **Health Checks**: Implement health check endpoints