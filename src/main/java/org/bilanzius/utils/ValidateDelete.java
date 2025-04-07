package org.bilanzius.utils;

import java.util.Scanner;

public class ValidateDelete
{

    public static boolean validateDeleteAction(String message)
    {

        Scanner scanner = new Scanner(System.in);

        System.out.println(message + " (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return !response.equals("yes") && !response.equals("y");
    }

}
