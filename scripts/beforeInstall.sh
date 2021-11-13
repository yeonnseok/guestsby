#!/bin/bash

REPOSITORY=/home/ubuntu/server
APP_NAME=brtrip

if [ -d $REPOSITORY/build ]; then
  echo "> Remove the previous build files and create new one"
  rm -rf $REPOSITORY/build
  mkdir -vp $REPOSITORY/build
fi

echo "> Down and remove the existing container and image"
docker stop $APP_NAME
sleep 5
docker rm $APP_NAME
sleep 5
docker rmi -f $(docker images -q)
sleep 5