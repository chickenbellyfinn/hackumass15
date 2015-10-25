HACS: Halloween Application for Candy Seekers 🎃  🎃  🎃
====================================

Authors:
-----
* [Akshay Thejaswi](https://github.com/chickenbellyfinn)
* [Daniel Beckwith](https://github.com/dbeckwith)
* [Aditya Nivarthi](https://github.com/SIZMW)
* [Vivek Krishnamurthy](https://github.com/vivekkmurthy)

Purpose:
-----
* The purpose of this app and server is to crowdsource data from children on Halloween about different houses and locations giving out candy. The information taken from the children provide locations and the type of candy being given out at those locations.
* Users are able to send information to the server about the candy they got at each location, in order to help build the global database of candy sources.
* Users can use the web interface to view all the locations giving out candy and the types of candy given out across all the locations.

Main Features:
-----
* TODO: main features

APIs:
-----
* Android is used for the mobiel app used for providing candy and locations.
* Fitbit Food is used for providing calories and content information about the candy being received at each location.
* Bing Maps is used for getting street addresses from latitude and longitude coordinates.
* Microsoft Azure for running the global location server and web interface.
* MongoDB for storage and managing the location and user data.

Usage:
-----
To run the servers, first install NodeJS and NPM. Then install the following dependencies:
```bash
$ npm install -g harp express body-parser mongodb lodash
```
The MongoDB server should automatically start. Then from the project root, to run the web server, start it with Harp:
```bash
$ sudo harp server webui --port 80
```
To run the API server, start it with Node:
```bash
$ cd apiserver
$ node server.js
```
The Web UI is now being served over HTTP on port 80, and the API server over port 8080.
