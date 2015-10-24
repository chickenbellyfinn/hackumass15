package net.halloweenapp;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * Created by Daniel on 10/24/2015.
 */
public class Server {

    public static final String HOSTNAME = "halloweenapp.cloudapp.net";
    public static final int PORT = 80;

    private static Server instance;

    public static Server getInstance() {
        if (instance == null) instance = new Server();
        return instance;
    }

    private HttpServer server;
    private boolean running;

    private Server() {
        running = false;
    }

    public void start() {
        InetSocketAddress addr = new InetSocketAddress(HOSTNAME, PORT);
        try {
            server = HttpServer.create(addr, 0);
            server.createContext("/", new HttpHandler() {
                @Override
                public void handle(HttpExchange httpExchange) throws IOException {
                    String response = "testing";

                    httpExchange.sendResponseHeaders(200, response.length());

                    OutputStream out = httpExchange.getResponseBody();
                    out.write(response.getBytes());
                    out.close();
                }
            });

            server.start();
            running = true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (running) {
            server.stop(3);
            running = false;
        }
    }

}
