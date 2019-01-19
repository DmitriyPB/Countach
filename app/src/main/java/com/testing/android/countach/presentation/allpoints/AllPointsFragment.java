package com.testing.android.countach.presentation.allpoints;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.testing.android.countach.CountachApp;
import com.testing.android.countach.R;
import com.testing.android.countach.domain.PinPoint;
import com.testing.android.countach.moxyandroidx.MvpAppCompatFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

final public class AllPointsFragment extends MvpAppCompatFragment implements AllPointsView, OnMapReadyCallback {

    private GoogleMap map;

    @Inject
    Provider<AllPointsPresenter> presenterProvider;

    @InjectPresenter
    AllPointsPresenter presenter;

    @ProvidePresenter
    AllPointsPresenter providePresenter() {
        return presenterProvider.get();
    }

    public static AllPointsFragment newInstance() {
        return new AllPointsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        CountachApp app = CountachApp.get(requireContext());
        app.getAppComponent().plusAllPointsComponent().inject(this);
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadAllPinPoints();
    }

    @Override
    public void onDestroyView() {
        clearMap();
        map = null;
        super.onDestroyView();
    }

    private void loadAllPinPoints() {
        presenter.loadAllPinPoints();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    @Override
    public void applyPinPoints(@Nullable List<? extends PinPoint> points) {
        clearMap();
        if (points != null) {
            List<LatLng> latLngs = pointsToLatLng(points);
            for (LatLng latLng : latLngs) {
                map.addMarker(new MarkerOptions().position(latLng));
            }
            zoomInAllMarkers(latLngs);
        }
    }

    private List<LatLng> pointsToLatLng(@NonNull List<? extends PinPoint> points) {
        List<LatLng> latLngs = new ArrayList<>(points.size());
        for (PinPoint point : points) {
            latLngs.add(new LatLng(point.getLat(), point.getLon()));
        }
        return latLngs;
    }

    private void zoomInAllMarkers(@NonNull List<LatLng> markersPos) {
        if (!markersPos.isEmpty()) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng pos : markersPos) {
                builder.include(pos);
            }
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
        }
    }

    private void clearMap() {
        if (map != null) {
            map.clear();
        }
    }
}
