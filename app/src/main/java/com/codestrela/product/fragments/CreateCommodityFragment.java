package com.codestrela.product.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.codestrela.product.R;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.databinding.FragmentCreateCommodityBinding;
import com.codestrela.product.viewmodels.CreateCommodityViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

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
    Uri filePath;
    ImageView commodityImageView;
    StorageReference storageReference;

    public static void addFragment(BaseActivity activity) {
        activity.replaceFragment(new CreateCommodityFragment(), true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new CreateCommodityViewModel(this);
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_commodity, container, false);
        binding.setVm(vm);
        commodityImageView = (ImageView) binding.getRoot().findViewById(R.id.selectBtn);
        uploadBtn = (Button) binding.getRoot().findViewById(R.id.uploadBtn);
        selectBtn = (ImageView) binding.getRoot().findViewById(R.id.selectBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageSelect();
            }
        });
        ((BaseActivity) getActivity()).setToolbarVisibility(false);

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
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                selectBtn.setVisibility(View.GONE);
                commodityImageView.setImageBitmap(bitmap);
                commodityImageView.setVisibility(View.VISIBLE);

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
                                    Toast.makeText(getContext(), "image uploaded", Toast.LENGTH_SHORT).show();
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
                    });

        }
    }
}

