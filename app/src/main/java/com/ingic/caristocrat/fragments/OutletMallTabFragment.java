package com.ingic.caristocrat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.OutletMallTabAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentOutletMallTabBinding;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.LuxuryMarketSearchFilter;
import com.ingic.caristocrat.helpers.RecyclerTouchListener;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.ClickListenerRecycler;
import com.ingic.caristocrat.interfaces.SearchByKeywordListener;
import com.ingic.caristocrat.interfaces.SortTypeChangeListener;
import com.ingic.caristocrat.interfaces.TextChangedListener;
import com.ingic.caristocrat.models.TradeCar;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 */
public class OutletMallTabFragment extends BaseFragment implements SortTypeChangeListener, TextChangedListener, SearchByKeywordListener {
    FragmentOutletMallTabBinding binding;
    OutletMallTabAdapter outletMallTabAdapter;
    int item_id, mostViewed, sortType;
    ArrayList<TradeCar> luxuryCategories, featuredCars, nonFeaturedCars;
    LuxuryMarketSearchFilter filter;
    String categoryKey, keyword;
    OutletMallFragment outletMallFragment;
    boolean allCars;
    LinearLayoutManager linearLayoutManager;
    int LIMIT = 50, OFFSET = 0, TOTAL_RECORDS = 0;
    boolean ON_CALL = false;

    public OutletMallTabFragment() {
    }

    public static OutletMallTabFragment Instance(int item_id, int mostViewed, String categoryKey, OutletMallFragment outletMallFragment) {
        OutletMallTabFragment outletMallTabFragment = new OutletMallTabFragment();
        outletMallTabFragment.item_id = item_id;
        outletMallTabFragment.mostViewed = mostViewed;
        outletMallTabFragment.categoryKey = categoryKey;
        outletMallTabFragment.outletMallFragment = outletMallFragment;
        return outletMallTabFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_outlet_mall_tab, container, false);

        featuredCars = new ArrayList<>();
        nonFeaturedCars = new ArrayList<>();

        filter = LuxuryMarketSearchFilter.getInstance();

        if (outletMallFragment != null) {
            outletMallFragment.setSortTypeChangeListener(this);
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (categoryKey != null) {
            if (categoryKey.equals(AppConstants.MainCategoriesType.LUXURY_NEW_CARS)) {
                binding.tvOutletMallHeader.setText(mainActivityContext.getResources().getString(R.string.specifications_prices));
            } else if (categoryKey.equals(AppConstants.MainCategoriesType.THE_OUTLET_MALL)) {
                binding.tvOutletMallHeader.setText(mainActivityContext.getResources().getString(R.string.best_deals_look_for));
            } else if (categoryKey.equals(AppConstants.MainCategoriesType.APPROVED_PRE_OWNED)) {
                binding.tvOutletMallHeader.setText(mainActivityContext.getResources().getString(R.string.looking_peace_mind));
            } else if (categoryKey.equals(AppConstants.MainCategoriesType.CLASSIC_CARS)) {
                binding.tvOutletMallHeader.setText(mainActivityContext.getResources().getString(R.string.oldiest_goldies));
            } else if (categoryKey.equals(AppConstants.MainCategoriesType.REVIEWS) && mostViewed == AppConstants.NOT_MOST_VIEWED) {
                binding.tvOutletMallHeader.setText(mainActivityContext.getResources().getString(R.string.latest_car_reviews_ratings));
            } else if (categoryKey.equals(AppConstants.MainCategoriesType.REVIEWS) && mostViewed == AppConstants.MOST_VIEWED) {
                binding.tvOutletMallHeader.setText(mainActivityContext.getResources().getString(R.string.most_popular_car_reviews));
            }
        }
        initializeAdapter();
        if (mainActivityContext.showLoader()) {
            getLuxuryMarketCategories(item_id);
        }
/*
        if (luxuryCategories == null) {
        } else {
            if (luxuryCategories.size() > 0) {
                if (outletMallTabAdapter != null) {
                    outletMallTabAdapter.addAll(luxuryCategories);
                    if (outletMallTabAdapter.getItemCount() > 0)
                        visibleView();
                    else
                        hideView();
                }
                if (filter.isFilterApply()) {
                    if (mainActivityContext.showLoader()) {
                        getLuxuryMarketCategories(item_id);
                    }
                }
            }
        }
*/
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefresh.setRefreshing(false);
                if (mainActivityContext.showLoader()) {
                    getLuxuryMarketCategories(item_id);
                }
            }
        });

