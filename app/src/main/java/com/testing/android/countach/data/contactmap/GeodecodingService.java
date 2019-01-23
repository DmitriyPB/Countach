package com.testing.android.countach.data.contactmap;

import androidx.annotation.NonNull;

public interface GeodecodingService {

    @NonNull
    String decode(double lat, double lon);
}
