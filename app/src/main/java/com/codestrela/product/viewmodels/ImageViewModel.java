package com.codestrela.product.viewmodels;

import android.os.Bundle;

import com.codestrela.product.fragments.ImageFragment;
import com.codestrela.product.util.BindableString;

public class ImageViewModel {
    public BindableString imageUrl=new BindableString();
    ImageFragment imageFragment;
    public ImageViewModel(ImageFragment imageFragment) {
        this.imageFragment=imageFragment;
        Bundle bundle=imageFragment.getArguments();
        String image=bundle.getString("imageUrl");
        imageUrl.set(image);
    }
}
