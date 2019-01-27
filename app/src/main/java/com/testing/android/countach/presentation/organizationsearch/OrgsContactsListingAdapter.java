package com.testing.android.countach.presentation.organizationsearch;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.testing.android.countach.R;
import com.testing.android.countach.domain.Contact;

final public class OrgsContactsListingAdapter extends ListAdapter<Contact, OrgsContactsListingAdapter.ContactViewHolder> {

    OrgsContactsListingAdapter() {
        super(new ContactItemDiffCallback());
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ContactViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_orgs_contact, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder contactViewHolder, int position) {
        contactViewHolder.bind(getItem(position));
    }

    private static class ContactItemDiffCallback extends DiffUtil.ItemCallback<Contact> {
        @Override
        public boolean areItemsTheSame(@NonNull Contact contact, @NonNull Contact anotherContact) {
            return contact.getLookup().equals(anotherContact.getLookup());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Contact contact, @NonNull Contact anotherContact) {
            return contact.equals(anotherContact);
        }
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewContactName;

        ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContactName = itemView.findViewById(R.id.text_view_orgs_contact_name);
        }

        void bind(final Contact contact) {
            textViewContactName.setText(contact.getName());
        }
    }
}
