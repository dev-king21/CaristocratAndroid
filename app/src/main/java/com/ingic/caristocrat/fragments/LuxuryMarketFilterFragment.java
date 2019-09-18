package com.ingic.caristocrat.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.BrandsListFilterActivity;
import com.ingic.caristocrat.adapters.CarBodyTypeAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentLuxuryMarketFilterBinding;
import com.ingic.caristocrat.dialogs.SelectCountryDialog;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.LuxuryMarketSearchFilter;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.OnRegionsSelectedListener;
import com.ingic.caristocrat.interfaces.SimpleDialogActionListener;
import com.ingic.caristocrat.models.Region;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.models.CarBodyStyle;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class LuxuryMarketFilterFragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, OnRegionsSelectedListener, TextWatcher {
    private FragmentLuxuryMarketFilterBinding binding;
    private CarBodyTypeAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<CarBodyStyle> arrayList;
    private int year;
    Bundle savedInstanceState;
    String categoryKey;
    int categoryId;
    LuxuryMarketSearchFilter filter;

    public LuxuryMarketFilterFragment() {
    }

    @SuppressLint("ValidFragment")
    public LuxuryMarketFilterFragment(String categoryKey,int _categoryId) {
        this.categoryKey = categoryKey;
        this.categoryId = _categoryId;
    }

    public static LuxuryMarketFilterFragment Instance() {
        return new LuxuryMarketFilterFragment();
    }

    public static LuxuryMarketFilterFragment Instance(String categoryKey, int _categoryId) {
        return new LuxuryMarketFilterFragment(categoryKey, _categoryId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        preferenceHelper.setBooleanPrefrence(AppConstants.FILTER_OPEN, true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_luxury_market_filter, container, false);

        filter = LuxuryMarketSearchFilter.getInstance();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();

        binding.tvSelectedBrandNames.setSelected(true);
        binding.llBody.setVisibility(View.GONE);

        binding.rangeSeekbarPrice.setMinValue(AppConstants.CAR_PRICE_MIN);
        binding.rangeSeekbarPrice.setMaxValue(AppConstants.CAR_PRICE_MAX);

        binding.rangeSeekbarMileage.setMinValue(AppConstants.CAR_MIN_MILEAGE);
        binding.rangeSeekbarMileage.setMaxValue(AppConstants.CAR_MAX_MILEAGE);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
/*
        binding.rangeSeekbarYear.setMinValue(AppConstants.CAR_YEAR_MIN_RANGE);
        binding.rangeSeekbarYear.setMaxValue(year);
*/
        binding.tvPriceRangeLabelMax.setText(mainActivityContext.getResources().getString(R.string.currency_unit) + " " + NumberFormat.getNumberInstance(Locale.US).format(AppConstants.CAR_PRICE_MAX));
        binding.tvClassicRangeLabelMax.setText(year + "");
        binding.tvMileageRangeLabelMax.setText(NumberFormat.getNumberInstance(Locale.US).format(AppConstants.CAR_MAX_MILEAGE) + " " + mainActivityContext.getResources().getString(R.string.mileage_unit));

        int spacingInPixels = mainActivityContext.getResources().getDimensionPixelSize(R.dimen.dp16);
        UIHelper.SpacesItemDecorationEnd spacesItemDecorationHome = new UIHelper.SpacesItemDecorationEnd(spacingInPixels);
        binding.recyclerView.addItemDecoration(spacesItemDecorationHome);

        adapter = new CarBodyTypeAdapter(mainActivityContext);
        linearLayoutManager = new LinearLayoutManager(mainActivityContext, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setAdapter(adapter);

        filter.setLuxuryNewCars(false);
        if (categoryKey != null) {
            switch (categoryKey) {
                case AppConstants.MainCategoriesType.THE_OUTLET_MALL:
                    setOutletMallFilter();
                    break;

                case AppConstants.MainCategoriesType.APPROVED_PRE_OWNED:
                    setApprovedCarFilter();
                    break;

                case AppConstants.MainCategoriesType.CLASSIC_CARS:
                    setClassicCarFilter();
                    break;

                case AppConstants.MainCategoriesType.LUXURY_NEW_CARS:
                    setNewLuxuryCarFilter();
                    break;

                case AppConstants.MainCategoriesType.REVIEWS:
                    setNewLuxuryCarFilter();
                    break;
            }
        }

        if (filter.getVersionName() != null) {
            binding.etVersion.setText(filter.getVersionName());
            binding.ivRemoveVersionName.setVisibility(View.VISIBLE);
        }

        if (filter.getDealerType() != null) {
            switch (filter.getDealerType()) {
                case AppConstants.DealerType.OFFICIAL:
                    binding.rbAutomatic.setChecked(true);
                    break;
                case AppConstants.DealerType.MARKET:
                    binding.rbManual.setChecked(true);
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        preferenceHelper.setIntegerPrefrence(AppConstants.FILTER_OPENED_SCREEN, AppConstants.FILTER_OPENED_FILTER);
        if (filter.getCarBodyStyles().size() > 0) {
            setFilterPreset();
        } else if (filter.getCarBodyStyles().size() == 0) {
            if (mainActivityContext.showLoader()) {
                getCarBodyTypes();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mainActivityContext.getBinding().btnFilterAction.setVisibility(View.GONE);
        mainActivityContext.getBinding().btnFilterAction.setText(mainActivityContext.getResources().getString(R.string.apply));
        mainActivityContext.getBinding().llCommentsLayout.setVisibility(View.GONE);
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(mainActivityContext);
        titlebar.showTitlebar(mainActivityContext);
        titlebar.setTitle(mainActivityContext.getResources().getString(R.string.filter));
        titlebar.showCloseText().setOnClickListener(this);
        titlebar.showCancelText().setOnClickListener(this);

        mainActivityContext.getBinding().btnFilterAction.setVisibility(View.VISIBLE);
        mainActivityContext.getBinding().btnFilterAction.setText(mainActivityContext.getResources().getString(R.string.apply));
        mainActivityContext.getBinding().btnFilterAction.setOnClickListener(this);
        mainActivityContext.getBinding().llCommentsLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvClose:
                close();
                break;

            case R.id.llFilterByBrand:
                filterByBrand();
                break;

            case R.id.llFilterByRegion:
                getRegions();
                break;

            case R.id.btnFilterAction:
                applyFilter();
                break;

            case R.id.tvCancel:
                clear();
                break;

            case R.id.rbAutomatic:
            case R.id.rbManual:
                setTransmissions(v);
                break;

            case R.id.ivRemoveVersionName:
                filter.setVersionName(null);
                binding.etVersion.setText("");
                binding.ivRemoveVersionName.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.getId() == R.id.radioGroupTransmission) {
            switch (checkedId) {
                case R.id.rbAutomatic:
                    filter.setTransmissionType(AppConstants.TransmissionTypes.AUTOMATIC);
                    break;

                case R.id.rbManual:
                    filter.setTransmissionType(AppConstants.TransmissionTypes.MANUAL);
                    break;
            }
        }
    }

    @Override
    public void onRegionsSelected(ArrayList<Region> selectedRegions) {
        if (filter.getSelectedRegions().size() == 0) {
            binding.tvRegionNames.setText(mainActivityContext.getResources().getString(R.string.select_regions));
            return;
        }
        if (filter.getSelectedRegions().size() > 0) {
            String regions = "";
            for (int pos = 0; pos < filter.getSelectedRegions().size(); pos++) {
                if (pos == filter.getSelectedRegions().size() - 1) {
                    regions += filter.getSelectedRegions().get(pos).getName();
                } else {
                    regions += filter.getSelectedRegions().get(pos).getName() + ", ";
                }
            }
            binding.tvRegionNames.setText(regions);
            filter.setFilter(true);
        }
    }

    private void setListeners() {
        binding.llFilterByBrand.setOnClickListener(this);

        binding.llFilterByRegion.setOnClickListener(this);

        binding.etVersion.addTextChangedListener(this);
/*
        binding.radioGroupTransmission.setOnCheckedChangeListener(this);
*/
        binding.rbManual.setOnClickListener(this);

        binding.rbAutomatic.setOnClickListener(this);

        binding.ivRemoveVersionName.setOnClickListener(this);

        binding.rangeSeekbarPrice.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                setPriceRange(minValue, maxValue);
            }
        });

        binding.rangeSeekbarPrice.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                filter.setMinPrice((Long) minValue);
                filter.setMaxPrice((Long) maxValue);
            }
        });

        binding.rangeSeekbarYear.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                setYearRange(minValue, maxValue);
            }
        });

        binding.rangeSeekbarYear.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                filter.setMinyear((Long) minValue);
                filter.setMaxYear((Long) maxValue);
            }
        });

        binding.rangeSeekbarModelYear.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                setModelYearRange(minValue, maxValue);
            }
        });

        binding.rangeSeekbarModelYear.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                filter.setMinyear((Long) minValue);
                filter.setMaxYear((Long) maxValue);
            }
        });

        binding.rangeSeekbarMileage.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                setMileageRange(minValue, maxValue);
            }
        });

        binding.rangeSeekbarMileage.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                filter.setMinMileage((Long) minValue);
                filter.setMaxMileage((Long) maxValue);
            }
        });

        binding.ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                filter.setRating(rating);
                filter.setFilter(true);
            }
        });
    }

    private void close() {
        preferenceHelper.setBooleanPrefrence(AppConstants.FILTER_OPEN, false);
        mainActivityContext.onBackPressed();
    }

    private void clear() {
        for (int pos = 0; pos < filter.getCarBodyStyles().size(); pos++) {
            if (filter.getCarBodyStyles().get(pos).isSelected()) {
                filter.setFilter(true);
                break;
            }
        }

        if (!filter.isFilter()) {
            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.no_filter_applied), Toast.LENGTH_SHORT);
            return;
        }

        UIHelper.showSimpleDialog(
                mainActivityContext,
                0,
                mainActivityContext.getResources().getString(R.string.clear_filters),
                mainActivityContext.getResources().getString(R.string.do_you_want_to_clear_filter),
                mainActivityContext.getResources().getString(R.string.clear),
                mainActivityContext.getResources().getString(R.string.cancel),
                false,
                false,
                new SimpleDialogActionListener() {
                    @Override
                    public void onDialogActionListener(DialogInterface dialog, int which, boolean positive, boolean logout) {
                        if (positive) {
                            binding.tvSelectedBrandNames.setText(mainActivityContext.getResources().getString(R.string.select_make));

                            binding.tvRegionNames.setText(mainActivityContext.getResources().getString(R.string.select_countries));

                            binding.etVersion.setText("");
                            binding.ivRemoveVersionName.setVisibility(View.INVISIBLE);

                            binding.rbAutomatic.setChecked(false);
                            binding.rbManual.setChecked(false);

                            binding.rangeSeekbarPrice.setMinStartValue(0);
                            binding.rangeSeekbarPrice.setMaxStartValue(AppConstants.CAR_PRICE_MAX);
                            binding.rangeSeekbarPrice.apply();
/*
                            binding.rangeSeekbarYear.setMinStartValue(AppConstants.CAR_YEAR_MIN_RANGE);
                            binding.rangeSeekbarYear.setMaxStartValue(year);
                            binding.rangeSeekbarYear.apply();
*/

                            if (categoryKey != null) {
                                switch (categoryKey) {
                                    case AppConstants.MainCategoriesType.THE_OUTLET_MALL:
                                        binding.rangeSeekbarModelYear.setMinStartValue(AppConstants.CAR_APPROVED_YEAR_MIN);
                                        binding.rangeSeekbarModelYear.setMaxStartValue(year);
                                        binding.rangeSeekbarModelYear.apply();
                                        break;

                                    case AppConstants.MainCategoriesType.APPROVED_PRE_OWNED:
                                        binding.rangeSeekbarModelYear.setMinStartValue(AppConstants.CAR_APPROVED_YEAR_MIN);
                                        binding.rangeSeekbarModelYear.setMaxStartValue(year);
                                        binding.rangeSeekbarModelYear.apply();
                                        break;

                                    case AppConstants.MainCategoriesType.CLASSIC_CARS:
                                        binding.rangeSeekbarModelYear.setMinStartValue(AppConstants.CAR_YEAR_MIN);
                                        binding.rangeSeekbarModelYear.setMaxStartValue(AppConstants.CAR_YEAR_MIN_RANGE);
                                        binding.rangeSeekbarModelYear.apply();
                                        break;

                                    case AppConstants.MainCategoriesType.LUXURY_NEW_CARS:
                                        break;
                                }
                            }

                            binding.rangeSeekbarMileage.setMinStartValue(AppConstants.CAR_MIN_MILEAGE);
                            binding.rangeSeekbarMileage.setMaxStartValue(AppConstants.CAR_MAX_MILEAGE);
                            binding.rangeSeekbarMileage.apply();

                            adapter.notifyDataSetChanged();

                            binding.ratingbar.setRating(0);

                            filter.resetFilter(true);
                            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.filter_cleared), Toast.LENGTH_SHORT);
                        }
                        dialog.dismiss();
                    }
                }
        );
    }

    private void setTransmissions(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {

            case R.id.rbAutomatic:
                if (checked) {
//                    filter.setAutomatic(true);
                    filter.setDealerType(AppConstants.DealerType.OFFICIAL);
                    binding.rbManual.setChecked(false);
                } else {
                    filter.setDealerType(null);
//                    filter.setAutomatic(false);
                }
                break;

            case R.id.rbManual:
                if (checked) {
//                    filter.setManual(true);
                    filter.setDealerType(AppConstants.DealerType.MARKET);
                    binding.rbAutomatic.setChecked(false);
                } else {
                    filter.setDealerType(null);
//                    filter.setManual(false);
                }
                break;
        }

    }

    private void transmissionAutomatic() {
        if (binding.rbAutomatic.isChecked()) {
            if (filter.getTransmissionType() != null) {
                filter.setTransmissionType(null);
                binding.radioGroupTransmission.clearCheck();
            }
        }
    }

    private void transmissionManual() {
        if (binding.rbManual.isChecked()) {
            if (filter.getTransmissionType() != null) {
                filter.setTransmissionType(null);
                binding.radioGroupTransmission.clearCheck();
            }
        }
    }

    private void selectTransmission(int transmissionType, boolean automatic) {
        if (filter.getTransmissionType() == null) {
            filter.setTransmissionType(transmissionType);
        } else {
            filter.setTransmissionType(null);
        }
    }

    private void filterByBrand() {
        Intent intent = new Intent(new Intent(mainActivityContext, BrandsListFilterActivity.class));
        intent.putExtra("categoryId",categoryId);
     mainActivityContext.startActivity(intent);
    }

    private void applyFilter() {
        if (!filter.isFilter()) {
            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.no_filter_applied), Toast.LENGTH_SHORT);
            return;
        }
        filter.setFilterApply(true);
        preferenceHelper.setBooleanPrefrence(AppConstants.FILTER_OPEN, false);
        mainActivityContext.onBackPressed();
    }

    private void setPriceRange(Number minValue, Number maxValue) {
        binding.tvPriceRangeLabelMin.setText(mainActivityContext.getResources().getString(R.string.currency_unit) + " " + NumberFormat.getNumberInstance(Locale.US).format(minValue));
        ;
        binding.tvPriceRangeLabelMax.setText(mainActivityContext.getResources().getString(R.string.currency_unit) + " " + NumberFormat.getNumberInstance(Locale.US).format(maxValue));
    }

    private void setYearRange(Number minValue, Number maxValue) {
        if ((long) minValue <= AppConstants.CAR_YEAR_MIN) {
            binding.tvClassicRangeLabel.setText(mainActivityContext.getResources().getString(R.string.classic));
        } else {
            binding.tvClassicRangeLabel.setText(String.valueOf(minValue));
        }
        binding.tvClassicRangeLabelMax.setText(String.valueOf(maxValue));
    }

    private void setModelYearRange(Number minValue, Number maxValue) {
        binding.tvModelYearLabelMin.setText(String.valueOf(minValue) + "");
        binding.tvModelYearLabelMax.setText(String.valueOf(maxValue) + "");
    }

    private void setMileageRange(Number minValue, Number maxValue) {
        binding.tvMileageRangeLabelMin.setText(NumberFormat.getNumberInstance(Locale.US).format(minValue) + " " + mainActivityContext.getResources().getString(R.string.mileage_unit));
        binding.tvMileageRangeLabelMax.setText(NumberFormat.getNumberInstance(Locale.US).format(maxValue) + " " + mainActivityContext.getResources().getString(R.string.mileage_unit));
    }

    private void setFilterPreset() {
        //Setting Filter If Already Selected
        /*
        if (filter.getBrandsList().size() > 0) {
            String brands = "";
            for (int pos = 0; pos < filter.getBrandsList().size(); pos++) {

                if (pos == filter.getBrandsList().size() - 1) {
                    brands += filter.getBrandsList().get(pos).getName();
                } else {
                    brands += filter.getBrandsList().get(pos).getName() + ", ";
                }

                if (filter.getBrandsList().get(pos).getModels() != null) {
                    for (int modelPos = 0; modelPos < filter.getBrandsList().get(pos).getModels().size(); modelPos++) {
                        if (pos == filter.getBrandsList().size() - 1 && modelPos == filter.getBrandsList().get(pos).getModels().size() - 1) {
                            brands += filter.getBrandsList().get(pos).getName() + " " + filter.getBrandsList().get(pos).getModels().get(modelPos).getName();
                        } else {
                            brands += filter.getBrandsList().get(pos).getName() + " " + filter.getBrandsList().get(pos).getModels().get(modelPos).getName() + ", ";
                        }
                    }
                } else {

                }
            }
            binding.tvSelectedBrandNames.setText(brands);
        }
        */

        if (filter.getFilterBrand() != null) {
            if (filter.getFilterBrand().getModels() != null && filter.getFilterBrand().getModels().size() > 0) {
                String name = "";
                for (int pos = 0; pos < filter.getFilterBrand().getModels().size(); pos++) {
                    name += filter.getFilterBrand().getName() + " " + filter.getFilterBrand().getModels().get(pos).getName() + ", ";
                }
                binding.tvSelectedBrandNames.setText(name);
            } else {
                binding.tvSelectedBrandNames.setText(filter.getFilterBrand().getName());
            }
        } else {
            binding.tvSelectedBrandNames.setText(mainActivityContext.getResources().getString(R.string.select_make));
        }

        if (filter.getSelectedRegions().size() > 0) {
            String regions = "";
            for (int pos = 0; pos < filter.getSelectedRegions().size(); pos++) {
                if (pos == filter.getSelectedRegions().size() - 1) {
                    regions += filter.getSelectedRegions().get(pos).getName();
                } else {
                    regions += filter.getSelectedRegions().get(pos).getName() + ", ";
                }
            }
            filter.setFilter(true);
            filter.setFilterApply(true);
            binding.tvRegionNames.setText(regions);
        }

        if (filter.isAutomatic()) {
            binding.rbAutomatic.setChecked(true);
        }

        if (filter.isManual()) {
            binding.rbManual.setChecked(true);
        }

        if (filter.getMinPrice() != null && filter.getMaxPrice() != null) {
            binding.rangeSeekbarPrice.setMinStartValue(filter.getMinPrice());
            binding.rangeSeekbarPrice.setMaxStartValue(filter.getMaxPrice());
            binding.rangeSeekbarPrice.apply();
        }

        if (filter.getMinyear() != null && filter.getMaxYear() != null) {
            binding.rangeSeekbarModelYear.setMinStartValue(filter.getMinyear());
            binding.rangeSeekbarModelYear.setMaxStartValue(filter.getMaxYear());
            binding.rangeSeekbarModelYear.apply();
        }

        if (filter.getMinMileage() != null && filter.getMaxMileage() != null) {
            binding.rangeSeekbarMileage.setMinStartValue(filter.getMinMileage());
            binding.rangeSeekbarMileage.setMaxStartValue(filter.getMaxMileage());
            binding.rangeSeekbarMileage.apply();
        }

        if (filter.getRating() > 0) {
            binding.ratingbar.setRating(filter.getRating());
        }
    }

    private void setOutletMallFilter() {
        binding.rangeSeekbarModelYear.setMinValue(AppConstants.CAR_APPROVED_YEAR_MIN);
        binding.rangeSeekbarModelYear.setMaxValue(year);

        binding.tvModelYearLabelMin.setText(AppConstants.CAR_APPROVED_YEAR_MIN + "");
        binding.tvModelYearLabelMax.setText(year + "");
    }

    private void setApprovedCarFilter() {
        binding.rangeSeekbarModelYear.setMinValue(AppConstants.CAR_APPROVED_YEAR_MIN);
        binding.rangeSeekbarModelYear.setMaxValue(year);

        binding.tvModelYearLabelMin.setText(AppConstants.CAR_APPROVED_YEAR_MIN + "");
        binding.tvModelYearLabelMax.setText(year + "");

        binding.llFilterByRegion.setVisibility(View.VISIBLE);
        binding.llMileage.setVisibility(View.VISIBLE);
    }

    private void setClassicCarFilter() {
        binding.rangeSeekbarModelYear.setMinValue(AppConstants.CAR_YEAR_MIN);
        binding.rangeSeekbarModelYear.setMaxValue(AppConstants.CAR_YEAR_MIN_RANGE);

        binding.tvModelYearLabelMin.setText(AppConstants.CAR_YEAR_MIN + "");
        binding.tvModelYearLabelMax.setText(AppConstants.CAR_YEAR_MIN_RANGE + "");

        binding.llFilterByRegion.setVisibility(View.VISIBLE);
        binding.llMileage.setVisibility(View.VISIBLE);
    }

    private void setNewLuxuryCarFilter() {
        filter.setLuxuryNewCars(true);
        binding.tvPriceRangeLabel.setText(mainActivityContext.getResources().getString(R.string.budget));
        binding.tvBodyStyleLabel.setText(mainActivityContext.getResources().getString(R.string.segment));
        binding.llFilterByRegion.setVisibility(View.GONE);
        binding.llTransmission.setVisibility(View.GONE);
        binding.llModelYear.setVisibility(View.GONE);
        binding.llRating.setVisibility(View.VISIBLE);
        binding.llBody.setVisibility(View.VISIBLE);
    }

    private void getCarBodyTypes() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("parent_id", 0);
        WebApiRequest.Instance(mainActivityContext).request(
                AppConstants.WebServicesKeys.GET_CAR_BODY_TYPES,
                binding.getRoot(),
                null,
                params,
                null,
                new WebApiRequest.WebServiceArrayResponse() {
                    @Override
                    public void onSuccess(ApiArrayResponse apiArrayResponse) {
                        arrayList = JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), CarBodyStyle.class);
                        if (arrayList.size() > 0) {
                            filter.setCarBodyStyles(arrayList);
                            adapter.notifyDataSetChanged();
                            setFilterPreset();
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

    private void getRegions() {
        WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.REGIONS, null, null, null, null, new WebApiRequest.WebServiceArrayResponse() {
            @Override
            public void onSuccess(ApiArrayResponse apiArrayResponse) {
                ArrayList<Region> regions = (ArrayList<Region>) JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), Region.class);
                for (int pos = 0; pos < regions.size(); pos++) {
                    regions.get(pos).setIs_my_region(0);
                }
                SelectCountryDialog selectCountryDialog = SelectCountryDialog.newInstance(mainActivityContext);
                selectCountryDialog.setRegions(regions);
                selectCountryDialog.setFromFilter(true);
                selectCountryDialog.setOnRegionsSelectedListener(LuxuryMarketFilterFragment.this);
                selectCountryDialog.show(mainActivityContext.getFragmentManager(), null);

                mainActivityContext.hideLoader();
            }

            @Override
            public void onError() {
                mainActivityContext.hideLoader();

            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() == 0) {
            filter.setVersionName(null);
            binding.ivRemoveVersionName.setVisibility(View.INVISIBLE);
        }
        if (s.toString().length() > 0) {
            filter.setVersionName(s.toString());
            binding.ivRemoveVersionName.setVisibility(View.VISIBLE);
            filter.setFilter(true);
        }
    }
}
