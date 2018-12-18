package com.example.kienphung.lesson6;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
    public static final String WHERE = " = ? ";
    private static final int REQUEST_PERMISSION = 1;
    private ContentResolver mContentResolver;
    private List<Contact> mContacts;
    private ContactAdapter mContactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContacts = new ArrayList<>();
        checkPermission();
    }

    public void initView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        mContactAdapter = new ContactAdapter(this, mContacts);
        recyclerView.setAdapter(mContactAdapter);
        loadContacts();
    }

    public List<Contact> loadContacts() {
        List<Contact> contacts = new ArrayList<>();
        mContentResolver = getContentResolver();
        Cursor cursor = mContentResolver.query
                (CONTENT_URI, null, null, null, null);
        if (cursor == null || cursor.getCount() <= 0)
        {
            return contacts;
        }
        while (cursor != null && cursor.moveToNext())
        {
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex
                    (ContactsContract.Contacts.DISPLAY_NAME));
            int phoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex
                    (ContactsContract.Contacts.HAS_PHONE_NUMBER)));
            if (phoneNumber > 0)
            {
                Cursor phoneCursor = mContentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + WHERE,
                        new String[]{id},
                        null);
                while (phoneCursor != null && phoneCursor.moveToNext())
                {
                    String number = phoneCursor.getString(phoneCursor.getColumnIndex
                            (ContactsContract.CommonDataKinds.Phone.NUMBER));
                    mContacts.add(new Contact(name, number));
                    mContactAdapter.notifyDataSetChanged();
                }
                phoneCursor.close();
            }
        }
        cursor.close();
        return contacts;
    }

    private void checkPermission() {
        String[] permissions = {Manifest.permission.READ_CONTACTS};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, permissions[0])
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, REQUEST_PERMISSION);
            return;
        }
        initView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] != PackageManager.PERMISSION_DENIED) {
                    initView();
                    return;
                }
                checkPermission();
                break;
            default:
                break;
        }
    }
}
