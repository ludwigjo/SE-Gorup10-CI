#!/bin/bash



run_mvn_and_ngrok() {
	# Run maven project and start ngrok tunnel
        # Don't display maven status but ngrok window
        mvn exec:java -D"exec.mainClass"="com.group10.CI.ContinuousIntegrationServer" -Dexec.args="$1" &> /dev/null &
        bg_pid=$!
        $PWD/ngrok http $1
        kill "$bg_pid"
}


# Needs a port number to run
if [ $# -eq 2 ]; then
	PORT_NUMBER=$1
	AUTH_TOKEN=$2

	# Downloads ngrok is not detected, downloads it
	if [ ! -f "ngrok" ]; then
		echo "--- ngrok not detected ---"
		echo "Downloading ngrok..."
		curl  -LO --tlsv1 "https://bin.equinox.io/c/4VmDzA7iaHb/ngrok-stable-linux-amd64.zip"   && \
		unzip ngrok-stable-linux-amd64.zip                                											&& \
		rm ngrok-stable-linux-amd64.zip
	fi
	
	# Configure ngrok with AUTH_TOKEN
	$PWD/ngrok authtoken $2

	# Run mvn project and ngrok tunnel
	run_mvn_and_ngrok $1 

elif [ $# -eq 1 ]; then
	PORT_NUMBER=$1

	# Expects that ngrok is already downloaded and auth
	if [ ! -f "$HOME/.ngrok2/ngrok.yml" ]; then
		echo "ngrok is not configured yet"
		echo ""
		echo "=============================================="
		echo ""
		echo "Add AUTH_TOKEN as a parameter to the call"
		echo ""
		echo '		./run_ngrok.sh PORT_NUMBER AUTH_TOKEN'
		echo ""
		exit
	fi

	# Run mvn project and ngrok tunnel
	run_mvn_and_ngrok $1

else
	echo "Argument missing: You need to provide port to runt ngrok server"
fi
