package edu.rit.cs;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class PubSubAgent implements Publisher, Subscriber {

    public URL serverURL = null;
    public String pubsub_id;
    public InetAddress ip;
    public List<Topic> subscribedtopics = Collections.synchronizedList(new ArrayList<Topic>());
    public List<String> subscribedkeywords = Collections.synchronizedList(new ArrayList<String>());
    public final Object objec = new Object();
    public boolean listen = true;


    public PubSubAgent(String[] args) {
        pubsub_id = "";
        try {
            serverURL = new URL("http://" + args[0] + ":" + args[1]);
            pubsub_id = loginid();
            ip = InetAddress.getLocalHost();
            String ipaddress = ("" + ip).split("/")[1];
            System.out.println("eh value of ip in pusub is : " + ipaddress);
            ip = InetAddress.getByName(ipaddress);
            //System.out.println("pubsub_id: "+pubsub_id);
        } catch (MalformedURLException | UnknownHostException e) {
            System.out.println("I am ere in catch in serverURL");
            e.printStackTrace();
        }
    }

    public void startconnection(String ipaddress, BufferedReader br) {

        new PubSubListner(this).start();
        new PubSubAgentEcho(ipaddress, pubsub_id, ip).start();
//        try {
//            System.out.println("The value of Ip address is: "+ipaddress);
//            System.out.println("The value of ip of event Manager is: "+InetAddress.getByName(ipaddress));
//        } catch (UnknownHostException e) {
//            System.out.println("Found error here");
//            e.printStackTrace();
//        }
        Socket socket;
		try {
			socket = new Socket(ipaddress, 11000);
			ObjectOutputStream outputstream = new ObjectOutputStream(socket.getOutputStream());

			System.out.println("pubsub_id: " + pubsub_id);
			System.out.println("Sending pubsub_id: " + pubsub_id + "  ip: " + ip);
			outputstream.writeObject(pubsub_id + ":" + ip);
//			List<String> list = new Vector<>();
//			list.add("test1");
//			list.add("test2");
//
//			Topic newTopic = new Topic(1, list, "Lololol");
//			advertise(newTopic);
//
//			Event ev = new Event(10, newTopic, "testing", "hello whos there tell its worked");
//			publish(ev);
//
//			subscribe(newTopic);
//			publish(ev);
//			subscribe("test1");
//			unsubscribe();


			socket.close();

		} catch (IOException e) {
			System.out.println("Error here at socket pubsub ");
			System.out.println(e.getMessage());
		    e.printStackTrace();
		}

		System.out.println("entering startconnection");
        this.pubsub_id = loginid();

        System.out.println("Enter the number to start 1) publisher 2) subscriber: ");
        Scanner scan = new Scanner(System.in);
        int option = scan.nextInt();
		String cont = "n";
        if (option == 1) {
            do {
                System.out.println("Enter the operation you want to " +
                        "perform  on publisher 1) advertise 2) publish 3) shutdown: ");

                scan = new Scanner(System.in);
                int operation_option = scan.nextInt();
                if (operation_option == 1) {
					//br = new BufferedReader(new InputStreamReader(System.in));
                	System.out.println("Enter the topic name: ");

                    scan = new Scanner(System.in);
                    String topic_name = scan.nextLine();
                    System.out.println("Enter the topic keywords (type \"stop\" to stop taking in the keywords): ");

                    List<String> keywords = new ArrayList<>();
                    String line;
                    try {
                        while (!((line = br.readLine()).equals("stop"))) {
                            keywords.add(line);
                            System.out.println(line + " in pubsub advertise");
                        }
						System.out.println("Do you want to go through another round(y/n): ");
						scan = new Scanner(System.in);
						cont = scan.nextLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Topic newtopic = new Topic(1, keywords, topic_name);

                    new performoperations(this, option, operation_option, newtopic).start();


                } else if(operation_option==2) {
                    System.out.println("Enter the event tile: ");
                    scan = new Scanner(System.in);
                    String title_name = scan.nextLine();
                    System.out.println("Enter the contents of the event: ");
                    scan = new Scanner(System.in);
                    String contents = scan.nextLine();

                    System.out.println("Enter the topic name to which the event belongs to: ");
                    scan = new Scanner(System.in);
                    String topic_name = scan.nextLine();
                    System.out.println("Enter the topic keywords: ");
                    //br = new BufferedReader(new InputStreamReader(System.in));
                    List<String> keywords = new ArrayList<>();
                    String line;
                    try {
                        while (!((line = br.readLine()).equals("stop"))) {
                            keywords.add(line);
                            System.out.println(line + " in pubsub advertise");
                        }


						System.out.println("Do you want to go through another round(y/n): ");
						scan = new Scanner(System.in);
						cont = scan.nextLine();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
					Topic newtopic = new Topic(1, keywords, topic_name);
                    Event event = new Event(1, newtopic, title_name, contents);



                    new performoperations(this, option, operation_option, event).start();
                }else {
					System.out.println("in else----------------");
                	//br = new BufferedReader(new InputStreamReader(System.in));
                	cont = "n";
                	this.listen = false;
				}

			} while (cont.equals("y"));
            this.listen = false;

        }else{
			do {
				System.out.println("Enter the operation you want to " +
						"perform  on subscriber 1) subscribe with topic  2) subscribe with keyword 3) unsubscribe topic " +
						"4) unsubscribe from all topics 5) shutdown: ");

				scan = new Scanner(System.in);
				int operation_option = scan.nextInt();
				if (operation_option == 1) {
					System.out.println("Enter the topic name: ");
					scan = new Scanner(System.in);
					String topic_name = scan.nextLine();
					System.out.println("Enter the topic keywords (type \"stop\" to stop taking in the keywords): ");
					//br = new BufferedReader(new InputStreamReader(System.in));
					List<String> keywords = new ArrayList<>();
					String line;
					try {
						while (!((line = br.readLine()).equals("stop"))) {
							keywords.add(line);
							System.out.println(line + " in pubsub advertise");
						}
						System.out.println("Do you want to go through another round(y/n): ");
						scan = new Scanner(System.in);
						cont = scan.nextLine();
					} catch (IOException e) {
						e.printStackTrace();
					}

					Topic newtopic = new Topic(1, keywords, topic_name);

					new performoperations(this, option, operation_option, newtopic).start();


				} else if(operation_option==2) {
					System.out.println("Enter the Keyword: ");
					scan = new Scanner(System.in);
					String keyword = scan.nextLine();

					//br = new BufferedReader(new InputStreamReader(System.in));
					try {
						System.out.println("Do you want to go through another round(y/n): ");
						scan = new Scanner(System.in);
						cont = scan.nextLine();

					} catch (Exception e) {
						e.printStackTrace();
					}

					new performoperations(this, option, operation_option, keyword).start();
				}else if (operation_option == 3) {
					System.out.println("Enter the topic name: ");
					scan = new Scanner(System.in);
					String topic_name = scan.nextLine();
					System.out.println("Enter the topic keywords (type \"stop\" to stop taking in the keywords): ");
					//br = new BufferedReader(new InputStreamReader(System.in));
					List<String> keywords = new ArrayList<>();
					String line;
					try {
						while (!((line = br.readLine()).equals("stop"))) {
							keywords.add(line);
							System.out.println(line + " in pubsub unsubcribe");
						}
						System.out.println("Do you want to go through another round(y/n): ");
						scan = new Scanner(System.in);
						cont = scan.nextLine();
					} catch (IOException e) {
						e.printStackTrace();
					}

					Topic newtopic = new Topic(1, keywords, topic_name);

					new performoperations(this, option, operation_option, newtopic).start();


				}else if(operation_option==4) {

					//br = new BufferedReader(new InputStreamReader(System.in));
					try {
						System.out.println("Do you want to go through another round(y/n): ");
						scan = new Scanner(System.in);
						cont = scan.nextLine();

					} catch (Exception e) {
						e.printStackTrace();
					}

					new performoperations(this, option, operation_option).start();
				}else {
					System.out.println("in else----------------");
					//br = new BufferedReader(new InputStreamReader(System.in));
					cont = "n";
                    this.listen = false;


				}
                this.listen = false;


			} while (cont.equals("y"));


		}



    }

    @Override
    public void subscribe(Topic topic) {
        // TODO Auto-generated method stub
        subscribedtopics.add(topic);
        JSONRPC2Session mySession = new JSONRPC2Session(serverURL);

        String method = "subscribe";
        int requestID = 100;
        Map<String, Object> myParams = new HashMap<>();
        myParams.put("topic", topic);
        System.out.println("The value of ip in pubsubagent in publish is: " + ip + "pubsub_id : " + pubsub_id);
        List<String> deviceinfo = new ArrayList<>();
        deviceinfo.add(pubsub_id);
        deviceinfo.add(ip + "");

        myParams.put("deviceinfo", deviceinfo);
        System.out.println(myParams + "The value if myparams in pubsubaget advertise");
        JSONRPC2Request request = new JSONRPC2Request(method, requestID);
        request.setNamedParams(myParams);
        JSONRPC2Response response = null;

        try {
            response = mySession.send(request);

        } catch (JSONRPC2SessionException e) {

            System.out.println("Error here!! at request bind");
            e.printStackTrace();
            System.err.println(e.getMessage());
            // handle exception...
        }

        // Print response result / error
        if (response.indicatesSuccess()) {
            System.out.println(response.getResult());
        } else {
            System.out.println("Error here!! at response! but in else statement");
            System.out.println(response.getError().getMessage());

        }


    }

    @Override
    public void subscribe(String keyword) {
        // TODO Auto-generated method stub
        subscribedkeywords.add(keyword);
        JSONRPC2Session mySession = new JSONRPC2Session(serverURL);

        String method = "subscribekeyword";
        int requestID = 1000;
        Map<String, Object> myParams = new HashMap<>();
        myParams.put("topic", keyword);
        System.out.println("The value of ip in pubsubagent in subscribe with keyword is: " + ip + "pubsub_id : " + pubsub_id);
        List<String> deviceinfo = new ArrayList<>();
        deviceinfo.add(pubsub_id);
        deviceinfo.add(ip + "");

        myParams.put("deviceinfo", deviceinfo);
        System.out.println(myParams + "The value if myparams in pubsubaget advertise");
        JSONRPC2Request request = new JSONRPC2Request(method, requestID);
        request.setNamedParams(myParams);
        JSONRPC2Response response = null;

        try {
            response = mySession.send(request);

        } catch (JSONRPC2SessionException e) {

            System.out.println("Error here!! at request bind");
            e.printStackTrace();
            System.err.println(e.getMessage());
            // handle exception...
        }

        // Print response result / error
        if (response.indicatesSuccess()) {
            System.out.println(response.getResult());
        } else {
            System.out.println("Error here!! at response! but in else statement");
            System.out.println(response.getError().getMessage());

        }


    }

    @Override
    public void unsubscribe(Topic topic) {
        // TODO Auto-generated method stub
        boolean b = subscribedtopics.remove(topic);
        JSONRPC2Session mySession = new JSONRPC2Session(serverURL);

        String method = "unsubscribe";
        int requestID = 10000;
        Map<String, Object> myParams = new HashMap<>();
        myParams.put("topic", topic);
        System.out.println("The value of ip in pubsubagent in publish is: " + ip + "pubsub_id : " + pubsub_id);
        List<String> deviceinfo = new ArrayList<>();
        deviceinfo.add(pubsub_id);
        deviceinfo.add(ip + "");

        myParams.put("deviceinfo", deviceinfo);
        System.out.println(myParams + "The value if myparams in pubsubaget advertise");
        JSONRPC2Request request = new JSONRPC2Request(method, requestID);
        request.setNamedParams(myParams);
        JSONRPC2Response response = null;

        try {
            response = mySession.send(request);

        } catch (JSONRPC2SessionException e) {

            System.out.println("Error here!! at request bind");
            e.printStackTrace();
            System.err.println(e.getMessage());
            // handle exception...
        }

        // Print response result / error
        if (response.indicatesSuccess()) {
            System.out.println(response.getResult());
        } else {
            System.out.println("Error here!! at response! but in else statement");
            System.out.println(response.getError().getMessage());

        }

    }

    @Override
    public void unsubscribe() {
        // TODO Auto-generated method stub
        subscribedtopics = Collections.synchronizedList(new ArrayList<Topic>());
        subscribedkeywords = Collections.synchronizedList(new ArrayList<String>());
        JSONRPC2Session mySession = new JSONRPC2Session(serverURL);
        String method = "unsubscribeall";
        int requestID = 10005;
        Map<String, Object> myParams = new HashMap<>();
        myParams.put("topic", "");
        System.out.println("The value of ip in pubsubagent in publish is: " + ip + "pubsub_id : " + pubsub_id);
        List<String> deviceinfo = new ArrayList<>();
        deviceinfo.add(pubsub_id);
        deviceinfo.add(ip + "");

        myParams.put("deviceinfo", deviceinfo);
        System.out.println(myParams + "The value if myparams in pubsubaget advertise");
        JSONRPC2Request request = new JSONRPC2Request(method, requestID);
        request.setNamedParams(myParams);
        JSONRPC2Response response = null;

        try {
            response = mySession.send(request);

        } catch (JSONRPC2SessionException e) {

            System.out.println("Error here!! at request bind");
            e.printStackTrace();
            System.err.println(e.getMessage());
            // handle exception...
        }

        // Print response result / error
        if (response.indicatesSuccess()) {
            System.out.println(response.getResult());
        } else {
            System.out.println("Error here!! at response! but in else statement");
            System.out.println(response.getError().getMessage());

        }


    }

    @Override
    public void listSubscribedTopics() {
        // TODO Auto-generated method stub
        synchronized (objec) {
            Iterator it = subscribedtopics.iterator();

            while (it.hasNext()) {
                Topic t = (Topic) it.next();
                System.out.println("The name of topic is: " + t.getName() + ".\nid is: " + t.getId() + ".\nKeywords are:" + t.getKeywords());
                System.out.println("-----------------------------------------------------------");

            }

            System.out.println("The Keywords subscribed to are: " + subscribedkeywords);
        }


    }

    @Override
    public void publish(Event event) {
        // TODO Auto-generated method stub

        JSONRPC2Session mySession = new JSONRPC2Session(serverURL);

        String method = "publish";
        int requestID = 10;
        Map<String, Object> myParams = new HashMap<>();
        myParams.put("event", event);
        System.out.println("The value of ip in pubsubagent in publish is: " + ip + "pubsub_id : " + pubsub_id);
        List<String> deviceinfo = new ArrayList<>();
        deviceinfo.add(pubsub_id);
        deviceinfo.add(ip + "");
        deviceinfo.add(2 + "");
        System.out.println("The value of deviceInfo in publish is: " + deviceinfo);

        myParams.put("deviceinfo", deviceinfo);
        System.out.println(myParams + "The value if myparams in pubsubaget advertise");
        JSONRPC2Request request = new JSONRPC2Request(method, requestID);
        request.setNamedParams(myParams);
        JSONRPC2Response response = null;

        try {
            response = mySession.send(request);

        } catch (JSONRPC2SessionException e) {

            System.out.println("Error here!! at request bind");
            e.printStackTrace();
            System.err.println(e.getMessage());
            // handle exception...
        }

        // Print response result / error
        if (response.indicatesSuccess()) {
            System.out.println(response.getResult());
        } else {
            System.out.println("Error here!! at response! but in else statement");
            System.out.println(response.getError().getMessage());

        }


    }

    @Override
    public void advertise(Topic newTopic) {
        // TODO Auto-generated method stub

        JSONRPC2Session mySession = new JSONRPC2Session(serverURL);

        String method = "advertise";
        int requestID = 0;
        Map<String, Object> myParams = new HashMap<>();
        myParams.put("topic", newTopic);
        System.out.println("The value of ip in pubsubagent in advertise is: " + ip + "pubsub_id : " + pubsub_id);
        List<String> deviceinfo = new ArrayList<>();
        deviceinfo.add(pubsub_id);
        deviceinfo.add(ip + "");

        myParams.put("deviceinfo", deviceinfo);
        System.out.println(myParams + "The value if myparams in pubsubaget advertise");
        JSONRPC2Request request = new JSONRPC2Request(method, requestID);
        request.setNamedParams(myParams);
        JSONRPC2Response response = null;

        try {
            response = mySession.send(request);

        } catch (JSONRPC2SessionException e) {

            System.out.println("Error here!! at request bind");
            e.printStackTrace();
            System.err.println(e.getMessage());
            // handle exception...
        }

        // Print response result / error
        if (response.indicatesSuccess()) {
            System.out.println(response.getResult());
        } else {
            System.out.println("Error here!! at response! but in else statement");
            System.out.println(response.getError().getMessage());

        }


    }

    public String loginid() {
        InetAddress ip;

        StringBuilder sb = new StringBuilder();
        try {

            ip = InetAddress.getLocalHost();
            System.out.println("Current IP address : " + ip.getHostAddress());

            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            byte[] mac = network.getHardwareAddress();

            System.out.println("Current MAC address : ");


            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }


        } catch (UnknownHostException e) {

            e.printStackTrace();

        } catch (SocketException e) {

            e.printStackTrace();

        }
        return sb.toString();
    }

    public static void main(String[] args) {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	new PubSubAgent(args).startconnection(args[0],br);
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("I am outta here!!!");

    }


}
