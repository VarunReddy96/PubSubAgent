package edu.rit.cs;

/**
* Demonstration of the JSON-RPC 2.0 Server framework usage. The request
* handlers are implemented as static nested classes for convenience, but in 
* real life applications may be defined as regular classes within their old 
* source files.
*
* @author Vladimir Dzhuvinov
* @version 2011-03-05
*/ 

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.server.MessageContext;
import com.thetransactioncompany.jsonrpc2.server.RequestHandler;

public class JsonHandler {



	public static class TopicPublishHandler implements RequestHandler {

		EventManager em;

		public TopicPublishHandler(EventManager em){
			this.em = em;
		}

		// Reports the method names of the handled requests
		public String[] handledRequests() {

			return new String[]{"advertise"};
		}


		// Processes the requests
		public JSONRPC2Response process(JSONRPC2Request req, MessageContext ctx) {

			if (req.getMethod().equals("advertise")) {

				// Echo first parameter
				String pubsub_macid = "";
				ConcurrentHashMap<String,InetAddress> advertise = new ConcurrentHashMap<>();
				InetAddress ip = null;
				Map<String,Object> myParams = req.getNamedParams();
				Gson gson = new Gson();
				Topic currTopic = gson.fromJson(myParams.get("topic").toString(), Topic.class);
				for(String s: myParams.keySet()){
					if(!(s.equals("topic"))){
						System.out.println();
						String[] temp = {};
						try {
							String deviceinfo = myParams.get(s).toString();
							temp = deviceinfo.split(",");
							String ipaddress = temp[1].substring(1,temp[1].length()-2);
							//System.out.println("The value of ipaddress in advertise topic JSONHANDLER is: "+ipaddress);
							ip = InetAddress.getByName(ipaddress.substring(2));
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
						pubsub_macid = temp[0].substring(2,temp[0].length()-1);
						//System.out.println("The value of the pubsub_macid is: "+pubsub_macid);
						//advertise.put(pubsub_macid,ip);
					}
				}
				if(em.topicsubscribers.containsKey(currTopic.getName())){
					Object input = "Topic already exists";
					return new JSONRPC2Response(input, req.getID());
				}else {


					advertise = new ConcurrentHashMap<>();
					em.Topicspresent.add(currTopic);
					this.em.topicsubscribers.put(currTopic.getName(), advertise);


					for(String s: em.networkmap.keySet()){
						Classfinder obj = currTopic;
						for(InetAddress ipad: em.networkmap.get(s).keySet()){
							//System.out.println("The boolean of s.equals(pubsub_macid) is in JSONHANDLER: "+ (s.equals(pubsub_macid)));
							//System.out.println("The value of pubsubmacid: "+pubsub_macid+ " s is: "+s);
							if(!(s.equals(pubsub_macid))) {
								//System.out.println("Sending notofications ipad: "+ipad+" ip: "+ip);
								new SendNotifications(ipad, em, obj, s).start();
							}else{
								//System.out.println("Not sending notifications because ipad: "+ipad+" ip: "+ip);
							}
						}

					}

					return new JSONRPC2Response("success", req.getID());
				}
			}
			else {

				// Method name not supported

				return new JSONRPC2Response(JSONRPC2Error.METHOD_NOT_FOUND, req.getID());
			}
		}
	}


	public static class TopicSubscriberHandler implements RequestHandler {

		EventManager em;

		public TopicSubscriberHandler(EventManager em){
			this.em = em;
		}

		// Reports the method names of the handled requests
		public String[] handledRequests() {

			return new String[]{"subscribe"};
		}


		// Processes the requests
		public JSONRPC2Response process(JSONRPC2Request req, MessageContext ctx) {

			if (req.getMethod().equals("subscribe")) {

				// Echo first parameter
				String pubsub_macid = "";
				ConcurrentHashMap<String,InetAddress> advertise = new ConcurrentHashMap<>();
				InetAddress ip = null;
				Map<String,Object> myParams = req.getNamedParams();
				Gson gson = new Gson();
				Topic currTopic = gson.fromJson(myParams.get("topic").toString(), Topic.class);
				for(String s: myParams.keySet()){
					if(!(s.equals("topic"))){
						//System.out.println();
						String[] temp = {};
						try {
							String deviceinfo = myParams.get(s).toString();
							temp = deviceinfo.split(",");
							String ipaddress = temp[1].substring(1,temp[1].length()-2);
							//System.out.println("The value of ipaddress in advertise topic JSONHANDLER is: "+ipaddress);
							ip = InetAddress.getByName(ipaddress.substring(2));
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
						pubsub_macid = temp[0].substring(2,temp[0].length()-1);
						//System.out.println("The value of the pubsub_macid is: "+pubsub_macid);
						//advertise.put(pubsub_macid,ip);
					}
				}
				if(em.topicsubscribers.containsKey(currTopic.getName())){
					em.topicsubscribers.get(currTopic.getName()).put(pubsub_macid,ip);
					//System.out.println("The subscriber is added to topic------"+em.topicsubscribers.get(currTopic.getName()));

					Object input = "Subscriber added to topic";
					return new JSONRPC2Response(input, req.getID());
				}else {
					Object input = "The topic you want to subscribe to is not Found!Error";
					return new JSONRPC2Response(input, req.getID());
				}

			}
			else {

				// Method name not supported

				return new JSONRPC2Response(JSONRPC2Error.METHOD_NOT_FOUND, req.getID());
			}
		}
	}

