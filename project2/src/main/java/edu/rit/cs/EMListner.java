package edu.rit.cs;

import java.net.ServerSocket;

public class EMListner extends Thread {

    public EventManager em;
    public EMListner(EventManager em){
        this.em = em;
    }
    public void run(){
        //System.out.println("Starting the EMLListner");
        try {
            while (true) {

                ServerSocket servsocket = new ServerSocket(11000);
                new packetanalyzer(servsocket.accept(),this.em).start();
                servsocket.close();
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
