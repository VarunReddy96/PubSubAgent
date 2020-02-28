package edu.rit.cs;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class SendNotifications extends Thread {
    public EventManager em;
    public InetAddress ip;
    public Classfinder obj;
    public String macid;
    public int Qos = 1;

    public SendNotifications(InetAddress ip, EventManager em, Classfinder obj, String macid){
        this.ip = ip;
        this.em = em;
        this.obj = obj;
        this.macid = macid;
    }

    public SendNotifications(InetAddress ip, EventManager em, Classfinder obj, String macid,int Qos){
        this.ip = ip;
        this.em = em;
        this.obj = obj;
        this.macid = macid;
        this.Qos = Qos;
    }

    public void run(){
        System.out.println("In send Notifications ip is: "+this.ip);
        try(Socket socket = new Socket(this.ip,9030)) {
            ObjectOutputStream outputstream = new ObjectOutputStream(socket.getOutputStream());

            //System.out.println("Sending the object to ip: "+ip+" macid: "+macid+" ------"+this.obj.sendclassname());
            outputstream.writeObject(this.obj);

        } catch (IOException e) {

            if(Qos!=0) {
                //System.out.println("Storing the message of classname: "+obj.sendclassname());
                ConcurrentHashMap<InetAddress, Classfinder> message = new ConcurrentHashMap<>();
                message.put(ip, obj);
                this.em.messagequeue.put(macid, message);
            }

            //e.printStackTrace();
        }
    }
}
