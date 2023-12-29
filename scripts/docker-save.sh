#!/bin/bash

mod=$1

echo "[Docker save image]"

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

  echo "* save friday-api image"
  docker save friday-api -o .docker/friday-api.tar.gz
fi

if [ "$mod" = "frontend" ]; then
  echo "* save friday-ui image"
  docker save friday-ui -o .docker/friday-ui.tar.gz
fi
