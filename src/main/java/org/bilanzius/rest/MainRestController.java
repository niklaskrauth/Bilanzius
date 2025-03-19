package org.bilanzius.rest;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MainRestController extends RequestHandler
{

    private int port = 8080;
    private String mainEndpoint = "/api";

    public MainRestController()
    {
    }

    public MainRestController(int port, String mainEndpoint)
    {
        this.port = port;
        this.mainEndpoint =
                mainEndpoint;
    }

    public void start() throws RuntimeException
    {

        HttpServer server;

        try {
            server =
                    HttpServer.create(new InetSocketAddress(this.port), 0);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }

        server.createContext(mainEndpoint + "/health", this::getHealth);
        server.createContext(mainEndpoint + "/help", this::getHelp);

        server.createContext(mainEndpoint + "/bankAccount/getAll", new BankAccountRestController()::getAllBankaccounts);
        server.createContext(mainEndpoint + "/bankAccount/create", new BankAccountRestController()::createBankAccount);
        server.createContext(mainEndpoint + "/bankAccount/update", new BankAccountRestController()::updateBankAccount);
        server.createContext(mainEndpoint + "/bankAccount/delete", new BankAccountRestController()::deleteBankAccount);

        server.createContext(mainEndpoint + "/category/getAll", new CategoryRestController()::getAllCategories);
        server.createContext(mainEndpoint + "/category/create", new CategoryRestController()::createCategory);
        server.createContext(mainEndpoint + "/category/update", new CategoryRestController()::updateCategory);
        server.createContext(mainEndpoint + "/category/delete", new CategoryRestController()::deleteCategory);

        server.setExecutor(null);
        server.start();
    }

    private void getHealth(HttpExchange exchange) throws IOException
    {
        handleRequest(exchange, "The app is alive", "GET");
    }

    private void getHelp(HttpExchange exchange) throws IOException
    {
        String response =
                """
                        /health - Check if backend is alive
                        /help - Get help
                        """;
        handleRequest(exchange, response, "GET");
    }
}
