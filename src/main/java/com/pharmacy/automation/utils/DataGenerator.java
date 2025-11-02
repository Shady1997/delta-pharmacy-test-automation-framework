package com.pharmacy.automation.utils;

import com.github.javafaker.Faker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;

public class DataGenerator {
    private static final Logger logger = LogManager.getLogger(DataGenerator.class);
    private static final Faker faker = new Faker(new Locale("en"));

    public static String generateEmail() {
        return faker.internet().emailAddress();
    }

    public static String generateFirstName() {
        return faker.name().firstName();
    }

    public static String generateLastName() {
        return faker.name().lastName();
    }

    public static String generateFullName() {
        return faker.name().fullName();
    }

    public static String generatePhoneNumber() {
        return faker.phoneNumber().phoneNumber();
    }

    public static String generateAddress() {
        return faker.address().fullAddress();
    }

    public static String generateCity() {
        return faker.address().city();
    }

    public static String generateZipCode() {
        return faker.address().zipCode();
    }

    public static String generateCountry() {
        return faker.address().country();
    }

    public static String generateCreditCard() {
        return faker.business().creditCardNumber();
    }

    public static String generateCreditCardExpiry() {
        return faker.business().creditCardExpiry();
    }

    public static String generatePassword() {
        return faker.internet().password(8, 16, true, true, true);
    }

    public static String generateCompanyName() {
        return faker.company().name();
    }

    public static String generateProductName() {
        return faker.commerce().productName();
    }

    public static String generatePrice() {
        return faker.commerce().price();
    }
}