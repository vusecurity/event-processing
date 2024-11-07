 CREATE USER "cepuser" WITH
         LOGIN
         NOSUPERUSER
         NOCREATEDB
         NOCREATEROLE
         INHERIT
         NOREPLICATION
         CONNECTION LIMIT -1
         PASSWORD 'development';

 CREATE DATABASE "fraudcep" WITH OWNER = "cepuser";