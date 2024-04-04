--CREATE SCHEMA IF NOT EXISTS app_schema;

CREATE TABLE PERSON_SEX_M (
  ID bigserial PRIMARY KEY,
  DT bigint,
  REGION_ID integer,
  INCOME numeric
);

CREATE TABLE PERSON_SEX_G (
  ID bigserial PRIMARY KEY,
  DT bigint,
  REGION_ID integer,
  INCOME numeric
);

CREATE TABLE STAT_REGION (
  REGION_ID integer,
  ALL_PEOPLE integer,
  ABLE_BODIED integer,
  ABLE_BODIED_PERCENT numeric,
  INCOME_AVG numeric,
  INCOME_MAX numeric,
  JOBLESS integer,
  JOBLESS_PERCENT numeric
);

CREATE TABLE STAT_RU (
  ALL_PEOPLE integer,
  ABLE_BODIED integer,
  ABLE_BODIED_PERCENT numeric,
  INCOME_AVG numeric,
  INCOME_MAX numeric,
  JOBLESS integer,
  JOBLESS_PERCENT numeric
);
