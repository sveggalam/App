# 📦 Microservices Architecture -- Student Management System

A fully containerized microservices-based system built using:

-   Spring Boot
-   Spring Cloud Gateway
-   Spring Data JPA
-   PostgreSQL
-   Docker & Docker Compose

This project demonstrates:

-   Microservices architecture
-   Database per service pattern
-   API Gateway pattern
-   Inter-service REST communication
-   Environment-based configuration
-   Dockerized deployment

------------------------------------------------------------------------

# 🏗 System Architecture

## High-Level Overview

Client → API Gateway (8080)

Routes: - /students/\*\* → Student Service (8081) - /library/\*\* →
Library Service (8082) - /mess/\*\* → Mess Service (8083)

Each service owns its own PostgreSQL database.

------------------------------------------------------------------------

# 🧱 Services Overview

  Service           Port   Database
  ----------------- ------ ------------
  API Gateway       8080   ---
  Student Service   8081   student-db
  Library Service   8082   library-db
  Mess Service      8083   mess-db

------------------------------------------------------------------------

# 🧩 Student Service (8081)

## Endpoints

POST /students/insert\
GET /students/allStudents\
GET /students/{id}\
GET /students/marks-range?min=&max=\
GET /students/firstname?name=\
GET /students/lastname?name=\
GET /students/topper

## Internal Layers

Controller → Service → Repository → student-db

### Inter-Service Call

On student creation:

StudentService → WebClient → POST /library/register

------------------------------------------------------------------------

# 🧩 Library Service (8082)

## Endpoints

POST /library/register\
GET /library/allStudents

Internal Layers:

Controller → Service → Repository → library-db

------------------------------------------------------------------------

# 🧩 Mess Service (8083)

## Endpoints

POST /mess/insert\
GET /mess/allStudents

Internal Layers:

Controller → Service → Repository → mess-db

### Inter-Service Call

MessService → WebClient → GET /students/{id}

If student exists → save\
Else → 404

------------------------------------------------------------------------

# 🗄 Database Per Service Pattern

  Service   Database    Table
  --------- ----------- ---------
  Student   studentdb   student
  Library   librarydb   library
  Mess      messdb      mess

No cross-database joins allowed.

------------------------------------------------------------------------

# 🐳 Running the System

## Start

docker compose up --build

## Check Containers

docker compose ps

------------------------------------------------------------------------

# 🧪 Sample API Test

Insert Student:

curl -X POST http://localhost:8080/students/insert -H "Content-Type:
application/json" -d
'{"id":1,"firstName":"John","lastName":"Doe","age":22,"course":"Java","marks":90}'

Verify Library Auto-Registration:

curl http://localhost:8080/library/allStudents

------------------------------------------------------------------------

# 📊 PlantUML -- Component Diagram

``` plantuml
@startuml
actor Client

Client --> "API Gateway (8080)"

"API Gateway (8080)" --> "Student Service (8081)"
"API Gateway (8080)" --> "Library Service (8082)"
"API Gateway (8080)" --> "Mess Service (8083)"

"Student Service (8081)" --> "student-db"
"Library Service (8082)" --> "library-db"
"Mess Service (8083)" --> "mess-db"

"Student Service (8081)" --> "Library Service (8082)" : POST /library/register
"Mess Service (8083)" --> "Student Service (8081)" : GET /students/{id}
@enduml
```

------------------------------------------------------------------------

# 📊 PlantUML -- Sequence Diagram (Student Insert)

``` plantuml
@startuml
actor Client

Client -> Gateway : POST /students/insert
Gateway -> Student : Forward Request
Student -> student-db : Save Student
Student -> Library : POST /library/register
Library -> library-db : Save Entry
Library --> Student : 200 OK
Student --> Gateway : 200 OK
Gateway --> Client : Response
@enduml
```

------------------------------------------------------------------------

# 📊 PlantUML -- Sequence Diagram (Mess Registration)

``` plantuml
@startuml
actor Client

Client -> Gateway : POST /mess/insert
Gateway -> Mess : Forward Request
Mess -> Student : GET /students/{id}
Student -> student-db : Fetch Student
Student --> Mess : Student Data
Mess -> mess-db : Save Entry
Mess --> Gateway : 201 Created
Gateway --> Client : Response
@enduml
```

------------------------------------------------------------------------

# 🏛 Architectural Patterns Used

-   Microservices Architecture
-   API Gateway Pattern
-   Database Per Service Pattern
-   Layered Architecture
-   REST-based Synchronous Communication
-   Containerized Deployment

------------------------------------------------------------------------

# ✅ Status

✔ Fully Dockerized\
✔ Inter-service communication verified\
✔ Database isolation implemented\
✔ Production-style architecture











CREATE TABLE book (
    id SERIAL PRIMARY KEY,
    title VARCHAR(100),
    author VARCHAR(100)
);

CREATE TABLE course_books (
    course VARCHAR(100) PRIMARY KEY
);

CREATE TABLE course_books_books (
    course_books_course VARCHAR(100),
    books_id INTEGER,
    FOREIGN KEY (course_books_course) REFERENCES course_books(course),
    FOREIGN KEY (books_id) REFERENCES book(id)
);


CREATE TABLE student_library (
    student_id INTEGER PRIMARY KEY,
    course VARCHAR(100)
);

CREATE TABLE student_library_books (
    student_library_student_id INTEGER,
    books_id INTEGER,
    FOREIGN KEY (student_library_student_id) REFERENCES student_library(student_id),
    FOREIGN KEY (books_id) REFERENCES book(id)
);
