package com.testing.android.countach.listing;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.testing.android.countach.CountachApp;
import com.testing.android.countach.R;
import com.testing.android.countach.domain.Contact;

import java.util.List;

final public class ContactListFragment extends MvpAppCompatFragment implements ContactListView {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private ContactListAdapter.OnContactClickListener listener;
    private ContactListAdapter contactAdapter;

    @InjectPresenter
    ContactListPresenter presenter;

    @ProvidePresenter
    ContactListPresenter providePresenter() {
        CountachApp app = CountachApp.get(requireContext());
        return new ContactListPresenter(app.getRepository(), app.getExecutors());
    }

    public static ContactListFragment newInstance() {
        return new ContactListFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ContactListAdapter.OnContactClickListener) {
            listener = (ContactListAdapter.OnContactClickListener) context;
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
        RecyclerView recyclerContactList = view.findViewById(R.id.recycler_view_contact_list);
        recyclerContactList.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerContactList.addItemDecoration(new ContactDecoration(requireContext()));
        contactAdapter = new ContactListAdapter(listener);
        recyclerContactList.setAdapter(contactAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadContactsWithPermissionCheck();
    }


    @Override
    public void onDestroyView() {
        contactAdapter = null;
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

    private void loadContactsWithPermissionCheck() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            presenter.loadContacts();
        }
    }

    @Override
    public void applyContacts(List<Contact> list) {
        contactAdapter.submitList(list);
    }

    private static class ContactDecoration extends RecyclerView.ItemDecoration {
        private Drawable divider;
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
