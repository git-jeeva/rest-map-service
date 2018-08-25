# RESTful API for Map Service
Simple REST Service using Spring Boot to find if the given cities are connected by roads.

## Installation
##### Source Repository
* GitHub URL: <http://github.com/git-jeeva/rest-map-service>
* Use Git clone or checkout with SVN using the web URL - <https://github.com/git-jeeva/rest-map-service.git>

##### Requirements:
* [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven 3.3.9](https://maven.apache.org)

##### Build Instructions
Please make sure JDK and Maven are in classpath and path.
  ```
  mvn clean install
  ```

##### Options for Running the Application
* *Option 1*: Execute the main method in the com.map.MapApplication class from an IDE.
* *Option 2*: Use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html):
  
  ```
  mvn spring-boot:run
  ```
* *Option 3*: Run using java
  ```
  java -jar <path/to/rest-map-service-0.0.1-SNAPSHOT.jar>

## Usage
The application endpoint can be accessed using a web browser

* Exposed endpoint
    * <http://localhost:8080/connected?origin=city1&destination=city2>
* Additional helpful endpoint to view connection path for a given city
    * <http://localhost:8080/connections?origin=city>
* Sample Request/Responses
    * <http://localhost:8080/connected?origin=Boston&destination=Newark> (yes)
    * <http://localhost:8080/connected?origin=Boston&destination=Philadelphia> (yes)
    * <http://localhost:8080/connected?origin=Philadelphia&destination=Albany> (no)
    * <http://localhost:8080/connections?origin=boston> (Boston - New York - Newark - Philadelphia)

## Test Coverage Report (Jacoco)
   * Coverage rport is generated during the build process and can be accessed from the following development location: 
   * <path/to/rest-map-service>/target/jacoco-ut/index.html

## REST API Documentation (Swagger)
REST API documentation is generated using Swagger and it can be accessed at:
   * <http://localhost:8080/swagger-ui.html>

## Operations (spring-boot-actuator)
Spring Boot Actuator is used to provide basic health and metric endpoints which can be accessed using the following URLs.
   * <http://localhost:8080/actuator/health>
   * <http://localhost:8080/actuator/metrics>
   * <http://localhost:8080/actuator/info>
 
## Design Notes & Technologies Used

##### Technical stack:
   * HTTP GET Request -> RestController -> MapService -> Business Logic

##### Data structure & Algorithm:
   * Cities are modeled as Nodes in a Graph.
   * Breadth-first search is used to find paths between cities (which are modeled as Nodes in the Graph) and 
    if any direct or transitive path exists, then they are considered as connected cities.
   * MapService implements the breadth first search traversal algorithm.
   * *Complexity*: The Breadth-First search has a worst case time complexity of O(|E| + |V|) and worst case space complexity of O(|V|),
    where cities (nodes) are denoted by Vertices (V) and connections between cities (roads) are denoted by edges (E)
   
##### Error handling
   * If there is an error while accessing the configuration file - city.txt,
   then an exception is thrown during application startup and it is gracefully handled 
   by displaying a friendly message on the console. (instead of the annoying stack traces)
   * Also if the required origin or destination city names are not provided in the request, then 
   an appropriate messages is returned.
   * Additionally, if the URL is accessed without providing any city name, then instead of returning 
   the default error page, a custom error message  is returned.
   * Error messages are configured in messages.properties to support i18n.

##### Logging
   * Logging is minimally used to log the application startup and it is handled by JUL Logger.

##### Testing
   * SpringBootTest based tests are used to test both the service (unit testing) and the REST endpoints 
   (integration testing).
   * Tested for given happy path and negative scenarios. 
   * Also tested with additional positive and negative cases along with validation cases 
   (for missing origin or destination or both) which return appropriate messages.

##### Test Coverage
   * Jacoco is used to analyse test coverage and produce report.

##### REST Clients
   * GET requests can be issued from any browser or other tools such as Postman, Swagger-ui.

##### Documentation 
   * swagger2 is used to generate REST API documentation and it can be visually accessed using swagger-ui. 

##### Operations: 
   * Spring Boot Actuator is used to provide basic health and metric endpoints

##### Development Tools
   * Developed and tested with JDK 1.8 / Maven 3.3.9 / Spring Boot 2.0.4 / IntelliJ 2016    