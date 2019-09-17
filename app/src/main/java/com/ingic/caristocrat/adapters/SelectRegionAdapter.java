package com.ingic.caristocrat.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.databinding.LayoutSelectRegionDialogBinding;
import com.ingic.caristocrat.dialogs.SelectCountryDialog;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.models.Region;

import java.util.ArrayList;

/**
 * on 10/5/2018.
 */
public class SelectRegionAdapter extends RecyclerView.Adapter<SelectRegionAdapter.MyViewHolder> {
    ArrayList<Region> country;
    Context context;
    //    ImageView previousImageView;
//    Country previousCountry;
    SelectCountryDialog selectCountryDialog;
    LayoutSelectRegionDialogBinding layoutSelectRegionDialogBinding;

    public SelectRegionAdapter(Context context, LayoutSelectRegionDialogBinding layoutSelectRegionDialogBinding, ArrayList<Region> country, SelectCountryDialog selectCountryDialog) {
        this.context = context;
        this.layoutSelectRegionDialogBinding = layoutSelectRegionDialogBinding;
        this.country = country;
        this.selectCountryDialog = selectCountryDialog;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_select_country, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        UIHelper.setImageWithGlide(context, holder.countryImage, country.get(position).getFlag());
        holder.name.setText(country.get(position).getName());
        if (country.get(position).getIs_my_region() == 1) {
            holder.selectedTick.setChecked(true);
            setSelectedRegion(holder.countryImage, holder.selectedTick, country.get(position));
        } else {
            holder.countryImage.setBackground(null);
            holder.selectedTick.setChecked(false);
        }
        holder.selectedTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectedRegion(holder.countryImage, holder.selectedTick, country.get(position));
            }
        });
        holder.countryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.selectedTick.performClick();


            }
        });

    }

    private void setSelectedRegion(ImageView currentImage, CheckBox tick, Region region) {
        currentImage.setBackground(null);
        if (tick.isChecked()) {
            selectCountryDialog.getSelectedRegions().add(new Integer(region.getId()));
            selectCountryDialog.getSelectedRegionsObjects().add(region);
            currentImage.setBackground(context.getResources().getDrawable(R.drawable.region_selected_drawable));
            layoutSelectRegionDialogBinding.btnClear.setText(context.getResources().getString(R.string.clear));
        } else {
            for (int i = 0; i < selectCountryDialog.getSelectedRegions().size(); i++) {
                if (selectCountryDialog.getSelectedRegionsObjects().get(i).getId() == region.getId()) {
                    selectCountryDialog.getSelectedRegions().remove(new Integer(region.getId()));
                    selectCountryDialog.getSelectedRegionsObjects().remove(i);
                    break;
                }
            }
            if (selectCountryDialog.getSelectedRegionsObjects().size() == 0) {
                layoutSelectRegionDialogBinding.btnClear.setText(context.getResources().getString(R.string.close));
            }
        }
    }

    @Override
    public int getItemCount() {
        return country.size();
    }

    public void addAll(ArrayList<Region> data) {
        country.clear();
        country.addAll(data);
        notifyDataSetChanged();
    }

    public void clearAllSelections() {
        for (int i = 0; i < country.size(); i++) {
            country.get(i).setIs_my_region(0);
        }
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CheckBox selectedTick;
        ImageView countryImage;


        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tvCountry);
            countryImage = (ImageView) itemView.findViewById(R.id.ivCountry);
            selectedTick = (CheckBox) itemView.findViewById(R.id.cbSelectedTick);

        }
    }
}

