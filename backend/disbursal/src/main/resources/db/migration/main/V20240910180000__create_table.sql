DROP TABLE IF EXISTS eg_ubp_disbursal;

CREATE TABLE eg_ubp_disbursal(
id                           character varying(64),
tenant_id                    character varying(64) NOT NULL,
disbursal_number             character varying(64) NOT NULL,
reference_id                 character varying(64) NOT NULL,
total_amount                 numeric(12,2) NOT NULL,
total_paid_amount            numeric(12,2) NOT NULL,
payment_status               character varying(64),
status                       character varying(64) NOT NULL,
wf_status                    character varying(64),
additional_details           JSONB,
created_by                   character varying(64)  NOT NULL,
last_modified_by             character varying(64),
created_time                 bigint,
last_modified_time           bigint,

CONSTRAINT pk_eg_ubp_disbursal PRIMARY KEY (id)
);