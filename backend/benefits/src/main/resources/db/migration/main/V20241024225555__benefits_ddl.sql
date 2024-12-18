CREATE TABLE benefits (
    benefit_id VARCHAR PRIMARY KEY,
    benefit_name VARCHAR NOT NULL UNIQUE,
    benefit_description VARCHAR,
    benefit_provider VARCHAR NOT NULL,
    eligibility_gender VARCHAR(1),
    eligibility_min_qualification VARCHAR,
    eligibility_attendance_percentage VARCHAR,
    eligibility_academic_class VARCHAR,
    eligibility_marks VARCHAR,
    eligibility_field_of_study VARCHAR,
    eligibility_annual_income VARCHAR,
    eligibility_caste VARCHAR, -- Comma-separated values: general, SC, ST, OBC
    eligibility_student_type VARCHAR,
    eligibility_disability BOOLEAN,
    eligibility_domicile VARCHAR,
    eligibility_age VARCHAR,
    eligibility_eligible_children VARCHAR,
    finance_parent_occupation VARCHAR,
    beneficiary_count_max INTEGER,
    allow_with_other_benefit BOOLEAN,
    allow_one_year_if_failed BOOLEAN,
    application_start bigint,
    application_end bigint,
    application_end_extended bigint,
    auto_renew_applicable BOOLEAN,
    status VARCHAR NOT NULL,
    finance_max_beneficiary_limit_allowed BOOLEAN,
    finance_max_beneficiary_allowed INTEGER,
    valid_till_date bigint,
    created_by                   character varying(64)  NOT NULL,
    last_modified_by             character varying(64),
    created_time                 bigint,
    last_modified_time           bigint,
    CHECK (eligibility_gender IN ('M', 'F', 'B')),
    CHECK (eligibility_student_type IN ('DAYSCHOLAR', 'HOSTELER', 'BOTH')),
    CHECK (status IN ('DRAFT', 'ACTIVE', 'CLOSED'))
);

CREATE TABLE benefits_category (
    id VARCHAR PRIMARY KEY,
    benefit_id VARCHAR NOT NULL REFERENCES benefits(benefit_id) ON DELETE CASCADE,
    caste VARCHAR,
    type VARCHAR,
    category VARCHAR,
    amount FLOAT,
    created_by                   character varying(64)  NOT NULL,
    last_modified_by             character varying(64),
    created_time                 bigint,
    last_modified_time           bigint
);

CREATE TABLE benefit_sponsor (
    id VARCHAR PRIMARY KEY,
    benefit_id VARCHAR NOT NULL REFERENCES benefits(benefit_id) ON DELETE CASCADE,
    sponsor_name VARCHAR,
    entity_type VARCHAR,
    share_percent DECIMAL(5,2),
    type VARCHAR,
    created_by                   character varying(64)  NOT NULL,
    last_modified_by             character varying(64),
    created_time                 bigint,
    last_modified_time           bigint,
    CHECK (type IN ('PRIMARY', 'SECONDARY'))
);

CREATE TABLE benefit_extensions (
    id VARCHAR PRIMARY KEY,
    benefit_id VARCHAR NOT NULL REFERENCES benefits(benefit_id) ON DELETE CASCADE,
    old_deadline DATE,
    new_deadline DATE
);
