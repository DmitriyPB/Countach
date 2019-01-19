package com.testing.android.countach.data.room.dao;

import com.testing.android.countach.data.room.entity.AddressBean;
import com.testing.android.countach.data.room.entity.ContactExtraEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public abstract class ContactExtraDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Long insertContact(ContactExtraEntity contact);

    @Query("SELECT * FROM contact_extra contact where contact.lookupKey = :lookupKey")
    public abstract ContactExtraEntity getContactByLookup(String lookupKey);

    @Query("SELECT contact.lat, contact.lon, contact.addressName FROM contact_extra contact")
    public abstract List<AddressBean> loadAllContactPoints();
}
