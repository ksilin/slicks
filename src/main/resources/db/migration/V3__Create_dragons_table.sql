CREATE TABLE "dragons" (
  "id"       BIGSERIAL PRIMARY KEY,
  "name" VARCHAR NOT NULL,
  "species" VARCHAR NOT NULL,
  "speed" INT NOT NULL DEFAULT 0,
  "firepower" INT NOT NULL DEFAULT 0
);