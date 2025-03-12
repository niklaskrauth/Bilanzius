package org.bilanzius.cli;

import org.bilanzius.utils.Localization;

import java.util.Scanner;

public class CLIContext implements IOContext {

    private final Scanner scanner;
    private final Localization localization;

    public CLIContext(Scanner scanner, Localization localization) {
        this.scanner = scanner;
        this.localization = localization;
    }

    @Override
    public String askUser(Question question) {
        do {
            System.out.print(question.question() + (question.defaultValue() == null || question.defaultValue().isBlank() ? " " : " [" + question.defaultValue() + "] "));
            String input = scanner.nextLine();

            if (input.isBlank()) {
                input = question.defaultValue();
            }

            try {
                question.validator().validate(input);
                return input;
            } catch (QuestionException ex) {
                System.out.println(ex.getMessage());
            }
        } while (question.repeatUntilValid());

        return question.defaultValue();
    }

    @Override
    public String askUser(String question) {
        return askUser(Question.create()
                .question(question)
                .build());
    }

    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public void printLocalized(String key, Object... params) {
        System.out.println(localization.getMessage(key, params));
    }

    @Override
    public void lineSeperator() {
        printLocalized("line_splitter");
    }
}
