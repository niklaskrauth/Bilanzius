package org.bilanzius.cli;

public interface IOContext
{

    String askUser(Question question);

    String askUser(String question);

    void print(String message);

    void printLocalized(String key, Object... params);

    void lineSeperator();
}
