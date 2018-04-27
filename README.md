# AlignWebsiteProject - Private

## 1.This is the back-end service APIs for student. It contains the APIs, data access objects and data models for the student. APIs are defined in package org.mehaexample.asdDemo.alignWebsite. Data access objects are defined in org.mehaexample.asdDemo.dao.alignprivate. Data models are defined in package org.mehaexample.asdDemo.model.alignprivate. 
## 2.Private database has following tables: StudentLogins, Students, PriorEducations, WorkExperiences, Electives, ExtraExperiences, Projects, Privacies, Photos, Courses.
## 3.Some APIs depend on Admin or Public objects. We leave these objects in private repo.

## Database Access

The database connection in this project is using Hibernate query language using
C3P0 as the connection pool. The connection has been protected with TLS layer. To
access the connection settings, you can look at the resources sections and look at
the cfg.xml file. There are currently 4 cfg xml in the student side to access the 
test databases and original databases for the private and public databases.
The username to access the databases for student is "backend_student", and the password
is "password".

## Database UML Diagram

For the higher level For all 2 schemas (private and public), you can look at the 
ALIGN diagram pdf files in this repositories. For more details of the Database construction, 
you can look inside the sql_scripts folder for the SQL Scripts to create the Databases.

## Student Facing APIs Documentation: