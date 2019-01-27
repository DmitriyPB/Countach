package com.testing.android.countach.data.yandexresponse.orgsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Properties_ {

    @SerializedName("CompanyMetaData")
    @Expose
    private CompanyMetaData companyMetaData;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("name")
    @Expose
    private String name;

    public CompanyMetaData getCompanyMetaData() {
        return companyMetaData;
    }

    public void setCompanyMetaData(CompanyMetaData companyMetaData) {
        this.companyMetaData = companyMetaData;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}