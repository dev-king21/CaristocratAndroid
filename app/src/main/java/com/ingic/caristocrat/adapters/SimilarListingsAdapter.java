package com.ingic.caristocrat.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.helpers.Utils;
import com.ingic.caristocrat.models.TradeCar;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * on 10/17/2018.
 */
public class SimilarListingsAdapter extends RecyclerView.Adapter<SimilarListingsAdapter.Holder> implements View.OnClickListener {
    MainActivity mainActivityContext;
    ArrayList<TradeCar> arrayList;
    int hidingItemId;
    private Double width = 120.0;

    public SimilarListingsAdapter(MainActivity mainActivityContext) {
        this.mainActivityContext = mainActivityContext;
        this.arrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivityContext).inflate(R.layout.layout_similar_luxury_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.cardView.getLayoutParams().width = width.intValue();
        holder.cardView.getLayoutParams().height = width.intValue();
        if (arrayList.get(position).getMedia() != null && arrayList.get(position).getMedia().size() > 0)
            setImageWithGlide(mainActivityContext, holder.ivItem, arrayList.get(position).getMedia().get(0).getFileUrl());
//        holder.tvKm.setText((!UIHelper.isEmptyOrNull(arrayList.get(position).getKilometre()) ? arrayList.get(position).getKilometre() : "-") +" "+ mainActivityContext.getResources().getString(R.string.km));
        holder.tvYear.setText(!UIHelper.isEmptyOrNull(arrayList.get(position).getYear() + "") ? arrayList.get(position).getYear() + "" : "-");
        holder.tvName.setText(Utils.getCarName(arrayList.get(position), true));
        holder.tvAmount.setText((arrayList.get(position).getCurrency() == null ? mainActivityContext.getCurrency() : arrayList.get(position).getCurrency()) + " " + (!UIHelper.isEmptyOrNull(arrayList.get(position).getAmount() + "") ? NumberFormat.getNumberInstance(Locale.US).format(arrayList.get(position).getAmount()) : "-"));
        if (arrayList.get(position).getKilometre() != null) {
            holder.tvKm.setText(NumberFormat.getNumberInstance(Locale.US).format(arrayList.get(position).getKilometre()) + " " + mainActivityContext.getResources().getString(R.string.km));
        }
//        if (arrayList.get(position).getId() != hidingItemId) {
//        }
        holder.tvName.setSelected(true);
        holder.tvAmount.setSelected(true);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void onClick(View view) {
        UIHelper.hideSoftKeyboard(mainActivityContext, view);
        switch (view.getId()) {
          /*  case R.id.tvLikesCount:
                UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.implemented_later), Toast.LENGTH_SHORT);
                break;
            case R.id.tvViewsCount:
                UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.implemented_later), Toast.LENGTH_SHORT);
                break;
            case R.id.tvCommentsCount:
                UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.implemented_later), Toast.LENGTH_SHORT);
                break;
*/
        }
    }

    public void addAll(ArrayList<TradeCar> arrayList) {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<TradeCar> arrayList, int hidingItemId) {
        this.arrayList=new ArrayList<>();
//        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
        for (int i = 0; i < this.arrayList.size(); i++) {
            if (this.arrayList.get(i).getId() == hidingItemId) {
                this.arrayList.remove(i);
            }
        }
        notifyDataSetChanged();
    }

    public TradeCar getItem(int pos) {
        if (arrayList.size() > 0)
            return arrayList.get(pos);
        else
            return new TradeCar();
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
        TextView tvKm;
        TextView tvYear;
        TextView tvName;
        TextView tvAmount;

        public Holder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);
            tvKm = (TextView) itemView.findViewById(R.id.tvKm);
            tvYear = (TextView) itemView.findViewById(R.id.tvYear);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);

        }

    }

    public static void setImageWithGlide(Context context, ImageView view, String url) {
        Glide.with(context).clear(view);
        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.centerCrop();
        requestOptions.dontAnimate().placeholder(R.drawable.image_placeholder);
//        requestOptions.timeout(10000);
//                        .apply(new RequestOptions().override(400, 250))
//        requestOptions.centerCrop();
//        requestOptions.onlyRetrieveFromCache(false);
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(view);
    }
}
