package com.ingic.caristocrat.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.helpers.Utils;
import com.ingic.caristocrat.models.TradeCar;
import com.ingic.caristocrat.webhelpers.models.News;

import java.util.ArrayList;

/**
 * on 1/15/2019.
 */
public class SimilarNewsListingAdapter extends RecyclerView.Adapter<SimilarNewsListingAdapter.Holder> implements View.OnClickListener {
    MainActivity mainActivityContext;
    ArrayList<News> arrayList;
    int hidingItemId;
    private Double width = 120.0;

    public SimilarNewsListingAdapter(MainActivity mainActivityContext) {
        this.mainActivityContext = mainActivityContext;
        this.arrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivityContext).inflate(R.layout.item_similar_news_listings, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.cardView.getLayoutParams().width = width.intValue();
        holder.cardView.getLayoutParams().height = width.intValue();
        if (arrayList.get(position).getMedia() != null && arrayList.get(position).getMedia().size() > 0)
            UIHelper.setImageWithGlide(mainActivityContext, holder.ivItem, arrayList.get(position).getMedia().get(0).getFileUrl());
        holder.tvName.setText(arrayList.get(position).getHeadline());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void onClick(View view) {
        UIHelper.hideSoftKeyboard(mainActivityContext, view);
        switch (view.getId()) {
        }
    }

    public void addAll(ArrayList<News> arrayList) {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
        notifyDataSetChanged();
    }

    public News getItem(int pos) {
        if (arrayList.size() > 0)
            return arrayList.get(pos);
        else
            return new News();
    }

    private void setListeners() {
//        binding.tvLikesCount.setOnClickListener(this);
//        binding.tvViewsCount.setOnClickListener(this);
//        binding.tvCommentsCount.setOnClickListener(this);

    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView ivItem;
        TextView tvName;


        public Holder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);
            tvName = (TextView) itemView.findViewById(R.id.tvName);


        }

    }
}
