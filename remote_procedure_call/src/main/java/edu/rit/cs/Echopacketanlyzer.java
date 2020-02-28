package edu.rit.cs;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

public class Echopacketanlyzer extends Thread {

    public EventManager em;
    public DatagramPacket packet;
    public Echopacketanlyzer(DatagramPacket packet,EventManager em){
        this.em = em;
        this.packet = packet;
    }
    public void run(){
        String message = new String(packet.getData());
        String[] vals = message.split(":");
        ConcurrentHashMap<InetAddress,Long> pubsubdetails = new ConcurrentHashMap<>();
        try {
            System.out.println("The value of "+vals[1]+ " comlete value :"+vals);
            Long start = System.currentTimeMillis();
            pubsubdetails.put(InetAddress.getByName(vals[1].substring(1)), start);
            System.out.println("The values placed in pubsubdetails are ip: "+vals[1]+ "time: "+start);
            em.networkmap.put(vals[0],pubsubdetails);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println("pubsubdetails: "+pubsubdetails);

        //em.networkmap.put(vals[0],pubsubdetails);
        System.out.println("In echoListner : ");
        for(String s: em.networkmap.keySet()){
            System.out.println("HERERERERE");
            System.out.println(em.networkmap.get(s));
        }
        System.out.println(em.networkmap);
        System.out.println();
    }
}
