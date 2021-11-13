#!/bin/bash

REPOSITORY=/home/ubuntu/server
cd $REPOSITORY

APP_NAME=brtrip

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z "$CURRENT_PID" ]
then
  echo "> 종료할 것 없음." >> /home/ubuntu/deploy.log
else

if [ -d $REPOSITORY/build ]; then
  rm -rf $REPOSITORY/build
  mkdir -vp $REPOSITORY/build
fi

docker stop $APP_NAME
sleep 5
docker rm $APP_NAME
sleep 5

if [[ "$(docker images -q mingukang-kr/$APP_NAME:latest 2> /dev/null)" != "" ]]; then
  docker rmi -f $(docker images -q)
fi

fi

echo "> $JAR_PATH 배포"
docker pull $APP_NAME
docker run --env-file /home/ubuntu/.env --publish 8080:8080 -it --detach --name $APP_NAME /bin/bash

echo "[$(date)] server deploy" >> /home/ubuntu/deploy.log