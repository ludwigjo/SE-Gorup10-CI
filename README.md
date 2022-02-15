# SE-Gorup10-CI

CI server implementation

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
