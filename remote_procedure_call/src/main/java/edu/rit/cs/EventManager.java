package edu.rit.cs;


import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventManager{
	
	public ConcurrentHashMap<String,ConcurrentHashMap<InetAddress,Long>> networkmap = new ConcurrentHashMap<>();
	public ConcurrentHashMap<String, ConcurrentHashMap<String ,InetAddress>> topicsubscribers = new ConcurrentHashMap<>();
	public ConcurrentHashMap<String,ConcurrentHashMap<InetAddress,Classfinder>> messagequeue = new ConcurrentHashMap<>();
	public List<Topic> Topicspresent = Collections.synchronizedList(new ArrayList<Topic>());
	public final Object objec = new Object();



	/*
	 * Start the repo service
	 */
	private void startService() {
		new OperationsListner(this).start();
		new EMListner(this).start();
		new Echolistner(this).start();
	}

	/*
	 * notify all subscribers of new event 
	 */
	private void notifySubscribers(Event event) {
		
	}
	
	/*
	 * add new topic when received advertisement of new topic
	 */
	private void addTopic(Topic topic){
		
	}
	
	/*
	 * add subscriber to the internal list
	 */
	private void addSubscriber(){
		
	}
	
	/*
	 * remove subscriber from the list
	 */
	private void removeSubscriber(){
		
	}
	
	/*
	 * show the list of subscriber for a specified topic
	 */
	private void showSubscribers(Topic topic){
		
	}
	
	
	public static void main(String[] args) {
		new EventManager().startService();
	}


}
