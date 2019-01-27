package com.testing.android.countach.data.room.dao;

import com.testing.android.countach.data.room.entity.OrgEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public abstract class OrganizationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract List<Long> insertOrganizations(List<OrgEntity> organizations);

    @Query("DELETE FROM organization WHERE orgId NOT IN (SELECT DISTINCT relation.orgId FROM contact_addr_relation relation);")
    public abstract void deleteStaleOrganization();

    @Query("SELECT * FROM organization org " +
            "where org.addressName LIKE :query ")
    public abstract List<OrgEntity> loadAllOrganizations(String query);
}
