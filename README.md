## Features

Here is some notable features and changes:
- client and server are now separated into two different modules: `projectClient` and `projectServer`,
common code is in `projectCommon` module;
- rewritten to use LWJGL 3 instead of LWJGL 2 (You can launch with new Java versions);
- bunch of optimizations.

## How to run

We use gradle to build and run the project.
To run the project, you need to run the following commands 
in the root directory of the project.

**Client**
`
gradlew projectClient:run
`

**Server**
`
gradlew projectServer:run
`

Minecraft server and client will be launched in 
`jars/` directory, by default.