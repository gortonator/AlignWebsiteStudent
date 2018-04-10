-- Before running, create the schema named AlignAdmin.
-- Or just create schema if necessary.
-- Create this schema after private schema.
CREATE SCHEMA IF NOT EXISTS AlignAdmin;
USE AlignAdmin;

DROP TABLE IF EXISTS AdminLogins;
DROP TABLE IF EXISTS AdministratorNotes;
DROP TABLE IF EXISTS Electives;
DROP TABLE IF EXISTS Administrators;

CREATE TABLE Electives (
	ElectiveId INT AUTO_INCREMENT,
    NeuId VARCHAR(16),
    CourseId VARCHAR(10),
    CourseName VARCHAR(255),
    CourseTerm ENUM('SPRING', 'SUMMER', 'FALL'),
    CourseYear INT NOT NULL,
    Retake BOOLEAN DEFAULT FALSE,
	Gpa VARCHAR(2) NOT NULL,
    Plagiarism BOOLEAN DEFAULT FALSE,
    CONSTRAINT pk_Electives_ElectiveId
		PRIMARY KEY (ElectiveId),
	CONSTRAINT uq_Electives_Elective
		UNIQUE (NeuId, CourseId, CourseTerm, CourseYear)
);

CREATE TABLE Administrators (
	AdministratorNeuId VARCHAR(16),
    Email VARCHAR(255) NOT NULL,
    FirstName VARCHAR(25) NOT NULL,
    MiddleName VARCHAR(25),
    LastName VARCHAR(25) NOT NULL,
    CONSTRAINT pk_Administrators_AdministratorNeuId
		PRIMARY KEY (AdministratorNeuId),
	CONSTRAINT uq_Administrators_AdministratorEmail
		UNIQUE (Email)
);

CREATE TABLE AdminLogins(
	Email VARCHAR(255) NOT NULL,
    AdminPassword VARCHAR(1000) NOT NULL,
    RegistrationKey VARCHAR(255),
    LoginTime TIMESTAMP,
    KeyExpiration TIMESTAMP,
    Confirmed BOOLEAN DEFAULT FALSE,
    CONSTRAINT pk_AdminLogins_Email
		PRIMARY KEY (Email),
	CONSTRAINT fk_AdminLogins_Email
		FOREIGN KEY (Email)
        REFERENCES Administrators(Email)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE AdministratorNotes (
	AdministratorNoteId INT AUTO_INCREMENT,
    NeuId VARCHAR(16) NOT NULL,
    AdministratorNeuId VARCHAR(16),
    Title VARCHAR(255),
    Description VARCHAR(1000),
    CONSTRAINT pk_AdministratorNotes_AdministratorNoteId
		PRIMARY KEY (AdministratorNoteId),
	CONSTRAINT fk_AdministratorNotes_AdministratorNeuId
		FOREIGN KEY (AdministratorNeuId)
        REFERENCES Administrators(AdministratorNeuId)
        ON UPDATE CASCADE ON DELETE SET NULL
);
