package com.ingic.caristocrat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.OutletMallViewPagerAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentOutletMallBinding;
import com.ingic.caristocrat.dialogs.EnableNotificationsDialog;
import com.ingic.caristocrat.dialogs.SelectCountryDialog;
import com.ingic.caristocrat.helpers.BasePreferenceHelper;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.LuxuryMarketSearchFilter;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.DialogCloseListener;
import com.ingic.caristocrat.interfaces.JustReload;
import com.ingic.caristocrat.interfaces.SortTypeChangeListener;
import com.ingic.caristocrat.interfaces.TextChangedListener;
import com.ingic.caristocrat.models.Region;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.ArrayList;

/**
 */
public class OutletMallFragment extends BaseFragment implements View.OnClickListener, TextWatcher, JustReload {
    FragmentOutletMallBinding binding;
    int item_id;
    boolean fromLuxuryMarket;
    String categoryKey, title;
    private SortTypeChangeListener sortTypeChangeListener;
    private TextChangedListener textChangedListener;
    private boolean isSearchEnabled = false;

    public OutletMallFragment() {
    }

    public static OutletMallFragment Instance() {
        return new OutletMallFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_outlet_mall, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showNotificationsAndRegionAlert();
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(mainActivityContext);
        titlebar.showBackButton(mainActivityContext, false).setOnClickListener(this);
        if (isSearchEnabled) {
            titlebar.showSearch().getText().clear();
            titlebar.showSearch().setHint(mainActivityContext.getResources().getString(R.string.search_car));
//            titlebar.showSearch().addTextChangedListener(this);
            titlebar.showSearch().setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        if (titlebar.showSearch().getText().toString().length() == 0) {
                            return false;
                        } else {
                            if (outletMallViewPagerAdapter.getItem(binding.vpOutletMall.getCurrentItem()) instanceof OutletMallTabFragment) {
                                OutletMallTabFragment fragment = (OutletMallTabFragment) outletMallViewPagerAdapter.getItem(binding.vpOutletMall.getCurrentItem());
                                fragment.onSearchActionDone(titlebar.showSearch().getText().toString(), false);
                            }
                        }
                        handled = true;
                    }
                    return handled;
                }
            });
        } else {
            if (categoryKey.equals(AppConstants.MainCategoriesType.REVIEWS)) {
                titlebar.showSort().setOnClickListener(this);
                binding.rbLatestReviews.setOnClickListener(this);
                binding.rbHighestReviews.setOnClickListener(this);
                binding.rbLowestReviews.setOnClickListener(this);
                binding.rbNumberReviews.setOnClickListener(this);
            } else {
                titlebar.showSort().setOnClickListener(this);
                binding.rbNewest.setOnClickListener(this);
                binding.rbOldest.setOnClickListener(this);
                binding.rbLowestPrice.setOnClickListener(this);
                binding.rbHighestPrice.setOnClickListener(this);
            }
            if (title != null) {
                titlebar.setTitle(title);
            }
            titlebar.showSearchButton(mainActivityContext).setOnClickListener(this);
            titlebar.showFilter().setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibBackbtn:
                UIHelper.hideSoftKeyboard(mainActivityContext);
                if (isSearchEnabled) {
                    isSearchEnabled = false;
                    setTitlebar(mainActivityContext.getTitlebar());
                    if (outletMallViewPagerAdapter.getItem(binding.vpOutletMall.getCurrentItem()) instanceof OutletMallTabFragment) {
                        OutletMallTabFragment fragment = (OutletMallTabFragment) outletMallViewPagerAdapter.getItem(binding.vpOutletMall.getCurrentItem());
                        fragment.onSearchActionDone(null, true);
                    }
                } else {
                    mainActivityContext.onBackPressed();
                }
                break;

            case R.id.ibFilter:
                mainActivityContext.replaceFragment(LuxuryMarketFilterFragment.Instance(categoryKey), LuxuryMarketFilterFragment.class.getSimpleName(), true, false);
                break;

            case R.id.ibSort:
                if (categoryKey.equals(AppConstants.MainCategoriesType.REVIEWS)) {
                    if (binding.llSorting.getVisibility() == View.VISIBLE) {
                        UIHelper.animation(Techniques.FadeOut, 400, 0, binding.llSorting);
                        binding.llSorting.setVisibility(View.GONE);
                    } else {
                        UIHelper.animation(Techniques.FadeIn, 400, 0, binding.llSorting);
                        binding.llSorting.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (binding.llSortingCars.getVisibility() == View.VISIBLE) {
                        UIHelper.animation(Techniques.FadeOut, 400, 0, binding.llSortingCars);
                        binding.llSortingCars.setVisibility(View.GONE);
                    } else {
                        UIHelper.animation(Techniques.FadeIn, 400, 0, binding.llSortingCars);
                        binding.llSortingCars.setVisibility(View.VISIBLE);
                    }
                }
                break;

            case R.id.rbLatestReviews:
                if (sortTypeChangeListener != null) {
                    if (outletMallViewPagerAdapter.getItem(binding.vpOutletMall.getCurrentItem()) instanceof OutletMallTabFragment) {
                        OutletMallTabFragment fragment = (OutletMallTabFragment) outletMallViewPagerAdapter.getItem(binding.vpOutletMall.getCurrentItem());
                        fragment.onSortTypeChanged(AppConstants.SortingOptions.LATEST_REVIEWS);
                    }
                    UIHelper.animation(Techniques.FadeOut, 400, 0, binding.llSorting);
                    binding.llSorting.setVisibility(View.GONE);
                }
                break;

            case R.id.rbHighestReviews:
                if (sortTypeChangeListener != null) {
                    if (outletMallViewPagerAdapter.getItem(binding.vpOutletMall.getCurrentItem()) instanceof OutletMallTabFragment) {
                        OutletMallTabFragment fragment = (OutletMallTabFragment) outletMallViewPagerAdapter.getItem(binding.vpOutletMall.getCurrentItem());
                        fragment.onSortTypeChanged(AppConstants.SortingOptions.HIGHEST_REVIEWS);
                    }
                    UIHelper.animation(Techniques.FadeOut, 400, 0, binding.llSorting);
                    binding.llSorting.setVisibility(View.GONE);
                }
                break;

            case R.id.rbLowestReviews:
                if (sortTypeChangeListener != null) {
                    if (outletMallViewPagerAdapter.getItem(binding.vpOutletMall.getCurrentItem()) instanceof OutletMallTabFragment) {
                        OutletMallTabFragment fragment = (OutletMallTabFragment) outletMallViewPagerAdapter.getItem(binding.vpOutletMall.getCurrentItem());
                        fragment.onSortTypeChanged(AppConstants.SortingOptions.LOWEST_REVIEWS);
                    }
                    UIHelper.animation(Techniques.FadeOut, 400, 0, binding.llSorting);
                    binding.llSorting.setVisibility(View.GONE);
                }
                break;

            case R.id.rbNumberReviews:
                if (sortTypeChangeListener != null) {
                    if (outletMallViewPagerAdapter.getItem(binding.vpOutletMall.getCurrentItem()) instanceof OutletMallTabFragment) {
                        OutletMallTabFragment fragment = (OutletMallTabFragment) outletMallViewPagerAdapter.getItem(binding.vpOutletMall.getCurrentItem());
                        fragment.onSortTypeChanged(AppConstants.SortingOptions.NUMBER_REVIEWS);
                    }
                    UIHelper.animation(Techniques.FadeOut, 400, 0, binding.llSorting);
                    binding.llSorting.setVisibility(View.GONE);
                }
                break;

            case R.id.rbNewest:
                if (sortTypeChangeListener != null) {
                    if (outletMallViewPagerAdapter.getItem(binding.vpOutletMall.getCurrentItem()) instanceof OutletMallTabFragment) {
                        OutletMallTabFragment fragment = (OutletMallTabFragment) outletMallViewPagerAdapter.getItem(binding.vpOutletMall.getCurrentItem());
                        fragment.onSortTypeChanged(AppConstants.SortingOptions.NEWEST);
                    }
                    UIHelper.animation(Techniques.FadeOut, 400, 0, binding.llSorting);
                    binding.llSortingCars.setVisibility(View.GONE);
                }
                break;

            case R.id.rbOldest:
                if (sortTypeChangeListener != null) {
                    if (outletMallViewPagerAdapter.getItem(binding.vpOutletMall.getCurrentItem()) instanceof OutletMallTabFragment) {
                        OutletMallTabFragment fragment = (OutletMallTabFragment) outletMallViewPagerAdapter.getItem(binding.vpOutletMall.getCurrentItem());
                        fragment.onSortTypeChanged(AppConstants.SortingOptions.OLDEST);
                    }
                    UIHelper.animation(Techniques.FadeOut, 400, 0, binding.llSorting);
                    binding.llSortingCars.setVisibility(View.GONE);
                }
                break;

            case R.id.rbLowestPrice:
                if (sortTypeChangeListener != null) {
                    if (outletMallViewPagerAdapter.getItem(binding.vpOutletMall.getCurrentItem()) instanceof OutletMallTabFragment) {
                        OutletMallTabFragment fragment = (OutletMallTabFragment) outletMallViewPagerAdapter.getItem(binding.vpOutletMall.getCurrentItem());
                        fragment.onSortTypeChanged(AppConstants.SortingOptions.LOWEST_PRICE);
                    }
                    UIHelper.animation(Techniques.FadeOut, 400, 0, binding.llSorting);
                    binding.llSortingCars.setVisibility(View.GONE);
                }
                break;

            case R.id.rbHighestPrice:
                if (sortTypeChangeListener != null) {
                    if (outletMallViewPagerAdapter.getItem(binding.vpOutletMall.getCurrentItem()) instanceof OutletMallTabFragment) {
                        OutletMallTabFragment fragment = (OutletMallTabFragment) outletMallViewPagerAdapter.getItem(binding.vpOutletMall.getCurrentItem());
                        fragment.onSortTypeChanged(AppConstants.SortingOptions.HIGHEST_PRICE);
                    }
                    UIHelper.animation(Techniques.FadeOut, 400, 0, binding.llSorting);
                    binding.llSortingCars.setVisibility(View.GONE);
                }
                break;

            case R.id.ibSearch:
                search();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fromLuxuryMarket = false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (outletMallViewPagerAdapter.getItem(binding.vpOutletMall.getCurrentItem()) instanceof OutletMallTabFragment) {
            OutletMallTabFragment fragment = (OutletMallTabFragment) outletMallViewPagerAdapter.getItem(binding.vpOutletMall.getCurrentItem());
            fragment.afterTextChanged(s);
        }
        /*
        if(textChangedListener != null){
            textChangedListener.afterTextChanged(s);
        }
        */
    }

    @Override
    public void load() {
        initializeAdapter();
    }

    private void showNotificationsAndRegionAlert() {
        if (preferenceHelper.getLoginStatus()) {
            if (preferenceHelper.getUser() != null) {
                if (preferenceHelper.getUser().getPush_notification() == 0 && fromLuxuryMarket) {
                    EnableNotificationsDialog enableNotificationsDialog = EnableNotificationsDialog.newInstance(mainActivityContext);
                    enableNotificationsDialog.show(mainActivityContext.getFragmentManager(), null);
                    enableNotificationsDialog.setDialogListener(new DialogCloseListener() {
                        @Override
                        public void onDismiss() {
                            if (mainActivityContext.internetConnected() && !preferenceHelper.getBooleanPrefrence(BasePreferenceHelper.KEY_REGION_SELECTED) && fromLuxuryMarket && !categoryKey.equals(AppConstants.MainCategoriesType.LUXURY_NEW_CARS)) {
                                getRegions();
                            } else {

                            }
                        }
                    });

                } else {
                    if (mainActivityContext.internetConnected() && !preferenceHelper.getBooleanPrefrence(BasePreferenceHelper.KEY_REGION_SELECTED) && fromLuxuryMarket && !categoryKey.equals(AppConstants.MainCategoriesType.LUXURY_NEW_CARS)) {
                        getRegions();
                    } else {
                        initializeAdapter();
                    }
                }
            }
        } else {
            if (mainActivityContext.internetConnected() && !preferenceHelper.getBooleanPrefrence(BasePreferenceHelper.KEY_REGION_SELECTED) && fromLuxuryMarket && !categoryKey.equals(AppConstants.MainCategoriesType.LUXURY_NEW_CARS)) {
                getRegions();
            } else {
                initializeAdapter();
            }
        }
    }

    OutletMallViewPagerAdapter outletMallViewPagerAdapter;

    private void initializeAdapter() {
        if (outletMallViewPagerAdapter == null) {
            outletMallViewPagerAdapter = new OutletMallViewPagerAdapter(getChildFragmentManager());
            if (categoryKey.equals(AppConstants.MainCategoriesType.REVIEWS)) {
                OutletMallTabFragment allCars = new OutletMallTabFragment();
                allCars.setItem_id(item_id);
                allCars.setMostViewed(AppConstants.NOT_MOST_VIEWED);
                allCars.setCategoryKey(categoryKey);
                allCars.setOutletMallFragment(this);
                allCars.setAllCars(true);
                outletMallViewPagerAdapter.addFragment(allCars, mainActivityContext.getResources().getString(R.string.all_cars));

                OutletMallTabFragment mostviewed = new OutletMallTabFragment();
                mostviewed.setItem_id(item_id);
                mostviewed.setMostViewed(AppConstants.MOST_VIEWED);
                mostviewed.setCategoryKey(categoryKey);
                mostviewed.setOutletMallFragment(this);
                outletMallViewPagerAdapter.addFragment(mostviewed, mainActivityContext.getResources().getString(R.string.most_viewed));
            } else {
                OutletMallTabFragment allCars = new OutletMallTabFragment();
                allCars.setItem_id(item_id);
                allCars.setMostViewed(AppConstants.NOT_MOST_VIEWED);
                allCars.setCategoryKey(categoryKey);
                allCars.setOutletMallFragment(this);
                allCars.setAllCars(true);
                outletMallViewPagerAdapter.addFragment(allCars, mainActivityContext.getResources().getString(R.string.discover));

                OutletMallTabFragment mostviewed = new OutletMallTabFragment();
                mostviewed.setItem_id(item_id);
                mostviewed.setMostViewed(AppConstants.MOST_VIEWED);
                mostviewed.setCategoryKey(categoryKey);
                mostviewed.setOutletMallFragment(this);
                outletMallViewPagerAdapter.addFragment(mostviewed, mainActivityContext.getResources().getString(R.string.most_viewed));
            }
            binding.vpOutletMall.setOffscreenPageLimit(0);
            binding.vpOutletMall.setAdapter(outletMallViewPagerAdapter);
            binding.tabLayout.setupWithViewPager(binding.vpOutletMall);
        } else {
            binding.vpOutletMall.setOffscreenPageLimit(0);
            binding.vpOutletMall.setAdapter(outletMallViewPagerAdapter);
            binding.tabLayout.setupWithViewPager(binding.vpOutletMall);
        }
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public void setFromLuxuryMarket(boolean fromLuxuryMarket) {
        this.fromLuxuryMarket = fromLuxuryMarket;
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private void search() {
        isSearchEnabled = true;
        setTitlebar(mainActivityContext.getTitlebar());
    }

    private void getRegions() {
        WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.REGIONS, binding.getRoot(), null, null, null, new WebApiRequest.WebServiceArrayResponse() {

            @Override
            public void onSuccess(ApiArrayResponse apiArrayResponse) {
                ArrayList<Region> regions = (ArrayList<Region>) JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), Region.class);
                if (categoryKey.equals(AppConstants.MainCategoriesType.LUXURY_NEW_CARS)) {
                    LuxuryMarketSearchFilter filter = LuxuryMarketSearchFilter.getInstance();
                    filter.setSelectedRegions(regions);
                    filter.setFilterApply(true);
                    load();
                } else {
                    SelectCountryDialog selectCountryDialog = SelectCountryDialog.newInstance(mainActivityContext, OutletMallFragment.this);
                    selectCountryDialog.setRegions(regions);
                    selectCountryDialog.show(mainActivityContext.getFragmentManager(), null);
                }
                mainActivityContext.hideLoader();
            }

            @Override
            public void onError() {
                mainActivityContext.hideLoader();

            }
        });
    }

    public SortTypeChangeListener getSortTypeChangeListener() {
        return sortTypeChangeListener;
    }

    public void setSortTypeChangeListener(SortTypeChangeListener sortTypeChangeListener) {
        this.sortTypeChangeListener = sortTypeChangeListener;
    }

    public TextChangedListener getTextChangedListener() {
        return textChangedListener;
    }

    public void setTextChangedListener(TextChangedListener textChangedListener) {
        this.textChangedListener = textChangedListener;
    }
}

