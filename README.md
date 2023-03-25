# Begin

## Description

The goal of this project is to refactor and improve the code of Minecraft 1.7.3 beta. The original codebase is known for being complex and difficult to work with, but this project aims to make it more accessible for developers who want to explore and experiment with the beta version.

## Features

- The client and server are now separated into two different modules: projectClient and projectServer, and common code is in the projectCommon module;
- The project has been rewritten to use LWJGL 3 instead of LWJGL 2, which means it can be launched with new Java versions;
- A bunch of optimizations have been implemented;
- Fixed loading of skins and capes from the Mojang servers.

## How to Run
1. Clone the repository to your machine. 
2. Open a terminal or command prompt and navigate to the root directory of the project.
3. Run the following commands in the terminal or command prompt:
* To run client `gradlew projectClient:run --args="username"`;
* To run server `gradlew projectServer:run`.
4. The Minecraft server and client will be launched in the jars/ directory, by default.

## License

All rights to the Minecraft project belong to Mojang. 
The Hypnosis package is licensed under the [MIT License](https://opensource.org/licenses/MIT).