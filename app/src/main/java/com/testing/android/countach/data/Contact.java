package com.testing.android.countach.data;

public class Contact {
    private final String name;
    private final String phoneNumber;
    private final String email;
    private final String lookup;

    public Contact(String name, String phoneNumber, String email, String lookup) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.lookup = lookup;
    }

    public String getEmail() {
        return email;
    }

    public String getLookup() {
        return lookup;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", lookup='" + lookup + '\'' +
                '}';
    }
}

