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

## Student Facing has the following APIs:
![1](https://user-images.githubusercontent.com/22485201/39385774-ae0826a0-4a26-11e8-9183-8ffc8fb11778.PNG)
![2](https://user-images.githubusercontent.com/22485201/39385775-ae269752-4a26-11e8-801a-e3b1b247dfa7.PNG)
![3](https://user-images.githubusercontent.com/22485201/39385776-ae543acc-4a26-11e8-9843-f8f9a30c10f0.PNG)
![4](https://user-images.githubusercontent.com/22485201/39385777-ae706e18-4a26-11e8-999a-4839045953a7.PNG)
![5](https://user-images.githubusercontent.com/22485201/39385778-ae8e5ca2-4a26-11e8-83c4-52224cfcab36.PNG)
![6](https://user-images.githubusercontent.com/22485201/39385779-aea8a288-4a26-11e8-9541-3f5ed6624196.PNG)
![7](https://user-images.githubusercontent.com/22485201/39385780-aecea8c0-4a26-11e8-8c0e-aa6edc3cb1a1.PNG)
![8](https://user-images.githubusercontent.com/22485201/39385781-aeeea210-4a26-11e8-9d6b-115540d76ffd.PNG)
![9](https://user-images.githubusercontent.com/22485201/39385783-af1f9d7a-4a26-11e8-9613-ef68d8634a32.PNG)
![10](https://user-images.githubusercontent.com/22485201/39385785-af51a504-4a26-11e8-95e3-6ffed7b2b4a6.PNG)
![11](https://user-images.githubusercontent.com/22485201/39385786-af6c8fcc-4a26-11e8-91b1-903feb074a78.PNG)
![12](https://user-images.githubusercontent.com/22485201/39385787-af8a1ad8-4a26-11e8-97ce-343fb136c61f.PNG)
![13](https://user-images.githubusercontent.com/22485201/39385788-afad1a60-4a26-11e8-92ae-d7f714ae4c8a.PNG)
![14](https://user-images.githubusercontent.com/22485201/39385789-afcf199e-4a26-11e8-8c2a-e6acca229bd4.PNG)
![15](https://user-images.githubusercontent.com/22485201/39385790-b017d7c4-4a26-11e8-90d6-a5fe0c942531.PNG)
![16](https://user-images.githubusercontent.com/22485201/39385791-b062de18-4a26-11e8-8b42-0b799d8ff447.PNG)
![17](https://user-images.githubusercontent.com/22485201/39385792-b09e4f0c-4a26-11e8-87b6-5ccc7f32025e.PNG)
![18](https://user-images.githubusercontent.com/22485201/39385793-b10f0e2c-4a26-11e8-92a0-ee4e796fdab0.PNG)
