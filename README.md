# SE-Gorup10-CI

CI server implementation

## How to run

For first time users of the server, run the shell script with a port number and ngrok auth token:

    ./run_ngrok.sh PORT_NUMBER AUTH_TOKEN

After running the program for the first time, the shell script can be called subsequent times with only the port number:

    ./run_ngrok.sh PORT_NUMBER

The Notification handler is using GitHub credentials to send the notifications. The values of these credentials are stored in a .env file in the root of the project. A GitHub username is needed which is called `GITHUB_USER`  and a token called `GITHUB_TOKEN`. The token can be created under [developer settings](https://github.com/settings/tokens).