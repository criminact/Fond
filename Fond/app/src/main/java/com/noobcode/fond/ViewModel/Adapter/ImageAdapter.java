package com.noobcode.fond.ViewModel.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.noobcode.fond.R;
import com.squareup.picasso.Picasso;

public class ImageAdapter extends PagerAdapter {

    public Context mContext;
    public String[] mImages;

    public ImageAdapter(Context mContext, String[] mImages) {
        this.mContext = mContext;
        this.mImages = mImages;
    }

    @Override
    public int getCount() {
        return mImages.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.get().load(Uri.parse(mImages[position])).fit().centerCrop().error(R.mipmap.logo).into(imageView);
        //imageView.setImageURI(Uri.parse(mImages[position]));
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}
