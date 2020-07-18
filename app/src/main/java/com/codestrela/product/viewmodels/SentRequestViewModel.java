package com.codestrela.product.viewmodels;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.codestrela.product.adapters.RequestAdapter;
import com.codestrela.product.fragments.RequestTabFragment;
import com.codestrela.product.fragments.SentRequestFragment;
import com.codestrela.product.util.BindableBoolean;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SentRequestViewModel {

    private static final String TAG = "RequestTabViewModel";
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String KEY = "documentIdKey";
    public RequestAdapter adapter;
    public BindableBoolean loading=new BindableBoolean();
    public BindableBoolean emptyList=new BindableBoolean();
    FirebaseFirestore db;
    String requesterName,commodityName,quantity;
    ArrayList<RowRequestViewModel> arrayList;
    SentRequestFragment sentRequestFragment;
    public SentRequestViewModel(SentRequestFragment sentRequestFragment) {
        this.sentRequestFragment=sentRequestFragment;
        db=FirebaseFirestore.getInstance();
        arrayList=new ArrayList<>();
        db=FirebaseFirestore.getInstance();
        loading.set(true);
        emptyList.set(false);
        adapter=new RequestAdapter(new ArrayList<RowRequestViewModel>());
        displayRequest();
    }

    public static String loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(KEY, "");
        return text;
    }

    public void displayRequest(){
        Log.e(TAG, "onComplete: wpwpempty "+loadData(sentRequestFragment.getActivity()));

        db.collection("db_v1").document("barter_doc").collection("requests").whereEqualTo("requested_to",loadData(sentRequestFragment.getActivity())).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().isEmpty()){
                            emptyList.set(true);
                            loading.set(false);
                            Log.e(TAG, "onComplete: empty "+loadData(sentRequestFragment.getActivity()));
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
                                RowRequestViewModel viewModel=new RowRequestViewModel(sentRequestFragment,commodityName,quantity,mode,specification,requestId,requestedTo,requestedBy);

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
