package com.ingic.caristocrat.fragments;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.SegmentMainWiseAdapter;
import com.ingic.caristocrat.adapters.SegmentedCarCategoryAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentCompareSegmentWiseBinding;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.LuxuryMarketSearchFilter;
import com.ingic.caristocrat.helpers.RecyclerTouchListener;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.ClickListenerRecycler;
import com.ingic.caristocrat.jsonListener.GetJSONListener;
import com.ingic.caristocrat.jsonListener.JSONClient;
import com.ingic.caristocrat.models.TradeCar;
import com.ingic.caristocrat.telr.TelrUtils;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.models.CarBodyStyle;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("ValidFragment")
public class CompareSegmentMainWiseFragment extends BaseFragment {
    FragmentCompareSegmentWiseBinding binding;
    SegmentedCarCategoryAdapter segmentedCarCategoryAdapter;
    SegmentMainWiseAdapter segmentSubWiseAdapter;
    ArrayList<CarBodyStyle> arrayList;
    ArrayList<String> carModels = new ArrayList<>();
    LuxuryMarketSearchFilter filter;
    private int lastExpandedPosition = -1;
    String comparisionFee = "";
    boolean isSubscribed = false;
    public static boolean payment = false;

    @SuppressLint("ValidFragment")
    public CompareSegmentMainWiseFragment(LuxuryMarketSearchFilter filter) {
        this.filter = filter;
    }

    public static CompareSegmentMainWiseFragment Instance(LuxuryMarketSearchFilter filter) {
        return new CompareSegmentMainWiseFragment(filter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare_segment_wise, container, false);
        initAdapter();
        return binding.getRoot();
    }

