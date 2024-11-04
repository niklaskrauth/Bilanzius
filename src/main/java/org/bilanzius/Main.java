package org.bilanzius;

import org.bilanzius.commandController.CommandController;
import org.bilanzius.utils.Localization;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Localization localization = Localization.getInstance();

        User user = new User("User", 0);

        System.out.println(localization.getMessage("greeting", user.getUsername()));
        Scanner input  = new Scanner(System.in);

        while(true) {

            System.out.println("----------------------------------------------------------------------------------");

            CommandController commandController = new CommandController(user);

            String stringInput = input.nextLine();

            if (stringInput != null) {
                String stringOutput = commandController.handleInput(stringInput);
                System.out.println(stringOutput);
            }

        }

    }
}