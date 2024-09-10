DROP TABLE IF EXISTS eg_ubp_application;


CREATE TABLE eg_ubp_application(
id                           character varying(64),
tenant_id                    character varying(64) NOT NULL,
application_number           character varying(64) NOT NULL,
individual_id                character varying(64) NOT NULL,
program_code                 character varying(64) NOT NULL,
status                       character varying(64) NOT NULL,
wf_status                    character varying(64),
additional_details           JSONB,
created_by                   character varying(64)  NOT NULL,
last_modified_by             character varying(64),
created_time                 bigint,
last_modified_time           bigint,

CONSTRAINT pk_eg_ubp_application PRIMARY KEY (id)
);

CREATE TABLE eg_ubp_application_documents (
  id                          character varying(64),
  filestore_id                character varying(64),
  document_type               character varying(64),
  document_uid                character varying(64),
  status                      character varying(64),
  application_id              character varying(64),
  additional_details          JSONB,
  created_by                  character varying(64)  NOT NULL,
  last_modified_by            character varying(64),
  created_time                bigint,
  last_modified_time          bigint,
  CONSTRAINT pk_eg_ubp_application_documents PRIMARY KEY (id),
  CONSTRAINT fk_eg_ubp_application_documents FOREIGN KEY (application_id) REFERENCES eg_ubp_application (id)
);