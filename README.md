# quizzapp

A small Spring Boot quiz application that exposes endpoints to create quizzes, retrieve quiz questions, submit answers,
and evaluate submissions. This project uses Spring Data JPA (MySQL) and simple DTO-based REST endpoints.

---

## Quick overview

- Java: 21
- Build: Maven
- DB: MySQL (jdbc:mysql://localhost:3306/quizzapp by default)
- Main packages:
    - `com.poyraz.controller` — REST controllers
    - `com.poyraz.service` — service interfaces and implementations
    - `com.poyraz.entity` — JPA entities (Quiz, Question, Answer, Submission)
    - `com.poyraz.dto` — request/response DTOs (including `SubmissionDTO` and `SubmissionResultDTO`)

---

## Requirements

- JDK 21
- Maven 3.6+
- MySQL server (or compatible DB) accessible from the running app

---

## Configuration / Environment

The application reads DB credentials from `application.properties` and environment variables. Example properties in
`src/main/resources/application.properties`:

- `spring.datasource.url` — JDBC URL (default: `jdbc:mysql://localhost:3306/quizzapp`)
- `spring.datasource.username` — can be set via `MYSQL_USERNAME` env var
- `spring.datasource.password` — can be set via `MYSQL_PASSWORD` env var

```cmd
set MYSQL_USERNAME=your_db_user
set MYSQL_PASSWORD=your_db_password
```

Do not commit secrets. Prefer configuring via environment variables or an `.env` file that is not shared publicly.

---

## Running the application (local)

Start the app with Maven:

```cmd
set MYSQL_USERNAME=your_db_user
set MYSQL_PASSWORD=your_db_password
mvn spring-boot:run
```

Or run the generated JAR:

```cmd
mvn package
java -jar target/quizzapp-0.0.1-SNAPSHOT.jar
```

The API listens on port 8080 by default.

---

## Docker

This project is dockerized. A minimal image is defined in `Dockerfile`:

```dockerfile
FROM eclipse-temurin:21-jre-alpine
WORKDIR /usr/app
COPY target/quizzapp-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Build and run the image locally

1) Package the app:

```cmd
mvn package
```

2) Build the image:

```cmd
docker build -t quizzapp:local .
```

3) Run the container (requires a reachable MySQL and optional Kafka env):

```cmd
docker run --rm -p 8080:8080 ^
  -e MYSQL_USERNAME=your_db_user ^
  -e MYSQL_PASSWORD=your_db_password ^
  quizzapp:local
```

If your DB is not on `localhost`, set `SPRING_DATASOURCE_URL` accordingly:

```cmd
docker run --rm -p 8080:8080 ^
  -e SPRING_DATASOURCE_URL="jdbc:mysql://host:3306/quizzapp" ^
  -e MYSQL_USERNAME=your_db_user ^
  -e MYSQL_PASSWORD=your_db_password ^
  quizzapp:local
```

---

## Docker Compose

A compose file (`docker-compose.yml`) is provided to run the application together with MySQL and Kafka:

```yaml
services:
  application:
    image: poyrazemun/quizzapp:latest
    ports:
      - "8080:8080"
    environment:
      - MYSQL_USERNAME=${DB_USERNAME}
      - MYSQL_PASSWORD=${DB_PASSWORD}
      - SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka-server:9092
    depends_on:
      mysqldb:
        condition: service_healthy
      kafka-server:
        condition: service_started

  mysqldb:
    image: mysql:8.0
    environment:
      - MYSQL_ROOT_PASSWORD=${DB_ROOT_PASS}
      - MYSQL_DATABASE=quizzapp
      - MYSQL_USER=${DB_USERNAME}
      - MYSQL_PASSWORD=${DB_PASSWORD}
    ports:
      - "3307:3306"

  kafka-server:
    image: apache/kafka:4.0.1
    ports:
      - "9092:9092"
```

Note: the stack uses a shared external bridge network `quizzapp-mysql-network`. Ensure it exists before bringing the
stack up:

```cmd
docker network create quizzapp-mysql-network
```

### Environment variables

Compose reads credentials from `.env` at the repo root. Do not store real passwords in documentation.
Use the following variable names in your private `.env` file:

- `DB_ROOT_PASS`
- `DB_USERNAME`
- `DB_PASSWORD`

Example (placeholder values only — replace locally, do not share):

```dotenv
DB_ROOT_PASS=***
DB_USERNAME=***
DB_PASSWORD=***
```

You can edit your local `.env` to match your setup. Keep this file private.

### Start the stack

```cmd
docker compose up -d
```

- Application: http://localhost:8080
- MySQL: localhost:3307 (mapped to container 3306)
- Kafka broker: localhost:9092 (container name: `kafka-server`)

### Logs and troubleshooting

- Check container logs:

```cmd
docker compose logs -f application
```

- Verify MySQL health:

```cmd
docker compose ps
```

If `mysqldb` is unhealthy, confirm port 3307 is free and your `.env` variables are set correctly.

- If you create quizzes via API, the app persists to MySQL `quizzapp` database inside the container.

---

## API endpoints

1) Create a quiz

- Method: POST
- URL: `/quiz/create`
- Query params:
    - `category` (string, e.g. `JAVA` or other valid Category enum)
    - `noOfQuestions` (positive int)
    - `quizName` (non-empty string)
- Example:

```
POST /quiz/create?category=JAVA&noOfQuestions=3&quizName=MyQuiz
```

- Response: 201 CREATED with a message containing the created quiz id.


1) Get quiz questions

- Method: GET
- URL: `/quiz/{id}`
- Path param: id (quiz id)
- Response: 200 OK with a JSON array of questions (QuestionWrapperDTO) containing question text and options. Example:

```json
[
  {
    "id": 1,
    "questionText": "...",
    "option1": "A",
    "option2": "B",
    "option3": "C",
    "option4": "D"
  }
]
```

1) Submit quiz (single submitter name + answers)

- Method: POST
- URL: `/quiz/submit/{id}`
- Path param: id (quiz id)
- Request body: `SubmissionDTO` — a JSON object with `submitterName` and `answers` (list of `AnswerDTO`)

Example request body (submission.json):

```json
{
  "submitterName": "Alice",
  "answers": [
    {
      "questionId": 101,
      "userAnswer": "B"
    },
    {
      "questionId": 102,
      "userAnswer": "D"
    },
    {
      "questionId": 103,
      "userAnswer": "A"
    }
  ]
}
```

Example curl (Windows cmd.exe):

```cmd
curl -H "Content-Type: application/json" -d @submission.json http://localhost:8080/quiz/submit/1
```

Example response (successful evaluation):

```json
{
  "correctAnswers": 2,
  "wrongAnswers": 1,
  "totalQuestions": 3,
  "percentage": 66.66666666666667
}
```

Notes about submission validation:

- The API validates that the set of submitted question IDs exactly matches the quiz's question IDs. If they differ, the
  submission is rejected.
- `submitterName` is provided once in the `SubmissionDTO` (the server groups answers under a single `Submission`
  record).

---

## Database model (short)

- `quiz` — quiz header (id, quiz_name, category, question_count, created_at)
- `question` — question data (id, question_text, options, right_answer, category, difficulty)
- `answer` — individual submitted answers (id, question_id, user_answer, submission_id)
- `submission` — groups answers for a single submission (id, submitter_name, quiz_id, submitted_at)

---

