package com.testing.android.countach.presentation.presenter;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.Loader;

public interface LoaderProvider {
    Loader<Cursor> provideLoader(Uri contentUri, String[] PROJECTION, String selection, String[] selectionArgs, String sort);
}
