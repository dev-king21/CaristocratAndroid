package com.ingic.caristocrat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gjiazhe.wavesidebar.WaveSideBar;
import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.BrandsListFilterAdapter;
import com.ingic.caristocrat.adapters.SectionAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentLuxuryMarketBrandsFilterBinding;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.LuxuryMarketSearchFilter;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.models.AlphabetItem;
import com.ingic.caristocrat.models.FilterBrand;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class LuxuryMarketBrandsFilterFragment extends BaseFragment implements View.OnClickListener, TextWatcher {
    private FragmentLuxuryMarketBrandsFilterBinding binding;
    private SectionedRecyclerViewAdapter sectionAdapter;
    private ArrayList<FilterBrand> arrayList;
    private BrandsListFilterAdapter adapter;
    private boolean isSearchEnabled = false;
    private List<AlphabetItem> mAlphabetItems;

    public LuxuryMarketBrandsFilterFragment() {
    }

    public static LuxuryMarketBrandsFilterFragment Instance() {
        return new LuxuryMarketBrandsFilterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_luxury_market_brands_filter, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sectionAdapter = new SectionedRecyclerViewAdapter();

        adapter = new BrandsListFilterAdapter(null, null, null, null);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(mainActivityContext, 2));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setAdapter(adapter);
//        adapter.addAll(items);
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
        if (mainActivityContext.showLoader()) {
            getCarBrands();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mainActivityContext.getBinding().btnFilterAction.setVisibility(View.GONE);
        mainActivityContext.getBinding().llCommentsLayout.setVisibility(View.GONE);
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(mainActivityContext);
        titlebar.showTitlebar(mainActivityContext);
        if(isSearchEnabled){
            titlebar.showSearch().addTextChangedListener(this);
        }else{
            titlebar.showSearchButton(mainActivityContext).setOnClickListener(this);
            titlebar.setTitle(mainActivityContext.getResources().getString(R.string.brands));
        }
        titlebar.showBackButton(mainActivityContext, false).setOnClickListener(this);

        mainActivityContext.getBinding().btnFilterAction.setVisibility(View.VISIBLE);
        mainActivityContext.getBinding().btnFilterAction.setText(mainActivityContext.getResources().getString(R.string.close));
        mainActivityContext.getBinding().btnFilterAction.setOnClickListener(this);
        mainActivityContext.getBinding().llCommentsLayout.setVisibility(View.VISIBLE);
        mainActivityContext.getBinding().nestedscroll.setNestedScrollingEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibBackbtn:
                backBtn();
                break;

            case R.id.ibSearch:
                search();
                break;

            case R.id.btnFilterAction:
                close();
                break;
        }
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

    private void close() {
        mainActivityContext.onBackPressed();
    }

    private void search(){
        if(adapter.getItemCount() == 0){
            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.no_brands_search), Toast.LENGTH_SHORT);
        }
        if(adapter.getItemCount() > 0){
            isSearchEnabled = true;
            setTitlebar(mainActivityContext.getTitlebar());
        }
    }

    private void backBtn(){
        UIHelper.hideSoftKeyboard(mainActivityContext);
        if(isSearchEnabled){
            isSearchEnabled = false;
            setTitlebar(mainActivityContext.getTitlebar());
        }else{
            LuxuryMarketSearchFilter.getInstance().resetBrandsList();
            mainActivityContext.onBackPressed();
        }
    }

    private void filter(String text) {
        if(text.length() == 0){
            adapter.addAll(arrayList);
            adapter.notifyDataSetChanged();
        }else{
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
        WebApiRequest.Instance(mainActivityContext).request(
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
                        if(arrayList.size() == 0){
                            binding.tvNoBrandMessage.setVisibility(View.VISIBLE);
                            binding.recyclerView.setVisibility(View.GONE);
                        }
                        if (arrayList.size() > 0) {
                            initialiseData();
                            adapter.addAll(arrayList);
                            adapter.notifyDataSetChanged();
                            binding.tvNoBrandMessage.setVisibility(View.GONE);
                            binding.recyclerView.setVisibility(View.VISIBLE);
                        }
                        mainActivityContext.hideLoader();
                    }

                    @Override
                    public void onError() {
                        mainActivityContext.hideLoader();
                    }
                }
        );
    }

    //Library Code
    protected void initialiseData() {
        //Alphabet fast scroller data
        mAlphabetItems = new ArrayList<>();
        List<String> strAlphabets = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            String name = arrayList.get(i).getName();
            if (name == null || name.trim().isEmpty())
                continue;

            String word = name.substring(0, 1);
            if (!strAlphabets.contains(word)) {
                strAlphabets.add(word);
                mAlphabetItems.add(new AlphabetItem(i, word, false));
            }
        }
    }

    private void initRecycler() {
        char fletter;
        int i = 0;
        ArrayList<FilterBrand> tempList = new ArrayList<>();
        for (i = 0; i < arrayList.size(); i++) {
            tempList = new ArrayList<>();
            fletter = arrayList.get(i).getName().toUpperCase().toCharArray()[0];
            tempList.add(arrayList.get(i));
            for (int j = i + 1; j < arrayList.size(); j++) {
                if (fletter == arrayList.get(j).getName().toCharArray()[0]) {
                    tempList.add(arrayList.get(j));
                } else {
                    i = j;
                    break;
                }
            }

            sectionAdapter.addSection(new SectionAdapter(mainActivityContext, fletter, tempList));

            GridLayoutManager glm = new GridLayoutManager(getContext(), 2);
            glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (sectionAdapter.getSectionItemViewType(position)) {
                        case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                            return 2;
                        default:
                            return 1;
                    }
                }
            });
            binding.recyclerView.setLayoutManager(glm);
            binding.recyclerView.setAdapter(sectionAdapter);
        }
    }
}
