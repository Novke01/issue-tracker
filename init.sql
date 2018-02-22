--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = ON;
SET check_function_bodies = FALSE;
SET client_min_messages = WARNING;
SET row_security = OFF;

CREATE DATABASE issuetracker WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'C' LC_CTYPE = 'C';

ALTER DATABASE issuetracker OWNER TO postgres;

\connect issuetracker

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = ON;
SET check_function_bodies = FALSE;
SET client_min_messages = WARNING;
SET row_security = OFF;

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = FALSE;

CREATE TABLE users
(
  id            BIGSERIAL NOT NULL CONSTRAINT users_pkey PRIMARY KEY,
  username      VARCHAR   NOT NULL CONSTRAINT users_username_key UNIQUE,
  password      VARCHAR   NOT NULL,
  first_name    VARCHAR   NOT NULL,
  last_name     VARCHAR   NOT NULL,
  email         VARCHAR   NOT NULL CONSTRAINT users_email_key UNIQUE,
  refresh_token VARCHAR   NOT NULL
);

CREATE TABLE repositories
(
  id          BIGSERIAL NOT NULL CONSTRAINT repositories_pkey PRIMARY KEY,
  name        VARCHAR   NOT NULL,
  url         VARCHAR   NOT NULL CONSTRAINT repositories_url_key UNIQUE,
  description VARCHAR   NOT NULL,
  "ownerId"   BIGINT    NOT NULL CONSTRAINT "owner_FK" REFERENCES users
);

CREATE TABLE contributors
(
  id             BIGSERIAL NOT NULL CONSTRAINT contributors_pkey PRIMARY KEY,
  "userId"       BIGINT    NOT NULL CONSTRAINT "user_FK" REFERENCES users,
  "repositoryId" BIGINT    NOT NULL CONSTRAINT "repository_FK" REFERENCES repositories ON DELETE CASCADE
);

CREATE TABLE issues
(
  id             BIGSERIAL NOT NULL CONSTRAINT issues_pkey PRIMARY KEY,
  "repositoryId" BIGINT    NOT NULL,
  title          VARCHAR   NOT NULL,
  description    VARCHAR   NOT NULL,
  created        BIGINT    NOT NULL,
  "ownerId"      BIGINT    NOT NULL,
  status         VARCHAR   NOT NULL,
  "milestoneId"  BIGINT
);

CREATE TABLE assignees
(
  id        BIGSERIAL NOT NULL CONSTRAINT assignees_pkey PRIMARY KEY,
  "userId"  BIGINT    NOT NULL CONSTRAINT "user_FK" REFERENCES users,
  "issueId" BIGINT    NOT NULL CONSTRAINT "issue_FK" REFERENCES issues ON DELETE CASCADE
);

CREATE TABLE wiki_pages
(
  id             BIGSERIAL NOT NULL CONSTRAINT wiki_pages_pkey PRIMARY KEY,
  name           VARCHAR   NOT NULL,
  content        TEXT      NOT NULL,
  "repositoryId" BIGINT    NOT NULL CONSTRAINT "repository_FK" REFERENCES repositories ON DELETE CASCADE
);

CREATE TABLE labels
(
  id             BIGSERIAL NOT NULL CONSTRAINT labels_pkey PRIMARY KEY,
  name           VARCHAR   NOT NULL,
  color          VARCHAR   NOT NULL,
  "repositoryId" BIGINT    NOT NULL CONSTRAINT "repository_FK" REFERENCES repositories ON DELETE CASCADE
);

CREATE TABLE "issueLabels"
(
  id        BIGSERIAL NOT NULL CONSTRAINT "issueLabels_pkey" PRIMARY KEY,
  "labelId" BIGINT    NOT NULL CONSTRAINT "label_FK" REFERENCES labels ON DELETE CASCADE,
  "issueId" BIGINT    NOT NULL CONSTRAINT "issue_FK" REFERENCES issues ON DELETE CASCADE
);

CREATE TABLE milestones
(
  id             BIGSERIAL NOT NULL CONSTRAINT milestones_pkey PRIMARY KEY,
  title          VARCHAR   NOT NULL,
  description    VARCHAR   NOT NULL,
  "dueDate"      TIMESTAMP NOT NULL,
  "repositoryId" BIGINT    NOT NULL CONSTRAINT "repository_FK" REFERENCES repositories ON DELETE CASCADE
);

CREATE TABLE pullrequests
(
  id             BIGSERIAL NOT NULL CONSTRAINT pullrequests_pkey PRIMARY KEY,
  title          VARCHAR   NOT NULL,
  url            VARCHAR   NOT NULL,
  "repositoryId" BIGINT    NOT NULL CONSTRAINT "repository_FK" REFERENCES repositories ON DELETE CASCADE
);

CREATE TABLE comments
(
  id              BIGSERIAL NOT NULL CONSTRAINT comments_pkey PRIMARY KEY,
  content         VARCHAR   NOT NULL,
  user_id         BIGINT    NOT NULL CONSTRAINT "user_FK" REFERENCES users,
  user_username   VARCHAR   NOT NULL,
  issue_id        BIGINT CONSTRAINT "issue_FK" REFERENCES issues ON DELETE CASCADE,
  pull_request_id BIGINT CONSTRAINT "pull_request_FK" REFERENCES pullrequests ON DELETE CASCADE
);
