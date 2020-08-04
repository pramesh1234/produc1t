package com.codestrela.product.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.codestrela.product.adapters.NotificationAdapter;
import com.codestrela.product.fragments.NotificationListFragment;
import com.codestrela.product.util.BindableBoolean;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class NotificationListViewModel {
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY = "documentIdKey";
    public BindableBoolean emptyList=new BindableBoolean();
    public BindableBoolean loading=new BindableBoolean();
    public NotificationAdapter adapter;
    FirebaseFirestore db;
    NotificationListFragment notificationListFragment;
    public NotificationListViewModel(NotificationListFragment notificationListFragment) {
        this.notificationListFragment=notificationListFragment;
        db=FirebaseFirestore.getInstance();
        notificationListFragment.getActivity().setTitle("Notifications");
        adapter=new NotificationAdapter(new ArrayList<RowNotificationListViewModel>());
        loading.set(true);
        getNotifications();
    }

    public static String loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(KEY, "");
        return text;
    }

    public void getNotifications() {
        db.collection("db_v1").document("barter_doc").collection("notifications").whereEqualTo("receiver", loadData(notificationListFragment.getActivity())).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().isEmpty()) {
                            emptyList.set(true);
                            loading.set(false);
                        }
                        if (task.isSuccessful()) {
                            loading.set(false);
                            for (final QueryDocumentSnapshot doc : task.getResult()) {

                                final String orderNo = doc.getString("order_no");
                                final String title = doc.getString("title");
                                final String orderReference=doc.getString("order_doc_reference");
                                RowNotificationListViewModel viewModel=new RowNotificationListViewModel(notificationListFragment,orderReference);
                                viewModel.orderNo.set(orderNo);
                                viewModel.title.set(title);
                                adapter.add(viewModel);


                            }


                        }

                    }
                });

    }
}
