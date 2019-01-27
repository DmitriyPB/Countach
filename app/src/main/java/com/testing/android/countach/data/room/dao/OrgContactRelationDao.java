package com.testing.android.countach.data.room.dao;

import com.testing.android.countach.data.room.entity.ContactAddressRelationEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public abstract class OrgContactRelationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertRelation(ContactAddressRelationEntity relation);

    @Query("DELETE FROM contact_addr_relation WHERE contactId=:contactId;")
    public abstract void deleteRelationsByContactId(int contactId);

    @Query("SELECT contact.lookupKey FROM contact_addr_relation relation  " +
            "left join contact_extra contact " +
            "on relation.contactId = contact.id " +
            "where relation.orgId = :orgId")
    public abstract List<String> getLookupsByOrgId(int orgId);
}
