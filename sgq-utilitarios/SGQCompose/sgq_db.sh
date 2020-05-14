#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE "sgq-incidentes";
    CREATE DATABASE "sgq-relatorios";
    CREATE DATABASE "sgq-transparencia";
    GRANT ALL PRIVILEGES ON DATABASE "sgq-incidentes" TO sgq;
    GRANT ALL PRIVILEGES ON DATABASE "sgq-relatorios" TO sgq;
    GRANT ALL PRIVILEGES ON DATABASE "sgq-transparencia" TO sgq;
EOSQL