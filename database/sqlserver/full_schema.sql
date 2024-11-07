/*
========================================================================
Version     : 1.2.6
Type        : FULL SCHEMA
DB          : MSSQL
========================================================================
*/

DECLARE @schema_db varchar(50) = 'dbo';

DECLARE @sql nvarchar(max);

IF (@schema_db <> 'dbo' AND NOT EXISTS (SELECT 1 FROM sys.schemas WHERE name = @schema_db))
BEGIN
    SET @sql = N'CREATE SCHEMA ' + QUOTENAME(@schema_db);
EXEC sp_executesql @sql;
END

SET @sql = N'CREATE SEQUENCE ' + QUOTENAME(@schema_db) + N'.rule_id_seq START WITH 1 INCREMENT BY 1;';
EXEC sp_executesql @sql;

SET @sql = N'CREATE SEQUENCE ' + QUOTENAME(@schema_db) + N'.statement_id_seq START WITH 1 INCREMENT BY 1;';
EXEC sp_executesql @sql;

SET @sql = N'
    CREATE TABLE ' + QUOTENAME(@schema_db) + N'.rule_event (
        id int not null,
        enabled bit not null,
        id_action int not null,
        id_channel int not null,
        id_critical_level int not null,
        id_operation_type int not null,
        id_rule_type int not null,
        json_format varchar(4000),
        last_update_date datetime2,
        name varchar(255),
        score int not null,
        primary key (id)
    );
';
EXEC sp_executesql @sql;

SET @sql = N'
    CREATE TABLE ' + QUOTENAME(@schema_db) + N'.rule_event_statement (
        id int not null,
        context bit not null,
        statement varchar(2000),
        rule_id int not null,
        primary key (id),
        foreign key (rule_id) references ' + QUOTENAME(@schema_db) + N'.rule_event (id)
    );
';
EXEC sp_executesql @sql;


/*
========================================================================
ESPERHA 8.6.0
========================================================================
*/

create table EHA_SSR_DATA (resourcekey varbinary(128), payload varbinary(MAX), vls numeric(32));
create unique index EHA_SSR_DATA_idx1 on EHA_SSR_DATA(resourcekey);

create table EHA_SSR_MGMT (inckey int identity(1,1) primary key, prefix smallint, resourcekey varbinary(128), payload varbinary(MAX));
create unique index EHA_SSR_MGMT_idx1 on EHA_SSR_MGMT(prefix, resourcekey);

create table EHA_SSR_EVENT (pageid numeric(32), payload varbinary(MAX));
create unique index EHA_SSR_EVENT_idx1 on EHA_SSR_EVENT(pageid);

create table EHA_SSR_CHECKPOINT (pageid numeric(32), enginetime numeric(32), systemtime numeric(32), lastcompacted_enginetime numeric(32), lastcompacted_systemtime numeric(32), payload varbinary(MAX));
create unique index EHA_SSR_CHECKPOINT_idx1 on EHA_SSR_CHECKPOINT(pageid);

create table EHA_SSR_REF (pageid numeric(32), create_enginetime numeric(32), create_systemtime numeric(32), lastupd_enginetime numeric(32), lastupd_systemtime numeric(32), payload varbinary(MAX));
create unique index EHA_SSR_REF_idx1 on EHA_SSR_REF(pageid);

create table EHA_SSR_SYSTEM (payload varbinary(MAX));