package com.example.kienphung.lesson6;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.viewHolder> {

    private Context mContext;
    private List<Contact> mContacts;

    public ContactAdapter(Context context, List<Contact> contacts) {
        mContext = context;
        mContacts = contacts;
    }

    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate
                (R.layout.item_contact, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
        viewHolder.bindData(mContacts.get(i));
    }

    @Override
    public int getItemCount() {
        return mContacts != null ? mContacts.size() : 0;
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        private TextView mTextName;
        private TextView mTextPhone;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            mTextName = itemView.findViewById(R.id.text_phone_name);
            mTextPhone = itemView.findViewById(R.id.text_phone_number);
        }

        public void bindData(Contact contact) {
            if (contact != null) {
                mTextName.setText(contact.getName());
                mTextPhone.setText(contact.getPhone());
            }
        }
    }
}
