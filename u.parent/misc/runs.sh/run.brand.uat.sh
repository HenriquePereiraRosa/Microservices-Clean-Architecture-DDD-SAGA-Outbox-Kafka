#!/bin/sh

BRAND_ARGS="-Dspring.profiles.active=local -Dapi.id=615404d5-6f51-11ec-899e-04d4c455d54a -Dapi.pwd=9343HYDF.8nfcd -Dspring.datasource.username=api -Dspring.datasource.password=r00t"

echo $BRAND_ARGS

nohup java $BRAND_ARGS -jar brand*.jar >/dev/null 2>&1&

echo "BRAND RUNNING!!!"
