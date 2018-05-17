package com.kurzdigital.keycloak;

import java.util.Set;

public class UnsupportedLocaleException extends RuntimeException {

    public UnsupportedLocaleException(Set<String> supportedLocales) {
        super("Given locale is not supported. Supported locales: " + supportedLocales);
    }
}
