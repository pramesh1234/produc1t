package com.codestrela.product.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.codestrela.product.MainActivity;
import com.codestrela.product.R;
import com.codestrela.product.base.activity.BaseActivity;
import com.codestrela.product.databinding.FragmentEditImageBinding;
import com.codestrela.product.util.AppUtil;
import com.codestrela.product.viewmodels.EditImageViewModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

public class EditImageFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int RESULT_OK = -1;
    private static final String TAG = "EditImageFragment";
    final int RequestPermissionCode = 1;
    FragmentEditImageBinding binding;
    EditImageViewModel vm;
    ImageView imageView, cropBtn;
    Uri uri;
    ImageButton browseImage,resetBtn;
    Button uploadBtn;
    Uri filepath;

    public static void addFragment(BaseActivity activity, Fragment fragment) {
        activity.addFragment(fragment, true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new EditImageViewModel(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_image, container, false);
        binding.setVm(vm);
        ((BaseActivity) getActivity()).setToolbarVisibility(false);
        Bundle bundle = getArguments();
        filepath = bundle.getParcelable("uri");
        imageView = (ImageView) binding.getRoot().findViewById(R.id.image);
        uploadBtn=(Button) binding.getRoot().findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
                if(filepath==null){
                    Log.e(TAG, "onClick: ojoia" );
                }else{
                    Log.e(TAG, "onClick: jo");
                }
                ((MainActivity)getActivity()).setSearchItem(filepath);
                ((MainActivity)getActivity()).getFragment().onBackCall();

            }
        });

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filepath);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            requestRuntimePermission();
        }
        resetBtn=(ImageButton) binding.getRoot().findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageBitmap(null);
            }
        });
        browseImage = (ImageButton) binding.getRoot().findViewById(R.id.cropBtn);
        browseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                  // CropImage.startPickImageActivity(getActivity());
                startCrop(filepath);

            }




        });


        return binding.getRoot();
    }

    private void requestRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
            AppUtil.showToast(getActivity(), "CAMERA permission allows to access CAMERA app");
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }

    private void startCrop(Uri filepath) {
        CropImage.activity(filepath)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(getActivity(),EditImageFragment.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: " );

        if(requestCode==CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode== Activity.RESULT_OK){
            Uri imageUri=CropImage.getPickImageResultUri(getActivity(),data);
            if(CropImage.isReadExternalStoragePermissionsRequired(getActivity(),imageUri)){
                uri= imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
            }else{
                startCrop(uri);
            }
        }
        Log.e(TAG, "onActivityResult: "+requestCode+" "+CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            Log.e(TAG, "onActivityResult: " );

            if(resultCode==RESULT_OK ){
                imageView.setImageURI(result.getUri());
                filepath=result.getUri();
                Log.e(TAG, "onActivityResult:1 " );
            }
        }
    }
}