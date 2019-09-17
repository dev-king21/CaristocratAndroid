package com.ingic.caristocrat.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.RegistrationActivity;
import com.ingic.caristocrat.models.WalkthroughWrapper;

import java.util.ArrayList;

/**
 * on 8/2/2018.
 */
public class WalkthroughPagerAdapter extends PagerAdapter {
    RegistrationActivity registrationActivityContext;
    ArrayList<WalkthroughWrapper> walkThroughContent = new ArrayList<>();
    TextView textViewDesc, textViewTitle;


    public WalkthroughPagerAdapter(RegistrationActivity registrationActivity, ArrayList<WalkthroughWrapper> walkThroughContent) {
        registrationActivityContext = registrationActivity;
        this.walkThroughContent = walkThroughContent;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return walkThroughContent.size()-1;
    }

    public void addAll(ArrayList<WalkthroughWrapper> data) {
        this.walkThroughContent.clear();
        this.walkThroughContent.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = LayoutInflater.from(registrationActivityContext);
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.item_walkthrough, container, false);
        textViewDesc = view.findViewById(R.id.tvWalkthroughDescription);
        textViewTitle = view.findViewById(R.id.tvTitle);
        textViewTitle.setText((walkThroughContent.get(position).getTitle() != null && !walkThroughContent.get(position).getTitle().isEmpty()) ? walkThroughContent.get(position).getTitle():"");
        textViewDesc.setText((walkThroughContent.get(position).getContent() != null && !walkThroughContent.get(position).getContent().isEmpty()) ? walkThroughContent.get(position).getContent():"");
        container.addView(view);
        return view;
    }


}
