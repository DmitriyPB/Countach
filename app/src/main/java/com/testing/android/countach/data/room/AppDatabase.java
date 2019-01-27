package com.testing.android.countach.data.room;

import com.testing.android.countach.data.room.dao.ContactExtraDao;
import com.testing.android.countach.data.room.dao.OrgContactRelationDao;
import com.testing.android.countach.data.room.dao.OrganizationDao;
import com.testing.android.countach.data.room.entity.ContactAddressRelationEntity;
import com.testing.android.countach.data.room.entity.ContactExtraEntity;
import com.testing.android.countach.data.room.entity.OrgEntity;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {OrgEntity.class, ContactExtraEntity.class, ContactAddressRelationEntity.class}, version = 6, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "dbb";

    public abstract ContactExtraDao contactExtraDao();

    public abstract OrgContactRelationDao relationDao();

    public abstract OrganizationDao organizationDao();
}