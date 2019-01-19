package com.testing.android.countach;

import android.os.Bundle;

import com.testing.android.countach.domain.Contact;
import com.testing.android.countach.presentation.details.ContactDetailsFragment;
import com.testing.android.countach.presentation.listing.ContactsListingAdapter;
import com.testing.android.countach.presentation.listing.ContactsListingFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

final public class MainActivity extends AppCompatActivity implements ContactsListingAdapter.OnContactClickListener {

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
        fragmentTransaction.replace(R.id.fragment_container, ContactsListingFragment.newInstance());
        fragmentTransaction.commit();
    }

    @Override
    public void onContactClicked(Contact contact) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, ContactDetailsFragment.newInstance(contact.getLookup()));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
