-- Before running, create the schema named AlignPrivate.
-- Or just create schema if necessary.
-- Create this schema before admin schema.
CREATE SCHEMA IF NOT EXISTS AlignPrivate;
USE AlignPrivate;

DROP TABLE IF EXISTS StudentLogins;
DROP TABLE IF EXISTS PriorEducations;
DROP TABLE IF EXISTS WorkExperiences;
DROP TABLE IF EXISTS Electives;
DROP TABLE IF EXISTS ExtraExperiences;
DROP TABLE IF EXISTS Projects;
DROP TABLE IF EXISTS Privacies;
DROP TABLE IF EXISTS Photos;
DROP TABLE IF EXISTS Students;
DROP TABLE IF EXISTS Courses;

CREATE TABLE Students (
	NeuId VARCHAR(16) NOT NULL,
    PublicId INT AUTO_INCREMENT,
    Email VARCHAR(255) NOT NULL,
    FirstName VARCHAR(25) NOT NULL,
    MiddleName VARCHAR(25),
    LastName VARCHAR(25) NOT NULL,
    Gender ENUM ('M', 'F') NOT NULL,
    Scholarship BOOLEAN DEFAULT FALSE,
    Visa VARCHAR(5),
    Phone VARCHAR(25),
	  Address VARCHAR(255),
    State VARCHAR(2),
    City VARCHAR(20),
    Zip VARCHAR(5),
    EntryTerm ENUM('SPRING', 'SUMMER', 'FALL'),
    EntryYear INT NOT NULL,
    ExpectedLastTerm ENUM('SPRING', 'SUMMER', 'FALL'),
    ExpectedLastYear INT NOT NULL,
    EnrollmentStatus ENUM('FULL_TIME', 'PART_TIME', 'GRADUATED', 'INACTIVE', 'DROPPED_OUT') NOT NULL,
    Campus ENUM('BOSTON', 'CHARLOTTE', 'SEATTLE', 'SILICON_VALLEY') NOT NULL,
    DegreeCandidacy ENUM('ASSOCIATE', 'BACHELORS', 'MASTERS', 'PHD') NOT NULL,
    Photo BLOB,
    Visible BOOLEAN DEFAULT TRUE,
    Linkedin VARCHAR(50),
    Facebook VARCHAR(50),
    Github VARCHAR(50),
    Website VARCHAR(50),
    Skills VARCHAR(255),
    Summary VARCHAR(255),
    CONSTRAINT pk_Students_NeuId
		PRIMARY KEY (NeuId),
	KEY PublicId (PublicId),
	CONSTRAINT uq_Students_Email
		UNIQUE (Email)
);

