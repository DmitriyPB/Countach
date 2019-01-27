package com.testing.android.countach.data.yandexresponse.geocoding;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeoObjectCollection {

    @SerializedName("metaDataProperty")
    @Expose
    private FeatureMember metaDataProperty;
    @SerializedName("featureMember")
    @Expose
    private List<FeatureMember> featureMember = null;

    public FeatureMember getMetaDataProperty() {
        return metaDataProperty;
    }

    public void setMetaDataProperty(FeatureMember metaDataProperty) {
        this.metaDataProperty = metaDataProperty;
    }

    public List<FeatureMember> getFeatureMember() {
        return featureMember;
    }

    public void setFeatureMember(List<FeatureMember> featureMember) {
        this.featureMember = featureMember;
    }
}
