package com.ingic.caristocrat.adapters;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.BrandsListFilterActivity;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.models.Version;

import java.util.ArrayList;

public class ModelsVersionsAdapter extends RecyclerView.Adapter<ModelsVersionsAdapter.Holder> {
    BrandsListFilterActivity activityContext;
    ArrayList<Version> versions;

    public ModelsVersionsAdapter(BrandsListFilterActivity activityContext) {
        this.activityContext = activityContext;
        this.versions = new ArrayList<>();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_filter_model_version, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.textview.setText(versions.get(position).getName());
        if (versions.get(position).isAll()) {
            holder.checkBox.setVisibility(View.GONE);
        } else {
            holder.checkBox.setVisibility(View.VISIBLE);
        }
        holder.textview.setSelected(true);

        if (versions.get(position).isSelected()) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        holder.textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (versions.get(position).isAll()) {
                    if (versions.get(position).isSelected()) {
//                        holder.textview.setBackground(activityContext.getResources().getDrawable(R.drawable.filter_brand_model_unselected));
//                        holder.textview.setTextColor(activityContext.getResources().getColor(R.color.colorBlack));
                        for (int pos = 0; pos < versions.size(); pos++) {
                            versions.get(pos).setSelected(false);
                        }
                    } else {
//                        holder.textview.setBackground(activityContext.getResources().getDrawable(R.drawable.filter_brand_model_selected));
//                        holder.textview.setTextColor(activityContext.getResources().getColor(R.color.colorWhite));
                        for (int pos = 0; pos < versions.size(); pos++) {
                            versions.get(pos).setSelected(true);
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        });

        /*
        holder.checkBox.setText(versions.get(position).getName());
        if (versions.get(position).isSelected()) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    versions.get(position).setSelected(true);
                } else {
                    versions.get(position).setSelected(false);
                }
            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return this.versions.size();
    }

    public void addAll(ArrayList<Version> versions) {
        this.versions.clear();
        this.versions.addAll(versions);
    }

    public void disableViewsForSomeSeconds(View view) {
        view.setEnabled(false);
        view.setAlpha((float) 0.75);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
                view.setAlpha((float) 1.0);
            }
        }, AppConstants.SPLASH_DURATION);
    }

    class Holder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView textview;

        Holder(View view) {
            super(view);
            checkBox = view.findViewById(R.id.checkbox);
            textview = view.findViewById(R.id.textview);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        versions.get(getAdapterPosition()).setSelected(true);
                    } else {
                        versions.get(getAdapterPosition()).setSelected(false);
                    }
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
