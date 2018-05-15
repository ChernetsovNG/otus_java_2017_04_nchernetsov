DROP TABLE IF EXISTS users;

CREATE TABLE users
(
  id    BIGSERIAL  PRIMARY KEY,
  name  TEXT       DEFAULT NULL,
  age   INTEGER    NOT NULL DEFAULT 0
)
WITH (
  OIDS=FALSE
);
ALTER TABLE users
  OWNER TO "postgres";
