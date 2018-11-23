package com.testing.android.countach;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.testing.android.countach.data.Contact;

public class ContactDetailFragment extends Fragment {
    private static final int PERMISSIONS_REQUEST_READ_CONTACT_DETAIL = 101;
    public static final int CONTACT_DETAILS_LOADER = 1;
    private TextView textViewName;
    private TextView textViewEmail;
    private TextView textViewPhone;

    private ContactDetailsLoaderCallbacks loaderCallbacks;

    public static ContactDetailFragment newInstance(String lookupKey) {
        ContactDetailFragment fragment = new ContactDetailFragment();
        Bundle args = new Bundle();
        args.putString(ContactDetailsLoaderCallbacks.LOOKUP_KEY_KEY, lookupKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);
        textViewName = root.findViewById(R.id.text_view_name);
        textViewEmail = root.findViewById(R.id.text_view_email);
        textViewPhone = root.findViewById(R.id.text_view_phone);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loaderCallbacks = new ContactDetailsLoaderCallbacks(this);
        loadContactDetailsWithPermissionCheck(getArguments());
    }

    @Override
    public void onDestroyView() {
        textViewName = null;
        textViewEmail = null;
        textViewPhone = null;
        super.onDestroyView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACT_DETAIL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContactDetails(getArguments());
            } else {
                Toast.makeText(requireContext(), R.string.permission_denied_warning, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void loadContactDetailsWithPermissionCheck(Bundle arguments) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requireContext().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACT_DETAIL);
        } else {
            loadContactDetails(arguments);
        }
    }

    private void loadContactDetails(Bundle arguments) {
        LoaderManager.getInstance(this).initLoader(CONTACT_DETAILS_LOADER, arguments, loaderCallbacks);
    }

    public void applyContact(Contact contact) {
        textViewName.setText(contact.getName());
        textViewEmail.setText(contact.getEmail());
        textViewPhone.setText(contact.getPhoneNumber());
    }
}
