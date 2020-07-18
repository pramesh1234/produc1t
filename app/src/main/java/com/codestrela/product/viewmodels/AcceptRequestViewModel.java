package com.codestrela.product.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.codestrela.product.adapters.RequestAdapter;
import com.codestrela.product.fragments.AcceptRequestFragment;
import com.codestrela.product.util.BindableBoolean;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class AcceptRequestViewModel {
    private static final String TAG = "RequestTabViewModel";
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY = "documentIdKey";
    public BindableBoolean loading=new BindableBoolean();
    public BindableBoolean emptyList=new BindableBoolean();
    public RequestAdapter adapter;
    FirebaseFirestore db;
    String requesterName,commodityName,quantity;
    ArrayList<RowRequestViewModel> arrayList;

    AcceptRequestFragment acceptRequestFragment;
    public AcceptRequestViewModel(AcceptRequestFragment acceptRequestFragment) {
        this.acceptRequestFragment=acceptRequestFragment;
        db=FirebaseFirestore.getInstance();
        arrayList=new ArrayList<>();
        loading.set(true);
        emptyList.set(false);
        db=FirebaseFirestore.getInstance();
        adapter=new RequestAdapter(new ArrayList<RowRequestViewModel>());
        displayRequest();
    }

    public static String loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(KEY, "");
        return text;
    }

    public void displayRequest(){
        Log.e(TAG, "onComplete: ye wwpwpempty "+loadData(acceptRequestFragment.getActivity()));

        db.collection("db_v1").document("barter_doc").collection("requests").whereEqualTo("requested_by",loadData(acceptRequestFragment.getActivity())).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().isEmpty()){
                            emptyList.set(true);
                            loading.set(false);

                            Log.e(TAG, "onComplete: empty "+loadData(acceptRequestFragment.getActivity()));
                        }
                        if(task.isSuccessful()){
                            loading.set(false);
                            for(QueryDocumentSnapshot doc : task.getResult()){
                                String requestedBy=doc.getString("requested_by");
                                String requestedTo=doc.getString("requested_to");
                                commodityName=doc.getString("commodity_name");
                                quantity=doc.getString("quantity");
                                String mode=doc.getString("mode");
                                String requestId=doc.getId();
                                String specification=doc.getString("specification");
                                RowRequestViewModel viewModel=new RowRequestViewModel(acceptRequestFragment,commodityName,quantity,mode,specification,requestId,requestedTo,requestedBy);

                                Log.e(TAG, "onComplete: pty "+commodityName);
                                viewModel.commodityName.set(commodityName);
                                viewModel.quantity.set(quantity);
                                arrayList.add(viewModel);

                                Log.e(TAG, "onComplete: "+commodityName);
                            }
                        }
                        adapter.addAll(arrayList);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
