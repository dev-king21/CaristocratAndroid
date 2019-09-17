package com.ingic.caristocrat.fragments;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.CompareCarsAdapter;
import com.ingic.caristocrat.adapters.SelectedCarsAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentCompareAnyCarBinding;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.LuxuryMarketSearchFilter;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.helpers.Utils;
import com.ingic.caristocrat.interfaces.DataNotFoundListener;
import com.ingic.caristocrat.models.TradeCar;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("ValidFragment")
public class CompareAnyCarFragment extends BaseFragment implements DataNotFoundListener, View.OnClickListener {
    FragmentCompareAnyCarBinding binding;
    CompareCarsAdapter compareCarsAdapter;
    SelectedCarsAdapter selectedCarsAdapter;
    ArrayList<TradeCar> cars = new ArrayList<>();
    LuxuryMarketSearchFilter filter;
//    CompareCarAutoCompleteAdapter compareCarAutoCompleteAdapter;

    @SuppressLint("ValidFragment")
    public CompareAnyCarFragment(LuxuryMarketSearchFilter filter) {
        this.filter = filter;
    }

    public CompareAnyCarFragment Instance(LuxuryMarketSearchFilter filter) {
        return new CompareAnyCarFragment(filter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare_any_car, container, false);
        initAdapter();
        return binding.getRoot();

    }

    private void initAdapter() {

        selectedCarsAdapter = new SelectedCarsAdapter(mainActivityContext, ((selectedCarsAdapter != null && selectedCarsAdapter.getArrayList() != null && selectedCarsAdapter.getArrayList().size() > 0) ? selectedCarsAdapter.getArrayList() : new ArrayList<>()));
        binding.rvSelectedCarList.setLayoutManager(new LinearLayoutManager(mainActivityContext, LinearLayoutManager.HORIZONTAL, false));
        selectedCarsAdapter.setMlistener(this);
        binding.rvSelectedCarList.setAdapter(selectedCarsAdapter);
        compareCarsAdapter = new CompareCarsAdapter(mainActivityContext, ((compareCarsAdapter != null && compareCarsAdapter.getArrayList() != null && compareCarsAdapter.getArrayList().size() > 0) ? compareCarsAdapter.getArrayList() : new ArrayList<>()), selectedCarsAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mainActivityContext);
        binding.rvCarList.setLayoutManager(layoutManager);
        binding.rvCarList.setAdapter(compareCarsAdapter);
        binding.btnCompare.setOnClickListener(this);
        binding.ivSearch.setOnClickListener(this);
        hideShowData();
        binding.etSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.etSearch.getText().toString().length() > 2 && Utils.isNetworkAvailable(mainActivityContext))
                    getCarsByName(AppConstants.WebServicesKeys.LUXURY_MARKET_CATEGORIES, binding.etSearch.getText().toString());
                return true;
            }
            return false;

        });
        //        compareCarAutoCompleteAdapter = new CompareCarAutoCompleteAdapter(mainActivityContext, R.layout.item_auto_complete, new ArrayList<>());
//        binding.etSearch.setThreshold(1);
//        binding.etSearch.setAdapter(compareCarAutoCompleteAdapter);

//        compareCarAutoCompleteAdapter.setAutoCompleteItemClickListener(((entity, pos, view) -> {
//            compareCarAutoCompleteAdapter.notifyDataSetInvalidated();
//            binding.etSearch.setText("");
//            UIHelper.hideSoftKeyboard(mainActivityContext);
//            addCar(entity);
//
//        }));

    }


