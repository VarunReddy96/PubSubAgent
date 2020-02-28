package edu.rit.cs;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class PubSubAgentEcho extends Thread{

    public String sendip;
    public String pubsub_id;
    public InetAddress myip;
    public PubSubAgentEcho(String sendip, String pubsub_id,InetAddress myip){
        this.sendip = sendip;
        this.pubsub_id = pubsub_id;
        this.myip = myip;
    }

    public void run() {
        DatagramSocket socket;
        try {
            String sendmessage = pubsub_id +":"+ myip;
            byte[] buff = sendmessage.getBytes();
            socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(buff,buff.length, InetAddress.getByName(sendip),9020);
            socket.send(packet);


            socket.close();

        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}
