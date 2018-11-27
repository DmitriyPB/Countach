package com.testing.android.countach.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.testing.android.countach.R;
import com.testing.android.countach.data.Contact;
import com.testing.android.countach.presentation.presenter.ContactListPresenter;
import com.testing.android.countach.presentation.presenter.LoaderProvider;
import com.testing.android.countach.presentation.view.ContactListView;
import com.testing.android.countach.ui.adapters.ContactAdapter;

import java.util.List;

public class ContactListFragment extends MvpAppCompatFragment implements ContactListView {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    public static final int CONTACTS_LOADER = 0;
    private ContactAdapter.OnContactClickListener listener;
    private ContactAdapter contactAdapter;

    @InjectPresenter
    ContactListPresenter presenter;

    @ProvidePresenter
    ContactListPresenter providePresenter() {
        return new ContactListPresenter(new LoaderProvider() {
            @Override
            public Loader<Cursor> provideLoader(Uri contentUri, String[] PROJECTION, String selection, String[] selectionArgs, String sort) {
                return new CursorLoader(
                        requireContext(),
                        contentUri,
                        PROJECTION,
                        selection,
                        selectionArgs,
                        sort);
            }
        });
    }

    public static ContactListFragment newInstance() {
        return new ContactListFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            loadContactsWithPermissionCheck();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerContactList = view.findViewById(R.id.recycler_view_contact_list);
        recyclerContactList.setLayoutManager(new LinearLayoutManager(requireContext()));
        contactAdapter = new ContactAdapter(listener);
        recyclerContactList.setAdapter(contactAdapter);
    }

    @Override
    public void onDestroyView() {
        contactAdapter = null;
        super.onDestroyView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContactsWithPermissionCheck();
            } else {
                Toast.makeText(requireContext(), R.string.permission_denied_warning, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ContactAdapter.OnContactClickListener) {
            listener = (ContactAdapter.OnContactClickListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnContactClickListener");
        }
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }

    private void loadContactsWithPermissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requireContext().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            presenter.loadContacts(LoaderManager.getInstance(this));
        }
    }

    @Override
    public void applyContacts(List<Contact> list) {
        contactAdapter.submitList(list);
    }

}
