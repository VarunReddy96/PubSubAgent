package edu.rit.cs;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ConcurrentHashMap;

public class Echolistner extends Thread {
    public EventManager em;
    public Echolistner(EventManager em){
        this.em = em;
    }
    public void run(){
        while(true){
            System.out.println("Starting the Echolistner");
            try {
                byte[] buff = new byte[256];
                DatagramSocket socket = new DatagramSocket(9020);
                DatagramPacket packet = new DatagramPacket(buff,buff.length);
                socket.receive(packet);
                new Echopacketanlyzer(packet,em).start();
                socket.close();

            }catch (Exception e){
            }
        }
    }
}
