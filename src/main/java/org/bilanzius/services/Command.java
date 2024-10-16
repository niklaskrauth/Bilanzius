package org.bilanzius.services;

public interface Command {
    String execute(String[] arguments);
}