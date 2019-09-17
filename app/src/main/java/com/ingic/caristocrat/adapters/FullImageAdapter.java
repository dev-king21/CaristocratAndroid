package com.ingic.caristocrat.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.webhelpers.models.Media;
import com.jsibbold.zoomage.ZoomageView;

import java.util.ArrayList;

public class FullImageAdapter extends PagerAdapter {
    MainActivity mainActivity;
    LayoutInflater layoutInflater;
    ArrayList<Media> arrayList;

    public FullImageAdapter(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.arrayList = new ArrayList<>();
        this.layoutInflater = (LayoutInflater) mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View rootView = layoutInflater.inflate(R.layout.layout_full_image, container, false);

        ZoomageView ivFullImage = rootView.findViewById(R.id.ivFullImage);
        UIHelper.setImageWithGlideNoPlaceHolder(mainActivity, ivFullImage, arrayList.get(position).getFileUrl());
//        ivFullImage.setOnTouchListener(new ImageMatrixTouchHandler(mainActivity));

        container.addView(rootView);
        return rootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void addAll(ArrayList<Media> arrayList){
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
    }
}
