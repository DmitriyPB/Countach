package com.testing.android.countach.presentation.organizationsearch;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.testing.android.countach.R;
import com.testing.android.countach.domain.organizationsearch.OrgSearchResult;
import com.testing.android.countach.moxyandroidx.MvpAppCompatFragment;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.support.AndroidSupportInjection;


final public class OrgsListingFragment extends MvpAppCompatFragment implements OrgsListingView {

    private static final String TAG = OrgsListingFragment.class.getSimpleName();

    private OrgsListingAdapter orgAdapter;
    private SearchView searchView;
    private ProgressBar progressBar;
    private RecyclerView recycler;

    @Inject
    Provider<OrgsListingPresenter> presenterProvider;

    @InjectPresenter
    OrgsListingPresenter presenter;

    @ProvidePresenter
    OrgsListingPresenter providePresenter() {
        return presenterProvider.get();
    }

    public static OrgsListingFragment newInstance() {
        return new OrgsListingFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    //    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        CountachApp app = CountachApp.get(requireContext());
//        app.getAppComponent().plusOrgsListingComponent().inject(this);
//        super.onCreate(savedInstanceState);
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orgs_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progress_bar_org_list);
        searchView = view.findViewById(R.id.search_view_org_list);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                loadOrganizations();
                return true;
            }
        });
        recycler = view.findViewById(R.id.recycler_view_org_list);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        orgAdapter = new OrgsListingAdapter(requireContext());
        recycler.setAdapter(orgAdapter);
    }

    @Override
    public void onDestroyView() {
        orgAdapter = null;
        searchView = null;
        progressBar = null;
        recycler = null;
        super.onDestroyView();
    }

    private void loadOrganizations() {
        if (searchView != null) {
            CharSequence raw = searchView.getQuery();
            String query = raw != null ? raw.toString() : null;
            presenter.loadOrganizationByQuery(query);
        }
    }

    @Override
    public void showLoadingIndicator(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        recycler.setVisibility(!show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void applyResult(List<OrgSearchResult> orgs) {
        orgAdapter.submitList(orgs);
    }
}
