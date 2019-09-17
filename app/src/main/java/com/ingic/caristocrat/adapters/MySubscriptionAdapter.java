package com.ingic.caristocrat.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.helpers.DateFormatHelper;
import com.ingic.caristocrat.webhelpers.models.Subscription;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class MySubscriptionAdapter extends StatelessSection {

    List<Subscription> list;
    final String title;

    public MySubscriptionAdapter(String title, List<Subscription> list) {
        super(SectionParameters.builder().itemResourceId(R.layout.subscription_item).headerResourceId(R.layout.subscription_header).build());
        this.title = title;
        this.list = list;
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

        itemHolder.title.setText(list.get(position).getTitle());
        String fromDate = list.get(position).getFromDate() == null ? "N/A" : list.get(position).getFromDate();
        String endDate = list.get(position).getToDate() == null ? "Lifetime" : list.get(position).getToDate();

        if ((!fromDate.equalsIgnoreCase("N/A")) || (!fromDate.equalsIgnoreCase("Lifetime")))
            fromDate = DateFormatHelper.changeServerToNotificationsDateFormat(fromDate);

        if ((!endDate.equalsIgnoreCase("N/A")) || (!endDate.equalsIgnoreCase("Lifetime")))
            endDate = DateFormatHelper.changeServerToNotificationsDateFormat(endDate);

        itemHolder.formDate.setText("Subscribed on: " + fromDate);
        itemHolder.toDate.setText("Valid till: " + endDate);

    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
        String section = title.replaceAll("_", " ").toUpperCase();
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