CREATE TABLE StudentLogins(
	Email VARCHAR(255) NOT NULL,
    StudentPassword VARCHAR(1000) NOT NULL,
    RegistrationKey VARCHAR(255),
    LoginTime TIMESTAMP,
    KeyExpiration TIMESTAMP,
    Confirmed BOOLEAN DEFAULT FALSE,
    CONSTRAINT pk_StudentLogins_Email
		PRIMARY KEY (Email),
	CONSTRAINT fk_StudentLogins_Email
		FOREIGN KEY (Email)
        REFERENCES Students(Email)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE PriorEducations (
	PriorEducationId INT AUTO_INCREMENT,
    NeuId VARCHAR(16),
    InstitutionName VARCHAR(100),
    MajorName VARCHAR(255),
    GraduationDate DATE NOT NULL,
	Gpa FLOAT(4, 2) DEFAULT 0.00,
    DegreeCandidacy ENUM('ASSOCIATE', 'BACHELORS', 'MASTERS', 'PHD') NOT NULL,
    CONSTRAINT pk_PriorEducations_PriorEducationId
		PRIMARY KEY (PriorEducationId),
	CONSTRAINT uq_PriorEducations_PriorEducation
		UNIQUE (NeuId, InstitutionName, MajorName, DegreeCandidacy),
	CONSTRAINT fk_PriorEducations_NeuId
		FOREIGN KEY (NeuId)
		REFERENCES Students(NeuId)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Courses (
	CourseId VARCHAR(10) NOT NULL,
    CourseName VARCHAR(255),
    Description VARCHAR(1250),
    CONSTRAINT pk_Courses_CourseId
		PRIMARY KEY (CourseId)
);

CREATE TABLE Electives (
	ElectiveId INT AUTO_INCREMENT,
    NeuId VARCHAR(16),
    CourseId VARCHAR(10),
    CourseName VARCHAR(255),
    CourseTerm ENUM('SPRING', 'SUMMER', 'FALL'),
    CourseYear INT NOT NULL,
    CONSTRAINT pk_Electives_ElectiveId
		PRIMARY KEY (ElectiveId),
	CONSTRAINT uq_Electives_Elective
		UNIQUE (NeuId, CourseId, CourseTerm, CourseYear),
	CONSTRAINT fk_Electives_NeuId
		FOREIGN KEY (NeuId)
		REFERENCES Students(NeuId)
        ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT fk_Electives_CourseId
		FOREIGN KEY (CourseId)
		REFERENCES Courses(CourseId)
        ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE WorkExperiences (
	WorkExperienceId INT AUTO_INCREMENT,
    NeuId VARCHAR(16),
    CompanyName VARCHAR(50),
    StartDate DATE NOT NULL,
    EndDate DATE,
    CurrentJob BOOLEAN,
    isCoop BOOLEAN,
    Title VARCHAR(255) NOT NULL,
    Description VARCHAR(1250),
    CONSTRAINT pk_WorkExperiences_WorkExperienceId
		PRIMARY KEY (WorkExperienceId),
	CONSTRAINT uq_WorkExperiences_WorkExperience
		UNIQUE (NeuId, CompanyName, StartDate),
	CONSTRAINT fk_WorkExperiences_NeuId
		FOREIGN KEY (NeuId)
        REFERENCES Students(NeuId)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE ExtraExperiences (
	ExtraExperienceId INT AUTO_INCREMENT,
    NeuId VARCHAR(16),
    CompanyName VARCHAR(50),
    StartDate DATE NOT NULL,
    EndDate DATE,
    Title VARCHAR(255) NOT NULL,
    Description VARCHAR(1250),
    CONSTRAINT pk_ExtraExperiences_ExtraExperienceId
		PRIMARY KEY (ExtraExperienceId),
	CONSTRAINT uq_ExtraExperiences_ExtraExperience
		UNIQUE (NeuId, CompanyName, StartDate),
	CONSTRAINT fk_ExtraExperiences_NeuId
		FOREIGN KEY (NeuId)
        REFERENCES Students(NeuId)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Projects (
	ProjectId INT AUTO_INCREMENT,
    NeuId VARCHAR(16),
    ProjectName VARCHAR(50),
    StartDate DATE NOT NULL,
    EndDate DATE,
    Description VARCHAR(1250),
    CONSTRAINT pk_Projects_ProjectId
		PRIMARY KEY (ProjectId),
	CONSTRAINT uq_Projects_Project
		UNIQUE (NeuId, ProjectName, StartDate),
	CONSTRAINT fk_Projects_NeuId
		FOREIGN KEY (NeuId)
        REFERENCES Students(NeuId)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Privacies (
	NeuId VARCHAR(16),
    PublicId INT NOT NULL,
    VisibleToPublic BOOLEAN DEFAULT TRUE,
    Photo BOOLEAN DEFAULT FALSE,
    Coop BOOLEAN DEFAULT FALSE,
    Phone BOOLEAN DEFAULT FALSE,
    Email BOOLEAN DEFAULT FALSE,
    Address BOOLEAN DEFAULT FALSE,
    Linkedin BOOLEAN DEFAULT FALSE,
    Github BOOLEAN DEFAULT FALSE,
    Facebook BOOLEAN DEFAULT FALSE,
    Website BOOLEAN DEFAULT FALSE,
    Course BOOLEAN DEFAULT FALSE,
    ExtraExperience BOOLEAN DEFAULT FALSE,
    Project BOOLEAN DEFAULT FALSE,
    Skill BOOLEAN DEFAULT FALSE,
    CONSTRAINT pk_Privacies_NeuId
		PRIMARY KEY (NeuId),
	CONSTRAINT fk_Privacies_PublicId
		FOREIGN KEY (PublicId)
        REFERENCES Students(PublicId)
        ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT fk_Privacies_NeuId
		FOREIGN KEY (NeuId)
        REFERENCES Students(NeuId)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Photos (
	NeuId VARCHAR(16),
    Photo MEDIUMBLOB,
    CONSTRAINT pk_Photos_NeuId
		PRIMARY KEY (NeuId),
	CONSTRAINT fk_Photos_NeuId
		FOREIGN KEY (NeuId)
        REFERENCES Students(NeuId)
        ON UPDATE CASCADE ON DELETE CASCADE
);