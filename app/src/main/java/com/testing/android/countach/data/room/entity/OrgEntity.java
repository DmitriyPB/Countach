package com.testing.android.countach.data.room.entity;

import com.testing.android.countach.domain.Organization;

import java.util.LinkedList;
import java.util.List;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "organization",
        indices = @Index(value = {"addressName"})
)
final public class OrgEntity implements Organization {

    @PrimaryKey
    private final long orgId;
    private final double lat;
    private final double lon;
    private final String addressName;

    public OrgEntity(Long orgId, double lat, double lon, String addressName) {
        this.orgId = orgId;
        this.lat = lat;
        this.lon = lon;
        this.addressName = addressName;
    }

    @Ignore
    public OrgEntity(Organization org) {
        this(org.getOrgId(), org.getLat(), org.getLon(), org.getAddressName());
    }

    @Override
    public Long getOrgId() {
        return orgId;
    }

    @Override
    public String getAddressName() {
        return addressName;
    }

    @Override
    public double getLat() {
        return lat;
    }

    @Override
    public double getLon() {
        return lon;
    }

    public static List<OrgEntity> listFrom(List<Organization> orgs) {
        List<OrgEntity> entities = new LinkedList<>();
        if (orgs != null && !orgs.isEmpty()) {
            for (Organization org : orgs) {
                entities.add(new OrgEntity(org));
            }
        }
        return entities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrgEntity)) return false;

        Organization organization = (Organization) o;

        if (Double.compare(organization.getLat(), getLat()) != 0) return false;
        if (Double.compare(organization.getLon(), getLon()) != 0) return false;
        if (getOrgId() != null ? !getOrgId().equals(organization.getOrgId()) : organization.getOrgId() != null)
            return false;
        return getAddressName() != null ? getAddressName().equals(organization.getAddressName()) : organization.getAddressName() == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getOrgId() != null ? getOrgId().hashCode() : 0;
        temp = Double.doubleToLongBits(getLat());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getLon());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (getAddressName() != null ? getAddressName().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OrgEntity{" +
                "orgId=" + orgId +
                ", lat=" + lat +
                ", lon=" + lon +
                ", addressName='" + addressName + '\'' +
                '}';
    }
}
