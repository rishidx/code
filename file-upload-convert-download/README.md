# File-Download
This project contains code to enable "file upload - download" from spring boot application.

Files get uploaded as multi-part and get store in in-memory database as byte array. The same is retrieve and process for download.

File Conversion
---------------

Currently, converting files from docx to pdf format. This code uses 3 APIs
* Aspose: It is NOT open-source. APIs distributed is just for trial and testing purpose. Buy license to use full features of this API.
* opensagres: Third party library uses Apache POI and iText. May face some problem on converting docx file with images and tables.
* docx4j: Uses https://converter-eval.plutext.com/ to convert file. 


Access
------

Once the application is up, it will run on default port 8080. Access the services:
* Upload file: http://localhost:8080/file/upload
	
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

* download file: http://localhost:8080/file/download/{id} OR http://localhost:8080/file/download?id={id}

* convert and download file:
Upload docx file using upload api and then perform following task
 - Aspose : http://localhost:8080/file/convert/aspose?id={id}
 - opensagres: http://localhost:8080/file/convert/opensagres?id={id}
 - docx4j: http://localhost:8080/file/convert/docx4j?id={id}

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
* [PDF-Converter-Java](https://github.com/plutext/PDF-Converter-Java)
* [Aspose.Words Java for docx4j](https://docs.aspose.com/display/wordsjava/Convert+Document+to+PDF)
* [XWPFDocument 2 PDF](https://github.com/opensagres/xdocreport/wiki/XWPFConverterPDFViaIText)

