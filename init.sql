--
-- PostgreSQL database dump
--

-- Dumped from database version 10.1
-- Dumped by pg_dump version 10.1

-- Started on 2018-02-23 21:48:26

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2941 (class 1262 OID 16446)
-- Name: issuetracker; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE issuetracker WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'English_United States.1252' LC_CTYPE = 'English_United States.1252';


ALTER DATABASE issuetracker OWNER TO postgres;

\connect issuetracker

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 1 (class 3079 OID 12924)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2943 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 217 (class 1259 OID 22314)
-- Name: assignees; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE assignees (
    id bigint NOT NULL,
    "userId" bigint NOT NULL,
    "issueId" bigint NOT NULL
);


ALTER TABLE assignees OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 22312)
-- Name: assignees_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE assignees_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE assignees_id_seq OWNER TO postgres;

--
-- TOC entry 2944 (class 0 OID 0)
-- Dependencies: 216
-- Name: assignees_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE assignees_id_seq OWNED BY assignees.id;


--
-- TOC entry 215 (class 1259 OID 22288)
-- Name: comments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE comments (
    id bigint NOT NULL,
    content character varying NOT NULL,
    user_id bigint NOT NULL,
    user_username character varying NOT NULL,
    issue_id bigint,
    pull_request_id bigint
);


ALTER TABLE comments OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 22286)
-- Name: comments_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE comments_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE comments_id_seq OWNER TO postgres;

--
-- TOC entry 2945 (class 0 OID 0)
-- Dependencies: 214
-- Name: comments_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE comments_id_seq OWNED BY comments.id;


--
-- TOC entry 201 (class 1259 OID 22162)
-- Name: contributors; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE contributors (
    id bigint NOT NULL,
    "userId" bigint NOT NULL,
    "repositoryId" bigint NOT NULL
);


ALTER TABLE contributors OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 22160)
-- Name: contributors_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE contributors_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE contributors_id_seq OWNER TO postgres;

--
-- TOC entry 2946 (class 0 OID 0)
-- Dependencies: 200
-- Name: contributors_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE contributors_id_seq OWNED BY contributors.id;


--
-- TOC entry 213 (class 1259 OID 22270)
-- Name: issueLabels; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE "issueLabels" (
    id bigint NOT NULL,
    "labelId" bigint NOT NULL,
    "issueId" bigint NOT NULL
);


ALTER TABLE "issueLabels" OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 22268)
-- Name: issueLabels_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE "issueLabels_id_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE "issueLabels_id_seq" OWNER TO postgres;

--
-- TOC entry 2947 (class 0 OID 0)
-- Dependencies: 212
-- Name: issueLabels_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE "issueLabels_id_seq" OWNED BY "issueLabels".id;


--
-- TOC entry 207 (class 1259 OID 22212)
-- Name: issues; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE issues (
    id bigint NOT NULL,
    "repositoryId" bigint NOT NULL,
    title character varying NOT NULL,
    description character varying NOT NULL,
    created bigint NOT NULL,
    "ownerId" bigint NOT NULL,
    status character varying NOT NULL,
    "milestoneId" bigint
);


ALTER TABLE issues OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 22210)
-- Name: issues_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE issues_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE issues_id_seq OWNER TO postgres;

--
-- TOC entry 2948 (class 0 OID 0)
-- Dependencies: 206
-- Name: issues_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE issues_id_seq OWNED BY issues.id;


--
-- TOC entry 211 (class 1259 OID 22254)
-- Name: labels; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE labels (
    id bigint NOT NULL,
    name character varying NOT NULL,
    color character varying NOT NULL,
    "repositoryId" bigint NOT NULL
);


ALTER TABLE labels OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 22252)
-- Name: labels_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE labels_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE labels_id_seq OWNER TO postgres;

--
-- TOC entry 2949 (class 0 OID 0)
-- Dependencies: 210
-- Name: labels_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE labels_id_seq OWNED BY labels.id;


--
-- TOC entry 203 (class 1259 OID 22180)
-- Name: milestones; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE milestones (
    id bigint NOT NULL,
    title character varying NOT NULL,
    description character varying NOT NULL,
    "dueDate" timestamp without time zone NOT NULL,
    "repositoryId" bigint NOT NULL
);


ALTER TABLE milestones OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 22178)
-- Name: milestones_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE milestones_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE milestones_id_seq OWNER TO postgres;

