package org.bilanzius.rest;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.bilanzius.persistence.sql.SqlBackend;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class MainRestController {

    private int port = 8080;
    private String mainEndpoint = "/api";
    private SqlBackend backend;

    /*
    - Klasse User oder so wo man zuerst den User und das passwort eingeben muss als body über post und dann darüber dann mit der url daten abfangen
    z.B. /api/user/withdrawMoney/amount
    oder so, muss man schauen wegen Sicherheit weil jedes mal den User anzugeben nervt also maybe Token oder so übern header,
    aber dann könnte man eh gleich UserDaten im header übergeben

    Ansonsten maybe noch random endpoint was das kann und was es gibt etc.
     */

    public MainRestController(SqlBackend backend) {
        this.backend = backend;
    }

    public MainRestController(int port, String mainEndpoint) {
        this.port = port;
        this.mainEndpoint = mainEndpoint;
    }

    public void start() throws RuntimeException {

        HttpServer server;

        try {
            server = HttpServer.create(new InetSocketAddress(this.port), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        server.createContext(mainEndpoint + "/health", this::getHealth);
        server.createContext(mainEndpoint + "/help", this::getHelp);

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

    private void getHelp(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {

            String response =
                    """
                    /health - Check if backend is alive
                    /help - Get help
                    """;

            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }
}
