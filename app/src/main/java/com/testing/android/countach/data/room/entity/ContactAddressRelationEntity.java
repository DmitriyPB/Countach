package com.testing.android.countach.data.room.entity;

import androidx.room.Entity;

@Entity(tableName = "contact_addr_relation",
        primaryKeys = {"contactId", "orgId"}
)
final public class ContactAddressRelationEntity {
    private final int contactId;
    private final int orgId;

    public ContactAddressRelationEntity(int contactId, int orgId) {
        this.contactId = contactId;
        this.orgId = orgId;
    }

    public int getContactId() {
        return contactId;
    }

    public int getOrgId() {
        return orgId;
    }

    @Override
    public String toString() {
        return "ContactAddressRelationEntity{" +
                "contactId=" + contactId +
                ", orgId=" + orgId +
                '}';
    }
}
