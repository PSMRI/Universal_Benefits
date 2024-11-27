ALTER TABLE eg_ubp_application
ADD COLUMN schema JSONB NOT NULL DEFAULT '{}'::JSONB;

ALTER TABLE eg_ubp_application
ADD COLUMN order_id character varying(64);

ALTER TABLE eg_ubp_application
ADD COLUMN transaction_id character varying(64);

ALTER TABLE eg_ubp_application
ADD COLUMN submission_id character varying(64);

ALTER TABLE eg_ubp_application
ADD COLUMN content_id character varying(64);
