package com.ingic.caristocrat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.SegmentedCarCategoryAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentSegmentedSubCategoryBinding;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.RecyclerTouchListener;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.ClickListenerRecycler;
import com.ingic.caristocrat.models.TradeCar;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.models.CarBodyStyle;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.ArrayList;
import java.util.HashMap;

/**
 */
public class SegmentedCarSubCategoryFragment extends BaseFragment {
    FragmentSegmentedSubCategoryBinding binding;
    SegmentedCarCategoryAdapter segmentedCarCategoryAdapter;
    Titlebar mTitleBar;
   CarBodyStyle car;

    public SegmentedCarSubCategoryFragment() {

    }

    public static SegmentedCarSubCategoryFragment Instance() {
        return new SegmentedCarSubCategoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_segmented_sub_category, container, false);
        initAdapter();
        return binding.getRoot();
    }

    private void initAdapter() {
        segmentedCarCategoryAdapter = new SegmentedCarCategoryAdapter(mainActivityContext, car.getChildTypes());
        binding.rvSegmentedCarSubCategory.setLayoutManager(new LinearLayoutManager(mainActivityContext));
        binding.rvSegmentedCarSubCategory.setAdapter(segmentedCarCategoryAdapter);
        binding.rvSegmentedCarSubCategory.addOnItemTouchListener(new RecyclerTouchListener(mainActivityContext, binding.rvSegmentedCarSubCategory, new ClickListenerRecycler() {
            @Override
            public void onClick(View view, int position) {
             compareCar(String.valueOf(car.getId()));
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setCar(CarBodyStyle car) {
        this.car = car;

    }

    private void compareCar(String carType) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("car_type", carType);
        params.put("category_id", 28);
        WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.LUXURY_MARKET_CATEGORIES, null, null, params, null, new WebApiRequest.WebServiceArrayResponse() {
            @Override
            public void onSuccess(ApiArrayResponse apiArrayResponse) {
                ArrayList<TradeCar> segmentedCar=new ArrayList<>();
                segmentedCar.addAll((ArrayList<TradeCar>) JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), TradeCar.class));
                if (segmentedCar.size() > 0) {
                   if(segmentedCar.size()>1) {
                       CompareResultFragment compareResultFragment = new CompareResultFragment();
                       compareResultFragment.setTradeCars(segmentedCar);
                       mainActivityContext.replaceFragment(compareResultFragment, CompareResultFragment.class.getSimpleName(), true, true);
                   }
                   else {
                       UIHelper.showToast(mainActivityContext, getString(R.string.there_is_no_enough_car_to_compare), Toast.LENGTH_SHORT);
                   }
                }
                else{
                    UIHelper.showToast(mainActivityContext, getString(R.string.there_is_no_data_to_compare), Toast.LENGTH_SHORT);

                }
                mainActivityContext.hideLoader();
            }

            @Override
            public void onError() {

                mainActivityContext.hideLoader();

            }
        });
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(mainActivityContext);
        titlebar.showBackButton(mainActivityContext, false);
        titlebar.setTitle(car.getName());
         mTitleBar = titlebar;
    }
}
