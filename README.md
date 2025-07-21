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
