package com.testing.android.countach.presentation.listing;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.testing.android.countach.R;
import com.testing.android.countach.domain.Contact;
import com.testing.android.countach.moxyandroidx.MvpAppCompatFragment;
import com.testing.android.countach.presentation.allpoints.AllPointsFragment;
import com.testing.android.countach.presentation.organizationsearch.OrgsListingFragment;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.support.AndroidSupportInjection;


final public class ContactsListingFragment extends MvpAppCompatFragment implements ContactsListingView {

    private static final String TAG = ContactsListingFragment.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private ContactsListingAdapter.OnContactClickListener listener;
    private ContactsListingAdapter contactAdapter;
    private SearchView searchView;
    private ProgressBar progressBar;
    private RecyclerView recycler;
    private Button buttonAllPoints;
    private Button buttonOrgSearch;

    @Inject
    Provider<ContactsListingPresenter> presenterProvider;

    @InjectPresenter
    ContactsListingPresenter presenter;

    @ProvidePresenter
    ContactsListingPresenter providePresenter() {
        return presenterProvider.get();
    }

    public static ContactsListingFragment newInstance() {
        return new ContactsListingFragment();
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        if (context instanceof ContactsListingAdapter.OnContactClickListener) {
            listener = (ContactsListingAdapter.OnContactClickListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnContactClickListener");
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
        progressBar = view.findViewById(R.id.progress_bar_contact_list);
        searchView = view.findViewById(R.id.search_view_contact_list);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                loadContactsWithPermissionCheck();
                return true;
            }
        });
        buttonAllPoints = view.findViewById(R.id.button_all_points);
        buttonAllPoints.setOnClickListener(this::onAllPointsButtonPressed);
        buttonOrgSearch = view.findViewById(R.id.button_org_search);
        buttonOrgSearch.setOnClickListener(this::onOrgSearchButtonPressed);
        recycler = view.findViewById(R.id.recycler_view_contact_list);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.addItemDecoration(new ContactDecoration(requireContext()));
        contactAdapter = new ContactsListingAdapter(listener);
        recycler.setAdapter(contactAdapter);
    }

    @Override
    public void onDestroyView() {
        contactAdapter = null;
        searchView = null;
        progressBar = null;
        recycler = null;
        buttonAllPoints = null;
        buttonOrgSearch = null;
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
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
    public void loadContactsWithPermissionCheck() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            if (searchView != null) {
                CharSequence raw = searchView.getQuery();
                String query = raw != null ? raw.toString() : null;
                presenter.loadContacts(query);
            }
        }
    }

    @Override
    public void showLoadingIndicator(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        recycler.setVisibility(!show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void applyContacts(List<Contact> list) {
        contactAdapter.submitList(list);
    }

    private void onAllPointsButtonPressed(View view) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, AllPointsFragment.newInstance());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void onOrgSearchButtonPressed(View view) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, OrgsListingFragment.newInstance());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private static class ContactDecoration extends RecyclerView.ItemDecoration {
        private final Drawable divider;
        private final Rect bounds = new Rect();

        ContactDecoration(Context context) {
            this.divider = ContextCompat.getDrawable(context, R.drawable.divider_contact);
        }

        @Override
        public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            canvas.save();
            int left;
            int right;
            if (parent.getClipToPadding()) {
                left = parent.getPaddingLeft();
                right = parent.getWidth() - parent.getPaddingRight();
                canvas.clipRect(left, parent.getPaddingTop(), right, parent.getHeight() - parent.getPaddingBottom());
            } else {
                left = 0;
                right = parent.getWidth();
            }

            int childCount = parent.getChildCount();

            for (int i = 0; i < childCount; ++i) {
                View child = parent.getChildAt(i);
                parent.getDecoratedBoundsWithMargins(child, this.bounds);
                int bottom = this.bounds.bottom + Math.round(child.getTranslationY());
                int top = bottom - this.divider.getIntrinsicHeight();
                this.divider.setBounds(left, top, right, bottom);
                this.divider.draw(canvas);
            }

            canvas.restore();
        }
    }
}
