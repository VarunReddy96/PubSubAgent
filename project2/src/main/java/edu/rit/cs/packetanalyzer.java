package edu.rit.cs;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class packetanalyzer extends Thread {

    public Socket socket;
    public EventManager em;
    public packetanalyzer(Socket socket,EventManager em){
        this.socket = socket;
        this.em = em;
    }

    public void run(){
        ObjectInputStream inputstream = null;
        try {
            inputstream = new ObjectInputStream(socket.getInputStream());
            String message = (String)inputstream.readObject();
            String[] vals = message.split(":");

            System.out.println("Before if statement: "+em.networkmap);
            if(em.networkmap.containsKey(vals[0])){
                System.out.println("in if statement");
                if(em.messagequeue.containsKey(vals[0])){
                    for(InetAddress ip: em.messagequeue.get(vals[0]).keySet()){
                        System.out.println("Sending Missed messages back to the clients");
                        new SendNotifications(ip,em,em.messagequeue.get(vals[0]).get(ip),vals[0]).start();
                    }
                }
            }else{

                ConcurrentHashMap<InetAddress,Long> pubsubdetails = new ConcurrentHashMap<>();
                pubsubdetails.put(InetAddress.getByName(vals[1].substring(1)), System.currentTimeMillis());
                em.networkmap.put(vals[0],pubsubdetails);
                for(String s: em.networkmap.keySet()){
                    System.out.println(s+"in packetanalyzer:-------"+em.networkmap.get(s));
                }
                System.out.println();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }



    }
}