	public static class TopicUnSubscriberHandler implements RequestHandler {

		EventManager em;

		public TopicUnSubscriberHandler(EventManager em){
			this.em = em;
		}

		// Reports the method names of the handled requests
		public String[] handledRequests() {

			return new String[]{"unsubscribe"};
		}


		// Processes the requests
		public JSONRPC2Response process(JSONRPC2Request req, MessageContext ctx) {

			if (req.getMethod().equals("unsubscribe")) {

				// Echo first parameter
				//System.out.println("In unsubscribe-----------------");
				String pubsub_macid = "";
				ConcurrentHashMap<String,InetAddress> advertise = new ConcurrentHashMap<>();
				InetAddress ip = null;
				Map<String,Object> myParams = req.getNamedParams();
				Gson gson = new Gson();
				Topic currTopic = gson.fromJson(myParams.get("topic").toString(), Topic.class);
				for(String s: myParams.keySet()){
					if(!(s.equals("topic"))){
						//System.out.println();
						String[] temp = {};
						try {
							String deviceinfo = myParams.get(s).toString();
							temp = deviceinfo.split(",");
							String ipaddress = temp[1].substring(1,temp[1].length()-2);
							//System.out.println("The value of ipaddress in advertise topic JSONHANDLER is: "+ipaddress);
							ip = InetAddress.getByName(ipaddress.substring(2));
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
						pubsub_macid = temp[0].substring(2,temp[0].length()-1);
						//System.out.println("The value of the pubsub_macid is: "+pubsub_macid);
						//advertise.put(pubsub_macid,ip);
					}
				}
				if(em.topicsubscribers.containsKey(currTopic.getName())){
					for(String s: em.topicsubscribers.get(currTopic.getName()).keySet()){
						if(s.equals(pubsub_macid)){
							em.topicsubscribers.get(currTopic.getName()).remove(s);
						}
					}

					//System.out.println("The subscriber is removed from to topic------"+em.topicsubscribers.get(currTopic.getName()));

					Object input = "Subscriber removed to topic";
					return new JSONRPC2Response(input, req.getID());
				}else {
					Object input = "The topic you want to unsubscribe to is not Found!Error";
					return new JSONRPC2Response(input, req.getID());
				}

			}
			else {

				// Method name not supported

				return new JSONRPC2Response(JSONRPC2Error.METHOD_NOT_FOUND, req.getID());
			}
		}
	}

	public static class TopicUnSubscriberallHandler implements RequestHandler {

		EventManager em;

		public TopicUnSubscriberallHandler(EventManager em){
			this.em = em;
		}

		// Reports the method names of the handled requests
		public String[] handledRequests() {

			return new String[]{"unsubscribeall"};
		}


		// Processes the requests
		public JSONRPC2Response process(JSONRPC2Request req, MessageContext ctx) {

			if (req.getMethod().equals("unsubscribeall")) {

				// Echo first parameter
				//System.out.println("In unsubscribeall-----------------");
				String pubsub_macid = "";
				ConcurrentHashMap<String,InetAddress> advertise = new ConcurrentHashMap<>();
				InetAddress ip = null;
				Map<String,Object> myParams = req.getNamedParams();
				for(String s: myParams.keySet()){
					if(!(s.equals("topic"))){
						//System.out.println();
						String[] temp = {};
						try {
							String deviceinfo = myParams.get(s).toString();
							temp = deviceinfo.split(",");
							String ipaddress = temp[1].substring(1,temp[1].length()-2);
							//System.out.println("The value of ipaddress in advertise topic JSONHANDLER is: "+ipaddress);
							ip = InetAddress.getByName(ipaddress.substring(2));
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
						pubsub_macid = temp[0].substring(2,temp[0].length()-1);
						//System.out.println("The value of the pubsub_macid is: "+pubsub_macid);
						//advertise.put(pubsub_macid,ip);
					}
				}

				synchronized (em.objec) {
					Iterator it = em.Topicspresent.iterator();

					while (it.hasNext()) {
						Topic t = (Topic) it.next();
						//System.out.println("The name of topic is: " + t.getName());

						for (String macid : em.topicsubscribers.get(t.getName()).keySet()) {
							if (macid.equals(pubsub_macid)) {
								em.topicsubscribers.get(t.getName()).remove(macid);
								//System.out.println("The value of em.topicsubscribers is: " + em.topicsubscribers.get(t.getName()) + " topic name is: " + t.getName());

							}
						}

					}
				}
					Object input = "Subscriber removed from all topics";
					return new JSONRPC2Response(input, req.getID());
			}
			else {

				// Method name not supported

				return new JSONRPC2Response(JSONRPC2Error.METHOD_NOT_FOUND, req.getID());
			}
		}
	}

	public static class TopicSubscriberkeywordHandler implements RequestHandler {

