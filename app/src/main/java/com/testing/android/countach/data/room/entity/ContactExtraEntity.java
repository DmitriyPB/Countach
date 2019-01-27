package com.testing.android.countach.data.room.entity;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "contact_extra",
        indices = @Index(value = {"lookupKey"}, unique = true)
)
final public class ContactExtraEntity {
    @PrimaryKey(autoGenerate = true)
    private final int id;
    private final String lookupKey;
    @Embedded
    private final AddressBean address;

    public ContactExtraEntity(int id, String lookupKey, AddressBean address) {
        this.id = id;
        this.lookupKey = lookupKey;
        this.address = address;
    }

    @Ignore
    public ContactExtraEntity(String lookupKey) {
        this(0, lookupKey, null);
    }

    @Ignore
    public ContactExtraEntity(String lookupKey, AddressBean address) {
        this(0, lookupKey, address);
    }

    public int getId() {
        return id;
    }

    public String getLookupKey() {
        return lookupKey;
    }

    public AddressBean getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "ContactExtraEntity{" +
                "id=" + id +
                ", lookupKey='" + lookupKey + '\'' +
                ", address=" + address +
                '}';
    }
}
