#!/bin/bash

env=$1
mod=$2
withTest=$3

TAG=$env
backendPath="friday-api"
frontendPath="friday-ui"

echo "[Docker image build]"

if [ -d .docker ]; then
  rm -rf .docker/*
else
  mkdir .docker
fi

if [ "$mod" = "" ]; then
  echo "mod argument is required"
  exit 1
fi

if [ "$mod" = "backend" ]; then
  echo "* build backend"

  if [ "$withTest" = "--withTest" ]; then
    echo "* test backend"
    command=("$backendPath/gradlew acceptanceTest -p $backendPath")
    echo $command
    $command
  fi

  gradleOpt="-x test"
  command=("$backendPath/gradlew clean build -p $backendPath $gradleOpt")
  echo $command
  $command

  echo "* start docker build"
  docker build -t friday-api:$TAG \
  --build-arg="JAR_FILE=friday.jar" \
  -f $backendPath/Dockerfile $backendPath
fi

if [ "$mod" = "frontend" ]; then
  echo "* build frontend"

  if [ "$withTest" = "--withTest" ]; then
    echo "* test frontend"
    npm run --prefix $frontendPath test
  fi

  npm ci --legacy-peer-deps --prefix $frontendPath
  npm run --prefix $frontendPath build

  echo "* start docker build"
  docker build -t friday-ui:$TAG \
  -f $frontendPath/Dockerfile $frontendPath
fi
