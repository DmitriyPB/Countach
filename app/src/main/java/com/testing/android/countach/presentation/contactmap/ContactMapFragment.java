package com.testing.android.countach.presentation.contactmap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.testing.android.countach.CountachApp;
import com.testing.android.countach.R;
import com.testing.android.countach.data.room.entity.AddressBean;
import com.testing.android.countach.domain.Address;
import com.testing.android.countach.moxyandroidx.MvpAppCompatFragment;

import javax.inject.Inject;
import javax.inject.Provider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

final public class ContactMapFragment extends MvpAppCompatFragment implements ContactMapView, OnMapReadyCallback {
    private static final String LOOKUP_KEY_KEY = "lookup_key_key";

    private GoogleMap map;
    private Marker marker;
    private Button saveButton;

    @Inject
    Provider<ContactMapPresenter> presenterProvider;

    @InjectPresenter
    ContactMapPresenter presenter;

    @ProvidePresenter
    ContactMapPresenter providePresenter() {
        return presenterProvider.get();
    }

    public static ContactMapFragment newInstance(String lookupKey) {
        ContactMapFragment fragment = new ContactMapFragment();
        Bundle args = new Bundle();
        args.putString(LOOKUP_KEY_KEY, lookupKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        CountachApp app = CountachApp.get(requireContext());
        app.getAppComponent().plusContactMapComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.contact_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        saveButton = view.findViewById(R.id.contact_save_button);
        saveButton.setOnClickListener(this::onSaveButtonPressed);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadContactPinPoint();
    }

    @Override
    public void onDestroyView() {
        marker = null;
        map = null;
        saveButton = null;
        super.onDestroyView();
    }

    private void loadContactPinPoint() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            String lookupKey = arguments.getString(LOOKUP_KEY_KEY);
            if (lookupKey != null) {
                presenter.loadPinPointForContact(lookupKey);
                return;
            }
        }
        throw new IllegalArgumentException("lookup key not found");
    }

    private void onSaveButtonPressed(View view) {
        presenter.submitPinPoint();
    }

    @Override
    public void applyContactPinPoint(@Nullable Address address) {applyContactPinPoint(address, true);}

    private void applyContactPinPoint(@Nullable Address address, boolean zoomIn) {
        if (address != null) {
            if (marker != null) {
                marker.remove();
            }
            LatLng latLng = new LatLng(address.getLat(), address.getLon());
            marker = map.addMarker(new MarkerOptions().title(address.getAddressName()).position(latLng));
            marker.showInfoWindow();
            if (zoomIn) {
                zoomInMarker(latLng);
            }
        }
    }

    @Override
    public void onSubmitSuccess() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void showGeodecodingIndicator(boolean show) {
        if (show) {
            if (marker != null) {
                marker.setTitle(getString(R.string.map_search_in_progress));
                marker.showInfoWindow();
            }
        }
    }

    @Override
    public void onGeodecodingFinished(Address point) {
        if (point == null || point.getAddressName().isEmpty()) {
            hideSaveButton();
            marker.hideInfoWindow();
            marker.setTitle(getString(R.string.map_address_not_found));
            marker.showInfoWindow();
        } else {
            applyContactPinPoint(point);
            showSaveButton();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapClickListener(latLng -> {
            hideSaveButton();
            if (marker != null) {
                marker.remove();
            }
            AddressBean point = new AddressBean(latLng.latitude, latLng.longitude, null);
            applyContactPinPoint(point, false);
            presenter.submitGeodecoding(point);
        });
    }

    private void zoomInMarker(@NonNull LatLng marker) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker, 17));
    }

    private void showSaveButton() {
        saveButton.setVisibility(View.VISIBLE);
    }

    private void hideSaveButton() {
        saveButton.setVisibility(View.GONE);
    }
}
