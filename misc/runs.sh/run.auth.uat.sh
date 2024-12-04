#!/bin/sh

AUTH_ARGS="-Dspring.profiles.active=local -Dspring.datasource.username=api -Dspring.datasource.password=r00t -Dapi.smtp.username=henrique.prosa@hotmail.com -Dapi.smtp.password=E2aOUTCWckdRPNqw -Dapi.jwt.secret=be6ef889fe911ecb5c904d4c455d54a"
echo $AUTH_ARGS

nohup java $AUTH_ARGS -jar auth*.jar >/dev/null 2>&1&

echo "AUTH RUNNING!!!"
