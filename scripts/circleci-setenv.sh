#!/bin/bash

echo "[Circleci export env vars]"

if [ "$CIRCLE_BRANCH" = "main" ]; then
  echo 'export REMOTE_SERVER=$PROD_SERVER' >> "$BASH_ENV" \
  && echo 'export REMOTE_DIR=main' >> "$BASH_ENV" \
  && echo 'export TAG=prod' >> "$BASH_ENV" \
  && echo 'export SERVER_URL=$PROD_URL' >> "$BASH_ENV" \
  && echo 'export REMOTE_ENV=prod' >> "$BASH_ENV"
elif [[ "$CIRCLE_BRANCH" = release* ]]; then
  echo 'export REMOTE_SERVER=$STAGE_SERVER' >> "$BASH_ENV" \
  && echo 'export REMOTE_DIR=release' >> "$BASH_ENV" \
  && echo 'export TAG=stage' >> "$BASH_ENV" \
  && echo 'export SERVER_URL=$STAGE_URL' >> "$BASH_ENV" \
  && echo 'export REMOTE_ENV=stage' >> $BASH_ENV
else
  echo 'export REMOTE_SERVER=$DEV_SERVER' >> "$BASH_ENV" \
  && echo 'export REMOTE_DIR=$CIRCLE_BRANCH' >> "$BASH_ENV" \
  && echo 'export TAG=dev' >> "$BASH_ENV" \
  && echo 'export SERVER_URL=$DEV_URL' >> "$BASH_ENV" \
  && echo 'export REMOTE_ENV=dev' >> $BASH_ENV
fi


