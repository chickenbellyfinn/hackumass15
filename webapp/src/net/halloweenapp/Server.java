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

    public void start(String hostname, int port) {
        InetSocketAddress addr = new InetSocketAddress(hostname, port);
        try {
            server = HttpServer.create(addr, 0);
            server.createContext("/", new HttpHandler() {
                @Override
                public void handle(HttpExchange httpExchange) throws IOException {
                    System.out.println("request from " + httpExchange.getRemoteAddress() + " for " + httpExchange.getRequestURI());
                    String response = "testing";

                    httpExchange.sendResponseHeaders(200, response.length());

                    OutputStream out = httpExchange.getResponseBody();
                    out.write(response.getBytes());
                    out.close();
                }
            });

            server.start();
            System.out.println("Server started");
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
