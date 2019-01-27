package com.testing.android.countach.data.room.entity;

import com.testing.android.countach.domain.Address;

import androidx.room.Ignore;

final public class AddressBean implements Address {
    private final double lat;
    private final double lon;
    private final String addressName;

    public AddressBean(double lat, double lon, String addressName) {
        this.lat = lat;
        this.lon = lon;
        this.addressName = addressName;
    }

    @Ignore
    public AddressBean(Address address) {
        this(
                address.getLat(),
                address.getLon(),
                address.getAddressName()
        );
    }

    @Override
    public double getLat() {
        return lat;
    }

    @Override
    public double getLon() {
        return lon;
    }

    @Override
    public String getAddressName() {
        return addressName;
    }

    @Override
    public String toString() {
        return "AddressBean{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", addressName='" + addressName + '\'' +
                '}';
    }
}
