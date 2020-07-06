package com.codestrela.product.viewmodels;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.codestrela.product.adapters.SelectContactAdapter;
import com.codestrela.product.data.Contact;
import com.codestrela.product.fragments.HomeFragment;
import com.codestrela.product.fragments.ListDialogFragment;
import com.codestrela.product.util.BindableBoolean;
import com.codestrela.product.util.BindableString;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ListDialogViewModel {
    public static final String CONTACT_LIST = "contact_list";
    private static final String TAG = "ListDialogViewModel";
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY = "documentIdKey";
    public BindableString groupName = new BindableString();
    public RowSelectContactViewModel viewModel;
    public BindableBoolean progressShow = new BindableBoolean();
    public BindableBoolean contactShow = new BindableBoolean();
    public ArrayList<String> data = new ArrayList<>();
    FirebaseFirestore db;
    public SelectContactAdapter adapter;
    ArrayList<String> groupMembersid;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    ListDialogFragment listDialogFragment;
    ArrayList<RowSelectContactViewModel> selectViewmodel;
    HashSet<Contact> contacts;
    ArrayList<Contact> contactList;

    public ListDialogViewModel() {
    }


    public ListDialogViewModel(ListDialogFragment listDialogFragment) {
        this.listDialogFragment = listDialogFragment;
        adapter = new SelectContactAdapter(new ArrayList<RowSelectContactViewModel>());
        firebaseFirestore = FirebaseFirestore.getInstance();
        selectViewmodel = new ArrayList<>();
        db=FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        groupMembersid = new ArrayList<>();
        progressShow.set(true);
        contactShow.set(false);
        try {
            onContact();
        } catch (Exception e) {

        }

    }

    public static String loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(KEY, "");
        return text;
    }

    public void onContact() {

        contacts = new HashSet<>();
        contactList = new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(listDialogFragment.getContext());
        Gson gson = new Gson();
        String json = sharedPreferences.getString(CONTACT_LIST, null);
        Type type = new TypeToken<ArrayList<Contact>>() {
        }.getType();
        contactList = gson.fromJson(json, type);
        if (contactList.isEmpty()) {
            Toast.makeText(listDialogFragment.getContext(), "Allow the contact list first", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < contactList.size(); i++) {
                contacts.add(new Contact(contactList.get(i).getName(), contactList.get(i).getNumber()));
            }
        }
        Iterator<Contact> i = contacts.iterator();
        String lastNumber = "";
        while (i.hasNext()) {
            Contact contact = i.next();
            String number = contact.getNumber();
            if ((number).equals(lastNumber)) {
                Log.e(TAG, "onContact: " + contact.getNumber() + " " + lastNumber);

            } else {
                lastNumber = contact.getNumber();
                viewModel = new RowSelectContactViewModel(this);
                String name = contact.getName();
                viewModel.contactName.set(name);
                viewModel.contactNumber.set("" + contact.getNumber());
                selectViewmodel.add(viewModel);
            }
        }
        adapter.addAll(selectViewmodel);
        progressShow.set(false);
        contactShow.set(true);
    }

    public void onSubmitContact(View view) {
        progressShow.set(true);
        contactShow.set(false);
        for (int i = 0; i < data.size(); i++) {


            firebaseFirestore.collection("db_v1").document("barter_doc").collection("users").whereEqualTo("phone_number", data.get(i)).
                    get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshots : task.getResult()) {
                                    groupMembersid.add(documentSnapshots.getId());
                                }

                            }
                        }
                    });
        }
        delay(3);

    }

    public void delay(int seconds) {
        final int milliseconds = seconds * 1000;
        listDialogFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DocumentReference doc = firebaseFirestore.collection("db_v1").document("barter_doc").collection("groups").document();
                        String groupId = doc.getId();
                        groupMembersid.add(loadData(listDialogFragment.getContext()));
                        ArrayList<String> commodities = new ArrayList<>();
                        Map<String, Object> group = new HashMap<>();
                        group.put("groupMembers", groupMembersid);
                        group.put("groupName", groupName.get());
                        group.put("groupAdmin", loadData(listDialogFragment.getContext()));
                        group.put("groupId", groupId);
                        group.put("commodities", commodities);
                        doc.set(group);
                        listDialogFragment.getDialog().dismiss();
                    }
                }, milliseconds);
            }
        });
    }

    public void onCancelPressed(View view) {
        listDialogFragment.getDialog().dismiss();
    }
    public void getContacts() {

        Cursor cursor = listDialogFragment.getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        while (cursor.moveToNext()) {
            String lastNumber = "";
            final String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            final String mobile = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            // Toast.makeText(getActivity(), "name: " + name, Toast.LENGTH_SHORT).show();
            String number;
            if (lastNumber.equals(mobile)) {

            } else {
                lastNumber = mobile;
                if (mobile.length() == 10) {
                    number = "+91" + mobile;
                } else {
                    number = mobile;
                }


                db.collection("db_v1").document("barter_doc").collection("users").whereEqualTo("phone_number", number).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()) {


                                    } else {
                                        CharSequence s = mobile;

                                        contacts.add(new Contact(name, mobile));
                                    }
                                    try {
                                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(listDialogFragment.getContext());
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        Gson gson = new Gson();
                                        String json = gson.toJson(contacts);
                                        editor.putString(CONTACT_LIST, json);
                                        editor.apply();
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        });
            }

        }
    }

}