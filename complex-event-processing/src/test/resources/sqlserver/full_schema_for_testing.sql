/*
========================================================================
Version     : 1.2.6
Type        : FULL SCHEMA FOR AUTOMATED TESTS
DB          : MSSQL
========================================================================
*/

CREATE SEQUENCE dbo.rule_id_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE dbo.statement_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE dbo.rule_event (
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

CREATE TABLE dbo.rule_event_statement (
                                          id int not null,
                                          context bit not null,
                                          statement varchar(2000),
                                          rule_id int not null,
                                          primary key (id),
                                          foreign key (rule_id) references dbo.rule_event (id)
);

/*
========================================================================
ESPERHA 8.6.0
========================================================================
*/

CREATE TABLE EHA_SSR_DATA (
                              resourcekey varbinary(128),
                              payload varbinary(MAX),
                              vls numeric(32)
);
CREATE UNIQUE INDEX EHA_SSR_DATA_idx1 ON EHA_SSR_DATA(resourcekey);

CREATE TABLE EHA_SSR_MGMT (
                              inckey int identity(1,1) primary key,
                              prefix smallint,
                              resourcekey varbinary(128),
                              payload varbinary(MAX)
);
CREATE UNIQUE INDEX EHA_SSR_MGMT_idx1 ON EHA_SSR_MGMT(prefix, resourcekey);

CREATE TABLE EHA_SSR_EVENT (
                               pageid numeric(32),
                               payload varbinary(MAX)
);
CREATE UNIQUE INDEX EHA_SSR_EVENT_idx1 ON EHA_SSR_EVENT(pageid);

CREATE TABLE EHA_SSR_CHECKPOINT (
                                    pageid numeric(32),
                                    enginetime numeric(32),
                                    systemtime numeric(32),
                                    lastcompacted_enginetime numeric(32),
                                    lastcompacted_systemtime numeric(32),
                                    payload varbinary(MAX)
);
CREATE UNIQUE INDEX EHA_SSR_CHECKPOINT_idx1 ON EHA_SSR_CHECKPOINT(pageid);

CREATE TABLE EHA_SSR_REF (
                             pageid numeric(32),
                             create_enginetime numeric(32),
                             create_systemtime numeric(32),
                             lastupd_enginetime numeric(32),
                             lastupd_systemtime numeric(32),
                             payload varbinary(MAX)
);
CREATE UNIQUE INDEX EHA_SSR_REF_idx1 ON EHA_SSR_REF(pageid);

CREATE TABLE EHA_SSR_SYSTEM (
    payload varbinary(MAX)
);