package com.codestrela.product.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.codestrela.product.adapters.ContactAdapter;
import com.codestrela.product.data.Contact;
import com.codestrela.product.fragments.GroupListMemberDialogFragment;
import com.codestrela.product.fragments.MyContactListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class MyContactViewModel {
    public static final String CONTACT_LIST = "contact_list";
    private static final String TAG = "MyContactViewModel";
    private static StringBuffer no = new StringBuffer(" ");
    public ContactAdapter contactAdapter;
    MyContactListFragment myContactListFragment;
    FirebaseFirestore db;
    ArrayList<Contact> contacts;
    HashSet<Contact> contactSet;
    CollectionReference usersCref;
    GroupListMemberDialogFragment fragment;

    public MyContactViewModel(MyContactListFragment myContactListFragment) {
        this.myContactListFragment = myContactListFragment;
        fragment = new GroupListMemberDialogFragment();
        db = FirebaseFirestore.getInstance();
        contacts = new ArrayList<>();
        contactSet = new HashSet<>();


    }

    public void getContacts() {
        Cursor cursor = myContactListFragment.getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        while (cursor.moveToNext()) {
            final String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            final String mobile = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contactSet.add(new Contact(name, mobile));
        }
        for (Contact set : contactSet) {
            final String contactNumber = set.getNumber();
            final String contactName = set.getName();
            String number;

            if (contactNumber.length() == 10) {
                number = "+91" + contactNumber;
            } else {
                number = contactNumber;
            }


            db.collection("db_v1").document("barter_doc").collection("users").whereEqualTo("phone_number", number).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                RowContactViewModel viewModel = new RowContactViewModel(fragment, myContactListFragment);

                                if (task.getResult().isEmpty()) {
                                    viewModel.contactName.set(contactName);
                                    viewModel.contactNumber.set(contactNumber);
                                    viewModel.visiblity.set(true);

                                } else {
                                    CharSequence s = contactNumber;
                                    no.append(s);
                                    viewModel.contactName.set(contactName);
                                    viewModel.contactNumber.set(contactNumber);
                                    contacts.add(new Contact(contactName, contactNumber));
                                    Log.e(TAG, "getContacts:" + contactName + "  phone " + contactNumber);
                                    viewModel.visiblity.set(false);
                                    Log.e(TAG, "array list: " + contacts.size());
                                }
                                try {


                      /*  SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(myContactListFragment.getContext());
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        Gson gson=new Gson();
                        String json=gson.toJson(contacts);
                        editor.putString(CONTACT_LIST,json);
                        editor.apply();*/
                                    contactAdapter.add(viewModel);
                                } catch (Exception e) {
                                    Log.e(TAG, "message : " + e.toString());
                                }
                            }
                        }
                    });

            Log.e(TAG, "getContacts: " + no);
            contactAdapter = new ContactAdapter(new ArrayList<RowContactViewModel>());

        }

    }
}
