package edu.rit.cs;

import java.net.ServerSocket;

public class OperationsListner extends Thread {

    public EventManager em;
    public OperationsListner(EventManager em){
        this.em = em;
    }
    public void run(){
        try {
            while (true) {
                ServerSocket servsocket = new ServerSocket(10001);
                new operationspacketanalyzer(servsocket.accept(),this.em).start();
                servsocket.close();
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
