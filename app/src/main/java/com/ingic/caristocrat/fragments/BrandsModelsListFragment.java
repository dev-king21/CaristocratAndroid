package com.ingic.caristocrat.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.BrandsListFilterActivity;
import com.ingic.caristocrat.adapters.BrandsModelsAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.ActivityBrandsListFilterBinding;
import com.ingic.caristocrat.databinding.FragmentModelsListBinding;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.LuxuryMarketSearchFilter;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.interfaces.BrandModelsSelectedListener;
import com.ingic.caristocrat.interfaces.BrandModelsVersionsSelectedListener;
import com.ingic.caristocrat.interfaces.DialogCloseListener;
import com.ingic.caristocrat.interfaces.OnModelsSelectedListener;
import com.ingic.caristocrat.interfaces.OnVersionSelectedListener;
import com.ingic.caristocrat.models.FilterBrand;
import com.ingic.caristocrat.models.Model;
import com.ingic.caristocrat.models.Version;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("ValidFragment")
public class BrandsModelsListFragment extends DialogFragment implements View.OnClickListener, DialogCloseListener, OnVersionSelectedListener, BrandModelsVersionsSelectedListener {
    ActivityBrandsListFilterBinding activityBrandsListFilterBinding;
    BrandsListFilterActivity activityContext;
    BrandsModelsAdapter adapter;
    FragmentModelsListBinding binding;
    ArrayList<Model> models;
    DialogCloseListener listener;
    OnModelsSelectedListener modelsSelectedListener;
    FilterBrand filterBrand;
    Titlebar titlebar;
    Button btnSelect;
    private LuxuryMarketSearchFilter filter;
    private BrandModelsSelectedListener brandModelsSelectedListener;

    public BrandsModelsListFragment(BrandsListFilterActivity activityContext, FilterBrand filterBrand, ActivityBrandsListFilterBinding activityBrandsListFilterBinding, DialogCloseListener listener, OnModelsSelectedListener modelsSelectedListener) {
        this.activityContext = activityContext;
        this.filterBrand = filterBrand;
        this.activityBrandsListFilterBinding = activityBrandsListFilterBinding;
        this.models = new ArrayList<>();
        this.listener = listener;
        this.modelsSelectedListener = modelsSelectedListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_models_list, container, false);

        this.filter = LuxuryMarketSearchFilter.getInstance();

        adapter = new BrandsModelsAdapter(activityContext, activityBrandsListFilterBinding, this, this);
        adapter.setBrandModelsVersionsSelectedListener(this);
        int spacingInPixels = activityContext.getResources().getDimensionPixelSize(R.dimen.dp8);
        SpacesItemDecorationAllSideEqual spacesItemDecorationHome = new SpacesItemDecorationAllSideEqual(spacingInPixels);

        binding.recyclerview.addItemDecoration(spacesItemDecorationHome);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activityContext, 3);
        binding.recyclerview.setLayoutManager(gridLayoutManager);
        binding.recyclerview.setAdapter(adapter);

        this.titlebar = activityBrandsListFilterBinding.titlebar;
        this.btnSelect = activityBrandsListFilterBinding.btnFilterAction;

        setTitlebar(titlebar);
        this.btnSelect.setText(activityContext.getResources().getString(R.string.select));

        setListeners();

        getCarModels(filterBrand.getId());

        return binding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listener.onDismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibBackbtn:
//                backBtn();
                selectModels();
                break;

            case R.id.btnFilterAction:
                selectModels();
                break;
        }
    }

    @Override
    public void onDismiss() {
        activityBrandsListFilterBinding.btnFilterAction.setText(getResources().getString(R.string.select));
        activityBrandsListFilterBinding.btnFilterAction.setOnClickListener(this);
        setTitlebar(activityBrandsListFilterBinding.titlebar);
    }

    @Override
    public void onVersionsSelected(boolean selected, int modelId, ArrayList<Version> selectedVersions) {
        for (int pos = 0; pos < models.size(); pos++) {
            if (models.get(pos).getId() == modelId) {
                models.get(pos).setSelected(selected);
                models.get(pos).setVersions(selectedVersions);
                break;
            }
        }
        adapter.addAll(models);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBrandModelsVersionSelected(Model model) {
        if (model != null) {
            for (int ind = 0; ind < adapter.getModels().size(); ind++) {
                if (model.getId() == adapter.getModels().get(ind).getId()) {
                    model.setSelected(true);
                    adapter.setModel(ind, model);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void setListeners() {
        this.btnSelect.setOnClickListener(this);
    }

    private void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(activityContext);
        titlebar.showTitlebar(activityContext);
        /*
        if (isSearchEnabled) {
            binding.sideBar.setVisibility(View.GONE);
            titlebar.showSearch().addTextChangedListener(this);
        } else {
            binding.sideBar.setVisibility(View.VISIBLE);
            titlebar.showSearchButton(context).setOnClickListener(this);
            titlebar.setTitle(context.getResources().getString(R.string.brands));
        }
        */
        titlebar.setTitle(activityContext.getResources().getString(R.string.models));
        titlebar.showBackButton(activityContext, false).setOnClickListener(this);
    }

    private void backBtn() {
        activityContext.onBackPressed();
    }

    private void selectModels() {
        ArrayList<Model> selectedModels = new ArrayList<>();
        boolean selected;
        if (models.size() > 0) {
            for (int position = 0; position < models.size(); position++) {
                if (models.get(position).isSelected()) {
                    selectedModels.add(models.get(position));
                }
            }
            if (selectedModels.size() > 0) {
                selected = true;
            } else {
                selected = false;
            }
            filterBrand.setModels(selectedModels);
            brandModelsSelectedListener.onBrandModelsSelected(filterBrand);
//            modelsSelectedListener.onModelsSelected(selected, filterBrand.getId(), selectedModels);
        }

        activityContext.onBackPressed();
    }

    private void getCarModels(int brandId) {
        Map<String, Object> params = new HashMap<>();
        params.put("brand_id", brandId);
        WebApiRequest.Instance(activityContext).request(AppConstants.WebServicesKeys.MODEL, null, null, params, null, new WebApiRequest.WebServiceArrayResponse() {
            @Override
            public void onSuccess(ApiArrayResponse apiArrayResponse) {
                models = JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), Model.class);
                if (models.size() == 0) {
                    binding.recyclerview.setVisibility(View.GONE);
                    binding.tvNoData.setVisibility(View.VISIBLE);
                } else {
                    binding.tvNoData.setVisibility(View.GONE);
                    binding.recyclerview.setVisibility(View.VISIBLE);

                    Model model = new Model();
                    model.setId(-1);
                    model.setAll(true);
                    model.setName(activityContext.getResources().getString(R.string.all_models));
                    models.add(0, model);
                    
                    if (filterBrand != null && filterBrand.getModels() != null) {
                        for (int i = 0; i < models.size(); i++) {
                            for (int j = 0; j < filterBrand.getModels().size(); j++) {
                                if (models.get(i).getId().equals(filterBrand.getModels().get(j).getId())) {
                                    models.get(i).setVersions(filterBrand.getModels().get(j).getVersions());
                                    models.get(i).setSelected(true);
                                }
                            }
                        }
                    }

                    adapter.addAll(models);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    public static class SpacesItemDecorationAllSideEqual extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecorationAllSideEqual(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.top = space;
            outRect.bottom = space;
            outRect.right = space;
        }
    }

    public void setBrandModelsSelectedListener(BrandModelsSelectedListener brandModelsSelectedListener) {
        this.brandModelsSelectedListener = brandModelsSelectedListener;
    }
}
