package com.codestrela.product.fragments;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.codestrela.product.R;
import com.codestrela.product.adapters.MyCommoditiesAdapter;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.data.Contact;
import com.codestrela.product.databinding.FragmentHomeBinding;
import com.codestrela.product.viewmodels.HomeViewModel;
import com.codestrela.product.viewmodels.RowCommodityViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    public static final String CONTACT_LIST = "contact_list";
    HomeViewModel vm;
    FragmentHomeBinding binding;
    ArrayList<Contact> contacts;
    FirebaseFirestore db;

    public static void addFragment(BaseActivity activity) {
        activity.replaceFragment(new HomeFragment(), true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new HomeViewModel(this);
        db = FirebaseFirestore.getInstance();
        contacts = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
        myAsyncTasks.execute();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        binding.setVm(vm);
        ViewPager viewPager = binding.getRoot().findViewById(R.id.event_view_pager);
        TabLayout tabLayout = binding.getRoot().findViewById(R.id.event_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        ((BaseActivity) getActivity()).setToolbarVisibility(false);
        return binding.getRoot();
    }

    class MyAsyncTasks extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && getActivity().checkSelfPermission(Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {
                getActivity().requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
            } else {
                getContacts();
            }
            return null;
        }

        public void getContacts() {

            Cursor cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);
            while (cursor.moveToNext()) {
                final String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                final String mobile = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                // Toast.makeText(getActivity(), "name: " + name, Toast.LENGTH_SHORT).show();
                String number;
                if (mobile.length() == 10) {
                    number = "+91" + mobile;
                } else {
                    number = mobile;
                }


                db.collection("db_v1").document("barter_doc").collection("users").whereEqualTo("Phone Number", number).get()
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
                                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
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
