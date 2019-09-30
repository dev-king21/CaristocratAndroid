package com.ingic.caristocrat.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.dialogs.PDFViewDialog;
import com.ingic.caristocrat.fragments.CompareFragment;
import com.ingic.caristocrat.fragments.MainDetailPageFragment;
import com.ingic.caristocrat.fragments.MySubscription;
import com.ingic.caristocrat.helpers.DateFormatHelper;
import com.ingic.caristocrat.webhelpers.models.News;
import com.ingic.caristocrat.webhelpers.models.Subscription;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class MySubscriptionAdapter extends StatelessSection {

    List<Subscription> list;
    final String title;
    MainActivity mActivityContext;

    public MySubscriptionAdapter(String title, List<Subscription> list, MainActivity mActivityContext) {
        super(SectionParameters.builder().itemResourceId(R.layout.subscription_item).headerResourceId(R.layout.subscription_header).build());
        this.title = title;
        this.list = list;
        this.mActivityContext = mActivityContext;
    }

    @Override
    public int getContentItemsTotal() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemHolder = (ItemViewHolder) holder;

        Animation anim = new AlphaAnimation(0.5f, 1.0f);
        anim.setDuration(500); //You can manage the blinking time with this parameter
        anim.setStartOffset(200);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);

        itemHolder.title.setText(list.get(position).getTitle());
        String subscriptionType = list.get(position).getType();

        if (list.get(position).getFromDate() != null && list.get(position).getToDate() != null)
            if (subscriptionType.equals("one_report_subscription")) // one_report_subscription, professional_comparison_subscription
                itemHolder.title.setOnClickListener(view -> {
                    System.out.println("sdssf");
                    //mActivityContext.replaceFragment(new PDFViewDialog().newInstance(mActivityContext, news.getRelated_car()), MainDetailPageFragment.class.getSimpleName(), true, false);
                   /* MainDetailPageFragment mainDetailPageFragment = new MainDetailPageFragment();
                    mainDetailPageFragment.setCategoryId(nonFeaturedNews.get(position).getId());
                    if (nonFeaturedNews.get(position).getMedia().size() > 0) {
                        mainDetailPageFragment.setImageUrl(nonFeaturedNews.get(position).getMedia().get(0).getFileUrl());
                    }
                    ArrayList<News> similarListing = new ArrayList<>();
                    similarListing.addAll(news);
                    similarListing.remove(nonFeaturedNews.get(position));
                    mainDetailPageFragment.setSimilarListing(similarListing);
                    mActivityContext.replaceFragment(mainDetailPageFragment, MainDetailPageFragment.class.getSimpleName(), true, false);*/
                });
            else if (subscriptionType.equals("professional_comparison_subscription"))
                itemHolder.title.setOnClickListener(view -> {
                    CompareFragment compareFragment = new CompareFragment();
                    compareFragment.setTab_id(1);
                    mActivityContext.replaceFragment(compareFragment, CompareFragment.class.getSimpleName(), true, true);
                });

        String fromDate = list.get(position).getFromDate() == null ? "N/A" : list.get(position).getFromDate();
        String endDate = list.get(position).getToDate() == null ? "Lifetime" : list.get(position).getToDate();

        if ((!fromDate.equalsIgnoreCase("N/A")) || (!fromDate.equalsIgnoreCase("Lifetime")))
            fromDate = DateFormatHelper.changeServerToNotificationsDateFormat(fromDate);

        if ((!endDate.equalsIgnoreCase("N/A")) || (!endDate.equalsIgnoreCase("Lifetime")))
            endDate = DateFormatHelper.changeServerToNotificationsDateFormat(endDate);

        /*SpannableString startDateStr = new SpannableString(fromDate);
        startDateStr.setSpan(new ForegroundColorSpan(Color.YELLOW), "Subscribed on: ".length(), startDateStr.length(), 0);
        SpannableString endDateStr = new SpannableString("Valid till: " + endDate);
        endDateStr.setSpan(new ForegroundColorSpan(Color.YELLOW), "Valid till: ".length(), endDateStr.length(), 0);*/

        itemHolder.formDate.setText(fromDate);
        itemHolder.toDate.setText(endDate);
        itemHolder.formDate.setAnimation(anim);
        itemHolder.toDate.setAnimation(anim);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
        String section = title.replaceAll("_", " ").toUpperCase();
        if (section.equalsIgnoreCase("one report subscription"))
            section = "subscribed reports".toUpperCase();
        headerHolder.title.setText(section);
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;

        HeaderViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tvTitle);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView title, formDate, toDate;

        ItemViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            formDate = view.findViewById(R.id.form_date);
            toDate = view.findViewById(R.id.todate);
        }
    }
}
