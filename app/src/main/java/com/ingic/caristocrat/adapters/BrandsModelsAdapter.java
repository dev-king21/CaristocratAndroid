package com.ingic.caristocrat.adapters;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.BrandsListFilterActivity;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.ActivityBrandsListFilterBinding;
import com.ingic.caristocrat.fragments.ModelsVersionsListFragment;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.BrandModelsVersionsSelectedListener;
import com.ingic.caristocrat.interfaces.DialogCloseListener;
import com.ingic.caristocrat.interfaces.OnVersionSelectedListener;
import com.ingic.caristocrat.models.Model;
import com.ingic.caristocrat.models.Version;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BrandsModelsAdapter extends RecyclerView.Adapter<BrandsModelsAdapter.Holder> {
    ActivityBrandsListFilterBinding activityBrandsListFilterBinding;
    BrandsListFilterActivity activityContext;
    DialogCloseListener listener;
    ArrayList<Model> models;
    OnVersionSelectedListener onVersionSelectedListener;
    BrandModelsVersionsSelectedListener brandModelsVersionsSelectedListener;

    public BrandsModelsAdapter(BrandsListFilterActivity activityContext, ActivityBrandsListFilterBinding activityBrandsListFilterBinding, DialogCloseListener listener, OnVersionSelectedListener onVersionSelectedListener) {
        this.activityContext = activityContext;
        this.models = new ArrayList<>();
        this.activityBrandsListFilterBinding = activityBrandsListFilterBinding;
        this.listener = listener;
        this.onVersionSelectedListener = onVersionSelectedListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_filter_model_version, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.textview.setText(models.get(position).getName());
        if (models.get(position).isAll()) {
            holder.checkBox.setVisibility(View.GONE);
        } else {
            holder.checkBox.setVisibility(View.VISIBLE);
        }

        holder.textview.setSelected(true);

        if (models.get(position).isSelected()) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        holder.textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (models.get(position).isAll()) {
                    if (models.get(position).isSelected()) {
//                        holder.textview.setBackground(activityContext.getResources().getDrawable(R.drawable.filter_brand_model_unselected));
//                        holder.textview.setTextColor(activityContext.getResources().getColor(R.color.colorBlack));
                        for (int pos = 0; pos < models.size(); pos++) {
                            models.get(pos).setSelected(false);
                        }
                    } else {
//                        holder.textview.setBackground(activityContext.getResources().getDrawable(R.drawable.filter_brand_model_selected));
//                        holder.textview.setTextColor(activityContext.getResources().getColor(R.color.colorWhite));
                        for (int pos = 0; pos < models.size(); pos++) {
                            models.get(pos).setSelected(true);
                        }
                    }
                    notifyDataSetChanged();
                } else {
                    disableViewsForSomeSeconds(holder.checkBox);
                    if (activityContext.showLoader()) {
                        getVersions(position, models.get(position).getId());
                    }
                }
            }
        });

        /*
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    models.get(position).setSelected(true);
                } else {
                    models.get(position).setSelected(false);
                }
            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return this.models.size();
    }

    private void getVersions(int position, int modelId) {
        Map<String, Object> params = new HashMap<>();
        params.put("model_id", modelId);
        WebApiRequest.Instance(activityContext).request(AppConstants.WebServicesKeys.CAR_VERSIONS, null, null, params, null, new WebApiRequest.WebServiceArrayResponse() {
            @Override
            public void onSuccess(ApiArrayResponse apiArrayResponse) {
                ArrayList<Version> versions = JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), Version.class);
                if (activityContext.getFilter().isLuxuryNewCars() && versions.size() > 0) {
                    ModelsVersionsListFragment modelsVersionsListFragment = new ModelsVersionsListFragment(activityContext, models.get(position), activityBrandsListFilterBinding, listener, onVersionSelectedListener);
                    modelsVersionsListFragment.setBrandModelsVersionsSelectedListener(brandModelsVersionsSelectedListener);
                    activityContext
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .add(activityContext.getDockFrameLayoutId(), modelsVersionsListFragment, ModelsVersionsListFragment.class.getSimpleName())
                            .addToBackStack(activityContext.getSupportFragmentManager().getBackStackEntryCount() == 0 ? "secondFrag" : null)
                            .commit();
                } else {
                    if (models.get(position).isSelected()) {
                        models.get(position).setSelected(false);
                    } else {
                        models.get(position).setSelected(true);
                    }
                    notifyDataSetChanged();
//                    UIHelper.showToast(activityContext, activityContext.getResources().getString(R.string.no_versions_to_show), Toast.LENGTH_SHORT);
                }
                activityContext.hideLoader();
            }

            @Override
            public void onError() {
                activityContext.hideLoader();
            }
        });
    }

    public ArrayList<Model> getModels() {
        return this.models;
    }

    public void setModel(int index, Model model) {
        this.models.set(index, model);
    }

    public void addAll(ArrayList<Model> models) {
        this.models.clear();
        this.models.addAll(models);
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

    public void setBrandModelsVersionsSelectedListener(BrandModelsVersionsSelectedListener brandModelsVersionsSelectedListener) {
        this.brandModelsVersionsSelectedListener = brandModelsVersionsSelectedListener;
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
                        models.get(getAdapterPosition()).setSelected(true);
                    } else {
                        models.get(getAdapterPosition()).setSelected(false);
                        models.get(getAdapterPosition()).setVersions(null);
                    }
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
