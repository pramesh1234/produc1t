package com.codestrela.product.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.codestrela.product.adapters.RequestAdapter;
import com.codestrela.product.adapters.TransactionAdapter;
import com.codestrela.product.fragments.SellingTransactionFragment;
import com.codestrela.product.util.BindableBoolean;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SellingTransactionViewModel {
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY = "documentIdKey";
    private static final String TAG = "TransactionViewModel";
    public BindableBoolean loading=new BindableBoolean();
    public TransactionAdapter adapter;
    public BindableBoolean emptyList=new BindableBoolean();
    SellingTransactionFragment sellingTransactionFragment;
    FirebaseFirestore db;
    ArrayList<RowTransactionViewModel> arrayList;
    String user;

    public SellingTransactionViewModel(SellingTransactionFragment sellingTransactionFragment) {
        this.sellingTransactionFragment = sellingTransactionFragment;
        db = FirebaseFirestore.getInstance();
        arrayList = new ArrayList<>();
        emptyList.set(false);
        loading.set(true);
        adapter = new TransactionAdapter(new ArrayList<RowTransactionViewModel>());
 getTrasactions();
    }

    public static String loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(KEY, "");
        return text;
    }

    public void getTrasactions() {
        db.collection("db_v1").document("barter_doc").collection("accepted_request").whereEqualTo("requested_to", loadData(sellingTransactionFragment.getActivity())).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().isEmpty()) {
                            emptyList.set(true);
                            loading.set(false);
                        }
                            if (task.isSuccessful()) {
                                for (final QueryDocumentSnapshot doc : task.getResult()) {

                                    final String requestedBy = doc.getString("requested_by");
                                    final String requestedTo = doc.getString("requested_to");
                                    final String commodityName = doc.getString("commodity_name");
                                    final String quantity = doc.getString("quantity");
                                    final String mode = doc.getString("mode");
                                    final String referenceId=doc.getString("reference_id");
                                    final String requestId = doc.getId();
                                    final String price=doc.getString("price");
                                    final String specification = doc.getString("specification");
                                    db.collection("db_v1").document("barter_doc").collection("users").document(requestedBy).get().addOnCompleteListener(
                                            new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        DocumentSnapshot documentSnapshot=task.getResult();
                                                         user=documentSnapshot.getString("name");
                                                        Log.e(TAG, "onComplete: yy "+user );
                                                        RowTransactionViewModel viewModel = new RowTransactionViewModel(sellingTransactionFragment, quantity, mode,price,"Seller Name");

                                                        Log.e(TAG, "onComplete: pty " + commodityName);
                                                        viewModel.commodityName.set(commodityName);
                                                        viewModel.referenceId.set(referenceId);
                                                        viewModel.requesterName.set(user);

                                                        loading.set(false);
                                                        adapter.add(viewModel);
                                                        Log.e(TAG, "onComplete: " );
                                                    }
                                                }
                                            }
                                    );


                                }


                        }

                    }
                });

    }
}
