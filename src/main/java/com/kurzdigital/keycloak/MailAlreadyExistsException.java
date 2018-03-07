package com.kurzdigital.keycloak;

public class MailAlreadyExistsException extends RuntimeException {

    public MailAlreadyExistsException() {
        super("E-Mail already used.");
    }
}
