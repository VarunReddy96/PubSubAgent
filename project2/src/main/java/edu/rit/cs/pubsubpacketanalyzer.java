package edu.rit.cs;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class pubsubpacketanalyzer extends Thread {
    public Socket socket;
    public PubSubAgent em;
    public pubsubpacketanalyzer(Socket socket,PubSubAgent em){
        this.socket = socket;
        this.em = em;
    }

    public void run(){


        try {
            //System.out.println("Received packet");
            ObjectInputStream inputstream = new ObjectInputStream(socket.getInputStream());

            Classfinder results = (Classfinder) inputstream.readObject();
            System.out.println("the value of the received packet in pubsubpacketanalyzeris: "+results.sendclassname());
            if(results.sendclassname().equals("topic")){
                System.out.println("The topic name advertised is: "+results.getTopicname());
            }else{
                System.out.println("The event content is : "+results.getContent()+" the tile of the " +
                        "event is :"+results.gettitle()+" in topic-----: "+results.getTopicname());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
