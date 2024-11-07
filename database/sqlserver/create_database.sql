CREATE LOGIN cepuser WITH PASSWORD = 'development#1';
GO
USE master;
GO
CREATE USER cepuser FOR LOGIN cepuser;
GO
CREATE DATABASE fraudcep;
ALTER AUTHORIZATION ON DATABASE::fraudcep TO cepuser;
GO