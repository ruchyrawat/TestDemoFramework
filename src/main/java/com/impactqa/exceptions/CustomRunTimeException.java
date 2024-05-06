package com.impactqa.exceptions;

public class CustomRunTimeException extends RuntimeException{
    private static final long serialVersionUID = -8460356990632230194L;

    private String message;


    public CustomRunTimeException(String message, Throwable cause) {
        super(message, cause);
        this.message =message;

    }

    public CustomRunTimeException(String message) {
        super(message);
        this.message =message;
    }

    public CustomRunTimeException(Throwable cause) {
        super(cause);
    }


    public String getMessageOnly() {
        return message;
    }
}
