package com.testing.android.countach.moxymapfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpDelegate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseMapFragment extends Fragment implements OnMapReadyCallback {

    private final static String KEY_MAP_VIEW_OUT_STATE = "mapview_state";

    private boolean isGoogleMapReady;
    private boolean mIsStateSaved;
    private MvpDelegate<? extends BaseMapFragment> mMvpDelegate;
    protected MapView mapView;

    @LayoutRes
    protected abstract int fragmentLayout();

    @IdRes
    protected abstract int mapViewId();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMvpDelegate().onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(fragmentLayout(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(mapViewId());

        if (savedInstanceState == null) {
            mapView.onCreate(null);
        } else {
            mapView.onCreate(savedInstanceState.getBundle(KEY_MAP_VIEW_OUT_STATE));
        }

        isGoogleMapReady = false;
        mapView.getMapAsync(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        mIsStateSaved = false;

        if (isGoogleMapReady) {
            getMvpDelegate().onAttach();
        }
    }

    public void onResume() {
        super.onResume();

        mIsStateSaved = false;
        if (isGoogleMapReady) {
            getMvpDelegate().onAttach();
        }
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mIsStateSaved = true;

        getMvpDelegate().onSaveInstanceState(outState);
        getMvpDelegate().onDetach();

        Bundle bundle = new Bundle();
        mapView.onSaveInstanceState(bundle);
        outState.putBundle(KEY_MAP_VIEW_OUT_STATE, bundle);
    }

    @Override
    public void onStop() {
        super.onStop();

        getMvpDelegate().onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getMvpDelegate().onDetach();
        getMvpDelegate().onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //We leave the screen and respectively all fragments will be destroyed
        if (getActivity().isFinishing()) {
            getMvpDelegate().onDestroy();
            return;
        }

        // When we rotate device isRemoving() return true for fragment placed in backstack
        // http://stackoverflow.com/questions/34649126/fragment-back-stack-and-isremoving
        if (mIsStateSaved) {
            mIsStateSaved = false;
            return;
        }

        // See https://github.com/Arello-Mobile/Moxy/issues/24
        boolean anyParentIsRemoving = false;
        Fragment parent = getParentFragment();
        while (!anyParentIsRemoving && parent != null) {
            anyParentIsRemoving = parent.isRemoving();
            parent = parent.getParentFragment();
        }

        if (isRemoving() || anyParentIsRemoving) {
            getMvpDelegate().onDestroy();
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    /**
     * @return The {@link MvpDelegate} being used by this Fragment.
     */
    public MvpDelegate getMvpDelegate() {
        if (mMvpDelegate == null) {
            mMvpDelegate = new MvpDelegate<>(this);
        }

        return mMvpDelegate;
    }

    @CallSuper
    @Override
    public void onMapReady(GoogleMap googleMap) {
        isGoogleMapReady = true;
        getMvpDelegate().onAttach();
    }
}
