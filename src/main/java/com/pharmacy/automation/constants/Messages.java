package com.pharmacy.automation.constants;

public final class Messages {
    private Messages() {}

    // Login Messages
    public static final String LOGIN_SUCCESS = "Login successful";
    public static final String LOGIN_FAILED = "Invalid credentials";
    public static final String LOGOUT_SUCCESS = "Logged out successfully";

    // Product Messages
    public static final String PRODUCT_ADDED = "Product added successfully";
    public static final String PRODUCT_UPDATED = "Product updated successfully";
    public static final String PRODUCT_DELETED = "Product deleted successfully";
    public static final String ADD_TO_CART_SUCCESS = "Product added to cart";

    // Order Messages
    public static final String ORDER_PLACED = "Order placed successfully";
    public static final String ORDER_CANCELLED = "Order cancelled";
    public static final String ORDER_NOT_FOUND = "Order not found";

    // Validation Messages
    public static final String REQUIRED_FIELD = "This field is required";
    public static final String INVALID_EMAIL = "Invalid email format";
    public static final String INVALID_PASSWORD = "Password must be at least 8 characters";

    // Error Messages
    public static final String ELEMENT_NOT_FOUND = "Element not found: %s";
    public static final String PAGE_NOT_LOADED = "Page did not load within timeout";
    public static final String OPERATION_FAILED = "Operation failed: %s";
}