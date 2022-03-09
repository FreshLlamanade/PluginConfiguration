package com.monst.pluginconfiguration.exception;

/**
 * An exception that is thrown when a user-inputted string could not be parsed.
 */
public class ArgumentParseException extends Exception {

    public ArgumentParseException() {
        super();
    }

    public ArgumentParseException(String message) {
        super(message);
    }

    public ArgumentParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgumentParseException(Throwable cause) {
        super(cause);
    }

}
