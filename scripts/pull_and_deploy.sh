#!/bin/bash

#pull latest changes, build app and deploy on port $1

if [ $# -eq 1 ]; then
        PORT_NUMBER=$1

        #pull latest changes of this branch
        git pull
        #build project on this branch
        mvn compile
        #deploy to port $1
        nohup mvn exec:java -D"exec.mainClass"="com.group10.CI.ContinuousIntegrationServer" -Dexec.args="$1" &>> log.log &

else
        echo "Argument missing: You need to provide port to runt ngrok server"
fi