package edu.rit.cs;

//The Client sessions package
import com.thetransactioncompany.jsonrpc2.client.*;

//The Base package for representing JSON-RPC 2.0 messages
import com.thetransactioncompany.jsonrpc2.*;

//The JSON Smart package for JSON encoding/decoding (optional)
import net.minidev.json.*;

//For creating URLs
import java.net.*;
import java.util.HashMap;
import java.util.Map;


public class JsonRPCClient {

	public static void main(String[] args) {


		// Creating a new session to a JSON-RPC 2.0 web service at a specified URL
		// The JSON-RPC 2.0 server URL
		URL serverURL = null;

		try {
			System.out.println("In try before:  "+"http://"+args[0]+":"+args[1]);
			serverURL = new URL("http://"+args[0]+":"+args[1]);
			System.out.println("In try after");

		} catch (MalformedURLException e) {
			System.out.println("ServerURL exception at line 31"+e.getMessage());
		}

		System.out.println("ServerURL at line 34");
		// Create new JSON-RPC 2.0 client session
		
		try{
		Thread.sleep(2000);
		}catch(Exception e){
			System.out.println("IN exception in sleep statement");
			System.out.println(e.getMessage());
		}
		
		JSONRPC2Session mySession = new JSONRPC2Session(serverURL);
		
		System.out.println("ServerURL at line 39");


		// Once the client session object is created, you can use to send a series
		// of JSON-RPC 2.0 requests and notifications to it.

		// Sending an example "getTime" request:
		// Construct new request
		String method = "getDate";
		int requestID = 0;
		JSONRPC2Request request = new JSONRPC2Request(method, requestID);
		if(method=="echo") {
			Map<String, Object> myParams = new HashMap<String, Object>();
			myParams.put("hello", "->world?");
			request.setNamedParams(myParams);
		}
		// Send request
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
		if (response.indicatesSuccess()){
			System.out.println("Error here!! at response");
			System.out.println(response.getResult());
		}
		else{
			System.out.println("Error here!! at response! but in else statement");
			System.out.println(response.getError().getMessage());
		    
		}

	}
}