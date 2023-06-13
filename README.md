# it-events-api
This is my API for IT events management. Instruction how to run this application is below the technologies stack.
## Features
* User registration
* Password encryption using BCrypt
* Login with JWT authentication
* Role-based authorization with Spring Security
* Administrator panel
* Organizer panel
* Unit and integration tests (85% covered)
* OpenAPI and Swagger UI
## Description
There are three user roles (ADMIN, ORGANIZER, USER).
* Non-authenticated user can:
  * create a new account
  * login with email & password
  * display a list of upcoming events
  * display a list of past events
  * search for events by city
* User with ADMIN, ORGANIZER and USER role can:
  * display list of users
  * search for users
  * display profiles of other users
  * update his profile data
  * update his password
* User with ADMIN role can:
  * display a list of users with more details
  * search for users
  * update user account data (enable/disable, unblock/block, add/remove role)
  * update user profile data
  * update a user's password
  * delete a user
* User with ORGANIZER role can:
  * create an event
  * display a list of his events
  * search for his events by city
  * update his events
  * display list of event participants
  * remove user from list of event participants
* User with USER role can:
  * display event details
  * display a list of his upcoming and past events
  * join event
  * leave event
## Technologies:
* Spring Boot 2.7.12
* Java 17
* Spring Web
* Spring Security
* JSON Web Token (JWT)
* Spring Data JPA
* H2
* MySQL
* Liquibase
* Spring Validation
* Lombok
* JUnit
* Mockito
* AssertJ
* OpenAPI
## How to run:
1. Choose a profile
<br>
There are two profiles - dev and prod. The dev profile uses the H2 database and the prod profile uses the MySQL database. The dev profile also contains test data. The prod profile contains only one user with admin role. Select one of the two Spring profiles (dev or prod) by setting the SPRING_PROFILES_ACTIVE environment variable or otherwise. You must set these additional environment variables before running:
   * for dev profile:
     * DB_NAME
   * for prod profile:
     * DB_NAME
     * DB_USERNAME
     * DB_PASSWORD
2. Build the project: mvn clean install 
3. Run the project: mvn spring-boot:run

The application will be available at http://localhost:8080.
<br>
Swagger UI will be available at http://localhost:8080/swagger-ui.html.