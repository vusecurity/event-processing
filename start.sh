#!/bin/bash

# Check if required parameters are provided
if [ $# -lt 2 ]; then
    echo "Usage: $0 <master_key_password> <db_password> [https]"
    exit 1
fi

export ESPERHA=false
export LOG_LEVEL=INFO
export MASTER_KEY_PASSWORD="$1"
export PASSWORD_DB="$2"
export PORT=9091
export SPRING_PROFILES_ACTIVE=raw-logger
export SSL_KEY_STORE_PASSWORD=Devel0pment
export URL_DB='jdbc:oracle:thin:@//localhost:1521/ORACLE'
export USER_DB=CEPUSER

# Check for the https argument (now as third parameter)
if [ "$3" = "https" ]; then
    export SPRING_PROFILES_ACTIVE="${SPRING_PROFILES_ACTIVE},https"
fi

echo "SPRING_PROFILES_ACTIVE is set to: $SPRING_PROFILES_ACTIVE"

java -jar ./complex-event-processing-1.2.6.jar
