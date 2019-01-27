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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressBean)) return false;

        AddressBean that = (AddressBean) o;

        if (Double.compare(that.getLat(), getLat()) != 0) return false;
        if (Double.compare(that.getLon(), getLon()) != 0) return false;
        return getAddressName() != null ? getAddressName().equals(that.getAddressName()) : that.getAddressName() == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(getLat());
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getLon());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (getAddressName() != null ? getAddressName().hashCode() : 0);
        return result;
    }
}
