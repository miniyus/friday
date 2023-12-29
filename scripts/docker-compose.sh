#!/bin/bash

# feature: docker compose up
# Args: docker-compose.sh $1={enviroment} $2={directory}

BASEDIR=$(dirname "$0")
compose=$1
env=$2

echo "[Docker Compose]" 

echo "option: compose=$compose"
echo "option: env=$env"

env_file=".env.$env"

if [ "$env" = "" ] || [ ! -f "$BASEDIR/../$env_file" ]; then
    env_file=".env"
fi

if [ "$compose" = "" ]; then
  file="docker-compose.yml"
else
  file="docker-compose.$compose.yml"
fi

if [ ! -f "$BASEDIR/../$file" ]; then
  echo "$BASEDIR/../$file is not exists"

  file="docker-compose.yml"
  echo "build default docker compose $file"
  if [ ! -f "$BASEDIR/../$file" ]; then
    echo "$BASEDIR/../$file is not exists"
    exit 1
  fi
fi

echo "[docker compose up] target: $file"

docker_compose="docker compose"

if ! docker info >/dev/null 2>&1; then
  echo "Docker is not running or not installed" >&2
  exit 2
fi

if ! $docker_compose >/dev/null 2>&1; then
  echo "Docker Compose is not installed" >&2
  exit 3
fi

if [ ! -f "$BASEDIR/../$env_file" ]; then
  echo "you must generate $BASEDIR/../$env_file file"
  exit 4
fi

if [ "$file" = "" ]; then
  echo "$docker_compose up -d --build"
  cd "$BASEDIR/../" && $docker_compose up -d --build
else
    echo "$docker_compose -f $file --env-file $env_file up -d --build"
    cd "$BASEDIR/../" && $docker_compose -f $file --env-file $env_file up -d --build
fi
