#!/bin/bash

APP_NAME=brtrip

echo "> Build new app image"
docker build -t $APP_NAME .

echo "> Deploy new app container"
docker run -it -d --name app $APP_NAME -p 8080:8080 /bin/bash