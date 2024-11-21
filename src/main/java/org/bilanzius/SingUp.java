package org.bilanzius;

import org.bilanzius.persistence.UserService;
import org.bilanzius.persistence.models.User;
import org.bilanzius.utils.Localization;

import java.util.Optional;
import java.util.Scanner;

import static org.bilanzius.utils.HashedPassword.fromPlainText;

public class SingUp {

    private UserService userService;
    private Localization localization = Localization.getInstance();

    public SingUp(UserService userService) {
        this.userService = userService;
    }

    public User waitUntilLoggedIn (Scanner input) {

        while (true) {

            System.out.println("----------------------------------------------------------------------------------");

            System.out.println("Username:");
            String stringInput = input.nextLine();

            Optional<org.bilanzius.persistence.models.User> userOptional = userService.findUserWithName(stringInput);
            if (userOptional.isPresent()) {

                User user = userOptional.get();
                System.out.println("Password:");
                stringInput = input.nextLine();

                if (fromPlainText(stringInput).equals(user.getHashedPassword())) {

                    System.out.println("----------------------------------------------------------------------------------");
                    System.out.println(localization.getMessage("greeting_user", user.getUsername()));

                    return user;

                } else {
                    System.out.println(localization.getMessage("wrongPassword"));
                }

            } else {
                System.out.println(localization.getMessage("wrongUsername"));
            }
        }
    }
}
