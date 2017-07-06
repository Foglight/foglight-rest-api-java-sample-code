# httpclient

This java sample is a spring boot web applicaiton, first we suggest you to read the pre-requisite. 
Then you can follow the build steps below to build this project and run the web application.

## Pre-requisite

maven 3.x
jre 1.8+

## Build Steps

  - Open command line and switch to the project folder
  - Run build command "mvn clean install"
  - Check the build jar in the target folder (suppose foglight-sample-SNAPSHOT-XXX.jar)
  - Run start application command "java -jar target\foglight-samples-SNAPSHOT-XXX.jar" to start the application.
  - Type the url "http://localhost:8082" from your browser to access to this application.

## Notice

You can change the web server listening port in <PROJECT DIR>/src/main/resources/application.properties.
The default listening port is set to 8082.

## Usage

First you need to configure the settings in Global Configuration.
Then you can navigate to other sample pages through the left navigation menu.