--
-- TOC entry 2950 (class 0 OID 0)
-- Dependencies: 202
-- Name: milestones_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE milestones_id_seq OWNED BY milestones.id;


--
-- TOC entry 205 (class 1259 OID 22196)
-- Name: pullrequests; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE pullrequests (
    id bigint NOT NULL,
    title character varying NOT NULL,
    url character varying NOT NULL,
    "repositoryId" bigint NOT NULL
);


ALTER TABLE pullrequests OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 22194)
-- Name: pullrequests_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE pullrequests_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pullrequests_id_seq OWNER TO postgres;

--
-- TOC entry 2951 (class 0 OID 0)
-- Dependencies: 204
-- Name: pullrequests_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE pullrequests_id_seq OWNED BY pullrequests.id;


--
-- TOC entry 199 (class 1259 OID 22144)
-- Name: repositories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE repositories (
    id bigint NOT NULL,
    name character varying NOT NULL,
    url character varying NOT NULL,
    description character varying NOT NULL,
    "ownerId" bigint NOT NULL
);


ALTER TABLE repositories OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 22142)
-- Name: repositories_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE repositories_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE repositories_id_seq OWNER TO postgres;

--
-- TOC entry 2952 (class 0 OID 0)
-- Dependencies: 198
-- Name: repositories_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE repositories_id_seq OWNED BY repositories.id;


--
-- TOC entry 197 (class 1259 OID 22129)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE users (
    id bigint NOT NULL,
    username character varying NOT NULL,
    password character varying NOT NULL,
    first_name character varying NOT NULL,
    last_name character varying NOT NULL,
    email character varying NOT NULL,
    refresh_token character varying NOT NULL
);


ALTER TABLE users OWNER TO postgres;

--
-- TOC entry 196 (class 1259 OID 22127)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE users_id_seq OWNER TO postgres;

--
-- TOC entry 2953 (class 0 OID 0)
-- Dependencies: 196
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE users_id_seq OWNED BY users.id;


--
-- TOC entry 209 (class 1259 OID 22238)
-- Name: wiki_pages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE wiki_pages (
    id bigint NOT NULL,
    name character varying NOT NULL,
    content text NOT NULL,
    "repositoryId" bigint NOT NULL
);


ALTER TABLE wiki_pages OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 22236)
-- Name: wiki_pages_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE wiki_pages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE wiki_pages_id_seq OWNER TO postgres;

--
-- TOC entry 2954 (class 0 OID 0)
-- Dependencies: 208
-- Name: wiki_pages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE wiki_pages_id_seq OWNED BY wiki_pages.id;


--
-- TOC entry 2748 (class 2604 OID 22317)
-- Name: assignees id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY assignees ALTER COLUMN id SET DEFAULT nextval('assignees_id_seq'::regclass);


--
-- TOC entry 2747 (class 2604 OID 22291)
-- Name: comments id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY comments ALTER COLUMN id SET DEFAULT nextval('comments_id_seq'::regclass);


--
-- TOC entry 2740 (class 2604 OID 22165)
-- Name: contributors id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY contributors ALTER COLUMN id SET DEFAULT nextval('contributors_id_seq'::regclass);


--
-- TOC entry 2746 (class 2604 OID 22273)
-- Name: issueLabels id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "issueLabels" ALTER COLUMN id SET DEFAULT nextval('"issueLabels_id_seq"'::regclass);


--
-- TOC entry 2743 (class 2604 OID 22215)
-- Name: issues id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY issues ALTER COLUMN id SET DEFAULT nextval('issues_id_seq'::regclass);


--
-- TOC entry 2745 (class 2604 OID 22257)
-- Name: labels id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY labels ALTER COLUMN id SET DEFAULT nextval('labels_id_seq'::regclass);


--
-- TOC entry 2741 (class 2604 OID 22183)
-- Name: milestones id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY milestones ALTER COLUMN id SET DEFAULT nextval('milestones_id_seq'::regclass);


--
-- TOC entry 2742 (class 2604 OID 22199)
-- Name: pullrequests id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pullrequests ALTER COLUMN id SET DEFAULT nextval('pullrequests_id_seq'::regclass);


--
-- TOC entry 2739 (class 2604 OID 22147)
-- Name: repositories id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY repositories ALTER COLUMN id SET DEFAULT nextval('repositories_id_seq'::regclass);


