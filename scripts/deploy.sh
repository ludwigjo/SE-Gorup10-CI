#!/bin/bash

# Deploy the app in current git branch to the port $1

if [ $# -eq 1 ]; then
        PORT_NUMBER=$1

        # compile the app from current branch
        mvn compile

        # deploy app to the port $1
        nohup mvn exec:java -D"exec.mainClass"="com.group10.CI.ContinuousIntegrationServer" -Dexec.args="$1" &>> log.log &

else
        echo "Argument missing: You need to provide port to runt ngrok server"
fi
