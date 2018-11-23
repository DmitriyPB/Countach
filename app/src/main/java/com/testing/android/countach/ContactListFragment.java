package com.testing.android.countach;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.testing.android.countach.adapters.ContactAdapter;
import com.testing.android.countach.data.Contact;

import java.util.List;

public class ContactListFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    public static final int CONTACTS_LOADER = 0;
    private OnContactClickListener listener;
    private ContactListLoaderCallbacks loaderCallbacks;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loaderCallbacks = new ContactListLoaderCallbacks(this);
        loadContactsWithPermissionCheck();
    }

    private void loadContactsWithPermissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            loadContacts();
        }
    }

    private void loadContacts() {
        LoaderManager.getInstance(this).initLoader(CONTACTS_LOADER, null, loaderCallbacks);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contact_list, container, false);
        RecyclerView recycler_view_contact_list = root.findViewById(R.id.recycler_view_contact_list);
        recycler_view_contact_list.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_view_contact_list.setAdapter(new ContactAdapter(listener));
        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContactsWithPermissionCheck();
            } else {
                Toast.makeText(getActivity(), getString(R.string.permission_denied_warning), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnContactClickListener) {
            listener = (OnContactClickListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnContactClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void applyContacts(List<Contact> list) {
        ((ContactAdapter) ((RecyclerView) getView().findViewById(R.id.recycler_view_contact_list)).getAdapter()).submitList(list);
    }

    public interface OnContactClickListener {
        void onContactClicked(Contact contact);
    }
}
