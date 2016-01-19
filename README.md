# swagger-karyon

The goal of this project is to demonstrate integrating Swagger with [Karyon](https://github.com/Netflix/karyon/tree/3.x).

## Pre-requisites

* [Java 8] (http://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html) 
* [Gradle] (http://gradle.org/)
* [Node.js] (https://nodejs.org/en)

## Running the app

1. gradlew tomcatRun
	* This builds and deploys the app on an embedded tomcat instance listening on port 9090
2. gradlew installSwaggerUiViewer
	* This installs the node app used to serve the swagger ui on port 3000
3. Browse to http://localhost:3000/view/?url=http://localhost:9090/karyon/swagger.json

### Issues found
1. Previously raised issue [#1068] (https://github.com/swagger-api/swagger-core/issues/1068) still exists. This prevents app startup with filter based deployment. See AppModule.java for example
2. Broken detection of jersey sub-resources in generated swagger.json resulting in invalid listing 
