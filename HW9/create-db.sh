#!/bin/bash
#

export PG_USER='postgres'
export PG_PORT=5432
export DB_NAME='otus_test_db'
export PGPASSWORD='postgres'

run() {
  $*
  if [ $? -ne 0 ]
  then
    echo "$* failed with exit code $?"
    exit 1
  else
    return 0
  fi
}

if [ -z "$1" ]
then
    echo "Create database on localhost"
    export PG_HOST=localhost
else
    export PG_HOST=$1
fi

echo "Create database"
psql -f sql_scripts/create-db.sql -h $PG_HOST -p $PG_PORT -U $PG_USER

echo "Create table"
psql -f sql_scripts/create-table.sql -h $PG_HOST -p $PG_PORT -d $DB_NAME -U $PG_USER

psql -h $PG_HOST -p $PG_PORT -U $PG_USER -d $DB_NAME -c "GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;"
psql -h $PG_HOST -p $PG_PORT -U $PG_USER -d $DB_NAME -c "GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO postgres;"
psql -h $PG_HOST -p $PG_PORT -U $PG_USER -d $DB_NAME -c "GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO postgres;"