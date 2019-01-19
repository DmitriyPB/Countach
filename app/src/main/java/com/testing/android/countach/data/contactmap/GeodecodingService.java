package com.testing.android.countach.data.contactmap;

import io.reactivex.Single;

public interface GeodecodingService {

    Single<String> decode(
            Double lat,
            Double lon
    );
}
