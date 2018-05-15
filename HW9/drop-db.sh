#!/bin/bash

export PG_USER='postgres'
export PGPASSWORD='postgres'
export DB_NAME='otus_test_db'

if [ -z "$1" ]
then
    echo "Delete database on localhost"
    export PG_HOST=localhost
else
    export PG_HOST=$1
fi

psql -h $PG_HOST -U $PG_USER -c "DROP DATABASE IF EXISTS $DB_NAME;"
