package com.testing.android.countach.details;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.testing.android.countach.CountachApp;
import com.testing.android.countach.R;
import com.testing.android.countach.domain.Contact;

import javax.inject.Inject;
import javax.inject.Provider;

final public class ContactDetailsFragment extends MvpAppCompatFragment implements ContactDetailsView {
    private static final String TAG = ContactDetailsFragment.class.getSimpleName();
    private static final String LOOKUP_KEY_KEY = "lookup_key_key";
    private static final int PERMISSIONS_REQUEST_READ_CONTACT_DETAIL = 101;

    private TextView textViewName;
    private TextView textViewEmail;
    private TextView textViewPhone;
    private ProgressBar progressBar;

    @Inject
    Provider<ContactDetailsPresenter> presenterProvider;

    @InjectPresenter
    ContactDetailsPresenter presenter;

    @ProvidePresenter
    ContactDetailsPresenter providePresenter() {
        return presenterProvider.get();
    }

    public static ContactDetailsFragment newInstance(String lookupKey) {
        ContactDetailsFragment fragment = new ContactDetailsFragment();
        Bundle args = new Bundle();
        args.putString(LOOKUP_KEY_KEY, lookupKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        CountachApp app = CountachApp.get(requireContext());
        app.getAppComponent().plus(new ContactDetailsModule()).inject(this);
        super.onCreate(savedInstanceState);
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
        progressBar = root.findViewById(R.id.progress_bar_contact_details);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadContactDetailsWithPermissionCheck();
    }

    @Override
    public void onDestroyView() {
        textViewName = null;
        textViewEmail = null;
        textViewPhone = null;
        progressBar = null;
        super.onDestroyView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACT_DETAIL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContactDetails();
            } else {
                Toast.makeText(requireContext(), R.string.permission_denied_warning, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void loadContactDetailsWithPermissionCheck() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACT_DETAIL);
        } else {
            loadContactDetails();
        }
    }

    private void loadContactDetails() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            String lookupKey = arguments.getString(ContactDetailsFragment.LOOKUP_KEY_KEY);
            if (lookupKey != null) {
                presenter.loadContactDetails(lookupKey);
                return;
            }
        }
        throw new IllegalArgumentException("lookup key not found");
    }

    @Override
    public void applyContact(Contact contact) {
        textViewName.setText(contact.getName());
        textViewEmail.setText(contact.getEmail());
        textViewPhone.setText(contact.getPhoneNumber());
    }

    @Override
    public void showLoadingIndicator(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}