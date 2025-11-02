package com.pharmacy.automation.config;

public enum Environment {
    TESTING("testing"),
    STAGING("staging"),
    PRODUCTION("production");

    private final String value;

    Environment(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Environment fromString(String env) {
        for (Environment e : Environment.values()) {
            if (e.value.equalsIgnoreCase(env)) {
                return e;
            }
        }
        return TESTING;
    }
}