--
-- TOC entry 2738 (class 2604 OID 22132)
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--
-- TOC entry 2744 (class 2604 OID 22241)
-- Name: wiki_pages id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY wiki_pages ALTER COLUMN id SET DEFAULT nextval('wiki_pages_id_seq'::regclass);


--
-- TOC entry 2936 (class 0 OID 22314)
-- Dependencies: 217
-- Data for Name: assignees; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO assignees (id, "userId", "issueId") VALUES (1, 1, 1);


--
-- TOC entry 2934 (class 0 OID 22288)
-- Dependencies: 215
-- Data for Name: comments; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2920 (class 0 OID 22162)
-- Dependencies: 201
-- Data for Name: contributors; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO contributors (id, "userId", "repositoryId") VALUES (1, 2, 1);
INSERT INTO contributors (id, "userId", "repositoryId") VALUES (2, 3, 1);
INSERT INTO contributors (id, "userId", "repositoryId") VALUES (3, 4, 1);


--
-- TOC entry 2932 (class 0 OID 22270)
-- Dependencies: 213
-- Data for Name: issueLabels; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO "issueLabels" (id, "labelId", "issueId") VALUES (1, 3, 1);


--
-- TOC entry 2926 (class 0 OID 22212)
-- Dependencies: 207
-- Data for Name: issues; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO issues (id, "repositoryId", title, description, created, "ownerId", status, "milestoneId") VALUES (1, 1, 'Test repository features', 'Test repository features description.', 1519416652478, 1, 'OPENED', 1);


--
-- TOC entry 2930 (class 0 OID 22254)
-- Dependencies: 211
-- Data for Name: labels; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO labels (id, name, color, "repositoryId") VALUES (1, 'bug', '#FF0000', 1);
INSERT INTO labels (id, name, color, "repositoryId") VALUES (2, 'enhancement', '#47A8D4', 1);
INSERT INTO labels (id, name, color, "repositoryId") VALUES (3, 'test', '#00FF00', 1);


--
-- TOC entry 2922 (class 0 OID 22180)
-- Dependencies: 203
-- Data for Name: milestones; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO milestones (id, title, description, "dueDate", "repositoryId") VALUES (1, 'Release 1.0', 'Release description.', '2018-02-24 00:00:00', 1);


--
-- TOC entry 2924 (class 0 OID 22196)
-- Dependencies: 205
-- Data for Name: pullrequests; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO pullrequests (id, title, url, "repositoryId") VALUES (1, 'Implement support for comment tags', 'https://github.com/Novke01/issue-tracker/pull/40', 1);
INSERT INTO pullrequests (id, title, url, "repositoryId") VALUES (2, 'Add support for milestones', 'https://github.com/Novke01/issue-tracker/pull/33', 1);


--
-- TOC entry 2918 (class 0 OID 22144)
-- Dependencies: 199
-- Data for Name: repositories; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO repositories (id, name, url, description, "ownerId") VALUES (1, 'Issue tracker', 'https://github.com/Novke01/issue-tracker', 'uks project', 1);
INSERT INTO repositories (id, name, url, description, "ownerId") VALUES (2, 'To Do Android', 'https://github.com/milekuglas/ToDoListAppAndroid', 'To Do Android app', 1);
INSERT INTO repositories (id, name, url, description, "ownerId") VALUES (3, 'Brick Breaker', 'https://github.com/milekuglas/BrickBreaker', 'Fun game written in Haskell', 1);


