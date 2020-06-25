package com.codestrela.product.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.codestrela.product.adapters.SelectContactAdapter;
import com.codestrela.product.data.Contact;
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
        mAuth = FirebaseAuth.getInstance();
        groupMembersid = new ArrayList<>();
        progressShow.set(false);
        contactShow.set(true);
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

}