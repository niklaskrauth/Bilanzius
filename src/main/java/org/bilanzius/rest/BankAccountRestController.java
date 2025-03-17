package org.bilanzius.rest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import org.bilanzius.persistence.BankAccountService;
import org.bilanzius.persistence.DatabaseException;
import org.bilanzius.persistence.DatabaseProvider;
import org.bilanzius.persistence.models.BankAccount;
import org.bilanzius.persistence.models.User;
import org.bilanzius.rest.dto.BankAccountDTO;

import java.io.IOException;
import java.util.*;

import static org.bilanzius.Main.MAX_BANK_ACCOUNTS;
import static org.bilanzius.utils.Requests.readRequestBody;


public class BankAccountRestController extends RequestHandler {

    private final BankAccountService bankAccountService;

    public BankAccountRestController() {
        bankAccountService = DatabaseProvider.getBankAccountService();
    }

    public void getAllBankaccounts(HttpExchange exchange) throws IOException {
        try {

            List<BankAccount> bankAccounts;
            List<BankAccountDTO> bankAccountDTOs = new ArrayList<>();

            handle(exchange);
            User user = (User) exchange.getAttribute("user");

            if (user == null) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            bankAccounts = bankAccountService.getBankAccountsOfUser(user, MAX_BANK_ACCOUNTS);

            if (bankAccounts.isEmpty()) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            bankAccounts.stream().map(
                    bankAccount -> new BankAccountDTO(bankAccount.getName(), bankAccount.getBalance())
            ).forEach(bankAccountDTOs::add);

            getRequestHandler(exchange, bankAccountDTOs);
        } catch (IOException | DatabaseException e) {
            exchange.sendResponseHeaders(404, -1);
        }
    }

    public void createBankAccount(HttpExchange exchange) throws IOException {
        try {

            BankAccountDTO bankAccountDTO;
            BankAccount bankAccount;
            Gson gson = new Gson();

            handle(exchange);
            User user = (User) exchange.getAttribute("user");

            if (user == null) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            bankAccountDTO = gson.fromJson(readRequestBody(exchange), BankAccountDTO.class);

            Optional<BankAccount> foundBankAccount = bankAccountService.getBankAccountOfUserByName(user, bankAccountDTO.name());
            if (foundBankAccount.isPresent()) {
                exchange.sendResponseHeaders(409, -1);
                return;
            }

            bankAccount = BankAccount.create(user, bankAccountDTO.name());

            bankAccountService.createBankAccount(bankAccount);

            postRequestHandler(exchange, bankAccountDTO);
        } catch (IOException | JsonSyntaxException | DatabaseException e ) {
            exchange.sendResponseHeaders(500, -1);
        }
    }

     public void updateBankAccount(HttpExchange exchange) throws IOException {
         try {
             BankAccountDTO bankAccountDTO;
             BankAccount bankAccount;

             handle(exchange);
             User user = (User) exchange.getAttribute("user");

             if (user == null) {
                 exchange.sendResponseHeaders(404, -1);
                 return;
             }

             String requestBody = readRequestBody(exchange);
             Gson gson = new Gson();

             bankAccountDTO = gson.fromJson(requestBody, BankAccountDTO.class);
             Optional<BankAccount> existingBankAccount = bankAccountService.getBankAccountOfUserByName(user, bankAccountDTO.name());

             if (existingBankAccount.isEmpty()) {
                 exchange.sendResponseHeaders(404, -1);
                 return;
             }

             bankAccount = existingBankAccount.get();
             bankAccount.setBalance(bankAccountDTO.balance());

             bankAccountService.updateBankAccount(bankAccount);

             putRequestHandler(exchange, bankAccountDTO);
         } catch (IOException | JsonSyntaxException | DatabaseException e) {
             exchange.sendResponseHeaders(500, -1);
         }
     }

    public void deleteBankAccount(HttpExchange exchange) throws IOException {
        try {
            BankAccountDTO bankAccountDTO;
            BankAccount bankAccount;

            handle(exchange);
            User user = (User) exchange.getAttribute("user");

            if (user == null) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            String requestBody = readRequestBody(exchange);
            Gson gson = new Gson();

            bankAccountDTO = gson.fromJson(requestBody, BankAccountDTO.class);
            Optional<BankAccount> existingBankAccount = bankAccountService.getBankAccountOfUserByName(user, bankAccountDTO.name());

            if (existingBankAccount.isEmpty()) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            bankAccount = existingBankAccount.get();
            bankAccountService.deleteBankAccount(bankAccount);

            deleteRequestHandler(exchange, bankAccountDTO);
        } catch (IOException | JsonSyntaxException | DatabaseException e) {
            exchange.sendResponseHeaders(500, -1);
        }
    }
}