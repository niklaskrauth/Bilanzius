package org.bilanzius.rest;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

//sus

public class BankAccountRestController implements HttpHandler {

    private final HttpHandler next;
    private static final Map<String, String> users = new HashMap<>();

    public BankAccountRestController(HttpHandler next) {
        this.next = next;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring("Basic ".length());
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            String[] parts = credentials.split(":", 2);

            if (parts.length == 2 && users.containsKey(parts[0]) && users.get(parts[0]).equals(parts[1])) {
                exchange.setAttribute("user", parts[0]); // Benutzer speichern
                next.handle(exchange); // Anfrage weiterleiten
                return;
            }
        }

        // Ungültige Authentifizierung
        exchange.sendResponseHeaders(401, -1);
    }
}
