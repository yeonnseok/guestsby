#!/bin/bash

REPOSITORY=/home/ubuntu/server

if [ -d $REPOSITORY/build ]; then
  echo "> Remove the previous build files and create new one"
  rm -rf $REPOSITORY/build
  mkdir -vp $REPOSITORY/build
fi

echo "> Down and remove the existing container and image"
docker stop app
docker rm app