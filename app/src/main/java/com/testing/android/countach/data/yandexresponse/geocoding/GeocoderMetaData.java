package com.testing.android.countach.data.yandexresponse.geocoding;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeocoderMetaData {

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("precision")
    @Expose
    private String precision;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }
}
