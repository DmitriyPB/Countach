package com.testing.android.countach;

import android.content.Context;

import com.testing.android.countach.data.ContactsDao;
import com.testing.android.countach.data.ContactsDaoImpl;
import com.testing.android.countach.data.room.AppDatabase;
import com.testing.android.countach.data.room.dao.ContactExtraDao;
import com.testing.android.countach.data.room.dao.OrgContactRelationDao;
import com.testing.android.countach.data.room.dao.OrganizationDao;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

import static com.testing.android.countach.data.room.AppDatabase.DATABASE_NAME;

@Module
final class DataModule {
    @Provides
    @Singleton
    OkHttpClient provideOKHttpClient() {
        return new OkHttpClient();
    }

    @Provides
    @Singleton
    AppDatabase provideRoomDb(Context context) {
        return Room
                .databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    ContactsDao provideContactDao(Context context) {
        return new ContactsDaoImpl(context);
    }

    @Provides
    ContactExtraDao provideContactExtraDao(AppDatabase db) {
        return db.contactExtraDao();
    }

    @Provides
    OrganizationDao provideOrganizationDao(AppDatabase db) {
        return db.organizationDao();
    }

    @Provides
    OrgContactRelationDao provideRelationDao(AppDatabase db) {
        return db.relationDao();
    }
}
