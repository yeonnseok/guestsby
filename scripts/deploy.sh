#!/bin/bash

REPOSITORY=/home/ubuntu/server
APP_NAME=brtrip

echo "> Build new docker image"
docker build -t $APP_NAME .

echo "> Deploy new app container"
docker run -p 8080:8080 -it -d --name $APP_NAME /bin/bash

echo "[$(date)] server deploy" >> /home/ubuntu/deploy.log