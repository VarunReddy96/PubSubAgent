package edu.rit.cs;

import java.net.ServerSocket;

public class PubSubListner extends Thread {
    public PubSubAgent em;
    public PubSubListner(PubSubAgent em){
        this.em = em;
    }
    public void run(){
        try {
            while (this.em.listen) {
                ServerSocket servsocket = new ServerSocket(9030);
                new pubsubpacketanalyzer(servsocket.accept(),this.em).start();
                servsocket.close();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
