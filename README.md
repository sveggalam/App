# App

### For knowing and killing tasks running on port
netstat -aon | findstr :8082
taskkill /PID <PID> /F

### For creating student table in test db
CREATE TABLE student (
    id INTEGER PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    age INTEGER CHECK (age >= 1),
    course VARCHAR(100) NOT NULL,
    marks INTEGER NOT NULL
);

### For creating library table in test db
CREATE TABLE library (
    id INTEGER PRIMARY KEY,
    course VARCHAR(100) NOT NULL
);

### For creating mess table in test db
CREATE TABLE mess (
    id INTEGER PRIMARY KEY,
    food_type VARCHAR(10) CHECK (food_type IN ('veg', 'nonveg'))
);












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
