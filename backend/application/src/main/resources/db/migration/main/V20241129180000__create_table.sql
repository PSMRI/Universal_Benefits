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

CREATE TABLE eg_ubp_applicant(
id                           character varying(64),
application_id               character varying(64),
student_name                 character varying(64) NOT NULL,
father_name                  character varying(64),
samagra_id                   character varying(64) NOT NULL,
school_name                  character varying(255),
school_address               character varying(255),
school_address_district      character varying(64),
current_class                character varying(64),
previous_year_marks          character varying(64),
student_type                 character varying(64),
aadhar_last_4_digits         character varying(4),
caste                        character varying(64),
income                       character varying(64),
gender                       character varying(64),
age                          integer,
disability                   boolean,

CONSTRAINT pk_eg_ubp_applicant PRIMARY KEY (id),
CONSTRAINT fk_eg_ubp_applicant FOREIGN KEY (application_id) REFERENCES eg_ubp_application (id)
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

ALTER TABLE eg_ubp_application ADD COLUMN schema JSONB NOT NULL DEFAULT '{}'::JSONB;

ALTER TABLE eg_ubp_application ADD COLUMN order_id character varying(64);

ALTER TABLE eg_ubp_application ADD COLUMN transaction_id character varying(64);

ALTER TABLE eg_ubp_application ADD COLUMN submission_id character varying(64);

ALTER TABLE eg_ubp_application ADD COLUMN content_id character varying(64);

ALTER TABLE eg_ubp_application ADD COLUMN batch_id integer;

ALTER TABLE eg_ubp_application ADD COLUMN disbursal_log character varying(150);
