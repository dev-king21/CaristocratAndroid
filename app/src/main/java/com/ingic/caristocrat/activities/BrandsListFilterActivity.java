package com.ingic.caristocrat.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.gjiazhe.wavesidebar.WaveSideBar;
import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.BrandsListFilterAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.ActivityBrandsListFilterBinding;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.LuxuryMarketSearchFilter;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.helpers.Utils;
import com.ingic.caristocrat.interfaces.BrandModelsSelectedListener;
import com.ingic.caristocrat.interfaces.DialogCloseListener;
import com.ingic.caristocrat.interfaces.OnModelsSelectedListener;
import com.ingic.caristocrat.models.FilterBrand;
import com.ingic.caristocrat.models.Model;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.ArrayList;

public class BrandsListFilterActivity extends BaseActivity implements View.OnClickListener, TextWatcher, DialogCloseListener, OnModelsSelectedListener, BrandModelsSelectedListener {
    private ActivityBrandsListFilterBinding binding;
    private BrandsListFilterActivity context;
    private BrandsListFilterAdapter adapter;
    private ArrayList<FilterBrand> arrayList;
    private LuxuryMarketSearchFilter filter;
    private boolean isSearchEnabled = false;
    private double itemWidth;
    private UIHelper.SpacesItemDecorationAllSideEqual spacesItemDecorationAllSideEqual;
    private int spacingInPixels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_brands_list_filter);
        this.context = this;

        itemWidth = UIHelper.screensize(this, "x") / 2.5;

        filter = LuxuryMarketSearchFilter.getInstance();

        setTitlebar(binding.titlebar);

        binding.btnFilterAction.setText(getResources().getString(R.string.close));
        binding.btnFilterAction.setOnClickListener(this);

        adapter = new BrandsListFilterAdapter(context, binding, this, this);
        adapter.setBrandModelsSelectedListener(this);
        if (filter.getFilterBrand() != null) {
            adapter.setSelectedBrand(filter.getFilterBrand());
        }
        adapter.setWidth(itemWidth);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        spacingInPixels = getResources().getDimensionPixelSize(R.dimen.dp8);
        spacesItemDecorationAllSideEqual = new UIHelper.SpacesItemDecorationAllSideEqual(spacingInPixels);
        binding.recyclerView.addItemDecoration(spacesItemDecorationAllSideEqual);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setAdapter(adapter);
        binding.sideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                for (int i = 0; i < adapter.getArrayList().size(); i++) {
                    if (adapter.getArrayList().get(i).getName().substring(0, 1).toUpperCase().equals(index)) {
//                        ((GridLayoutManager) binding.recyclerView.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                        binding.recyclerView.getLayoutManager().scrollToPosition(i);
                        return;
                    }
                }

            }
        });
        if (showLoader()) {
            getCarBrands();
        }
    }

    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(context);
        titlebar.showTitlebar(context);
        if (isSearchEnabled) {
            binding.sideBar.setVisibility(View.GONE);
            titlebar.showSearch().setHint(context.getResources().getString(R.string.search_brands));
            titlebar.showSearch().addTextChangedListener(this);
        } else {
            binding.sideBar.setVisibility(View.GONE);
            titlebar.showSearchButton(context).setOnClickListener(this);
            titlebar.setTitle(context.getResources().getString(R.string.brands));
        }
        titlebar.showBackButton(context, false).setOnClickListener(this);