    private void initAdapter() {

        segmentSubWiseAdapter = new SegmentMainWiseAdapter(mainActivityContext);
        binding.listMenu.setAdapter(segmentSubWiseAdapter);
        binding.listMenu.setOnItemClickListener((parent, v, position, id) -> {
            if (arrayList.get(position).getChildTypes().size() == 0)
                compareCar(String.valueOf(arrayList.get(position).getId()), false, arrayList.get(position).getName());
            else
                showChildTypes(arrayList.get(position));
        });

        segmentedCarCategoryAdapter = new SegmentedCarCategoryAdapter(mainActivityContext, new ArrayList<>());
        binding.rvSegmentedCar.setLayoutManager(new LinearLayoutManager(mainActivityContext));
        binding.rvSegmentedCar.setAdapter(segmentedCarCategoryAdapter);
        if (arrayList == null) {
            getCarTypes();
        } else {
            if (arrayList.size() > 0) {
                segmentSubWiseAdapter.addAll(arrayList);
                segmentSubWiseAdapter.notifyDataSetChanged();
            }
        }
        binding.rvSegmentedCar.addOnItemTouchListener(new RecyclerTouchListener(mainActivityContext, binding.rvSegmentedCar, new ClickListenerRecycler() {
            @Override
            public void onClick(View view, int position) {
                if (arrayList.get(position).getChildTypes().size() > 0) {
                    SegmentedCarSubCategoryFragment segmentedCarSubCategoryFragment = new SegmentedCarSubCategoryFragment();
                    segmentedCarSubCategoryFragment.setCar(arrayList.get(position));
                    mainActivityContext.replaceFragment(segmentedCarSubCategoryFragment, SegmentedCarSubCategoryFragment.class.getSimpleName(), true, true);
                } else {
                    compareCar(String.valueOf(arrayList.get(position).getId()), false, arrayList.get(position).getName());
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    public void visibleView() {
        binding.rvSegmentedCar.setVisibility(View.VISIBLE);
        binding.noDataLayout.setVisibility(View.GONE);

    }

    public void hideView() {
        binding.rvSegmentedCar.setVisibility(View.GONE);
        binding.noDataLayout.setVisibility(View.VISIBLE);
    }

    private void showChildTypes(CarBodyStyle carStyle) {
        CompareSegmentSubWiseFragment compareSegmentSubWiseFragment = new CompareSegmentSubWiseFragment(LuxuryMarketSearchFilter.getInstance(), carStyle);
        compareSegmentSubWiseFragment.setTitle("Back to Comparison Main");
        mainActivityContext.replaceFragment(compareSegmentSubWiseFragment, CompareSegmentSubWiseFragment.class.getSimpleName(), true, true);
    }

    private void compareCar(String carType, boolean isChild, String title) {
        if (mainActivityContext.showLoader()) {
            HashMap<String, Object> params = new HashMap<>();
            if (!isChild) {
                params.put("car_type", carType);
            } else {
                params.put("car_sub_type", carType);
            }
            params.put("category_id", AppConstants.WebServicesKeys.LUXURY_MARKET_CATEGORIES);
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

                if (filter.getRating() >= 0) {
                    params.put("rating", filter.getRating());
                }
            }

            params.put("service_type", 1);

            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.GET_CARS, null, null, params, null, new WebApiRequest.WebServiceArrayResponse() {
                @Override
                public void onSuccess(ApiArrayResponse apiArrayResponse) {
                    ArrayList<TradeCar> segmentedCar = new ArrayList<>();
                    segmentedCar.addAll((ArrayList<TradeCar>) JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), TradeCar.class));
                    if (segmentedCar.size() > 0) {
                        if (segmentedCar.size() > 1) {
                            CompareResultFragment compareResultFragment = new CompareResultFragment();
                            compareResultFragment.setTradeCars(segmentedCar);
                            compareResultFragment.setTitle(title);
                            mainActivityContext.replaceFragment(compareResultFragment, CompareResultFragment.class.getSimpleName(), false, true);
                        } else {
                            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.there_is_no_enough_car_to_compare), Toast.LENGTH_SHORT);

                        }
                    } else {
                        UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.there_is_no_data_to_compare), Toast.LENGTH_SHORT);

                    }
                    mainActivityContext.hideLoader();
                }

                @Override
                public void onError() {
                    mainActivityContext.hideLoader();
                }
            });
        }
    }

    private void getCarTypes() {
        if (mainActivityContext.internetConnected()) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("parent_id", 0);
            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.GET_CAR_BODY_TYPES, null, null, params, null, new WebApiRequest.WebServiceArrayResponse() {
                @Override
                public void onSuccess(ApiArrayResponse apiArrayResponse) {
                    arrayList = new ArrayList<>();
                    arrayList = JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), CarBodyStyle.class);
                    if (arrayList.size() > 0) {
                        segmentSubWiseAdapter.addAll(arrayList);
                        segmentSubWiseAdapter.notifyDataSetChanged();
                    }
                    mainActivityContext.hideLoader();
                }

                @Override
                public void onError() {
                    if (segmentedCarCategoryAdapter.getItemCount() == 0) {
                        hideView();
                    } else {
                        visibleView();
                    }
                    mainActivityContext.hideLoader();

                }
            });
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        //titlebar.hideTitlebar();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (payment) {
            isSubscribed = true;
            payment = false;
        }

        if (preferenceHelper.getLoginStatus())
            checkProComparisonSub();
    }

    private void checkProComparisonSub() {
        String url = AppConstants.BASE_URL + AppConstants.WebServices.CHECK_PRO_COMPARISON_SUB + preferenceHelper.getUser().getId();
        JSONClient client = new JSONClient(getActivity(), new GetJSONListener() {
            @Override
            public void onRemoteCallComplete(String jsonFromNet) {
                Log.i("response--->", jsonFromNet);
                try {
                    JSONObject jObject = new JSONObject(jsonFromNet);
                    if (jObject.optBoolean("success")) {
                        //subscribed
                        isSubscribed = true;
                    } else {
                        //Not Subscribed
                        isSubscribed = false;
                    }
                    JSONObject fee = jObject.optJSONObject("proComparisonFee");
                    comparisionFee = fee.optString("amount", "");
                    if(MainDetailPageFragment.login)
                    {
                        TelrUtils.IntentTelr(comparisionFee, getActivity(), preferenceHelper.getUser());
                        payment = true;
                        MainDetailPageFragment.login = false;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Get", null);
        client.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);


    }
}
