/*
========================================================================
Version     : 1.2.6
Type        : FULL SCHEMA
DB          : POSTGRES
========================================================================
*/

CREATE SEQUENCE public.rule_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.statement_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.rule_event (
    id integer NOT NULL,
    enabled boolean NOT NULL,
    id_action integer NOT NULL,
    id_channel integer NOT NULL,
    id_critical_level integer NOT NULL,
    id_operation_type integer NOT NULL,
    id_rule_type integer NOT NULL,
    json_format character varying(2500),
    name character varying(255),
    score integer NOT NULL,
    last_update_date timestamp with time zone
);

CREATE TABLE public.rule_event_statement (
    id integer NOT NULL,
    context boolean NOT NULL,
    statement character varying(1500),
    rule_id integer NOT NULL
);

/*
========================================================================
ESPERHA 8.6.0
========================================================================
*/

CREATE TABLE EHA_SSR_DATA (
	resourcekey bytea, 
	payload bytea, 
	vls bigint
);

CREATE TABLE EHA_SSR_MGMT (
	inckey serial, 
	prefix smallint, 
	resourcekey bytea, 
	payload bytea
);

CREATE TABLE EHA_SSR_EVENT (
	pageid bigint, 
	payload bytea
);

CREATE TABLE EHA_SSR_CHECKPOINT (
	pageid bigint, 
	enginetime bigint, 
	systemtime bigint, 
	lastcompacted_enginetime bigint, 
	lastcompacted_systemtime bigint, 
	payload bytea
);


CREATE TABLE EHA_SSR_REF (
	pageid bigint, 
	create_enginetime bigint, 
	create_systemtime bigint, 
	lastupd_enginetime bigint, 
	lastupd_systemtime bigint, 
	payload bytea
);


CREATE TABLE EHA_SSR_SYSTEM (
	payload bytea
);


ALTER TABLE ONLY public.rule_event_statement
	ADD CONSTRAINT rule_event_statement_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.rule_event
	ADD CONSTRAINT rule_pkey PRIMARY KEY (id);


ALTER TABLE ONLY EHA_SSR_MGMT
	ADD CONSTRAINT inckey PRIMARY KEY (inckey);


ALTER TABLE ONLY public.rule_event_statement
    ADD CONSTRAINT fk_statement_rule FOREIGN KEY (rule_id) REFERENCES public.rule_event(id);


CREATE UNIQUE INDEX EHA_SSR_DATA_idx1 on EHA_SSR_DATA(resourcekey);
CREATE UNIQUE INDEX EHA_SSR_MGMT_idx1 on EHA_SSR_MGMT(prefix, resourcekey);
CREATE UNIQUE INDEX EHA_SSR_EVENT_idx1 on EHA_SSR_EVENT(pageid);
CREATE UNIQUE INDEX EHA_SSR_CHECKPOINT_idx1 on EHA_SSR_CHECKPOINT(pageid);
CREATE UNIQUE INDEX EHA_SSR_REF_idx1 on EHA_SSR_REF(pageid);
