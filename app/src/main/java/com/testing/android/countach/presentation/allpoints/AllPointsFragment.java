package com.testing.android.countach.presentation.allpoints;

import android.content.Context;
import android.os.Bundle;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.testing.android.countach.CountachApp;
import com.testing.android.countach.R;
import com.testing.android.countach.domain.PinPoint;
import com.testing.android.countach.moxymapfragment.BaseMapFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import dagger.android.support.AndroidSupportInjection;

final public class AllPointsFragment extends BaseMapFragment implements AllPointsView, OnMapReadyCallback {

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
    protected int fragmentLayout() {
        return R.layout.fragment_contact_map;
    }

    @Override
    protected int mapViewId() {
        return R.id.contact_map_view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        CountachApp app = CountachApp.get(requireContext());
//        app.getAppComponent().plusAllPointsComponent().inject(this);
//        super.onCreate(savedInstanceState);
//    }

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
        super.onMapReady(googleMap);
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
