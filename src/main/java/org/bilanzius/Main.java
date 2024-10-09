package org.bilanzius;

import org.bilanzius.middleware.Middleware;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // Die Main Methode sollte nie geändert werden außer notwendig!

        System.out.println("Hello User");
        Scanner input  = new Scanner(System.in);

        while(true) {

            System.out.println("-----------------------------------------");

            Middleware middleware = new Middleware();

            String stringInput = input.nextLine();

            if (stringInput != null) {
                String stringOutput = middleware.handleInput(stringInput);
                System.out.println(stringOutput);
            }

        }

    }
}