package com.testing.android.countach.domain;

final public class Contact {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;

        Contact contact = (Contact) o;

        if (getName() != null ? !getName().equals(contact.getName()) : contact.getName() != null)
            return false;
        if (getPhoneNumber() != null ? !getPhoneNumber().equals(contact.getPhoneNumber()) : contact.getPhoneNumber() != null)
            return false;
        if (getEmail() != null ? !getEmail().equals(contact.getEmail()) : contact.getEmail() != null)
            return false;
        return getLookup() != null ? getLookup().equals(contact.getLookup()) : contact.getLookup() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getPhoneNumber() != null ? getPhoneNumber().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getLookup() != null ? getLookup().hashCode() : 0);
        return result;
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

