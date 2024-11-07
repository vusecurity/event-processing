-- Create user
CREATE USER cepuser IDENTIFIED BY development
  DEFAULT TABLESPACE users
  TEMPORARY TABLESPACE temp;

-- Grant basic privileges
GRANT CONNECT, RESOURCE TO cepuser;

ALTER USER cepuser QUOTA unlimited ON USERS;

EXIT;
