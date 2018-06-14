# ihm-code-exercise

Simple RESTful API that provides ability to manage advertisers collection

Author
-
[Yury Zhyshko](mailto:yzhishko@gmail.com) 
 
Languages
-
Java

Dependencies
-

- Java Development Kit 8 or later
- Docker 1.13.x

If you have not H2 installed:

```bash
docker run -d -p 1521:1521 -p 81:81 -v /path/to/local/data_dir:/opt/h2-data --name=MyH2Instance oscarfonts/h2
```
After that DB access point will be available at <http://localhost:1521>.
<br/>H2 UI  <http://localhost:81>

Running
-

###docker run

- clones repository
- performs build
- creates a Docker image
- launch the application

```bash
git clone https://github.com/yzhishko/ihm-code-exercise
cd ihm-code-exercise
./gradlew build docker
docker run -p 8080:8080 -p 8090:8090 --net host -t com.code.api/advertise
```

###test

```bash
./gradlew clean test jacocoTestReport
```
Tests results are available in ${projectDir}/build/reports

API
-

### Swagger

After application is up and running Swagger UI is available at <http://localhost:8080/swagger-ui.html>
<br/>
Contains the description of all endpoints to access.

###Actuator

Available at <http://localhost:8090/actuator>
<br/>
Flyway: <http://localhost:8090/actuator/flyway>
<br/>
Info: <http://localhost:8090/actuator/info>