#!/bin/sh

AUTH_ARGS="-Dspring.profiles.active=local -Dspring.datasource.username=api -Dspring.datasource.password=r00t -Dapi.smtp.username=henrique.prosa@hotmail.com -Dapi.smtp.password=E2aOUTCWckdRPNqw -Dapi.jwt.secret=be6ef889fe911ecb5c904d4c455d54a"
GATEWAY_ARGS="-Dspring.profiles.active=local"
USER_ARGS="-Dspring.profiles.active=local -Dapi.id=61518ff9-6f51-11ec-899e-04d4c455d54a -Dapi.pwd=@us3r_@pi!09.123 -Dspring.datasource.username=api -Dspring.datasource.password=r00t"
BRAND_ARGS="-Dspring.profiles.active=local -Dapi.id=615404d5-6f51-11ec-899e-04d4c455d54a -Dapi.pwd=9343HYDF.8nfcd -Dspring.datasource.username=api -Dspring.datasource.password=r00t"

echo $AUTH_ARGS
echo $USER_ARGS
echo $BRAND_ARGS
echo $GATEWAY_ARGS


nohup java -jar discovery*.jar >/dev/null 2>&1&
nohup java $GATEWAY_ARGS -jar gateway*.jar > /home/ubuntu/gateway.out &
nohup java $AUTH_ARGS -jar auth*.jar > /home/ubuntu/auth.out &
nohup java $USER_ARGS -jar user*.jar > /home/ubuntu/user.out &
nohup java $BRAND_ARGS -jar brand*.jar > /home/ubuntu/brand.out &

echo "SERVERS RUNNING!!!"
