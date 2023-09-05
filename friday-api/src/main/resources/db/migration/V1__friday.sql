CREATE TABLE
    "api_recevied_log" (
        "id" bigserial PRIMARY KEY,
        "user_id" bigint NOT NULL,
        "method" varchar(20) NOT NULL,
        "url" varchar(100) NOT NULL,
        "user_agent" varchar(20) NOT NULL,
        "request" json NOT NULL,
        "request_meta" json NOT NULL,
        "sns_id" varchar(50) NOT NULL,
        "status_code" integer NOT NULL,
        "response_meta" json NOT NULL,
        "ip" varchar(20) NOT NULL,
        "created_at" timestamp,
        "updated_at" timestamp
    );

CREATE TYPE USER_ROLE AS ENUM ('ADMIN', 'MANAGER', 'USER');

CREATE TABLE
    "auth_user" (
        "id" bigserial PRIMARY KEY,
        "sns_id" varchar(100),
        "provider" varchar(20),
        "email" varchar(100) UNIQUE NOT NULL,
        "name" varchar(50) NOT NULL,
        "password" varchar(50) NOT NULL,
        "role" USER_ROLE NOT NULL DEFAULT 'USER',
        "created_at" timestamp,
        "updated_at" timestamp,
        "deleted_at" timestamp
    );

CREATE TABLE
    "access_token" (
        "id" bigserial PRIMARY KEY,
        "user_id" bigint,
        "type" varchar(20) NOT NULL,
        "token" varchar(255) NOT NULL,
        "expiresAt" timestamp NOT NULL,
        "revoked" boolean NOT NULL,
        "created_at" timestamp,
        "updated_at" timestamp,
        "deleted_at" timestamp
    );

CREATE TABLE
    "refresh_token" (
        "id" bigserial PRIMARY KEY,
        "access_token_id" bigint,
        "type" varchar(20) NOT NULL,
        "token" varchar(255) NOT NULL,
        "expiresAt" timestamp NOT NULL,
        "revoked" boolean NOT NULL,
        "created_at" timestamp,
        "updated_at" timestamp,
        "deleted_at" timestamp
    );

CREATE TABLE
    "host" (
        "id" bigserial PRIMARY KEY,
        "user_id" bigint NOT NULL,
        "host" varchar(100) NOT NULL,
        "summary" varchar(50) NOT NULL,
        "description" varchar(100) NOT NULL,
        "path" varchar(100) NOT NULL,
        "publish" boolean NOT NULL,
        "created_at" timestamp,
        "updated_at" timestamp,
        "deleted_at" timestamp
    );

CREATE TABLE
    "search" (
        "id" bigserial PRIMARY KEY,
        "host_id" bigint NOT NULL,
        "query_key" varchar(20),
        "query" varchar(100) NOT NULL,
        "description" varchar(100) NOT NULL,
        "publish" boolean NOT NULL,
        "created_at" timestamp,
        "updated_at" timestamp,
        "deleted_at" timestamp
    );

COMMENT ON TABLE "api_recevied_log" IS 'api 수신 로그';

COMMENT ON COLUMN "api_recevied_log"."user_id" IS '계정정보 id';

COMMENT ON COLUMN "api_recevied_log"."method" IS 'HTTP Method';

COMMENT ON COLUMN "api_recevied_log"."url" IS 'Request URL';

COMMENT ON COLUMN "api_recevied_log"."user_agent" IS 'User-Agent Header';

COMMENT ON COLUMN "api_recevied_log"."request" IS 'Request Cotent';

COMMENT ON COLUMN "api_recevied_log"."request_meta" IS 'request meta';

COMMENT ON COLUMN "api_recevied_log"."sns_id" IS '로그인 계정 키';

COMMENT ON COLUMN "api_recevied_log"."status_code" IS '응답 상태 코드';

COMMENT ON COLUMN "api_recevied_log"."response_meta" IS 'response meta';

COMMENT ON COLUMN "api_recevied_log"."ip" IS 'ip address';

COMMENT ON COLUMN "api_recevied_log"."created_at" IS '생성일';

COMMENT ON COLUMN "api_recevied_log"."updated_at" IS '수정일';

COMMENT ON TABLE "auth_user" IS 'user table';

COMMENT ON COLUMN "auth_user"."sns_id" IS 'sns id';

COMMENT ON COLUMN "auth_user"."provider" IS 'sns provider';

COMMENT ON COLUMN "auth_user"."email" IS 'email';

COMMENT ON COLUMN "auth_user"."name" IS 'user"s name';

COMMENT ON COLUMN "auth_user"."password" IS 'user password';

COMMENT ON COLUMN "auth_user"."role" IS 'role';

COMMENT ON TABLE "access_token" IS 'access token';

COMMENT ON COLUMN "access_token"."type" IS 'fixed Bearer';

COMMENT ON COLUMN "access_token"."token" IS 'access token';

COMMENT ON TABLE "refresh_token" IS 'refresh token';

COMMENT ON COLUMN "refresh_token"."type" IS 'fixed Bearer';

COMMENT ON COLUMN "refresh_token"."token" IS 'access token';

COMMENT ON TABLE "host" IS 'host';

COMMENT ON TABLE "search" IS 'search';

ALTER TABLE "api_recevied_log"
ADD
    FOREIGN KEY ("user_id") REFERENCES "auth_user" ("id");

ALTER TABLE "access_token"
ADD
    FOREIGN KEY ("user_id") REFERENCES "auth_user" ("id");

ALTER TABLE "refresh_token"
ADD
    FOREIGN KEY ("access_token_id") REFERENCES "access_token" ("id");

ALTER TABLE "host"
ADD
    FOREIGN KEY ("user_id") REFERENCES "auth_user" ("id");

ALTER TABLE "search"
ADD
    FOREIGN KEY ("host_id") REFERENCES "host" ("id");