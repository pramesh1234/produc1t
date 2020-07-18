package com.codestrela.product;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.data.Contact;
import com.codestrela.product.fragments.CreateCommodityFragment;
import com.codestrela.product.fragments.HomeFragment;
import com.codestrela.product.fragments.PhoneSignInFragment;
import com.codestrela.product.fragments.SplashFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;


public class MainActivity extends BaseActivity {
    public static final String CONTACT_LIST = "contact_list";
    private static final String TAG = "MainActivity";
ProgressDialog dialog;

    CreateCommodityFragment fragment;
    private Uri mSearchItem;

    public CreateCommodityFragment getFragment() {
        return this.fragment;
    }
FirebaseFirestore db;

    public void setFragment(CreateCommodityFragment fragment) {
        this.fragment = fragment;
    }

    ArrayList<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        db=FirebaseFirestore.getInstance();
        contacts=new ArrayList<>();
        setSupportActionBar(toolbar);
        setToolbarVisibility(false);
        SplashFragment.addFragment(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PhoneSignInFragment.addFragment(MainActivity.this);
            }
        }, 3000);
    }

    public Uri getSearchItem(){
        Log.e(TAG, "getSearchItem: "+this.mSearchItem);
        return this.mSearchItem;
    }

    public void setSearchItem(Uri searchItem){
        Log.e(TAG, "setSearchItem: "+this.mSearchItem );
        this.mSearchItem = searchItem;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
    public void onSyncContact(){
        dialog=new ProgressDialog(this);
        dialog.setMessage("Processing please wait..");
        dialog.show();
        ContactThread thread = new ContactThread(7);
        thread.start();
        dialog.setCanceledOnTouchOutside(false);
        final Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
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
                                        Log.e(TAG, "onSyncContact: "+cursor.getPosition() );

                                    }
                                    try {
                                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
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
            Log.e(TAG, "onSyn: "+cursor.getPosition() );
        }

    }

    class ContactThread extends Thread {
        int seconds;

        public ContactThread(int seconds) {
            this.seconds = seconds;
        }

        @Override
        public void run() {
            super.run();
            for (int i = 0; i < seconds; i++) {
                Log.e(TAG, "run: ");


                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            dialog.dismiss();
        }
    }

}
