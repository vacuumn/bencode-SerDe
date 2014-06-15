package com.github.vacuumn.bencode;

import java.io.IOException;

/**
 * @author vacuumn@gmail.com
 */
public class BencodingException extends IOException {

    protected String bencodedString;

    public BencodingException(String message) {
        super(message);
    }

    public BencodingException(String message, Throwable cause) {
        super(message, cause);
    }

    public BencodingException(String message, String bencodedString) {
        super(message);
        this.bencodedString = bencodedString;
    }

    public BencodingException(String message, Exception cause, String bencodedString) {
        super(message, cause);
        this.bencodedString = bencodedString;
    }

    public String getBencodedString() {
        return bencodedString;
    }
}
