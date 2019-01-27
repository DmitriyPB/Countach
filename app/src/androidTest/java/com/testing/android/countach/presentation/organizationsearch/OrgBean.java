package com.testing.android.countach.presentation.organizationsearch;

import com.testing.android.countach.domain.Organization;

class OrgBean implements Organization {

    private Long orgId;
    private String addressName;
    private double lat;
    private double lon;

    public OrgBean(Long orgId, String addressName, double lat, double lon) {
        this.orgId = orgId;
        this.addressName = addressName;
        this.lat = lat;
        this.lon = lon;
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
}
