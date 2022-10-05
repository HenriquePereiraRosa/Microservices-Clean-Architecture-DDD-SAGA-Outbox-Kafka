#!/bin/sh

nohup java -jar gateway*.jar >/dev/null 2>&1&
echo "GATEWAY RUNNING!!!"
