package org.bilanzius.rest;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class RestController {

    private int port = 8080;
    private String mainEndpoint = "/api";

    public RestController() {
    }

    public RestController(int port, String mainEndpoint) {
        this.port = port;
        this.mainEndpoint = mainEndpoint;
    }

    public void mainRestController() {

        HttpServer server;

        try {
            server = HttpServer.create(new InetSocketAddress(this.port), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        server.createContext(mainEndpoint + "/health", this::getHealth);

        server.setExecutor(null);
        server.start();
    }

    private void getHealth(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {

            String response =  "Backend is alive!";

            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }
}
