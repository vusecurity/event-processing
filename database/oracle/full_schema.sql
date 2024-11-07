/*========================================================================
Version     : 1.2.6
Type        : FULL
DB          : ORACLE
========================================================================*/

create sequence RULE_ID_SEQ;
create sequence STATEMENT_ID_SEQ;

create table RULE_EVENT
(
    ID                NUMBER(10) not null
        primary key,
    ENABLED           NUMBER(1)  not null,
    ID_ACTION         NUMBER(10) not null,
    ID_CHANNEL        NUMBER(10) not null,
    ID_CRITICAL_LEVEL NUMBER(10) not null,
    ID_OPERATION_TYPE NUMBER(10) not null,
    ID_RULE_TYPE      NUMBER(10) not null,
    JSON_FORMAT       VARCHAR2(4000 char),
    LAST_UPDATE_DATE  TIMESTAMP(6),
    NAME              VARCHAR2(255 char),
    SCORE             NUMBER(10) not null
);

create table RULE_EVENT_STATEMENT
(
    ID        NUMBER(10) not null
        primary key,
    CONTEXT   NUMBER(1)  not null,
    STATEMENT VARCHAR2(2000 char),
    RULE_ID   NUMBER(10) not null
        constraint FK_STATEMENT_RULE
        references RULE_EVENT
);


/*========================================================================
ESPERHA
========================================================================*/


create table EHA_SSR_DATA (resourcekey raw(128), payload blob, vls numeric(32));
create unique index EHA_SSR_DATA_idx1 on EHA_SSR_DATA(resourcekey);

create table EHA_SSR_MGMT (inckey number primary key, prefix smallint, resourcekey raw(128), payload blob);
create unique index EHA_SSR_MGMT_idx1 on EHA_SSR_MGMT(prefix, resourcekey);
create sequence EHA_SSR_MGMT_SEQ;
create trigger EHA_SSR_MGMT_TRG_INCKEY
    before insert on EHA_SSR_MGMT
    for each row
begin
    select EHA_SSR_MGMT_SEQ.nextval
    into :new.inckey
    from dual;
end;
/

create table EHA_SSR_EVENT (pageid numeric(32), payload blob);
create unique index EHA_SSR_EVENT_idx1 on EHA_SSR_EVENT(pageid);

create table EHA_SSR_CHECKPOINT (pageid numeric(32), enginetime numeric(32), systemtime numeric(32), lastcompacted_enginetime numeric(32), lastcompacted_systemtime numeric(32), payload blob);
create unique index EHA_SSR_CHECKPOINT_idx1 on EHA_SSR_CHECKPOINT(pageid);

create table EHA_SSR_REF (pageid numeric(32), create_enginetime numeric(32), create_systemtime numeric(32), lastupd_enginetime numeric(32), lastupd_systemtime numeric(32), payload blob);
create unique index EHA_SSR_REF_idx1 on EHA_SSR_REF(pageid);

create table EHA_SSR_SYSTEM (payload blob);

EXIT;