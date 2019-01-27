package com.testing.android.countach;

import android.os.Bundle;

import com.testing.android.countach.domain.Contact;
import com.testing.android.countach.presentation.details.ContactDetailsFragment;
import com.testing.android.countach.presentation.listing.ContactsListingAdapter;

import javax.inject.Inject;
import javax.inject.Provider;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

final public class MainActivity extends AppCompatActivity implements ContactsListingAdapter.OnContactClickListener, HasSupportFragmentInjector {

    private static final String TAG = MainActivity.class.getSimpleName();
    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Inject
    Provider<Fragment> initialFragmentProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            setFragment(initialFragmentProvider.get());
        }
    }

    public void setFragment(Fragment fragment) {
        setFragment(fragment, false);
    }

    public void setFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public void onContactClicked(Contact contact) {
        setFragment(ContactDetailsFragment.newInstance(contact.getLookup()), true);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }
}
