package com.testing.android.countach;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.testing.android.countach.data.Contact;

public class MainActivity extends AppCompatActivity implements ContactListFragment.OnContactClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            initContactListFragment();
        }
    }

    private void initContactListFragment() {
        Log.d(TAG, "initContactListFragment()");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ContactListFragment fragment = new ContactListFragment();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onContactClicked(Contact contact) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ContactDetailFragment fragment = new ContactDetailFragment();
        Bundle args = new Bundle();
        args.putString(ContactDetailsLoaderCallbacks.LOOKUP_KEY_KEY, contact.getLookup());
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
