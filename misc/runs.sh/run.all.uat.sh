#!/bin/sh

DISCO_ARGS="-Dspring.profiles.active=local"
GATEWAY_ARGS="-Dspring.profiles.active=uat"
AUTH_ARGS="-Dspring.profiles.active=uat -Dspring.datasource.username=cp7chdhu9lapi9c8 -Dspring.datasource.password=qvyxo56guda7qr9k\
	-Dapi.smtp.username=henrique.prosa@hotmail.com -Dapi.smtp.password=E2aOUTCWckdRPNqw\
	-Dapi.jwt.secret=be6ef889fe911ecb5c904d4c455d54a -Dapi.front.url=http://3.88.154.158:3000"
USER_ARGS="-Dspring.profiles.active=uat -Dapi.id=61518ff9-6f51-11ec-899e-04d4c455d54a -Dapi.pwd=@us3r_@pi!09.123 -Dspring.datasource.username=e9nfeopf5fvfrmj1 -Dspring.datasource.password=ylb2qy2gr3trrapb"
BRAND_ARGS="-Dspring.profiles.active=uat -Dapi.id=615404d5-6f51-11ec-899e-04d4c455d54a -Dapi.pwd=9343HYDF.8nfcd -Dspring.datasource.username=c8fe2mi7oq5ti81l -Dspring.datasource.password=z1x2ua8cw9saj1ci"

echo $DISCO_ARGS
echo $GATEWAY_ARGS
echo $AUTH_ARGS
echo $USER_ARGS
echo $BRAND_ARGS

nohup java $DISCO_ARGS -jar discovery*.jar >/dev/null 2>&1&
nohup java $GATEWAY_ARGS -jar gateway*.jar >/dev/null 2>&1&
nohup java $AUTH_ARGS -jar auth*.jar >/dev/null 2>&1&
nohup java $USER_ARGS -jar user*.jar >/dev/null 2>&1&
nohup java $BRAND_ARGS -jar brand*.jar >/dev/null 2>&1&

echo "SERVERS RUNNING!!!"