//    private void searchwatcher() {
//        binding.etSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.length() > 2 && Utils.isNetworkAvailable(mainActivityContext))
//                    getCarsByName(28, charSequence.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//    }

    private void hideShowData() {
        if (compareCarsAdapter.getItemCount() > 0) {
            binding.rvCarList.setVisibility(View.VISIBLE);
            binding.noDataLayout.setVisibility(View.GONE);
        } else {
            binding.rvCarList.setVisibility(View.GONE);
            binding.noDataLayout.setVisibility(View.VISIBLE);
        }

        if (selectedCarsAdapter.getItemCount() > 0) {
            binding.rvSelectedCarList.setVisibility(View.VISIBLE);
            binding.tvNoSelectedCar.setVisibility(View.GONE);
        } else {
            binding.rvSelectedCarList.setVisibility(View.GONE);
            binding.tvNoSelectedCar.setVisibility(View.VISIBLE);
        }
    }


    private void getCarsByName(int id, String keyword) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("category_id", id);
        params.put("model_name", keyword);

        if (filter.isFilterApply()) {
            //Set Selected Brands in Filter
            if (filter.getBrandsList().size() > 0) {
                String brandsIds = "", modelsIds = "";
                for (int pos = 0; pos < filter.getBrandsList().size(); pos++) {
                    if (pos == filter.getBrandsList().size() - 1) {
                        brandsIds += filter.getBrandsList().get(pos).getId();
                    } else {
                        brandsIds += filter.getBrandsList().get(pos).getId() + ",";
                    }

                    if (filter.getBrandsList().get(pos).getModels() != null) {
                        if (filter.getBrandsList().get(pos).getModels().size() > 0) {
                            for (int modelPos = 0; modelPos < filter.getBrandsList().get(pos).getModels().size(); modelPos++) {
                                modelsIds += filter.getBrandsList().get(pos).getModels().get(modelPos).getId() + ",";
                            }
                        }
                    }
                }
                if (modelsIds.length() > 0) {
                    if (modelsIds.charAt(modelsIds.length() - 1) == ',') {
                        modelsIds = modelsIds.substring(0, modelsIds.length() - 1);
                    }
                }
                params.put("model_ids", modelsIds);
            }

            //Set Version name in filter
            if (filter.getVersionName() != null) {
                params.put("version", filter.getVersionName());
            }

            //Set Min Price in Filter
            if (filter.getMinPrice() != null) {
                params.put("min_price", filter.getMinPrice());
            }

            //Set Max Price in Filter
            if (filter.getMaxPrice() != null) {
                params.put("max_price", filter.getMaxPrice());
            }

            //Set Body Styles in Filter
            if (filter.getCarBodyStyles().size() > 0) {
                boolean isSelected = false;
                String carBodyTypeIds = "";
                for (int pos = 0; pos < filter.getCarBodyStyles().size(); pos++) {
                    if (filter.getCarBodyStyles().get(pos).isSelected()) {
                        isSelected = true;
                        if (pos == filter.getCarBodyStyles().size() - 1) {
                            carBodyTypeIds += filter.getCarBodyStyles().get(pos).getId();
                        } else {
                            carBodyTypeIds += filter.getCarBodyStyles().get(pos).getId() + ",";
                        }
                    }
                }
                if (isSelected) {
                    params.put("car_type", carBodyTypeIds);
                }
            }

            if (filter.getRating() >= 0) {
                params.put("rating", filter.getRating());
            }
        }

        mainActivityContext.showLoader();
        WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.LUXURY_MARKET_CATEGORIES, null, null, params, null, new WebApiRequest.WebServiceArrayResponse() {
            @Override
            public void onSuccess(ApiArrayResponse apiArrayResponse) {
                UIHelper.hideSoftKeyboard(mainActivityContext);
                cars.clear();
                compareCarsAdapter.clearAll();
                cars.addAll((ArrayList<TradeCar>) JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), TradeCar.class));
                if (cars.size() > 0) {

                    compareCarsAdapter.addAll(cars);

                }
                hideShowData();
                mainActivityContext.hideLoader();
            }

            @Override
            public void onError() {
                hideShowData();
                mainActivityContext.hideLoader();

            }
        });
    }

    private void compareCar(String ids) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("category_id", AppConstants.WebServicesKeys.LUXURY_MARKET_CATEGORIES);
        params.put("car_ids", ids);
        WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.LUXURY_MARKET_CATEGORIES, null, null, params, null, new WebApiRequest.WebServiceArrayResponse() {
            @Override
            public void onSuccess(ApiArrayResponse apiArrayResponse) {
                cars.clear();
                cars.addAll((ArrayList<TradeCar>) JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), TradeCar.class));
                if (cars.size() > 0) {
                    CompareResultFragment compareResultFragment = new CompareResultFragment();
                    compareResultFragment.setTradeCars(cars);
                    mainActivityContext.replaceFragment(compareResultFragment, CompareResultFragment.class.getSimpleName(), true, true);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {

    }

    @Override
    public void isDataFound(boolean flag) {
        if (flag) {
            binding.rvSelectedCarList.setVisibility(View.VISIBLE);
            binding.tvNoSelectedCar.setVisibility(View.GONE);
        } else {
            binding.rvSelectedCarList.setVisibility(View.GONE);
            binding.tvNoSelectedCar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCompare:
                disableViewsForSomeSeconds(binding.btnCompare);
                if (selectedCarsAdapter.getItemCount() > 1) {
                    String carIds = "";
                    for (int i = 0; i < selectedCarsAdapter.getItemCount(); i++) {
                        carIds += selectedCarsAdapter.getArrayList().get(i).getId() + ",";
                    }
                    compareCar(carIds.endsWith(",") ? carIds.substring(0, carIds.length() - 1) : carIds);

                } else {
                    if (selectedCarsAdapter.getItemCount() == 0) {
                        UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.there_is_no_data_to_compare), Toast.LENGTH_SHORT);
                    } else {
                        UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.comparision_with_one_car), Toast.LENGTH_SHORT);
                    }

                }
                break;

            case R.id.ivSearch:
                if (binding.etSearch.getText().toString().length() >= 2 && Utils.isNetworkAvailable(mainActivityContext))
                    getCarsByName(AppConstants.WebServicesKeys.LUXURY_MARKET_CATEGORIES, binding.etSearch.getText().toString());
                break;
        }
    }
}
