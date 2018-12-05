package com.testing.android.countach;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.testing.android.countach.domain.Contact;
import com.testing.android.countach.listing.ContactListAdapter;
import com.testing.android.countach.details.ContactDetailFragment;
import com.testing.android.countach.listing.ContactListFragment;

final public class MainActivity extends AppCompatActivity implements ContactListAdapter.OnContactClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            initContactListFragment();
        }
    }

    private void initContactListFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, ContactListFragment.newInstance());
        fragmentTransaction.commit();
    }

    @Override
    public void onContactClicked(Contact contact) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, ContactDetailFragment.newInstance(contact.getLookup()));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
