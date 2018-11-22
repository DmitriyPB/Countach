package com.testing.android.countach.adapters;

import android.support.annotation.NonNull;

import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.testing.android.countach.ContactListFragment;
import com.testing.android.countach.R;
import com.testing.android.countach.data.Contact;

public class ContactAdapter extends ListAdapter<Contact, ContactAdapter.ContactViewHolder> {

    private ContactListFragment.OnContactClickListener clickListener;

    public ContactAdapter(ContactListFragment.OnContactClickListener clickListener) {
        super(new ContactItemDiffCallback());
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ContactViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_contact, viewGroup, false));
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
            return contact == anotherContact;
        }
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        private TextView text_view_contact_name;
        private TextView text_view_contact_phone_number;

        ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            text_view_contact_name = itemView.findViewById(R.id.text_view_contact_name);
            text_view_contact_phone_number = itemView.findViewById(R.id.text_view_contact_phone_number);
        }

        void bind(final Contact contact) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onContactClicked(contact);
                    }
                }
            });
            text_view_contact_name.setText(contact.getName());
            text_view_contact_phone_number.setText(contact.getPhoneNumber());
        }
    }
}
