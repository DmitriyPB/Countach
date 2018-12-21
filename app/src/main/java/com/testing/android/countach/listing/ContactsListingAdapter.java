package com.testing.android.countach.listing;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.testing.android.countach.R;
import com.testing.android.countach.domain.Contact;

final public class ContactsListingAdapter extends ListAdapter<Contact, ContactsListingAdapter.ContactViewHolder> {

    private OnContactClickListener clickListener;

    public ContactsListingAdapter(OnContactClickListener clickListener) {
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
            return contact.equals(anotherContact);
        }
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewContactName;
        private TextView textViewContactPhoneNumber;
        private Contact contact;

        ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContactName = itemView.findViewById(R.id.text_view_contact_name);
            textViewContactPhoneNumber = itemView.findViewById(R.id.text_view_contact_phone_number);
            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onContactClicked(contact);
                }
            });
        }

        void bind(final Contact contact) {
            this.contact = contact;
            textViewContactName.setText(contact.getName());
            textViewContactPhoneNumber.setText(contact.getPhoneNumber());
        }
    }

    public interface OnContactClickListener {
        void onContactClicked(Contact contact);
    }
}
