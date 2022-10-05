#!/bin/sh

USER_ARGS="-Dspring.profiles.active=local -Dapi.id=61518ff9-6f51-11ec-899e-04d4c455d54a -Dapi.pwd=@us3r_@pi!09.123 -Dspring.datasource.username=api -Dspring.datasource.password=r00t"

echo $USER_ARGS

nohup java $USER_ARGS -jar user*.jar > /home/ubuntu/user.out &

echo "USER RUNNING!!!"
