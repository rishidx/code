# File-Upload-Convert-Download
This project contains code to enable "file - upload - download" from spring boot application.

Files get uploaded as multipart and get store in in-memory database as byte array. The same is retrieve and process for download.

Access
------

Once the application is up, it will run on default port 8080. Access the services:
* Upload file: http://localhost:8080/file/upload
	Postman code looks like
	
```
POST /file/upload HTTP/1.1
Host: localhost:8080
Cache-Control: no-cache
Postman-Token: 38d966dd-b677-658a-098d-32e4cf877b98
Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW

------WebKitFormBoundary7MA4YWxkTrZu0gW
Content-Disposition: form-data; name="file"; filename="Employee.docx"
Content-Type: application/vnd.openxmlformats-officedocument.wordprocessingml.document
------WebKitFormBoundary7MA4YWxkTrZu0gW--
```
![File Upload SnapShot](https://github.com/rishidx/code/blob/master/file-upload-convert-download/postman-upload.PNG)

* download file: http://localhost:8080/file/download/1 OR http://localhost:8080/file/download?id=1

Build
=====
To build this project run the command: `mvn clean package` or `mvn clean install` in this directory

Run
====
To run spring boot module run the command: `mvn spring-boot:run` in this directory

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.7.RELEASE/maven-plugin/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.7.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.2.7.RELEASE/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Save blob object in database](https://www.viralpatel.net/tutorial-save-get-blob-object-spring-3-mvc-hibernate/)
* [Content-Disposition](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Disposition)
* [Uploading and Downloading Files with Spring Boot](https://www.devglan.com/spring-boot/spring-boot-file-upload-download)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

