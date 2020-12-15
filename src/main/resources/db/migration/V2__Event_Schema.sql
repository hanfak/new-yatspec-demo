CREATE SCHEMA IF NOT EXISTS records;

CREATE SEQUENCE aggregates_sequence START 1;
CREATE SEQUENCE events_sequence START 1;
-- TODO Add index on AGG_REFERENCE
CREATE TABLE records.aggregates(
   AGG_ID               integer       DEFAULT nextval('aggregates_sequence'::regclass) NOT NULL UNIQUE,
   AGG_REFERENCE        varchar(10)   NOT NULL UNIQUE PRIMARY KEY,
   AGG_DATA             varchar(50)   NOT NULL,
   AGG_STATE            varchar(20)   NOT NULL,
   LAST_MODIFIED_TIME   TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
   CREATED_AT           TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX IDX_AGG_REFERENCE_1 ON records.aggregates(AGG_REFERENCE);

CREATE TABLE records.events(
   EVENT_ID             integer       DEFAULT nextval('events_sequence'::regclass) NOT NULL UNIQUE,
   AGG_REFERENCE        varchar(10)   REFERENCES records.aggregates(AGG_REFERENCE),
   EVENT_DATA           varchar(50)   NOT NULL,
   EVENT_STATE          varchar(20)   NOT NULL,
   ERROR_CODE           varchar(20),
   ERROR_MESSAGE        varchar(100),
   LAST_MODIFIED_TIME   TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL,
   CREATED_AT           TIMESTAMP     DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX IDX_EVENT_ID ON records.events(EVENT_ID);
CREATE INDEX IDX_AGG_REFERENCE_2 ON records.events(AGG_REFERENCE);
