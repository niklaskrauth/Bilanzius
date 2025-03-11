package org.bilanzius.cli;

@FunctionalInterface
public interface QuestionValidator {

    /**
     * Validates the input string and throws {@link QuestionException} if invalid.
     */
    void validate(String input) throws QuestionException;
}
