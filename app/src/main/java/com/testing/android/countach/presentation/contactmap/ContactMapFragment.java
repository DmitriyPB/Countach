package com.testing.android.countach.presentation.contactmap;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.testing.android.countach.CountachApp;
import com.testing.android.countach.R;
import com.testing.android.countach.data.room.entity.AddressBean;
import com.testing.android.countach.domain.Address;
import com.testing.android.countach.moxymapfragment.BaseMapFragment;

import javax.inject.Inject;
import javax.inject.Provider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

final public class ContactMapFragment extends BaseMapFragment implements ContactMapView {
    private static final String ARG_LOOKUP_KEY_KEY = "lookup_key_key";
    private static final String STATE_LAT = "lat";
    private static final String STATE_LON = "lon";
    private static final String STATE_ADDRESS_NAME = "addressName";

    private GoogleMap map;
    private Marker marker;
    private Button saveButton;

    @Inject
    Provider<ContactMapPresenter> presenterProvider;

    @InjectPresenter
    ContactMapPresenter presenter;
    private Address address;

    @ProvidePresenter
    ContactMapPresenter providePresenter() {
        return presenterProvider.get();
    }

    public static ContactMapFragment newInstance(String lookupKey) {
        ContactMapFragment fragment = new ContactMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOOKUP_KEY_KEY, lookupKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int fragmentLayout() {
        return R.layout.fragment_contact_map;
    }

    @Override
    protected int mapViewId() {
        return R.id.contact_map_view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        CountachApp app = CountachApp.get(requireContext());
        app.getAppComponent().plusContactMapComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        saveButton = view.findViewById(R.id.contact_save_button);
        saveButton.setOnClickListener(this::onSaveButtonPressed);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        address = BundledAddress.readFromBundle(savedInstanceState);
        if (savedInstanceState == null) {
            presenter.loadPinPointForContact(extractLookupKey());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        BundledAddress.writeToBundle(address, outState);
    }

    @Override
    public void onDestroyView() {
        marker = null;
        map = null;
        saveButton = null;
        super.onDestroyView();
    }

    private String extractLookupKey() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            String lookupKey = arguments.getString(ARG_LOOKUP_KEY_KEY);
            if (lookupKey != null) {
                return lookupKey;
            }
        }
        throw new IllegalArgumentException("lookup key not found");
    }

    private void onSaveButtonPressed(View view) {
        presenter.submitPinPoint(extractLookupKey(), address);
    }

    @Override
    public void applyContactPinPoint(@Nullable Address address) {applyContactPinPoint(address, true);}

    private void applyContactPinPoint(@Nullable Address address, boolean zoomIn) {
        this.address = address;
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
        super.onMapReady(googleMap);
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

    private static class BundledAddress implements Address {
        private final String addressName;
        private final double lat;
        private final double lon;

        private BundledAddress(String addressName, double lat, double lon) {
            this.addressName = addressName;
            this.lat = lat;
            this.lon = lon;
        }

        @Override
        public String getAddressName() {
            return addressName;
        }

        @Override
        public double getLat() {
            return lat;
        }

        @Override
        public double getLon() {
            return lon;
        }

        private static void writeToBundle(@Nullable Address address, @NonNull Bundle outState) {
            if (address != null) {
                outState.putString(STATE_ADDRESS_NAME, address.getAddressName());
                outState.putDouble(STATE_LAT, address.getLat());
                outState.putDouble(STATE_LON, address.getLon());
            }
        }

        private static BundledAddress readFromBundle(@Nullable Bundle savedInstanceState) {
            if (savedInstanceState != null) {
                double lat = savedInstanceState.getDouble(STATE_LAT);
                double lon = savedInstanceState.getDouble(STATE_LON);
                String addressName = savedInstanceState.getString(STATE_ADDRESS_NAME);
                return new BundledAddress(addressName, lat, lon);
            }
            return null;
        }
    }
}
