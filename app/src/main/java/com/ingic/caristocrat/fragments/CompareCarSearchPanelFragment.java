package com.ingic.caristocrat.fragments;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.CompareCarSearchPanelAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentCompareCarSearchPanelBinding;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.LuxuryMarketSearchFilter;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.models.CompareCarPanel;
import com.ingic.caristocrat.models.FilterBrand;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class CompareCarSearchPanelFragment extends BaseFragment implements View.OnClickListener {
    FragmentCompareCarSearchPanelBinding binding;
    private CompareCarSearchPanelAdapter adapter;
    private ArrayList<CompareCarPanel> arrayList = new ArrayList<>();
    private ArrayList<FilterBrand> brands;
    private GridLayoutManager gridLayoutManager;
    private LuxuryMarketSearchFilter filter;
    private UIHelper.SpacesItemDecorationAllSideEqual spacesItemDecorationAllSideEqual;
    private int spacingInPixels;

    public CompareCarSearchPanelFragment(LuxuryMarketSearchFilter filter) {
        this.filter = filter;
    }

    public CompareCarSearchPanelFragment Instance(LuxuryMarketSearchFilter filter) {
        return new CompareCarSearchPanelFragment(filter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CompareCarPanel compareCarPanel = new CompareCarPanel();
        compareCarPanel.setMoreCar(false);

        arrayList.add(compareCarPanel);

        compareCarPanel = new CompareCarPanel();
        compareCarPanel.setMoreCar(false);

        arrayList.add(compareCarPanel);

        compareCarPanel = new CompareCarPanel();
        compareCarPanel.setMoreCar(true);

        arrayList.add(compareCarPanel);

        spacingInPixels = mainActivityContext.getResources().getDimensionPixelSize(R.dimen.dp8);
        spacesItemDecorationAllSideEqual = new UIHelper.SpacesItemDecorationAllSideEqual(spacingInPixels);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare_car_search_panel, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (adapter == null) {
            adapter = new CompareCarSearchPanelAdapter(mainActivityContext, this, filter);
            gridLayoutManager = new GridLayoutManager(mainActivityContext, 2);
            binding.recyclerview.addItemDecoration(spacesItemDecorationAllSideEqual);
            binding.recyclerview.setLayoutManager(gridLayoutManager);
            binding.recyclerview.setAdapter(adapter);
            adapter.addAll(arrayList);
            adapter.notifyDataSetChanged();
        } else {
            gridLayoutManager = new GridLayoutManager(mainActivityContext, 2);
            binding.recyclerview.addItemDecoration(spacesItemDecorationAllSideEqual);
            binding.recyclerview.setLayoutManager(gridLayoutManager);
            binding.recyclerview.setAdapter(adapter);
        }

        if (brands == null) {
            getCarBrands();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mainActivityContext.getBinding().btnFilterAction.setVisibility(View.GONE);
        mainActivityContext.getBinding().btnFilterAction.setText(mainActivityContext.getResources().getString(R.string.apply));
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        mainActivityContext.getBinding().btnFilterAction.setVisibility(View.VISIBLE);
        mainActivityContext.getBinding().btnFilterAction.setText(mainActivityContext.getResources().getString(R.string.compare_cars));
        mainActivityContext.getBinding().btnFilterAction.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFilterAction:
                compareCars();
                break;
        }
    }

    public void compareCars() {
        if (adapter.getSelectedCars().size() >= 2) {
            CompareResultFragment compareResultFragment = new CompareResultFragment();
            compareResultFragment.setTradeCars(adapter.getSelectedCars());
            compareResultFragment.setFromSearchPanel(true);
            mainActivityContext.addFragment(compareResultFragment, CompareResultFragment.class.getSimpleName(), true, true);
        } else {
            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.select_2_car_compare), Toast.LENGTH_LONG);
        }
    }

    public ArrayList<FilterBrand> getBrands() {
        return brands;
    }

    private void getCarBrands() {
        if (mainActivityContext.showLoader()) {
            WebApiRequest.Instance(mainActivityContext).request(
                    AppConstants.WebServicesKeys.GET_CAR_BRANDS,
                    null,
                    null,
                    null,
                    null,
                    new WebApiRequest.WebServiceArrayResponse() {
                        @Override
                        public void onSuccess(ApiArrayResponse apiArrayResponse) {
                            brands = new ArrayList<>();
                            brands = JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), FilterBrand.class);
                            mainActivityContext.hideLoader();
                        }

                        @Override
                        public void onError() {
                            mainActivityContext.hideLoader();
                        }
                    }
            );
        }
    }
}
