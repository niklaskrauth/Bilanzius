package org.bilanzius.rest;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jdk.jfr.Name;
import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.DatabaseProvider;
import org.bilanzius.persistence.UserService;
import org.bilanzius.persistence.models.User;
import org.bilanzius.utils.HashedPassword;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

public class RequestHandler implements HttpHandler {

    private final UserService userService;

    public RequestHandler() {
        this.userService = DatabaseProvider.getUserService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

        if (authHeader != null && authHeader.startsWith("Basic ")) {

            String base64Credentials = authHeader.substring("Basic ".length());
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            String[] parts = credentials.split(":", 2);

            HashedPassword hashedPassword = HashedPassword.fromPlainText(parts[1]);
            Optional<User> user;

            try {
                user = userService.findUserWithCredentials(parts[0], hashedPassword);
            } catch (DatabaseException e){
                return;
            }

            if (parts.length == 2 && user.isPresent()) {
                exchange.setAttribute("user", user.get());
                return;
            } else {
                exchange.sendResponseHeaders(401, -1);
            }
        }

        exchange.sendResponseHeaders(401, -1);
    }

    public void handleRequest(HttpExchange exchange, Object response, String method) throws IOException {
        if (method.equalsIgnoreCase(exchange.getRequestMethod())) {

            Gson gson = new Gson();
            String jsonResponse = gson.toJson(response);

            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");

            byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, responseBytes.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }


    public void getRequestHandler(HttpExchange exchange, Object response) throws IOException {
        handleRequest(exchange, response, "GET");
    }

    public void postRequestHandler(HttpExchange exchange, Object response) throws IOException {
        handleRequest(exchange, response, "POST");
    }

    public void putRequestHandler(HttpExchange exchange, Object response) throws IOException {
        handleRequest(exchange, response, "PUT");
    }

    public void deleteRequestHandler(HttpExchange exchange, Object response) throws IOException {
        handleRequest(exchange, response, "DELETE");
    }
}
