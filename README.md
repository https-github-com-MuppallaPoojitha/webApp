# WebApp

This is a basic web app with user creation, user information update and get user information([API docs](https://app.swaggerhub.com/apis-docs/csye6225/spring2021/assignment-02)).

### Technology Stack

* Programming Language: Java 11
* Web Framework: Springboot 2.4.2
* Security: Spring
* Database: postgresql
* Project Management: Maven
* Plugins: Lombok
* Version Control: Git
* Test Tool: Postman
* IDE: IntelliJ

### Installation and Deployment

> Pre-requisites
> * Java 11
> * Postgresql
> * Maven

1. Clone this  git repository to your local machine.
2. <code>$ cd webapp</code>
3. <code>$ mvn clean install</code> to build the project.
4. <code>$ java -jar target/webappone-0.0.1-SNAPSHOT.jar</code> to execute the application.
5. Run the command to build the module:mvn clean Install



### Deployment

This application runs locally on http://localhost:8080/.

### Authentication

This web app uses [basic authentication](https://en.wikipedia.org/wiki/Basic_access_authentication).

### Testing

All test cases are written with Mockito and jUnit. They can be found under the '/src' folder. 

A complete API test script can be found on [here](https://www.postman.com/viobai/workspace/csye6225-webapp/documentation/14507754-930e1511-15dc-4cfb-88e4-a6ad19ae331f) with Postman.

### Application Endpoints
1.Register a User :http://localhost:8080/v1/user  (No auth required)
2.Get user Details: http://localhost:8080/v1/user/self   (Basic Auth Required)
3.Update User Details: http://localhost:8080/v1/user/self (Basic Auth Required)
4.Create a Book: http://localhost:8080/books  (Basic Auth required)
5.Get all Books: http://localhost:8080/books.(No AUTH REQUIRED)
6.Get a Book by id:  http://localhost:8080/books/{id} (No auth Required)
7.Delete a book: http://localhost:8080/books/{id}   (Basic Auth required)