		EventManager em;

		public TopicSubscriberkeywordHandler(EventManager em){
			this.em = em;
		}

		// Reports the method names of the handled requests
		public String[] handledRequests() {

			return new String[]{"subscribekeyword"};
		}


		// Processes the requests
		public JSONRPC2Response process(JSONRPC2Request req, MessageContext ctx) {

			if (req.getMethod().equals("subscribekeyword")) {

				// Echo first parameter
				boolean check = false;
				//System.out.println("In SubscribedKeyword------------");
				String pubsub_macid = "";
				ConcurrentHashMap<String,InetAddress> advertise = new ConcurrentHashMap<>();
				InetAddress ip = null;
				Map<String,Object> myParams = req.getNamedParams();
				Gson gson = new Gson();
				String keyword = myParams.get("topic").toString();
				for(String s: myParams.keySet()){
					if(!(s.equals("topic"))){
						//System.out.println();
						String[] temp = {};
						try {
							String deviceinfo = myParams.get(s).toString();
							temp = deviceinfo.split(",");
							String ipaddress = temp[1].substring(1,temp[1].length()-2);
							//System.out.println("The value of ipaddress in advertise topic JSONHANDLER is: "+ipaddress);
							ip = InetAddress.getByName(ipaddress.substring(2));
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
						pubsub_macid = temp[0].substring(2,temp[0].length()-1);
						//System.out.println("The value of the pubsub_macid is: "+pubsub_macid);
						//advertise.put(pubsub_macid,ip);
					}
				}
				Object input = "";

				synchronized (em.objec){
					Iterator it = em.Topicspresent.iterator();

					while (it.hasNext()){
						Topic t =(Topic)it.next();
						//System.out.println("The name of topic is: "+t.getName());

						for(String keywords: t.getKeywords()){
							if(keywords.equals(keyword)){
								em.topicsubscribers.get(t.getName()).put(pubsub_macid,ip);
								check = true;
								//System.out.println("The value of em.topicsubscribers is: "+em.topicsubscribers.get(t.getName())+ " topic name is: "+t.getName());

							}
						}
					}


				}

				if(!check){
					input = "Keyword Not found";
					return new JSONRPC2Response(input, req.getID());

				}else{
					input = "Subscriber added to topic";
					return new JSONRPC2Response(input, req.getID());
				}


			}
			else {

				// Method name not supported

				return new JSONRPC2Response(JSONRPC2Error.METHOD_NOT_FOUND, req.getID());
			}
		}
	}




	public static class EventHandler implements RequestHandler {

		EventManager em;

		public EventHandler(EventManager em){
			this.em = em;
		}

		// Reports the method names of the handled requests
		public String[] handledRequests() {

			return new String[]{"publish"};
		}


		// Processes the requests
		public JSONRPC2Response process(JSONRPC2Request req, MessageContext ctx) {

			if (req.getMethod().equals("publish")) {

				// Echo first parameter
				System.out.println("In EventHandler");
				int Qos = 0;
				Map<String,Object> myParams = req.getNamedParams();
				Gson gson = new Gson();
				Event currEvent = gson.fromJson(myParams.get("event").toString(), Event.class);
				String pubsub_macid = "";
				ConcurrentHashMap<String,InetAddress> advertise = new ConcurrentHashMap<>();
				InetAddress ip = null;
				for(String s: myParams.keySet()){
					if(!(s.equals("event"))){
						//System.out.println();
						String[] temp = {};
						try {
							String deviceinfo = myParams.get(s).toString();
							temp = deviceinfo.split(",");
							System.out.println(deviceinfo+"-------------"+temp[2]);
							String ipaddress = temp[1].substring(1,temp[1].length()-1);
							//System.out.println("The value of ipaddress in advertise topic JSONHANDLER is: "+ipaddress);
							ip = InetAddress.getByName(ipaddress.substring(2));
							Qos =Integer.parseInt(temp[2].substring(1,temp[2].length()-2));
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
						pubsub_macid = temp[0].substring(2,temp[0].length()-1);
						//System.out.println("The value of the pubsub_macid is: "+pubsub_macid);
						//advertise.put(pubsub_macid,ip);
					}
				}
				if(em.topicsubscribers.containsKey(currEvent.getTopic().getName())){
					ConcurrentHashMap<String ,InetAddress> subscriberslist = em.topicsubscribers.get(currEvent.getTopic().getName());
					for(String macid: subscriberslist.keySet()){
						Classfinder obj = currEvent;
						//System.out.println("Sending notifications of publish macid: "+macid + "");
						new SendNotifications(subscriberslist.get(macid),em,obj,macid,Qos).start();
					}

					return new JSONRPC2Response("success", req.getID());
				}else {

					Object input = "No topic found under this event";
					//System.out.println("Topic currEvent details: id: " + currEvent.getId() + "-----Keywords: " + currEvent.getKeywords() + "------name: " + currEvent.getName());

					return new JSONRPC2Response(input, req.getID());
				}
			}
			else {

				// Method name not supported

				return new JSONRPC2Response(JSONRPC2Error.METHOD_NOT_FOUND, req.getID());
			}
		}
	}

}
