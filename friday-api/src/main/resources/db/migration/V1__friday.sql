CREATE TYPE USER_ROLE AS ENUM ('ADMIN', 'MANAGER', 'USER');

CREATE TABLE
    "auth_user"
(
    "id"         bigserial PRIMARY KEY,
    "sns_id"     varchar(100),
    "provider"   varchar(20),
    "email"      varchar(100) UNIQUE NOT NULL,
    "name"       varchar(50)         NOT NULL,
    "password"   varchar(50)         NOT NULL,
    "role"       USER_ROLE           NOT NULL DEFAULT 'USER',
    "created_at" timestamp,
    "updated_at" timestamp,
    "deleted_at" timestamp
);

CREATE TABLE
    "host"
(
    "id"          bigserial PRIMARY KEY,
    "user_id"     bigint       NOT NULL,
    "host"        varchar(100) NOT NULL,
    "summary"     varchar(50)  NOT NULL,
    "description" varchar(100) NOT NULL,
    "path"        varchar(100) NOT NULL,
    "publish"     boolean      NOT NULL,
    "created_at"  timestamp,
    "updated_at"  timestamp,
    "deleted_at"  timestamp
);

CREATE TABLE
    "search"
(
    "id"          bigserial PRIMARY KEY,
    "host_id"     bigint       NOT NULL,
    "query_key"   varchar(20),
    "query"       varchar(100) NOT NULL,
    "description" varchar(100) NOT NULL,
    "publish"     boolean      NOT NULL,
    "created_at"  timestamp,
    "updated_at"  timestamp,
    "deleted_at"  timestamp
);

CREATE TABLE "file"
(
    "id"         bigserial PRIMARY KEY,
    "mime_type"  varchar(50)  NOT NULL,
    "size"       int          NOT NULL,
    "path"       varchar(255) NOT NULL,
    "extension"  varchar(20)  NOT NULL,
    "created_at" timestamp,
    "updated_at" timestamp,
    "deleted_at" timestamp
);

CREATE TABLE "search_file"
(
    "id"         bigserial PRIMARY KEY,
    "search_id"  bigint NOT NULL,
    "file_id"    bigint NOT NULL,
    "created_at" timestamp,
    "updated_at" timestamp,
    "deleted_at" timestamp
);

COMMENT ON TABLE "auth_user" IS 'user table';

COMMENT ON COLUMN "auth_user"."sns_id" IS 'sns id';

COMMENT ON COLUMN "auth_user"."provider" IS 'sns provider';

COMMENT ON COLUMN "auth_user"."email" IS 'email';

COMMENT ON COLUMN "auth_user"."name" IS 'user"s name';

COMMENT ON COLUMN "auth_user"."password" IS 'user password';

COMMENT ON COLUMN "auth_user"."role" IS 'role';

COMMENT ON TABLE "host" IS 'host';

COMMENT ON TABLE "search" IS 'search';

COMMENT ON TABLE "file" IS 'file';

COMMENT ON TABLE "search_file" IS 'search file';

ALTER TABLE "host"
    ADD
        FOREIGN KEY ("user_id") REFERENCES "auth_user" ("id");

ALTER TABLE "search"
    ADD
        FOREIGN KEY ("host_id") REFERENCES "host" ("id");

ALTER TABLE "search_file"
    ADD
        FOREIGN KEY ("search_id") REFERENCES "search" ("id");

ALTER TABLE "search_file"
    ADD
        FOREIGN KEY ("file_id") REFERENCES "file" ("id");
