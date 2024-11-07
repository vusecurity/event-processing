@echo off

:: Accept parameters
set "ESPERHA=false"
set "LOG_LEVEL=INFO"
set "PORT=9091"

:: Get passwords from command line arguments
set "MASTER_KEY_PASSWORD=%2"
set "PASSWORD_DB=%3"

:: Default spring profiles
set "SPRING_PROFILES_ACTIVE=raw-logger"

:: Check for HTTPS argument
set "ARG1=%1"
set "HTTPS_ENABLED=false"

echo Argument 1: %ARG1%

IF "%ARG1%"=="https" (
    set "HTTPS_ENABLED=true"
)

IF "%HTTPS_ENABLED%"=="true" (
    set "SPRING_PROFILES_ACTIVE=%SPRING_PROFILES_ACTIVE%,https"
)

echo SPRING_PROFILES_ACTIVE: %SPRING_PROFILES_ACTIVE%

set "SSL_KEY_STORE_PASSWORD=Devel0pment"
set "URL_DB=jdbc:oracle:thin:@//localhost:1521/ORACLE"
set "USER_DB=CEPUSER"

:: Validate required parameters
IF "%MASTER_KEY_PASSWORD%"=="" (
    echo Error: Master key password is required
    echo Usage: start.bat [https] [master_key_password] [db_password]
    exit /b 1
)

IF "%PASSWORD_DB%"=="" (
    echo Error: Database password is required
    echo Usage: start.bat [https] [master_key_password] [db_password]
    exit /b 1
)

:: Start the application
java -jar ./complex-event-processing/target/complex-event-processing-1.2.6.jar