/*
        titlebar.btnFilterAction.setVisibility(View.VISIBLE);
        titlebar.btnFilterAction.setText(mainActivityContext.getResources().getString(R.string.close));
        titlebar.btnFilterAction.setOnClickListener(this);
        titlebar.llCommentsLayout.setVisibility(View.VISIBLE);
        titlebar.nestedscroll.setNestedScrollingEnabled(false);
*/
    }

    @Override
    public void onBackPressed() {
        backBtn();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        filter(s.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibBackbtn:
//                backBtn();
                close();
                break;

            case R.id.ibSearch:
                search();
                break;

            case R.id.btnFilterAction:
                close();
                break;
        }
    }

    //This function will only be called when app navigates back from models list selection screen to this brand listing screen either with selected models or without selection
    @Override
    public void onDismiss() {
        binding.btnFilterAction.setText(getResources().getString(R.string.close));
        binding.btnFilterAction.setOnClickListener(this);
        setTitlebar(binding.titlebar);
    }

    @Override
    public void onModelsSelected(boolean selected, int brandId, ArrayList<Model> selectedModels) {
        for (int pos = 0; pos < arrayList.size(); pos++) {
            if (arrayList.get(pos).getId() == brandId) {
                filter.addBrand(arrayList.get(pos));
                arrayList.get(pos).setSelected(selected);
                arrayList.get(pos).setModels(selectedModels);
                break;
            }
        }
        adapter.addAll(arrayList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBrandModelsSelected(FilterBrand filterBrand) {
        if (filterBrand != null) {
            filter.setFilterBrand(filterBrand);
            adapter.setSelectedBrand(filterBrand);
            adapter.notifyDataSetChanged();
        }
    }

    public int getDockFrameLayoutId() {
        return binding.mainFrame.getId();
    }

    public boolean showLoader() {
        UIHelper.hideSoftKeyboard(this);
        if (!Utils.isNetworkAvailable(this)) {
            UIHelper.showSnackbar(getMainFrameLayout(), getResources().getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG);
            return false;
        } else {
            binding.progressBarContainer.setVisibility(View.VISIBLE);
            return true;
        }
    }

    public void hideLoader() {
        binding.progressBarContainer.setVisibility(View.GONE);
    }

    private void close() {
        if (adapter.getSelectedBrand() != null) {
            filter.setFilter(true);
            filter.setFilterBrand(adapter.getSelectedBrand());
        }
        finish();
        /*
        for (int pos = 0; pos < arrayList.size(); pos++) {
            if (arrayList.get(pos).isSelected()) {
                brandsList.add(arrayList.get(pos));
            }
        }
        */
    }

    private void search() {
        if (adapter.getItemCount() == 0) {
            UIHelper.showToast(context, context.getResources().getString(R.string.no_brands_search), Toast.LENGTH_SHORT);
        }
        if (adapter.getItemCount() > 0) {
            isSearchEnabled = true;
            setTitlebar(binding.titlebar);
        }
    }

    private void backBtn() {
        UIHelper.hideSoftKeyboard(context);
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            if (isSearchEnabled) {
                isSearchEnabled = false;
                setTitlebar(binding.titlebar);
            } else {
//                filter.resetBrandsList();
//                finish();
                close();
            }
        }
    }

    private void filter(String text) {
        if (text.length() == 0) {
            adapter.addAll(arrayList);
            adapter.notifyDataSetChanged();
        } else {
            ArrayList<FilterBrand> filteredList = new ArrayList<>();

            for (FilterBrand filterBrand : arrayList) {
                if (filterBrand.getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(filterBrand);
                }
            }

            adapter.filterList(filteredList);
            adapter.notifyDataSetChanged();
        }
    }

    private void getCarBrands() {
        WebApiRequest.Instance(context).request(
                AppConstants.WebServicesKeys.GET_CAR_BRANDS,
                binding.getRoot(),
                null,
                null,
                null,
                new WebApiRequest.WebServiceArrayResponse() {
                    @Override
                    public void onSuccess(ApiArrayResponse apiArrayResponse) {
                        arrayList = new ArrayList<>();
                        arrayList = JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), FilterBrand.class);
                        if (arrayList.size() == 0) {
//                            binding.tvNoBrandMessage.setVisibility(View.VISIBLE);
                            binding.recyclerView.setVisibility(View.GONE);
                        }
                        if (arrayList.size() > 0) {
                            if (filter.getFilterBrand() != null) {
                                for (int i = 0; i < arrayList.size(); i++) {
                                    if (arrayList.get(i).getId() == filter.getFilterBrand().getId()) {
                                        arrayList.get(i).setSelected(true);
                                        arrayList.get(i).setModels(filter.getFilterBrand().getModels());
                                    }
                                }
                            }

                            adapter.addAll(arrayList);
                            adapter.notifyDataSetChanged();
//                            binding.tvNoBrandMessage.setVisibility(View.GONE);
                            binding.recyclerView.setVisibility(View.VISIBLE);
                        }
                        hideLoader();
                    }

                    @Override
                    public void onError() {
                        hideLoader();
                    }
                }
        );
    }

    public LuxuryMarketSearchFilter getFilter() {
        return filter;
    }
}
