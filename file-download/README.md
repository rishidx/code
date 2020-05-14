# File-Download
This project contains code to enable "file download" from spring boot application.

Sample .docx and .pdf files are kept inside resources folder. Once application startup, these files are processed and store in H2 in-memory database.

Access
------

Once the application is up, it will run on default port 8080. Access the services and download files by either hitting one of these url
* [docx file --> http://localhost:8080/file/1 OR http://localhost:8080/file?id=1]
* [pdf file --> http://localhost:8080/file/2 OR http://localhost:8080/file?id=2] 

Build
=====
To build this project run the command: 'mvn clean package' or 'mvn clean install' in this directory

Run
====
To run spring boot module run the command: 'mvn spring-boot:run' in this directory

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.7.RELEASE/maven-plugin/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.7.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.2.7.RELEASE/reference/htmlsingle/#boot-features-jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

