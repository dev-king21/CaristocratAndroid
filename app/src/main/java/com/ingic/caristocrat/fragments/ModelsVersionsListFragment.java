package com.ingic.caristocrat.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.BrandsListFilterActivity;
import com.ingic.caristocrat.adapters.ModelsVersionsAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.ActivityBrandsListFilterBinding;
import com.ingic.caristocrat.databinding.FragmentVersionsListBinding;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.LuxuryMarketSearchFilter;
import com.ingic.caristocrat.helpers.Titlebar;
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

@SuppressLint("ValidFragment")
public class ModelsVersionsListFragment extends DialogFragment implements View.OnClickListener {
    ActivityBrandsListFilterBinding activityBrandsListFilterBinding;
    BrandsListFilterActivity activityContext;
    ModelsVersionsAdapter adapter;
    FragmentVersionsListBinding binding;
    ArrayList<Version> versions;
    DialogCloseListener listener;
    OnVersionSelectedListener onVersionSelectedListener;
    Model model;
    Titlebar titlebar;
    Button btnSelect;
    LuxuryMarketSearchFilter filter;
    BrandModelsVersionsSelectedListener brandModelsVersionsSelectedListener;

    public ModelsVersionsListFragment(BrandsListFilterActivity activityContext, Model model, ActivityBrandsListFilterBinding activityBrandsListFilterBinding, DialogCloseListener listener, OnVersionSelectedListener onVersionSelectedListener) {
        this.activityContext = activityContext;
        this.model = model;
        this.activityBrandsListFilterBinding = activityBrandsListFilterBinding;
        this.versions = new ArrayList<>();
        this.listener = listener;
        this.onVersionSelectedListener = onVersionSelectedListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_versions_list, container, false);

        this.filter = LuxuryMarketSearchFilter.getInstance();

        adapter = new ModelsVersionsAdapter(activityContext);

        int spacingInPixels = activityContext.getResources().getDimensionPixelSize(R.dimen.dp8);
        BrandsModelsListFragment.SpacesItemDecorationAllSideEqual spacesItemDecorationHome = new BrandsModelsListFragment.SpacesItemDecorationAllSideEqual(spacingInPixels);

        binding.recyclerview.addItemDecoration(spacesItemDecorationHome);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activityContext, 3);
        binding.recyclerview.setLayoutManager(gridLayoutManager);
        binding.recyclerview.setAdapter(adapter);

        this.titlebar = activityBrandsListFilterBinding.titlebar;
        this.btnSelect = activityBrandsListFilterBinding.btnFilterAction;

        setTitlebar(titlebar);
        this.btnSelect.setText(activityContext.getResources().getString(R.string.select));

        setListeners();

        getVersions(model.getId());

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
                selectVersions();
                break;

            case R.id.btnFilterAction:
                selectVersions();
                break;
        }
    }

    private void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(activityContext);
        titlebar.showTitlebar(activityContext);
        titlebar.setTitle(activityContext.getResources().getString(R.string.versions));
        titlebar.showBackButton(activityContext, false).setOnClickListener(this);
    }

    private void setListeners() {
        this.btnSelect.setOnClickListener(this);
    }

    private void selectVersions() {
        ArrayList<Version> selectedVersions = new ArrayList<>();
        boolean selected = false;

        if (versions.size() > 0) {
            for (int position = 0; position < versions.size(); position++) {
                if (versions.get(position).isSelected()) {
                    selectedVersions.add(versions.get(position));
                }
            }
            if (selectedVersions.size() > 0) {
                selected = true;
            } else {
                selected = false;
            }
            model.setVersions(selectedVersions);
            brandModelsVersionsSelectedListener.onBrandModelsVersionSelected(model);
//            onVersionSelectedListener.onVersionsSelected(selected, model.getId(), selectedVersions);
        }

        activityContext.onBackPressed();
    }

    private void getVersions(int modelId) {
        Map<String, Object> params = new HashMap<>();
        params.put("model_id", modelId);
        WebApiRequest.Instance(activityContext).request(AppConstants.WebServicesKeys.CAR_VERSIONS, null, null, params, null, new WebApiRequest.WebServiceArrayResponse() {
            @Override
            public void onSuccess(ApiArrayResponse apiArrayResponse) {
                versions = JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), Version.class);
                if (versions.size() == 0) {
                    binding.recyclerview.setVisibility(View.GONE);
                    binding.tvNoData.setVisibility(View.VISIBLE);
                } else {
                    binding.tvNoData.setVisibility(View.GONE);
                    binding.recyclerview.setVisibility(View.VISIBLE);

                    Version version = new Version();
                    version.setId(-1);
                    version.setAll(true);
                    version.setName(activityContext.getResources().getString(R.string.all_versions));
                    versions.add(0, version);

                    if (model != null && model.getVersions() != null) {
                        for (int i = 0; i < versions.size(); i++) {
                            for (int j = 0; j < model.getVersions().size(); j++) {
                                if (versions.get(i).getId() == model.getVersions().get(j).getId()) {
                                    versions.get(i).setSelected(true);
                                }
                            }
                        }
                    }

                    adapter.addAll(versions);
                    adapter.notifyDataSetChanged();
                }
                activityContext.hideLoader();
            }

            @Override
            public void onError() {
                activityContext.hideLoader();
            }
        });
    }

    public void setBrandModelsVersionsSelectedListener(BrandModelsVersionsSelectedListener brandModelsVersionsSelectedListener) {
        this.brandModelsVersionsSelectedListener = brandModelsVersionsSelectedListener;
    }
}
