package com.kurzdigital.keycloak;

import java.util.Set;

public class UnsupportetLocaleException extends RuntimeException {

    public UnsupportetLocaleException(Set<String> supportedLocales) {
        super("Given locale is not supported. Supported locales: " + supportedLocales);
    }
}
