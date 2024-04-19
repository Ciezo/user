## user-service-api

### Project Introduction
This project uses `Spring Boot` to create and facilitate `User` entities to which are represented as consumers (end-users)
of the application. A `User` entity can be defined by their common attributes such as the following: 

1. First name
2. Last name
3. Birthday
4. Email
5. Username
6. Password

These attributes are just common examples to which an end-user of an application can be defined, and these values
are normally parsed from a `Form` when a user submits them, say, through a web page or a designated form from a 
desktop application.

---

### Dependencies
- Spring Boot JPA
- Spring Security
- Spring Boot Starter Web
- MySQL JDBC Driver
- Lombok
- JWTs

`pom.xml`
```
<!-- MySQL JDBC Driver -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
 
<!-- Spring Boot JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- Spring Boot Starter Web -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<!-- JWTs -->
<dependency>
	<groupId>io.jsonwebtoken</groupId>
	<artifactId>jjwt-api</artifactId>
	<version>0.11.5</version>
</dependency>
<dependency>
	<groupId>io.jsonwebtoken</groupId>
	<artifactId>jjwt-impl</artifactId>
	<version>0.11.5</version>
</dependency>
<dependency>
	<groupId>io.jsonwebtoken</groupId>
	<artifactId>jjwt-jackson</artifactId>
	<version>0.11.5</version>
</dependency>
```

### How to run?
1. Clone the repository
2. Open terminal then, `mvn clean install`
3. To run, use, `mvn spring-boot:run`