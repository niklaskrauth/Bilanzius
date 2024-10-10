package org.bilanzius;

import org.bilanzius.commandController.CommandController;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello User");
        Scanner input  = new Scanner(System.in);

        while(true) {

            System.out.println("-----------------------------------------");

            CommandController commandController = new CommandController();

            String stringInput = input.nextLine();

            if (stringInput != null) {
                String stringOutput = commandController.handleInput(stringInput);
                System.out.println(stringOutput);
            }

        }

    }
}