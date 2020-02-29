package edu.rit.cs;

import com.thetransactioncompany.jsonrpc2.JSONRPC2ParseException;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.server.Dispatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class operationspacketanalyzer extends Thread {
    public EventManager em;
    private String name;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Dispatcher dispatcher;
    public operationspacketanalyzer(Socket socket,EventManager em){
        this.em = em;
        this.socket = socket;
        this.dispatcher = new Dispatcher();

        // Register the "echo", "getDate" and "getTime" handlers with it.
        dispatcher.register(new JsonHandler.TopicPublishHandler(this.em));
        dispatcher.register(new JsonHandler.EventHandler(this.em));
        dispatcher.register(new JsonHandler.TopicSubscriberHandler(this.em));
        dispatcher.register(new JsonHandler.TopicSubscriberkeywordHandler(this.em));
        dispatcher.register(new JsonHandler.TopicUnSubscriberHandler(this.em));
        dispatcher.register(new JsonHandler.TopicUnSubscriberallHandler(this.em));
    }
    public void run(){
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream(), true);

            // read request
            String line;
            line = in.readLine();
            //System.out.println(line);
            StringBuilder raw = new StringBuilder();
            raw.append("" + line);
            boolean isPost = line.startsWith("POST");
            int contentLength = 0;
            while (!(line = in.readLine()).equals("")) {
                //System.out.println(line);
                raw.append('\n' + line);
                if (isPost) {
                    final String contentHeader = "Content-Length: ";
                    if (line.startsWith(contentHeader)) {
                        contentLength = Integer.parseInt(line.substring(contentHeader.length()));
                    }
                }
            }
            StringBuilder body = new StringBuilder();
            if (isPost) {
                int c = 0;
                for (int i = 0; i < contentLength; i++) {
                    try {
                        c = in.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    body.append((char) c);
                }
            }

            //System.out.println(body.toString());
            JSONRPC2Request request = null;
            try {
                request = JSONRPC2Request.parse(body.toString());
            } catch (JSONRPC2ParseException e) {
                e.printStackTrace();
            }
            JSONRPC2Response resp = dispatcher.process(request, null);
            // send response
            out.write("HTTP/1.1 200 OK\r\n");
            out.write("Content-Type: application/json\r\n");
            out.write("\r\n");
            out.write(resp.toJSONString());
            // do not in.close();
            out.flush();
            out.close();
            socket.close();

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
