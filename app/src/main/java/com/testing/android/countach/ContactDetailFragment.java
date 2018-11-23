package com.testing.android.countach;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.testing.android.countach.data.Contact;

public class ContactDetailFragment extends Fragment {
    public static final int CONTACT_DETAILS_LOADER = 1;
    private TextView text_view_name;
    private TextView text_view_email;
    private TextView text_view_phone;

    private ContactDetailsLoaderCallbacks loaderCallbacks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contact_detail, container, false);
        text_view_name = root.findViewById(R.id.text_view_name);
        text_view_email = root.findViewById(R.id.text_view_email);
        text_view_phone = root.findViewById(R.id.text_view_phone);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loaderCallbacks = new ContactDetailsLoaderCallbacks(this);
        loadContactDetails(getArguments());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        text_view_name = null;
        text_view_email = null;
        text_view_phone = null;
    }

    private void loadContactDetails(Bundle arguments) {
        LoaderManager.getInstance(this).initLoader(CONTACT_DETAILS_LOADER, arguments, loaderCallbacks);
    }

    public void applyContact(Contact contact) {
        text_view_name.setText(contact.getName());
        text_view_email.setText(contact.getEmail());
        text_view_phone.setText(contact.getPhoneNumber());
    }
}
