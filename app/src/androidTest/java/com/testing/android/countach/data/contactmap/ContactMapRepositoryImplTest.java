package com.testing.android.countach.data.contactmap;

import android.content.Context;

import com.testing.android.countach.data.room.AppDatabase;
import com.testing.android.countach.data.room.dao.ContactExtraDao;
import com.testing.android.countach.data.room.dao.OrgContactRelationDao;
import com.testing.android.countach.data.room.dao.OrganizationDao;
import com.testing.android.countach.data.room.entity.AddressBean;
import com.testing.android.countach.data.room.entity.ContactExtraEntity;
import com.testing.android.countach.data.room.entity.OrgEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;

public class ContactMapRepositoryImplTest {
    private AppDatabase db;
    private ContactExtraDao contactExtraDao;
    private OrgContactRelationDao orgContactRelationDao;
    private OrganizationDao organizationDao;
    @Mock
    private GeodecodingService decodingService;
    @Mock
    private OrganizationsSearchService orgSearchService;
    private ContactMapRepositoryImpl repository;

    @Before
    public void createDb() {
        MockitoAnnotations.initMocks(this);
        Context context = ApplicationProvider.getApplicationContext();
        db = Room
                .inMemoryDatabaseBuilder(context.getApplicationContext(), AppDatabase.class)
                .fallbackToDestructiveMigration()
                .build();
        contactExtraDao = db.contactExtraDao();
        organizationDao = db.organizationDao();
        orgContactRelationDao = db.relationDao();
        repository = new ContactMapRepositoryImpl(
                new ApiKeyFromResources(context),
                decodingService,
                orgSearchService,
                contactExtraDao,
                organizationDao,
                orgContactRelationDao,
                db
        );
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testFirstContactSubmit() throws Exception {
        OrgEntity firstOrg = new OrgEntity(1L, 1, 1, "orgOneAddress");
        OrgEntity secondOrg = new OrgEntity(2L, 2, 2, "orgTwoAddress");
        Mockito.when(orgSearchService.searchForOrganizations(anyString(), anyString()))
                .thenReturn(Arrays.asList(
                        firstOrg,
                        secondOrg
                ));

        String lookup = "testFirstContactLookup";
        AddressBean expectedAddress = new AddressBean(2, 3, "testAddressName");
        repository.submitContact(lookup, expectedAddress).blockingAwait();

        ContactExtraEntity actualContact = contactExtraDao.getContactByLookup(lookup);
        assertNotNull(actualContact);
        AddressBean actualAddress = actualContact.getAddress();
        assertEquals(expectedAddress, actualAddress);

        List<OrgEntity> allOrganizations = organizationDao.loadAllOrganizations("%%");
        assertEquals(2, allOrganizations.size());
        OrgEntity firstOrgActual = allOrganizations.get(0);
        OrgEntity secondOrgActual = allOrganizations.get(1);
        assertEquals(firstOrg, firstOrgActual);
        assertEquals(secondOrg, secondOrgActual);

        List<String> lookupsByOrgId = orgContactRelationDao.getLookupsByOrgId(1);
        assertEquals(1, lookupsByOrgId.size());
        String actualLookup = lookupsByOrgId.get(0);
        assertEquals(lookup, actualLookup);

    }
}