//        binding.swipeRefresh.setColorSchemeColors(ContextCompat.getColor(mainActivityContext,R.color.colorBlack));
//        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (mainActivityContext.showLoader()) {
//                    getLuxuryMarketCategories(item_id);
//                }
//                binding.swipeRefresh.setRefreshing(false);
//            }
//        });
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {

    }

    @Override
    public void onSortTypeChanged(int sortType) {
        this.sortType = sortType;
        if (mainActivityContext.showLoader()) {
            OFFSET = 0;
            getLuxuryMarketCategories(item_id);
        }
//        setsortedAdapter(sortType);
//        if (allCars) {
//        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (luxuryCategories != null) {
            if (s.toString().length() > 0) {
                filter(s.toString());
            } else {
                outletMallTabAdapter.addAll(luxuryCategories);
                outletMallTabAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onSearchActionDone(String keyword, boolean cancel) {
        UIHelper.hideSoftKeyboard(mainActivityContext);
        if (cancel) {
            this.keyword = null;
        } else {
            this.keyword = keyword;
        }
        OFFSET = 0;
        if (mainActivityContext.showLoader()) {
            getLuxuryMarketCategories(item_id);
        }
    }

    private void filter(String text) {
        if (luxuryCategories != null) {
            ArrayList<TradeCar> filteredList = new ArrayList<>();

            for (TradeCar tradeCar : luxuryCategories) {
                if (tradeCar.getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(tradeCar);
                }
            }

            outletMallTabAdapter.filterList(filteredList);
            outletMallTabAdapter.notifyDataSetChanged();
        }
    }

    private void getLuxuryMarketCategories(int item_id) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("category_id", item_id);
        if (mostViewed == AppConstants.MOST_VIEWED) {
            params.put("most_viewed", mostViewed);
        }

        if (filter.isFilterApply()) {
            //Set Selected Brands in Filter

            if (filter.getFilterBrand() != null) {
                params.put("brand_ids", filter.getFilterBrand().getId());

                if (filter.getFilterBrand().getModels() != null) {
                    String modelsIds = "", versionIds = "";
                    for (int pos = 0; pos < filter.getFilterBrand().getModels().size(); pos++) {
                        modelsIds += filter.getFilterBrand().getModels().get(pos).getId() + ",";

                        if (filter.getFilterBrand().getModels().get(pos).getVersions() != null) {
                            for (int verPos = 0; verPos < filter.getFilterBrand().getModels().get(pos).getVersions().size(); verPos++) {
                                versionIds += filter.getFilterBrand().getModels().get(pos).getVersions().get(verPos).getId() + ",";
                            }
                        }
                    }
                    if (versionIds.length() > 0) {
                        params.put("version_id", versionIds.substring(0, versionIds.length() - 1));
                    } else if (modelsIds.length() > 0) {
                        params.put("model_ids", modelsIds.substring(0, modelsIds.length() - 1));
                    }
                }
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

            //Set Min Year in Filter
            if (filter.getMinyear() != null) {
                params.put("min_year", filter.getMinyear());
            }

            //Set Max Year in Filter
            if (filter.getMaxYear() != null) {
                params.put("max_year", filter.getMaxYear());
            }

            //Set Min Mileage in Filter
            if (filter.getMinMileage() != null) {
                params.put("min_mileage", filter.getMinMileage());
            }

            //Set Max Mileage in Filter
            if (filter.getMaxMileage() != null) {
                params.put("max_mileage", filter.getMaxMileage());
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

            if (filter.getSelectedRegions().size() > 0) {
                String regionsIds = "";
                for (int pos = 0; pos < filter.getSelectedRegions().size(); pos++) {
                    if (pos == filter.getSelectedRegions().size() - 1) {
                        regionsIds += filter.getSelectedRegions().get(pos).getId();
                    } else {
                        regionsIds += filter.getSelectedRegions().get(pos).getId() + ",";
                    }
                }
                params.put("regions", regionsIds);
            }

            if (filter.getDealerType() != null) {
                params.put("dealer", filter.getDealerType());
            }

            if (filter.getRating() >= 0) {
                params.put("rating", filter.getRating());
            }
        }

        if (categoryKey != null) {
            if (categoryKey.equals(AppConstants.MainCategoriesType.REVIEWS)) {
                params.put("is_for_review", 1);
                params.put("service_type", 2);
            } else {
                params.put("is_for_review", 0);
//                params.put("service_type", 1);
            }
        }

        switch (sortType) {
            case AppConstants.SortingOptions.NEWEST:
                params.put("sort_by_created", -1);
                break;

            case AppConstants.SortingOptions.OLDEST:
                params.put("sort_by_created", 1);
                break;

            case AppConstants.SortingOptions.LOWEST_PRICE:
                params.put("sort_by_price", 1);
                break;

            case AppConstants.SortingOptions.HIGHEST_PRICE:
                params.put("sort_by_price", -1);
                break;

            case AppConstants.SortingOptions.LATEST_REVIEWS:
                params.put("sort_by_latest_review", 1);
                break;

            case AppConstants.SortingOptions.HIGHEST_REVIEWS:
                params.put("sort_by_rating", -1);
                break;

            case AppConstants.SortingOptions.LOWEST_REVIEWS:
                params.put("sort_by_rating", 1);
                break;

            case AppConstants.SortingOptions.NUMBER_REVIEWS:
                params.put("sort_by_review_count", -1);
                break;
        }

        if (this.keyword != null) {
            params.put("car_title", keyword);
        }

        params.put("limit", LIMIT);
        params.put("offset", OFFSET);

        WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.GET_CARS, null, null, params, null, new WebApiRequest.WebServiceArrayResponse() {
            @Override
            public void onSuccess(ApiArrayResponse apiArrayResponse) {
                TOTAL_RECORDS = apiArrayResponse.getTotal_count();
                luxuryCategories = (ArrayList<TradeCar>) JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), TradeCar.class);

                if (luxuryCategories.size() == 0 && outletMallTabAdapter.getItemCount() == 0) {
                    hideView();
                    mainActivityContext.hideLoader();
                    return;
                } else {
                    if (OFFSET == 0) {
                        if (outletMallTabAdapter != null) {
                            outletMallTabAdapter.clear();
                            outletMallTabAdapter.concatenate(luxuryCategories);
                            outletMallTabAdapter.notifyDataSetChanged();
                            binding.recyclerViewOutletMall.smoothScrollToPosition(0);
                        }
                    } else {
                        outletMallTabAdapter.concatenate(luxuryCategories);
                        outletMallTabAdapter.notifyDataSetChanged();
                    }
                    visibleView();
                }
/*
                luxuryCategories = (ArrayList<TradeCar>) JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), TradeCar.class);
                if (luxuryCategories.size() > 0) {
                    if (outletMallTabAdapter != null) {
                        outletMallTabAdapter.clear();
                    }
                    if (nonFeaturedCars != null) {
                        nonFeaturedCars.clear();
                    }
                    if (featuredCars != null) {
                        featuredCars.clear();
                    }
                    for (int pos = 0; pos < luxuryCategories.size(); pos++) {
                        if (luxuryCategories.get(pos).getFeatured() == 1) {
                            featuredCars.add(luxuryCategories.get(pos));
                        } else {
                            nonFeaturedCars.add(luxuryCategories.get(pos));
                        }
                    }
                    if (sortType > 0) {
                        setsortedAdapter(sortType);
                        if (categoryKey.equals(AppConstants.MainCategoriesType.REVIEWS)) {
                            if (outletMallFragment != null) {
                                outletMallFragment.binding.rbHighestReviews.setChecked(true);
                            }
                        } else {
                            if (outletMallFragment != null) {
                                outletMallFragment.binding.rbNewest.setChecked(true);
                            }

                        }
                    } else {
                        outletMallTabAdapter.concatenate(featuredCars);
                        outletMallTabAdapter.concatenate(nonFeaturedCars);
                        outletMallTabAdapter.notifyDataSetChanged();
                    }
                    visibleView();
                } else {
                    hideView();
                }
                mainActivityContext.hideLoader();
*/
                mainActivityContext.hideLoader();
                ON_CALL = false;
            }

            @Override
            public void onError() {
                if (outletMallTabAdapter.getItemCount() > 0)
                    visibleView();
                else
                    hideView();
                mainActivityContext.hideLoader();
                ON_CALL = false;
            }
        });
    }

    private void initializeAdapter() {
        outletMallTabAdapter = new OutletMallTabAdapter(mainActivityContext);
        if (categoryKey.equals(AppConstants.MainCategoriesType.REVIEWS)) {
            this.sortType = AppConstants.SortingOptions.HIGHEST_REVIEWS;
            outletMallTabAdapter.setReview(true);
        } else {
            this.sortType = AppConstants.SortingOptions.NEWEST;
        }
        outletMallTabAdapter.setCategoryKey(categoryKey);
        linearLayoutManager = new LinearLayoutManager(mainActivityContext);
        binding.recyclerViewOutletMall.setLayoutManager(linearLayoutManager);
        binding.tvOutletMallHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.recyclerViewOutletMall.setAdapter(outletMallTabAdapter);
        binding.recyclerViewOutletMall.addOnItemTouchListener(new RecyclerTouchListener(mainActivityContext, binding.recyclerViewOutletMall, new ClickListenerRecycler() {
            @Override
            public void onClick(View view, int position) {
                if (categoryKey.equals(AppConstants.MainCategoriesType.REVIEWS)) {
                    ReviewDetailsFragment reviewDetailsFragment = new ReviewDetailsFragment();
                    reviewDetailsFragment.setTradeCar(outletMallTabAdapter.getAll().get(position));
                    mainActivityContext.addFragment(reviewDetailsFragment, ReviewDetailsFragment.class.getSimpleName(), true, false);
                } else {
                    LuxuryMarketDetailsFragment luxuryMarketDetailsFragment = new LuxuryMarketDetailsFragment();
                    luxuryMarketDetailsFragment.setCurrentTradeCar(outletMallTabAdapter.getAll().get(position));
                    luxuryMarketDetailsFragment.setSimilarListings(luxuryCategories);
                    luxuryMarketDetailsFragment.setCategoryKey(categoryKey);
                    mainActivityContext.addFragment(luxuryMarketDetailsFragment, LuxuryMarketDetailsFragment.class.getSimpleName(), true, false);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        binding.recyclerViewOutletMall.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (lastVisibleItem == outletMallTabAdapter.getItemCount() - 1) {
                    if (outletMallTabAdapter.getItemCount() < TOTAL_RECORDS - 1) {
                        if (!ON_CALL) {
                            OFFSET = OFFSET + LIMIT;
                            ON_CALL = true;
                            if (mainActivityContext.showLoader()) {
                                getLuxuryMarketCategories(item_id);
                            }
                        }
                    }
                }
            }
        });

    }

    public void visibleView() {
        binding.recyclerViewOutletMall.setVisibility(View.VISIBLE);
        binding.noDataLayout.setVisibility(View.GONE);
    }

    public void hideView() {
        binding.recyclerViewOutletMall.setVisibility(View.GONE);
        binding.noDataLayout.setVisibility(View.VISIBLE);
    }

    private void setsortedAdapter(int sortType) {
        switch (sortType) {
            case AppConstants.SortingOptions.LATEST_REVIEWS:

                /*
                if (luxuryCategories != null && luxuryCategories.size() > 0) {
                    ArrayList<TradeCar> cars = new ArrayList<>();
                    cars.addAll(luxuryCategories);
                    if (outletMallTabAdapter != null) {
//                        Collections.reverse(cars);
                        outletMallTabAdapter.clear();
//                        outletMallTabAdapter.concatenate(featuredCars);
                        outletMallTabAdapter.concatenate(cars);
                        outletMallTabAdapter.notifyDataSetChanged();
                    }
                }
                */
                break;

            case AppConstants.SortingOptions.HIGHEST_REVIEWS:
                /*
                if (luxuryCategories != null && luxuryCategories.size() > 0) {
                    ArrayList<TradeCar> cars = new ArrayList<>();
                    cars.addAll(luxuryCategories);
                    Collections.sort(cars, new Comparator<TradeCar>() {
                        @Override
                        public int compare(TradeCar o1, TradeCar o2) {
                            return Float.compare(o1.getAverage_rating(), o2.getAverage_rating());
                        }
                    });
                    if (outletMallTabAdapter != null) {
                        Collections.reverse(cars);
                        outletMallTabAdapter.clear();
//                        outletMallTabAdapter.concatenate(luxuryCategories);
                        outletMallTabAdapter.concatenate(cars);
                        outletMallTabAdapter.notifyDataSetChanged();
                        binding.recyclerViewOutletMall.smoothScrollToPosition(0);
                    }
                }
                */
                break;

            case AppConstants.SortingOptions.LOWEST_REVIEWS:
                /*
                if (luxuryCategories != null && luxuryCategories.size() > 0) {
                    ArrayList<TradeCar> cars = new ArrayList<>();
                    cars.addAll(luxuryCategories);
                    Collections.sort(cars, new Comparator<TradeCar>() {
                        @Override
                        public int compare(TradeCar o1, TradeCar o2) {
                            return Float.compare(o1.getAverage_rating(), o2.getAverage_rating());
                        }
                    });
                    if (outletMallTabAdapter != null) {
                        outletMallTabAdapter.clear();
//                        outletMallTabAdapter.concatenate(featuredCars);
                        outletMallTabAdapter.concatenate(cars);
                        outletMallTabAdapter.notifyDataSetChanged();
                        binding.recyclerViewOutletMall.smoothScrollToPosition(0);
                    }
                }
                */
                break;

            case AppConstants.SortingOptions.NUMBER_REVIEWS:
                /*
                if (luxuryCategories != null && luxuryCategories.size() > 0) {
                    ArrayList<TradeCar> cars = new ArrayList<>();
                    cars.addAll(luxuryCategories);
                    Collections.sort(cars, new Comparator<TradeCar>() {
                        @Override
                        public int compare(TradeCar o1, TradeCar o2) {
                            return Float.compare(o1.getReview_count(), o2.getReview_count());
                        }
                    });
                    if (outletMallTabAdapter != null) {
                        Collections.reverse(cars);
                        outletMallTabAdapter.clear();
//                        outletMallTabAdapter.concatenate(featuredCars);
                        outletMallTabAdapter.concatenate(cars);
                        outletMallTabAdapter.notifyDataSetChanged();
                        binding.recyclerViewOutletMall.smoothScrollToPosition(0);
                    }
                }
                */
                break;

            case AppConstants.SortingOptions.NEWEST:
                /*
                if (nonFeaturedCars != null && nonFeaturedCars.size() > 0) {
                    if (outletMallTabAdapter != null) {
                        outletMallTabAdapter.clear();
                        outletMallTabAdapter.concatenate(featuredCars);
                        outletMallTabAdapter.concatenate(nonFeaturedCars);
                        outletMallTabAdapter.notifyDataSetChanged();
                    }
                }
                */
                break;

            case AppConstants.SortingOptions.OLDEST:
                /*
                if (nonFeaturedCars != null && nonFeaturedCars.size() > 0) {
                    ArrayList<TradeCar> cars = new ArrayList<>();
                    cars.addAll(nonFeaturedCars);
                    if (outletMallTabAdapter != null) {
                        Collections.reverse(cars);
                        outletMallTabAdapter.clear();
                        outletMallTabAdapter.concatenate(featuredCars);
                        outletMallTabAdapter.concatenate(cars);
                        outletMallTabAdapter.notifyDataSetChanged();
                    }
                }
                */
                break;

            case AppConstants.SortingOptions.LOWEST_PRICE:
                /*
                if (nonFeaturedCars != null && nonFeaturedCars.size() > 0) {
                    ArrayList<TradeCar> cars = new ArrayList<>();
                    cars.addAll(nonFeaturedCars);
                    Collections.sort(cars, new Comparator<TradeCar>() {
                        @Override
                        public int compare(TradeCar o1, TradeCar o2) {
                            return Double.compare(o1.getAmount(), o2.getAmount());
                        }
                    });
                    if (outletMallTabAdapter != null) {
                        outletMallTabAdapter.clear();
                        outletMallTabAdapter.concatenate(featuredCars);
                        outletMallTabAdapter.concatenate(cars);
                        outletMallTabAdapter.notifyDataSetChanged();
                        binding.recyclerViewOutletMall.smoothScrollToPosition(0);
                    }
                }
                */
                break;

            case AppConstants.SortingOptions.HIGHEST_PRICE:
                /*
                if (nonFeaturedCars != null && nonFeaturedCars.size() > 0) {
                    ArrayList<TradeCar> cars = new ArrayList<>();
                    cars.addAll(nonFeaturedCars);
                    Collections.sort(cars, new Comparator<TradeCar>() {
                        @Override
                        public int compare(TradeCar o1, TradeCar o2) {
                            return Double.compare(o1.getAmount(), o2.getAmount());
                        }
                    });
                    if (outletMallTabAdapter != null) {
                        Collections.reverse(cars);
                        outletMallTabAdapter.clear();
                        outletMallTabAdapter.concatenate(featuredCars);
                        outletMallTabAdapter.concatenate(cars);
                        outletMallTabAdapter.notifyDataSetChanged();
                        binding.recyclerViewOutletMall.smoothScrollToPosition(0);
                    }
                }
                */
                break;
        }
//        Log.i("count", outletMallTabAdapter.getItemCount() + " in list");
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public void setMostViewed(int mostViewed) {
        this.mostViewed = mostViewed;
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

    public void setOutletMallFragment(OutletMallFragment outletMallFragment) {
        this.outletMallFragment = outletMallFragment;
        this.outletMallFragment.setTextChangedListener(this);
    }

    public void setAllCars(boolean allCars) {
        this.allCars = allCars;
    }
}
