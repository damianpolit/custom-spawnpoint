package com.dpolit.customspawnpoint.exceptions;

public class NotPlayerInstanceException extends RuntimeException {

    private static final String MESSAGE = "Command allowed only for players";

    public NotPlayerInstanceException() {
        super(MESSAGE);
    }
}
