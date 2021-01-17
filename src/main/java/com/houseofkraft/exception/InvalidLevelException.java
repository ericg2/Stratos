package com.houseofkraft.exception;

public class InvalidLevelException extends Exception {

    public InvalidLevelException(Throwable t) {
        super(t);
    }

    public InvalidLevelException(String e, Throwable t) {
        super(e, t);
    }

    public InvalidLevelException(String e) {
        super(e);
    }

    public InvalidLevelException() {
        super();
    }
}
