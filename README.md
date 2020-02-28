#pubsub system launching instructions

To compile and build a .jar
```
mvn package
```
Step 1) Build a docker image by using docker compose

```
docker-compose up
```

Step 2) Create a docker container using the image created and go to the bash
```
docker run -it csci652:latest bash
```
Step 3) In the container start the Event Manager.(Important!!)

```
java -cp target/project2-1.0-SNAPSHOT.jar edu.rit.cs.EventManager
```

Step 4) Open as many new terminal to run PubSubAgents
 and in each terminal
 
*) to run publisher or subscriber.
```
docker run -it csci652:latest bash
java -cp target/remote_procedure_call-1.0-SNAPSHOT.jar edu.rit.cs.PubSubAgent localhost 10001
```

select the options accordingly when prompted.


Server Output
```
The server is running.
{"method":"getDate","id":0,"jsonrpc":"2.0"}
```

Client outputs the date on the server. For example,
```
Sep 19, 2019  
```