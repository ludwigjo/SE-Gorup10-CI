# SE-Group10-CI

A CI server built to handle Java Maven projects.  

The program contains a few core features. Which are described bellow. The server is based on the skeleton project that we were provided in the beginning and is present in the ContinuousIntegrationServer.java file. 

This is the flow the server is using:

A post request is received as a webhook from GitHub with information about the pull request. The relevant information in the payload from the webhook such as repository name, branch and commit SHA is then parsed. With the information about the build, the repository is cloned and checked out to a temp directory handled by the GitHandler.java. When the branch is ready, the CompileHandler derived from the BuildHandler is compiling the project. The test suite in the project is the executed by the TestHandler.java. Once that is done, a notification is sent with the result to the specified git commit with the NotificationHandler.java. 

Since we are aiming for P+, history of the builds are stored and available on the server. This is done with the HistoryHandler.java. As soon as a notification is delivered, the build information is stored on our server. This build info is later accessible through a get request on the pattern /history/reponame/commit-id.

## How to run

For first time users of the server, run the shell script with a port number and ngrok auth token:

    ./run_ngrok.sh PORT_NUMBER AUTH_TOKEN

After running the program for the first time, the shell script can be called subsequent times with only the port number:

    ./run_ngrok.sh PORT_NUMBER

The Notification handler is using GitHub credentials to send the notifications. The values of these credentials are stored in a .env file in the resources folder of the project. This folder will be added in the build. A GitHub username is needed which is called `GITHUB_USER`  and a token called `GITHUB_TOKEN`. The token can be created under [developer settings](https://github.com/settings/tokens).


## Assessment of Team (Essence)
_As of February 10th._

Many processes which aid in the progress of the team were set up in the beginning of the course, such as meeting and getting to know each other, getting to know each other's strengths and weaknesses, and setting up channels for effective communications. During the first meeting we had about this assignment we went over the lab description, if we wanted to attempt the P+ tasks and how to best split up the different tasks. Since then we have continuously added new issues to better understand what is needed to be done to meet the requirements of each task. The work has been done both individually and together as a group, as we found this approach to work well for us during the previous assignment.

We would thus argue that we are somewhere between Collaborating and Performing. We still have some items left on the checklist from previous states (such as “Mechanisms to grow the team are in place” from state 1 (Seeded)). While all items in the Collaborating state are checked and some in the Performing state, we argue that we are in between as we are missing some items in the previous states. To fully reach the Performing state we should focus on refining the team structure and responsibilities so we can become a better team; however, since we have chosen a flat structure in our team it can be difficult to decide who is responsible for doing these things.
