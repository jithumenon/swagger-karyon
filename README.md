# swagger-karyon

The goal of this project is to demonstrate integrating [Swagger] (http://swagger.io/) with [Karyon](https://github.com/Netflix/karyon/tree/3.x). Ultimately, this could be added to [swagger-samples] (https://github.com/swagger-api/swagger-samples).

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

### Why use Node.js?
Without a fix for issue [#1068] (https://github.com/swagger-api/swagger-core/issues/1068), the webapp cannot serve the static resources associated with swagger-ui that lives under webapp/docs. So, to demonstrate the busted sub-resource discovery in Swagger, we are using Node.js to serve the static resources. Once issue [#1068] (https://github.com/swagger-api/swagger-core/issues/1068) is fixed, the app can be deployed using a filter, which will in turn serve the included swagger-ui under localhost:9090/karyon/docs.