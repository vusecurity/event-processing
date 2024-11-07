# Event Processing Service (Complex Event Processing or CEP)

This service provides a layer on top of [EsperTech Esper runtime](https://www.espertech.com/esper) for complex event processing.

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Database Setup](#database-setup)
3. [Installation](#installation)
    - [Native Installation](#native-installation)
    - [Docker Installation](#docker-installation)
4. [Configuration](#configuration)
5. [Running the Service](#running-the-service)
6. [Verifying the Service](#verifying-the-service)
7. [API Documentation](#api-documentation)
8. [Developer Notes](#developer-notes)

## Prerequisites

- Java JDK (version specified in pom.xml)
- Maven
- Docker (for containerized deployment)
- Database (Oracle, Postgres, or SQLServer)

## Database Setup

1. Create a database named `fraudcep`.
2. Create a user named `cepuser`.
3. Execute the SQL scripts located in the [database](database) folder:
    - `create_database.sql`
    - `full_schema.sql`

> Supported databases: [Oracle](database/oracle), [Postgres](database/postgres), and [SQLServer](database/sqlserver)

## Installation

### Native Installation

1. Install EsperHA dependencies (if using EsperHA):

   If you are an employee of VU Inc:

   1. Clone the repository named `event-processing-bins`
   2. Follow the instructions provided in that repository to install EsperHA dependencies.

   If you are not an employee of VU Inc:

   You will need to obtain your own EsperHA runtime libraries. Please contact EsperTech or visit their website for information on how to acquire these libraries.


2. Build the project:
   ```bash
   mvn clean package -DskipTests
   ```

3. Verify and update the [application.yml](complex-event-processing/src/main/resources/application.yml) as needed.

### Docker Installation

1. Complete steps 1-3 of the Native Installation.
2. Build the Docker image:
   ```bash
   # Alpine
   docker build -t cep-local .

   # RedHat
   docker build -f Dockerfile-rh -t cep-local .
   ```

## Configuration

Configure the following parameters:

- `ESPERHA`: Enable/Disable EsperHA (default: `true`)
- `LOG_LEVEL`: OFF | ERROR | WARN | INFO | DEBUG | TRACE | ALL
- `MASTER_KEY_PASSWORD`: Secret key for decrypting values
- `PORT`: Service port
- `URL_DB`: Database connection URL
- `USER_DB`: Database username
- `PASSWORD_DB`: Database password
- `CONNECTION_POOL_DB`: Max database connections (default: 10)
- `IDLE_CONNECTION_DB`: Max idle connections

### Encryption
This project uses jasypt for property decryption. To encrypt a value:

```bash
java -jar vu-jasypt-encryption-tool.jar <password> <valueToEncrypt>
```

Replace `<password>` with the `MASTER_KEY_PASSWORD` value.

### HTTPS Configuration
To use HTTPS, add the `https` profile and configure these properties:

```yaml
server:
ssl:
key-store: ${SSL_KEY_STORE:classpath:keystore.p12}
key-store-password: ${SSL_KEY_STORE_PASSWORD}
keyStoreType: ${SSL_KEY_STORE_TYPE:PKCS12}
keyAlias: ${SSL_KEY_ALIAS:localhost}
```

## Running the Service

### Native

Windows:
```bash
start.bat [https] [master_key_password] [db_password]
```

Linux:
```bash
start.sh [https] [master_key_password] [db_password]
```

### Docker

```bash
docker run \\
-v espertech.license:/home/vu/license/espertech.license \\
--env SPRING_PROFILES_ACTIVE=raw-logger \\
--env PORT=9091 \\
--env URL_DB=jdbc:oracle:thin:@db-oracle:1521/databasename \\
--env DATABASE_DRIVER=org.oracle.Driver \\
--env MASTER_KEY_PASSWORD=secret \\
--env USER_DB=CEPUSER \\
--env PASSWORD_DB=development \\
--env IDLE_CONNECTION_DB=1 \\
--env ESPERHA=true \\
--env LOG_LEVEL=INFO \\
-p 9091:9091 \\
cep-local
```

## Verifying the Service

1. Check service health:
   ```
   GET http://localhost:9091/event-processing/actuator/health
   ```

2. Verify CEP engine version:
   ```
   GET http://localhost:9091/event-processing/version
   ```

3. Send a test request:
   ```
   POST http://localhost:9091/event-processing/cep/transaction
   ```
   (See README for sample request body)

## API Documentation

Access Swagger UI:
```
http://localhost:9091/event-processing/swagger-ui.html
```

## Developer Notes

### Raw Console Logger

To enable raw console output, set:
```
SPRING_PROFILES_ACTIVE=raw-logger
```

or override the spring property:
```
spring.profiles.active=raw-logger
```

## Configuring SonarQube for the Project

To run SonarQube analysis (`sonar:sonar`) as part of your Maven build, you need to configure the following environment variables:

- `SONAR_HOST_URL`: The URL of the SonarQube server.
- `SONAR_LOGIN`: The authentication token for the SonarQube server.

### Steps to Set Up SonarQube

1. **Set the environment variables:**

   - **On Linux/macOS**, add the following lines to your terminal session, or to your `~/.bashrc` or `~/.zshrc` file:
     ```bash
     export SONAR_HOST_URL=http://your-sonar-server-url
     export SONAR_LOGIN=your-sonar-authentication-token
     ```

   - **On Windows**, you can set these variables in the Command Prompt for the current session:
     ```cmd
     set SONAR_HOST_URL=http://your-sonar-server-url
     set SONAR_LOGIN=your-sonar-authentication-token
     ```

2. **Verify the configuration**:

   After setting the environment variables, you can verify that they are correctly configured by running:

   - **On Linux/macOS**:
     ```bash
     echo $SONAR_HOST_URL
     echo $SONAR_LOGIN
     ```

   - **On Windows**:
     ```cmd
     echo %SONAR_HOST_URL%
     echo %SONAR_LOGIN%
     ```

3. **Running the SonarQube Analysis**:

   Once the environment variables are configured, you can run the SonarQube analysis as part of your Maven build:
   ```bash
   ./mvnw clean verify sonar:sonar
