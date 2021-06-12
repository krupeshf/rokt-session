# Session Service

## What is session service
This service filter sessions from a raw data file and returns response as JSON via api call

## Assumptions
* Build system needs to have java and docker daemon running
* Raw data file is available on the system (mounted or local storage)
* Data is stored in ascending order of Date field (existing assumption)
* Response to the service is not bigger than max memory given to java process
    * If the response is bigger, please update `-Xmx128m`

## Tech stack
* Java 14
* Spring Boot Framework
* Docker

## How its implemented
* Service is a web based where api call is made and it returns JSON response
* Controller accepts the call, parse requested dates into valid Objects
* Controller sends request to Service where data processing happens
* Service reads single line at a time, starts storing the response, as soon as to time is reached, stops reading file.
* Service returns the response to Controller
* Controller returns JSON response (lombok is used to serialize Java Class in JSON Object)

## Run Tests
Spring boot starter package contains maven wrapper which can test and build packages  
To test unit and integration tests, run `./mvnw test` on command line

## Build
Build artifact is a docker image.  
* First create package using `./mvnw package`
* Build docker image `docker build . -t session:<image_tag>`

## Test service locally
* Run docker image created as part of build system
  ```shell
  docker run --rm --name session --publish 8080:8080 --volume <source_location_of_test_file>:<destination_location>:ro session:<image_tag>
  
  example:
  docker run --rm --name session --publish 8080:8080 --volume /Users/krupeshf/temp/processSession/sample3.txt:/app/sample.txt:ro session:latest
  ```
  detached mode
  ```shell
  docker run --detach --rm --name session --publish 8080:8080 --volume <source_location_of_test_file>:<destination_location>:ro session:<image_tag>
  
  example:
  docker run --detach --rm --name session --publish 8080:8080 --volume /Users/krupeshf/temp/processSession/sample3.txt:/app/sample.txt:ro session:latest
  ```
* Request data
  * Assumption here is the response size is smaller than java memory - this is to show that the full file is not loaded in the memory
    * If we want response size bigger, please increase max memory given to java process
  ```shell
  curl --location --request GET 'http://localhost:8080/?path=<destination_location>&fromDateTime=<from_time_in_ISO_format>&toDateTime=<to_time_in_ISO_format>'
  
  example:
  curl --location --request GET 'http://localhost:8080/?path=/app/sample.txt&fromDateTime=2000-01-01T13:13:53Z&toDateTime=2000-01-01T13:13:53Z'
  ```

## Future considerations
* Change the fetch time from `O(n)` to `O(log n)`
  * Update linear fetch service to use binary search
  * We can implement using `RandomAccessFile`
  * Some work has already been laid out using `FetchService` interface
* Use [JIB](https://cloud.google.com/java/getting-started/jib) for docker image creation
  * This skips steps to install docker on the build system and thus making build system more secured
* Datadog to collect logs and metrics
  * include datadog as part of maven dependency
  * set java runtime arguments `-javaagent:dd-java-agent.jar`
* Do not store response in memory
  * Stream response using `StreamingResponseBody`
* Fetch file from a website or some storage bucket

## Where is source code available
Latest source code is available at [krupeshf/rokt-session](https://github.com/krupeshf/rokt-session)  
All the commits and progress can be tracked
