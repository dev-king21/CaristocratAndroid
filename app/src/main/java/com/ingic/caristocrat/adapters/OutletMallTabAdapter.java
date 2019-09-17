package com.ingic.caristocrat.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.helpers.RoundImageView;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.helpers.Utils;
import com.ingic.caristocrat.models.FilterBrand;
import com.ingic.caristocrat.models.TradeCar;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 */
public class OutletMallTabAdapter extends RecyclerView.Adapter<OutletMallTabAdapter.MyViewHolder> {
    ArrayList<TradeCar> luxuryCategories;
    MainActivity context;
    String categoryKey;
    boolean review;

    public OutletMallTabAdapter(MainActivity context) {
        this.context = context;
        this.luxuryCategories = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_outletmall, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (luxuryCategories.get(position).getMedia() != null && luxuryCategories.get(position).getMedia().size() > 0)
            UIHelper.setImageWithGlideNoPlaceHolder(context, holder.outletMallImgae, luxuryCategories.get(position).getMedia().get(0).getFileUrl());
        holder.outletMallTitle.setText(Utils.getCarName(luxuryCategories.get(position), false));
        holder.year.setText(!UIHelper.isEmptyOrNull(luxuryCategories.get(position).getYear() + "") ? luxuryCategories.get(position).getYear() + "" : "-");
        holder.price.setText((luxuryCategories.get(position).getCurrency() == null ? context.getCurrency() : luxuryCategories.get(position).getCurrency()) + " " + (!UIHelper.isEmptyOrNull(luxuryCategories.get(position).getAmount() + "") ? NumberFormat.getNumberInstance(Locale.US).format(luxuryCategories.get(position).getAmount()) : "-"));
        holder.price.setSelected(true);
        if (categoryKey != null) {
            if (categoryKey.equals(AppConstants.MainCategoriesType.LUXURY_NEW_CARS)) {
                if (luxuryCategories.get(position).getModel() != null) {
                    if (luxuryCategories.get(position).getModel().getBrand() != null) {
                        if (luxuryCategories.get(position).getModel().getBrand().getMedia().size() > 0) {
                            UIHelper.setImageWithGlide(context, holder.ivBrandLogo, luxuryCategories.get(position).getModel().getBrand().getMedia().get(0).getFileUrl());
                            holder.cardViewBrandLogo.setVisibility(View.VISIBLE);
//                            holder.price.setVisibility(View.GONE);
                        }
                    }
                }
            } else if (categoryKey.equals(AppConstants.MainCategoriesType.APPROVED_PRE_OWNED) || categoryKey.equals(AppConstants.MainCategoriesType.CLASSIC_CARS)) {
                if (luxuryCategories.get(position).getAmount_mkp() != null) {
                    holder.price.setText((luxuryCategories.get(position).getCurrency() == null ? context.getCurrency() : luxuryCategories.get(position).getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(luxuryCategories.get(position).getAmount_mkp()));
                }
                if (luxuryCategories.get(position).getKilometre() != null) {
                    holder.tvMileage.setText(NumberFormat.getNumberInstance(Locale.US).format(luxuryCategories.get(position).getKilometre()) + " KM");
                    holder.tvMileage.setVisibility(View.VISIBLE);
                    holder.rivSeparator.setVisibility(View.VISIBLE);
                }
            } else if (categoryKey.equals(AppConstants.MainCategoriesType.REVIEWS)) {
//                holder.price.setVisibility(View.GONE);
                holder.ratingbar.setRating(luxuryCategories.get(position).getAverage_rating());
                holder.ratingbar.setVisibility(View.VISIBLE);
            }
        }
        if (luxuryCategories.get(position).getFeatured() == 1 && !review) {
            holder.tvFeatured.setVisibility(View.VISIBLE);
        } else {
            holder.tvFeatured.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return luxuryCategories.size();
    }

    public void addAll(ArrayList<TradeCar> data) {
        this.luxuryCategories.clear();
        this.luxuryCategories.addAll(data);
        this.notifyDataSetChanged();
    }

    public void concatenate(ArrayList<TradeCar> data) {
        this.luxuryCategories.addAll(data);
    }

    public void clear() {
        this.luxuryCategories.clear();
    }

    public void filterList(ArrayList<TradeCar> arrayList) {
        this.luxuryCategories.clear();
        this.luxuryCategories.addAll(arrayList);
    }

    public ArrayList<TradeCar> getAll() {
        return luxuryCategories;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView outletMallTitle, tvMileage, year, price, tvFeatured;
        RoundImageView rivSeparator;
        ImageView outletMallImgae, ivBrandLogo;
        CardView cardViewBrandLogo;
        RatingBar ratingbar;

        public MyViewHolder(View itemView) {
            super(itemView);
            outletMallTitle = (TextView) itemView.findViewById(R.id.tvOutMallTitle);
            tvMileage = (TextView) itemView.findViewById(R.id.tvMileage);
            year = (TextView) itemView.findViewById(R.id.tvYear);
            price = (TextView) itemView.findViewById(R.id.tvPrice);
            outletMallImgae = (ImageView) itemView.findViewById(R.id.ivOutletMall);
            ivBrandLogo = (ImageView) itemView.findViewById(R.id.ivBrandLogo);
            cardViewBrandLogo = (CardView) itemView.findViewById(R.id.cardViewBrandLogo);
            rivSeparator = itemView.findViewById(R.id.rivSeparator);
            ratingbar = itemView.findViewById(R.id.ratingbar);
            tvFeatured = itemView.findViewById(R.id.tvFeatured);
        }
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

    public void setReview(boolean review) {
        this.review = review;
    }
}
