package org.bilanzius;

import org.bilanzius.persistence.UserService;
import org.bilanzius.persistence.models.User;
import org.bilanzius.utils.Localization;

import java.util.Optional;
import java.util.Scanner;

import static org.bilanzius.utils.HashedPassword.fromPlainText;

public class SingUp {

    private UserService userService;
    private Localization localization;

    public SingUp(UserService userService, Localization localization) {
        this.userService = userService;
        this.localization = localization;
    }

    public User login(Scanner input) {

        while (true) {

            System.out.println("----------------------------------------------------------------------------------");

            System.out.println("Username:");
            String stringInput = input.nextLine();

            if (stringInput == null) {
                return null;
            }

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
