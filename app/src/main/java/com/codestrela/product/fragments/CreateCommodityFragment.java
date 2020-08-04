package com.codestrela.product.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.codestrela.product.MainActivity;
import com.codestrela.product.R;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.databinding.FragmentCreateCommodityBinding;
import com.codestrela.product.util.AppUtil;
import com.codestrela.product.viewmodels.CreateCommodityViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateCommodityFragment extends Fragment {
    public int req = 0;
    public String imageUrl;
    CreateCommodityViewModel vm;
    FragmentCreateCommodityBinding binding;
    Button uploadBtn;
    ImageView selectBtn;
    public String category;
    Uri filePath;
    ImageView commodityImageView;
    public String unitItem;
    StorageReference storageReference;
    ArrayList<String> categoryList = new ArrayList<>();
    ArrayAdapter<String> categoryAdapter,unitAdapter;
    Spinner categorySpinner,unitSpinner;
    ArrayList<String> unitList = new ArrayList<>();

    CreateCommodityFragment fragment;


    public static void addFragment(BaseActivity activity) {
        activity.replaceFragment(new CreateCommodityFragment(), true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isConnected(Objects.requireNonNull(getActivity()))){
            buildDialog(getContext()).show(); }
        vm = new CreateCommodityViewModel(this);
        storageReference = FirebaseStorage.getInstance().getReference();
        getActivity().setTitle("Create Commodity");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_commodity, container, false);
        binding.setVm(vm);
        unitSpinner=(Spinner) binding.getRoot().findViewById(R.id.unitSpinner);
        commodityImageView = (ImageView) binding.getRoot().findViewById(R.id.selectBtn);
        selectBtn = (ImageView) binding.getRoot().findViewById(R.id.selectBtn);
        categoryList.add("Property");
        categoryList.add("Furniture");
        unitList.add("whole");
        unitList.add("per pc.");
        fragment=new CreateCommodityFragment();
        ((MainActivity)getActivity()).setFragment(this);
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageSelect();
            }
        });

        categoryAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitAdapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,unitList);
        categorySpinner=(Spinner) binding.getRoot().findViewById(R.id.categorySpinner);
        unitSpinner.setAdapter(unitAdapter);
        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                unitItem = (String) adapterView.getItemAtPosition(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = (String) adapterView.getItemAtPosition(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ((BaseActivity) getActivity()).setToolbarVisibility(true);

        RadioGroup modeRadioGroup = (RadioGroup) binding.getRoot().findViewById(R.id.rgMode);
        modeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rentId:
                        vm.mode.set("rent");
                        break;
                    case R.id.saleId:
                        vm.mode.set("sale");
                        break;
                    case R.id.bothId:
                        vm.mode.set("rent/sale");
                        break;
                }
            }
        });
        RadioGroup typeRadioGroup = (RadioGroup) binding.getRoot().findViewById(R.id.rgType);
        typeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.publicRb:
                        vm.type.set("public");
                        break;
                    case R.id.privateRb:
                        vm.type.set("private");
                        break;
                    case R.id.groupRb:
                        vm.type.set("group");
                }
            }
        });
        return binding.getRoot();
    }

    public void onImageSelect() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK
                && data != null) {
            req = 1;
            EditImageFragment fragment=new EditImageFragment();
            Bundle bundle=new Bundle();
            filePath = data.getData();
            bundle.putParcelable("uri",filePath);
            fragment.setArguments(bundle);
            EditImageFragment.addFragment((BaseActivity)getActivity(),fragment);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                selectBtn.setVisibility(View.GONE);
                commodityImageView.setImageBitmap(bitmap);
                commodityImageView.setVisibility(View.VISIBLE);
              //
                // commodityImageView.setCropToPadding(true);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    public void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("uploading...");
        progressDialog.show();
        if (filePath != null) {
            final StorageReference reference = storageReference.child("image/" + UUID.randomUUID().toString());
            reference.putFile(filePath).
                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    progressDialog.dismiss();
                                    imageUrl = uri.toString();
                                }
                            });
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            int progress = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("uploaded " + progress + "%");
                        }
                    })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    AppUtil.showToast(getActivity(),"upload unsucessfull!!");
                }
            });

        }
    }
    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null&&netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                getActivity().finish();
            }
        });

        return builder;
    }
    public void onBackCall(){
        filePath = ((MainActivity)getActivity()).getSearchItem();
        //  selectBtn.setVisibility(View.GONE);
        Log.e(TAG, "onResume: 11" );

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
            commodityImageView.setImageBitmap(bitmap);
            uploadImage();
        }catch (Exception e){
            Log.e(TAG, "onResume: " );
        }
    }

}