--
-- TOC entry 2916 (class 0 OID 22129)
-- Dependencies: 197
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO users (id, username, password, first_name, last_name, email, refresh_token) VALUES (1, 'milorad', '$2a$10$X.1vl1K8xNFDUS0TIfazy.sOAaXEW9bnlq2hKwNGXOnc9VFOj2go2', 'Milorad', 'Vojnovic', 'miloradvojnovic@gmail.com', '2J594Tb5sPqJp4zf4ZDwLhhv7O0SkIVKerwv7uwTkCAJ0n99a8');
INSERT INTO users (id, username, password, first_name, last_name, email, refresh_token) VALUES (2, 'arsenije', '$2a$10$dkFIVLxEZDaQpZn74M0kauc7CDkRmgwT1Za.spIe0/wWMVz84Vlae', 'Arsenije', 'Vladisavljev', 'arsenijevladisavljev@gmail.com', 'wWsmDTBrX6Ov10qDLvAMX1VBb0wggVIiuI3tXsBJVi04aiI8Et');
INSERT INTO users (id, username, password, first_name, last_name, email, refresh_token) VALUES (3, 'aleksandar', '$2a$10$PC4FWpExwFCb5hNMp4ZoAeFWFMLWA2wZK4wMsoyWHj.Dk30i2Abia', 'Aleksandar', 'Novakovic', 'aleksandarnovakovic@gmail.com', '5DTaHs3nOhFhwd1ronp2jpC1JcOQ8u9wzYJE8MuFwJYjgn9thO');
INSERT INTO users (id, username, password, first_name, last_name, email, refresh_token) VALUES (4, 'alexandar', '$2a$10$Xf/YbwSeXHmeNLyeGRyhfOUdZzp2X9vz3l5XkN6nJY/9IhaifwK52', 'Aleksandar', 'Kahriman', 'aleksandarkahriman@gmail.com', 'tcQjQLqtWQbHUlb4y0NSfE0BieZIW5ohQNb7mU3OdFXbmUc7XS');


