package com.testing.android.countach.data.contactmap;

import android.content.Context;

import com.testing.android.countach.R;

import javax.inject.Inject;

final public class ApiKeyFromResources implements ApiKey {
    private Context context;

    @Inject
    ApiKeyFromResources(Context context) {
        this.context = context;
    }

    @Override
    public String get() {
        return context.getString(R.string.yandex_api_key);
    }
}
