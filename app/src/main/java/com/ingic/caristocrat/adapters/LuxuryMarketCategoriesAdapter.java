package com.ingic.caristocrat.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.fragments.OutletMallFragment;
import com.ingic.caristocrat.helpers.LuxuryMarketSearchFilter;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.webhelpers.models.Category;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;

/**
 * on 9/4/2018.
 */
public class LuxuryMarketCategoriesAdapter extends RecyclerView.Adapter<LuxuryMarketCategoriesAdapter.MyHolder> {
    MainActivity mainActivityContext;
    ArrayList<Category> categories;
    LuxuryMarketSearchFilter filter;


    public LuxuryMarketCategoriesAdapter(MainActivity mainActivityContext, ArrayList<Category> categories) {
        this.mainActivityContext = mainActivityContext;
        this.categories = categories;
        this.filter = LuxuryMarketSearchFilter.getInstance();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivityContext).inflate(R.layout.item_luxury_market_category, parent, false);
        return new MyHolder(view);
    }

    public void addAll(ArrayList<Category> arrayList) {
        this.categories.clear();
        this.categories.addAll(arrayList);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        holder.parentFlipView.setFlipDuration(1000);
        holder.headerBack.setText(!UIHelper.isEmptyOrNull(categories.get(position).getName()) ? categories.get(position).getName() : "");
        holder.headerFront.setText(!UIHelper.isEmptyOrNull(categories.get(position).getName()) ? categories.get(position).getName() : "");
        holder.descFront.setText(!UIHelper.isEmptyOrNull(categories.get(position).getSubtitle()) ? categories.get(position).getSubtitle() : "");
        holder.descBack.setText(!UIHelper.isEmptyOrNull(categories.get(position).getDescription()) ? categories.get(position).getDescription() : "");
        if (categories.get(position).getMedia() != null && categories.get(position).getMedia().size() > 0) {
            UIHelper.setImageWithGlide(mainActivityContext, holder.bgBack, categories.get(position).getMedia().get(0).getFileUrl());
            UIHelper.setImageWithGlide(mainActivityContext, holder.bgFront, categories.get(position).getMedia().get(0).getFileUrl());

        }
        holder.infoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.parentFlipView.setFlipOnTouch(true);
                holder.parentFlipView.setFlipEnabled(true);
                holder.parentFlipView.flipTheView();
                holder.parentFlipView.setFlipOnTouch(false);
                holder.parentFlipView.setFlipEnabled(false);


            }
        });

        holder.infoFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.parentFlipView.setFlipOnTouch(true);
                holder.parentFlipView.setFlipEnabled(true);
                holder.parentFlipView.flipTheView();
                holder.parentFlipView.setFlipOnTouch(false);
                holder.parentFlipView.setFlipEnabled(false);

            }
        });
        holder.parentFlipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OutletMallFragment outletMallFragment = new OutletMallFragment();
                outletMallFragment.setItem_id(categories.get(position).getId());
                outletMallFragment.setFromLuxuryMarket(true);
                outletMallFragment.setTitle(categories.get(position).getName());
                outletMallFragment.setCategoryKey(categories.get(position).getSlug());
                filter.resetFilter(false);
                mainActivityContext.replaceFragment(outletMallFragment, OutletMallFragment.class.getSimpleName(), true, true);
            }
        });


    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        EasyFlipView parentFlipView;
        TextView headerFront, headerBack;
        TextView descFront, descBack;
        ImageButton infoFront, infoBack;
        LinearLayout bgFront, bgBack;

        public MyHolder(View itemView) {
            super(itemView);
            parentFlipView = (EasyFlipView) itemView.findViewById(R.id.easyFilpView);
            headerFront = (TextView) itemView.findViewById(R.id.tvLuxuryMarketItemHeader);
            descFront = (TextView) itemView.findViewById(R.id.tvLuxuryMarketItemDesc);
            infoFront = (ImageButton) itemView.findViewById(R.id.ibInfo);
            bgBack = (LinearLayout) itemView.findViewById(R.id.llBackLuxuryMarketItem);
            bgFront = (LinearLayout) itemView.findViewById(R.id.llLuxuryMarketItem);
            headerBack = (TextView) itemView.findViewById(R.id.tvBackLuxuryMarketItemHeader);
            descBack = (TextView) itemView.findViewById(R.id.tvBackLuxuryMarketItemDesc);
            infoBack = (ImageButton) itemView.findViewById(R.id.ibBackInfo);
        }
    }
}
