package com.testing.android.countach.presentation.organizationsearch;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.testing.android.countach.R;
import com.testing.android.countach.domain.organizationsearch.OrgSearchResult;

final public class OrgsListingAdapter extends ListAdapter<OrgSearchResult, OrgsListingAdapter.OrgViewHolder> {

    private Context context;

    OrgsListingAdapter(Context context) {
        super(new OrgItemDiffCallback());
        this.context = context;
    }

    @NonNull
    @Override
    public OrgViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new OrgViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_org, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrgViewHolder viewHolder, int position) {
        viewHolder.bind(getItem(position));
    }

    private static class OrgItemDiffCallback extends DiffUtil.ItemCallback<OrgSearchResult> {
        @Override
        public boolean areItemsTheSame(@NonNull OrgSearchResult org, @NonNull OrgSearchResult anotherOrg) {
            return org.getOrg().equals(anotherOrg.getOrg());
        }

        @Override
        public boolean areContentsTheSame(@NonNull OrgSearchResult org, @NonNull OrgSearchResult anotherOrg) {
            return org.equals(anotherOrg);
        }
    }

    class OrgViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewOrgName;
        private final RecyclerView recyclerContacts;
        private final OrgsContactsListingAdapter orgAdapter;

        OrgViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOrgName = itemView.findViewById(R.id.text_view_org_name);
            recyclerContacts = itemView.findViewById(R.id.recycler_view_orgs_contact_list);
            recyclerContacts.setLayoutManager(new LinearLayoutManager(context));
            orgAdapter = new OrgsContactsListingAdapter();
            recyclerContacts.setAdapter(orgAdapter);
        }

        void bind(final OrgSearchResult result) {
            textViewOrgName.setText(result.getOrg().getAddressName());
            orgAdapter.submitList(result.getNearestContacts());
        }
    }
}
