JWKProvider
===========

Overview
--------

JWKProvider is a Spring Boot application designed for managing JSON Web Keys (JWK) and generating JWT tokens. It uses Nimbus JOSE+JWT library for cryptographic operations and JSONDB for storing key information. The application provides REST endpoints for interacting with JWKs and generating JWT tokens.

Features
--------

*   **JWK Management**: Generate and retrieve JSON Web Keys.
*   **JWT Generation**: Create signed JWT tokens with custom claims and expiration times.
*   **REST API**: Endpoints for managing JWKs and generating JWTs.
*   **JSONDB Integration**: Lightweight, file-based storage for persisting keys.

Prerequisites
-------------

*   Java 21 or higher
*   Maven
*   Docker (if you want to use Docker for deployment)

Project Structure
-----------------

*   **`src`**: Contains the application source code.
*   **`docker-compose.yml`**: Docker Compose file for containerized deployment.
*   **`pom.xml`**: Maven build configuration file.

Configuration
-------------

Configuration properties for JSONDB can be set in `application.properties`:

*   `jwk.stub.jsondb.filepath`: Path where JSONDB stores the data file.
*   `jwk.stub.jsondb.packageToScan`: Package to scan for JSONDB entities.

API Endpoints
-------------

### Generate JWT

*   **Endpoint**: `POST /jwt/{kid}`
*   **Request Body**:

```json
{
 "duration": "3000",   
 "claims": 
    {
     "key1": "value1",     
     "key2": "value2"   
    } 
}
```

*   **Response**:

```json
{
  "base64": "encodedJWTString",   
  "claimSet": 
  {     
    "key1": "value1",     
    "key2": "value2"
  } 
}
```


### Retrieve JWK Set

*   **Endpoint**: `GET /jwkset`
*   **Response**: JSON representation of the JWK set.

### Generate JWK

*   **Endpoint**: `POST /jwkset`
*   **Response**: Public JWK JSON representation.

Building the Project
--------------------

To build the project, run:

bash

Copy code

`./mvnw clean install`

Running the Application
-----------------------

You can run the application using Docker Compose:

`docker-compose up`

Or directly using Maven:

`./mvnw spring-boot:run`

* * *
