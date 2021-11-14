#!/bin/bash

REPOSITORY=/home/ubuntu/server

if [ -d $REPOSITORY/build ]; then
  echo "> Remove the previous build files and create new one"
  rm -rf $REPOSITORY/build
fi

mkdir -vp $REPOSITORY/build

if [ -n "$(docker ps -q -f name=app)" ]; then
  echo "> stop app container"
  docker stop app
  if [ -n "$(docker ps -aq -f status=exited -f name=app)" ]; then
    echo "> remove app container"
    docker rm app
  fi
fi