--
-- TOC entry 2928 (class 0 OID 22238)
-- Dependencies: 209
-- Data for Name: wiki_pages; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO wiki_pages (id, name, content, "repositoryId") VALUES (1, 'Wiki page example', '
# Headers

# H1
## H2
### H3
#### H4
##### H5
###### H6

Alternatively, for H1 and H2, an underline-ish style:

Alt-H1
======

Alt-H2
------



# Emphasis

Emphasis, aka italics, with *asterisks* or _underscores_.

Strong emphasis, aka bold, with **asterisks** or __underscores__.

Combined emphasis with **asterisks and _underscores_**.

Strikethrough uses two tildes. ~~Scratch this.~~

# Lists

1. First ordered list item
2. Another item
  * Unordered sub-list.
1. Actual numbers don''t matter, just that it''s a number
  1. Ordered sub-list
4. And another item.

⋅⋅⋅You can have properly indented paragraphs within list items. Notice the blank line above, and the leading spaces (at least one, but we''ll use three here to also align the raw Markdown).

⋅⋅⋅To have a line break without a paragraph, you will need to use two trailing spaces.⋅⋅
⋅⋅⋅Note that this line is separate, but within the same paragraph.⋅⋅
⋅⋅⋅(This is contrary to the typical GFM line break behaviour, where trailing spaces are not required.)

* Unordered list can use asterisks
- Or minuses
+ Or pluses


# Links

There are two ways to create links.

[I''m an inline-style link](https://www.google.com)

[I''m an inline-style link with title](https://www.google.com "Google''s Homepage")

[I''m a reference-style link][Arbitrary case-insensitive reference text]

[I''m a relative reference to a repository file](../blob/master/LICENSE)

[You can use numbers for reference-style link definitions][1]

Or leave it empty and use the [link text itself].

URLs and URLs in angle brackets will automatically get turned into links.
http://www.example.com or <http://www.example.com> and sometimes
example.com (but not on Github, for example).

Some text to show that the reference links can follow later.

[arbitrary case-insensitive reference text]: https://www.mozilla.org
[1]: http://slashdot.org
[link text itself]: http://www.reddit.com



# Images

Here''s our logo (hover to see the title text):

Inline-style:
![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "Logo Title Text 1")

Reference-style:
![alt text][logo]

[logo]: https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "Logo Title Text 2"

# Code and Syntax Highlighting

Inline `code` has `back-ticks around` it.

```javascript
var s = "JavaScript syntax highlighting";
alert(s);
```

```python
s = "Python syntax highlighting"
print s
```

```
No language indicated, so no syntax highlighting.
But let''s throw in a <b>tag</b>.

```


# Blockquotes

> Blockquotes are very handy in email to emulate reply text.
> This line is part of the same quote.

Quote break.

> This is a very long line that will still be quoted properly when it wraps. Oh boy let''s keep writing to make sure this is long enough to actually wrap for everyone. Oh, you can *put* **Markdown** into a blockquote.


# Horizontal Rule

Three or more...

---

Hyphens

***

Asterisks

___

Underscores

# Tables

Colons can be used to align columns.

| Tables        | Are           | Cool  |
| ------------- |:-------------:| -----:|
| col 3 is      | right-aligned | $1600 |
| col 2 is      | centered      |   $12 |
| zebra stripes | are neat      |    $1 |

There must be at least 3 dashes separating each header cell.
The outer pipes (|) are optional, and you don''t need to make the
raw Markdown line up prettily. You can also use inline Markdown.

Markdown | Less | Pretty
--- | --- | ---
*Still* | `renders]` | **nicely**
1 | 2 | 3

', 1);


--
-- TOC entry 2955 (class 0 OID 0)
-- Dependencies: 216
-- Name: assignees_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('assignees_id_seq', 1, true);


--
-- TOC entry 2956 (class 0 OID 0)
-- Dependencies: 214
-- Name: comments_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('comments_id_seq', 1, false);


--
-- TOC entry 2957 (class 0 OID 0)
-- Dependencies: 200
-- Name: contributors_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('contributors_id_seq', 3, true);


--
-- TOC entry 2958 (class 0 OID 0)
-- Dependencies: 212
-- Name: issueLabels_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('"issueLabels_id_seq"', 1, true);


--
-- TOC entry 2959 (class 0 OID 0)
-- Dependencies: 206
-- Name: issues_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('issues_id_seq', 1, true);


--
-- TOC entry 2960 (class 0 OID 0)
-- Dependencies: 210
-- Name: labels_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('labels_id_seq', 3, true);


--
-- TOC entry 2961 (class 0 OID 0)
-- Dependencies: 202
-- Name: milestones_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('milestones_id_seq', 1, true);


--
-- TOC entry 2962 (class 0 OID 0)
-- Dependencies: 204
-- Name: pullrequests_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('pullrequests_id_seq', 2, true);


--
-- TOC entry 2963 (class 0 OID 0)
-- Dependencies: 198
-- Name: repositories_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('repositories_id_seq', 3, true);


--
-- TOC entry 2964 (class 0 OID 0)
-- Dependencies: 196
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('users_id_seq', 4, true);


--
-- TOC entry 2965 (class 0 OID 0)
-- Dependencies: 208
-- Name: wiki_pages_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('wiki_pages_id_seq', 1, true);


--
-- TOC entry 2776 (class 2606 OID 22319)
-- Name: assignees assignees_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY assignees
    ADD CONSTRAINT assignees_pkey PRIMARY KEY (id);


--
-- TOC entry 2774 (class 2606 OID 22296)
-- Name: comments comments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY comments
    ADD CONSTRAINT comments_pkey PRIMARY KEY (id);


--
-- TOC entry 2760 (class 2606 OID 22167)
-- Name: contributors contributors_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY contributors
    ADD CONSTRAINT contributors_pkey PRIMARY KEY (id);


--
-- TOC entry 2772 (class 2606 OID 22275)
-- Name: issueLabels issueLabels_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "issueLabels"
    ADD CONSTRAINT "issueLabels_pkey" PRIMARY KEY (id);


--
-- TOC entry 2766 (class 2606 OID 22220)
-- Name: issues issues_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY issues
    ADD CONSTRAINT issues_pkey PRIMARY KEY (id);


--
-- TOC entry 2770 (class 2606 OID 22262)
-- Name: labels labels_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY labels
    ADD CONSTRAINT labels_pkey PRIMARY KEY (id);


--
-- TOC entry 2762 (class 2606 OID 22188)
-- Name: milestones milestones_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY milestones
    ADD CONSTRAINT milestones_pkey PRIMARY KEY (id);


--
-- TOC entry 2764 (class 2606 OID 22204)
-- Name: pullrequests pullrequests_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pullrequests
    ADD CONSTRAINT pullrequests_pkey PRIMARY KEY (id);


--
-- TOC entry 2756 (class 2606 OID 22152)
-- Name: repositories repositories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY repositories
    ADD CONSTRAINT repositories_pkey PRIMARY KEY (id);


--
-- TOC entry 2758 (class 2606 OID 22154)
-- Name: repositories repositories_url_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY repositories
    ADD CONSTRAINT repositories_url_key UNIQUE (url);


--
-- TOC entry 2750 (class 2606 OID 22141)
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- TOC entry 2752 (class 2606 OID 22137)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 2754 (class 2606 OID 22139)
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- TOC entry 2768 (class 2606 OID 22246)
-- Name: wiki_pages wiki_pages_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY wiki_pages
    ADD CONSTRAINT wiki_pages_pkey PRIMARY KEY (id);


--
-- TOC entry 2787 (class 2606 OID 22276)
-- Name: issueLabels issue_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "issueLabels"
    ADD CONSTRAINT "issue_FK" FOREIGN KEY ("issueId") REFERENCES issues(id) ON DELETE CASCADE;


--
-- TOC entry 2789 (class 2606 OID 22297)
-- Name: comments issue_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY comments
    ADD CONSTRAINT "issue_FK" FOREIGN KEY (issue_id) REFERENCES issues(id) ON DELETE CASCADE;


--
-- TOC entry 2792 (class 2606 OID 22320)
-- Name: assignees issue_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY assignees
    ADD CONSTRAINT "issue_FK" FOREIGN KEY ("issueId") REFERENCES issues(id) ON DELETE CASCADE;


--
-- TOC entry 2788 (class 2606 OID 22281)
-- Name: issueLabels label_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY "issueLabels"
    ADD CONSTRAINT "label_FK" FOREIGN KEY ("labelId") REFERENCES labels(id) ON DELETE CASCADE;


--
-- TOC entry 2782 (class 2606 OID 22221)
-- Name: issues milestone_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY issues
    ADD CONSTRAINT "milestone_FK" FOREIGN KEY ("milestoneId") REFERENCES milestones(id) ON DELETE SET NULL;


--
-- TOC entry 2777 (class 2606 OID 22155)
-- Name: repositories owner_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY repositories
    ADD CONSTRAINT "owner_FK" FOREIGN KEY ("ownerId") REFERENCES users(id);


--
-- TOC entry 2783 (class 2606 OID 22226)
-- Name: issues owner_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY issues
    ADD CONSTRAINT "owner_FK" FOREIGN KEY ("ownerId") REFERENCES users(id);


--
-- TOC entry 2790 (class 2606 OID 22302)
-- Name: comments pull_request_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY comments
    ADD CONSTRAINT "pull_request_FK" FOREIGN KEY (pull_request_id) REFERENCES pullrequests(id) ON DELETE CASCADE;


--
-- TOC entry 2778 (class 2606 OID 22168)
-- Name: contributors repository_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY contributors
    ADD CONSTRAINT "repository_FK" FOREIGN KEY ("repositoryId") REFERENCES repositories(id) ON DELETE CASCADE;


--
-- TOC entry 2780 (class 2606 OID 22189)
-- Name: milestones repository_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY milestones
    ADD CONSTRAINT "repository_FK" FOREIGN KEY ("repositoryId") REFERENCES repositories(id) ON DELETE CASCADE;


--
-- TOC entry 2781 (class 2606 OID 22205)
-- Name: pullrequests repository_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pullrequests
    ADD CONSTRAINT "repository_FK" FOREIGN KEY ("repositoryId") REFERENCES repositories(id) ON DELETE CASCADE;


--
-- TOC entry 2784 (class 2606 OID 22231)
-- Name: issues repository_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY issues
    ADD CONSTRAINT "repository_FK" FOREIGN KEY ("repositoryId") REFERENCES repositories(id) ON DELETE CASCADE;


--
-- TOC entry 2785 (class 2606 OID 22247)
-- Name: wiki_pages repository_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY wiki_pages
    ADD CONSTRAINT "repository_FK" FOREIGN KEY ("repositoryId") REFERENCES repositories(id) ON DELETE CASCADE;


--
-- TOC entry 2786 (class 2606 OID 22263)
-- Name: labels repository_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY labels
    ADD CONSTRAINT "repository_FK" FOREIGN KEY ("repositoryId") REFERENCES repositories(id) ON DELETE CASCADE;


--
-- TOC entry 2779 (class 2606 OID 22173)
-- Name: contributors user_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY contributors
    ADD CONSTRAINT "user_FK" FOREIGN KEY ("userId") REFERENCES users(id);


--
-- TOC entry 2791 (class 2606 OID 22307)
-- Name: comments user_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY comments
    ADD CONSTRAINT "user_FK" FOREIGN KEY (user_id) REFERENCES users(id);


--
-- TOC entry 2793 (class 2606 OID 22325)
-- Name: assignees user_FK; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY assignees
    ADD CONSTRAINT "user_FK" FOREIGN KEY ("userId") REFERENCES users(id);


-- Completed on 2018-02-23 21:48:27

--
-- PostgreSQL database dump complete
--

