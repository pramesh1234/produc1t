package com.codestrela.product.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.codestrela.product.adapters.AdminOrderAdapter;
import com.codestrela.product.adapters.MyOrderAdapter;
import com.codestrela.product.fragments.AdminOrderListFragment;
import com.codestrela.product.fragments.MyOrdersFragment;
import com.codestrela.product.util.BindableBoolean;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class AdminOrderListViewModel {
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY = "documentIdKey";
    public AdminOrderAdapter adminOrderAdapter;
    public BindableBoolean emptyList=new BindableBoolean();
    public BindableBoolean loading=new BindableBoolean();
    FirebaseFirestore db;
    AdminOrderListFragment adminOrderListFragment;
    public AdminOrderListViewModel(AdminOrderListFragment adminOrderListFragment) {
        this.adminOrderListFragment=adminOrderListFragment;
        loading.set(true);
        adminOrderAdapter=new AdminOrderAdapter(new ArrayList<RowAdminOrder>());
        db= FirebaseFirestore.getInstance();
        getOrders();
        adminOrderListFragment.getActivity().setTitle("Admin Order");
    }

    public static String loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(KEY, "");
        return text;
    }

    public void getOrders() {
        db.collection("db_v1").document("barter_doc").collection("accepted_request").whereEqualTo("requested_to", loadData(adminOrderListFragment.getActivity())).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().isEmpty()) {
                            emptyList.set(true);
                            loading.set(false);
                        }
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot doc : task.getResult()) {
                                loading.set(false);
                                String documentId=doc.getId();
                                final String requestedBy = doc.getString("requested_by");
                                final String requestedTo = doc.getString("requested_to");
                                final String commodityName = doc.getString("commodity_name");
                                final String quantity = doc.getString("quantity");
                                final String mode = doc.getString("mode");
                                final String referenceId=doc.getString("reference_id");
                                final String requestId = doc.getId();
                                final String status = doc.getString("order_status");

                                final Date date=doc.getDate("created_date");
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//                                 String strDate= formatter.format(date);
                                final String specification = doc.getString("specification");
                                RowAdminOrder viewModel=new RowAdminOrder(adminOrderListFragment,documentId,requestedBy,referenceId);
                                viewModel.commodityName.set(commodityName);
                                viewModel.orderId.set(referenceId);
                                viewModel.orderStatus.set(status);
                   //             viewModel.orderDate.set(strDate);
                                adminOrderAdapter.add(viewModel);


                            }


                        }

                    }
                });

    }
}
