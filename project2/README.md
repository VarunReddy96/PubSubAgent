#pubsub system launching instructions

To compile and build a .jar
```
mvn package
```
Step 1) Build a docker image from the Dockerfile using the command 

```
docker build -t csci652:latest .
```



Start Event Manager 
```
java -cp target/project2-1.0-SNAPSHOT.jar edu.rit.cs.EventManager
```

Start PubsubAgent
```
java -cp target/remote_procedure_call-1.0-SNAPSHOT.jar edu.rit.cs.PubSubAgent localhost 10001
```

Server Output
```
The server is running.
{"method":"getDate","id":0,"jsonrpc":"2.0"}
```

Client outputs the date on the server. For example,
```
Sep 19, 2019  
```