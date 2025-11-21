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

---

## Running the application

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

