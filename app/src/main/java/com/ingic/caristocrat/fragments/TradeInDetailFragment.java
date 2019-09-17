package com.ingic.caristocrat.fragments;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.SubcategoriesFeatureAdapter;
import com.ingic.caristocrat.adapters.TradeInOffersAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentTradeInDetailBinding;
import com.ingic.caristocrat.dialogs.CallConsultantDialog;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.helpers.Utils;
import com.ingic.caristocrat.models.TradeCar;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.models.Media;
import com.ingic.caristocrat.webhelpers.models.News;
import com.ingic.caristocrat.webhelpers.models.TradedCars;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 */
public class TradeInDetailFragment extends BaseFragment implements View.OnClickListener {
    FragmentTradeInDetailBinding binding;
    SubcategoriesFeatureAdapter featureAdapter;
    ArrayList<News> featuredArticleArrayList;
    TradeCar currentTradeCar;
    TradeInOffersAdapter tradeInOffersAdapter;
    double offeredAmount;
    boolean fromNotification;
    int refId;
    TradedCars tradedCars;
    String screenType, actionType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trade_in_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        featureAdapter = new SubcategoriesFeatureAdapter(mainActivityContext, null, false);
        binding.viewpager.setAdapter(featureAdapter);

        binding.tvName.setOnClickListener(this);
        if (screenType != null) {
            if (screenType.equals(AppConstants.MyTradeInScreenTypes.EVALUATION)) {
                binding.llTopLayout.setOnClickListener(view1 -> {
                    TradeInYourCarFragment tradeInYourCarFragment = new TradeInYourCarFragment();
                    tradeInYourCarFragment.setProfile(true);
                    tradeInYourCarFragment.setEdit(true);
                    tradeInYourCarFragment.setTradeCar(currentTradeCar);
                    mainActivityContext.replaceFragment(tradeInYourCarFragment, TradeInYourCarFragment.class.getSimpleName(), true, false);
                });
            }
        }

        if (fromNotification) {
            getTradeInDetailFromNotification(refId);
        }
        binding.ibBackbtn.setOnClickListener(this);
        binding.ibCallConsultant.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppBarLayout.LayoutParams params = new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT);
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED | AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
        mainActivityContext.getCollapsingToolBarLayout().setLayoutParams(params);
        mainActivityContext.getCollapsingToolBarLayout().requestLayout();
        mainActivityContext.getRvSubCategoryItem().setVisibility(View.GONE);
        mainActivityContext.getCollapsingToolBarLayout().setVisibility(View.VISIBLE);
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(mainActivityContext);
        titlebar.showBackButton(mainActivityContext, true);
        titlebar.showTransparentTitlebar(mainActivityContext);
        mainActivityContext.getCollapsingToolBarLayout().setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibBackbtn:
                mainActivityContext.onBackPressed();
                break;

            case R.id.tvName:
                if (screenType != null) {
                    if (screenType.equals(AppConstants.MyTradeInScreenTypes.TRADE_INS)) {
                        if (currentTradeCar != null) {
                            LuxuryMarketDetailsFragment luxuryMarketDetailsFragment = new LuxuryMarketDetailsFragment();
                            if (currentTradeCar.getCategory() != null) {
                                luxuryMarketDetailsFragment.setCategoryKey(currentTradeCar.getCategory().getSlug());
                            }
                            luxuryMarketDetailsFragment.setCurrentTradeCar(currentTradeCar);
                            mainActivityContext.replaceFragment(luxuryMarketDetailsFragment, LuxuryMarketDetailsFragment.class.getSimpleName(), true, true);
                        }
                    } else {
                        binding.llTopLayout.performClick();
                    }
                }
                break;

            case R.id.ibCallConsultant:
                if (currentTradeCar == null) {
                    UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.no_data_found), Toast.LENGTH_LONG);
                    return;
                }
                IntrectionCall(currentTradeCar.getId(), AppConstants.Interaction.PHONE);
//                if (currentTradeCar != null) {
//                    if (currentTradeCar.getUser() != null) {
//                        if (currentTradeCar.getUser().getShowroom_details() != null) {
//                            if (currentTradeCar.getUser().getShowroom_details().getName() != null && currentTradeCar.getUser().getShowroom_details().getPhone() != null) {
//                                callConsultant(currentTradeCar.getUser().getShowroom_details().getName(), currentTradeCar.getUser().getShowroom_details().getPhone());
//                            }
//                        }
//                    }
//                }
                if (currentTradeCar.getCategory() != null) {
                    if (currentTradeCar.getCategory().getSlug().equals(AppConstants.MainCategoriesType.LUXURY_NEW_CARS)) {
                        if (tradedCars != null && tradedCars.getDealerInfo() != null && tradedCars.getDealerInfo().getShowroom_details() != null) {
                            UIHelper.setImageWithGlide(mainActivityContext, binding.rivProfilePic, tradedCars.getDealerInfo().getShowroom_details().getLogoUrl());

                            if (tradedCars.getDealerInfo().getShowroom_details().getName() != null) {
                                binding.tvVendorName.setText(tradedCars.getDealerInfo().getShowroom_details().getName());
                            }
                        }
                    } else {
                        if (currentTradeCar.getUser() != null) {
                            if (currentTradeCar.getUser().getShowroom_details() != null && currentTradeCar.getUser().getShowroom_details().getLogoUrl() != null) {
                                UIHelper.setImageWithGlide(mainActivityContext, binding.rivProfilePic, currentTradeCar.getUser().getShowroom_details().getLogoUrl());

                                if (currentTradeCar.getUser().getShowroom_details().getName() != null) {
                                    binding.tvVendorName.setText(currentTradeCar.getUser().getShowroom_details().getName());
                                }
                            }
                        }
                    }
                }
                break;
        }
    }

    public void setCurrentTradeCar(TradeCar currentTradeCar) {
        this.currentTradeCar = currentTradeCar;
    }

    public void setOfferedAmount(double offeredAmount) {
        this.offeredAmount = offeredAmount;
    }

    private void getTradeInDetail(int id) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.MY_TRADE_INS_DETAIL, binding.getRoot(), null, params, new WebApiRequest.WebServiceObjectResponse() {
            @Override
            public void onSuccess(ApiResponse apiResponse) {
                currentTradeCar = (TradeCar) JsonHelpers.convertToModelClass(apiResponse.getData(), TradeCar.class);
                setValues(currentTradeCar);
                mainActivityContext.hideLoader();
            }

            @Override
            public void onError() {
                mainActivityContext.hideLoader();
            }
        }, null);
    }

    private void getTradeInDetailFromNotification(int id) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.GET_TRADED_IN_CAR, binding.getRoot(), null, params, new WebApiRequest.WebServiceObjectResponse() {
            @Override
            public void onSuccess(ApiResponse apiResponse) {
                tradedCars = (TradedCars) JsonHelpers.convertToModelClass(apiResponse.getData(), TradedCars.class);
                if (screenType != null) {
                    if (screenType.equals(AppConstants.MyTradeInScreenTypes.TRADE_INS)) {
                        currentTradeCar = tradedCars.getMyCar();
                        setOfferedAmount(tradedCars.getAmount());
                        setValues(tradedCars.getMyCar());
                        setMyCarDetail(tradedCars.getTradeAgainstCar());
                    } else if (screenType.equals(AppConstants.MyTradeInScreenTypes.EVALUATION)) {
                        currentTradeCar = tradedCars.getTradeAgainstCar();
                        setOfferedAmount(tradedCars.getAmount());
                        setValues(tradedCars.getTradeAgainstCar());
//                        setMyCarDetail(tradedCars.getTradeAgainstCar());
                    }
                }

                mainActivityContext.hideLoader();
            }

            @Override
            public void onError() {
                mainActivityContext.hideLoader();
            }
        }, null);
    }

    private void setValues(TradeCar result) {
        if (result != null) {
            binding.tvName.setText(Utils.getCarNameByBrand(result, false));

            if (result.getYear() != null && result.getKilometre() != null && result.getChassis() != null) {
                binding.tvAttributes.setText(result.getYear() + " - " + NumberFormat.getNumberInstance(Locale.US).format(result.getKilometre()) + " " + mainActivityContext.getResources().getString(R.string.km) + " - " + mainActivityContext.getResources().getString(R.string.chassis) + " " + result.getChassis());
            } else if (result.getYear() != null && result.getKilometre() != null) {
                binding.tvAttributes.setText(result.getYear() + " - " + NumberFormat.getNumberInstance(Locale.US).format(result.getKilometre()) + " " + mainActivityContext.getResources().getString(R.string.km));
            } else if (result.getYear() != null && result.getChassis() != null) {
                binding.tvAttributes.setText(result.getYear() + " - " + mainActivityContext.getResources().getString(R.string.chassis) + " " + result.getChassis());
            } else if (result.getYear() != null) {
                binding.tvAttributes.setText(result.getYear() + "");
            }

            if (result.getMedia() != null && result.getMedia().size() > 0) {
                setFeaturedNews(result.getMedia());
            }

            if (screenType.equals(AppConstants.MyTradeInScreenTypes.TRADE_INS)) {
                binding.tvOfferTitle.setText(mainActivityContext.getResources().getString(R.string.dealers));
            }

//            tradedCars.setOfferDetails(new ArrayList<>()); ////////
//            tradedCars.setExpired(false); //////////

            if (tradedCars != null && tradedCars.getOfferDetails() != null && tradedCars.getOfferDetails().size() > 0) {
                TradeInOffersAdapter tradeInOffersAdapter = new TradeInOffersAdapter(mainActivityContext);
                tradeInOffersAdapter.setExpired(tradedCars.isExpired());
                binding.rvtradeOffers.setLayoutManager(new LinearLayoutManager(mainActivityContext));
                binding.rvtradeOffers.setAdapter(tradeInOffersAdapter);
                tradeInOffersAdapter.addAll(tradedCars.getOfferDetails());
                tradeInOffersAdapter.notifyDataSetChanged();
                binding.llBidsInformation.setVisibility(View.VISIBLE);
            }

            if (tradedCars.isExpired()) {
                binding.tvNoOffers.setText(mainActivityContext.getResources().getString(R.string.no_offer_available));
            } else {
                binding.tvNoOffers.setText(mainActivityContext.getResources().getString(R.string.waiting_for_offer));
                if (screenType.equals(AppConstants.MyTradeInScreenTypes.TRADE_INS)) {
                } else {
                    binding.tvOfferTitle.setVisibility(View.GONE);
                    binding.rvtradeOffers.setVisibility(View.GONE);
                    binding.tvNoOffers.setVisibility(View.VISIBLE);
                }
            }
/*
            if (tradedCars != null && tradedCars.getOfferDetails() != null && tradedCars.getOfferDetails().size() > 0) {
                TradeInOffersAdapter tradeInOffersAdapter = new TradeInOffersAdapter(mainActivityContext);
                tradeInOffersAdapter.setExpired(tradedCars.isExpired());
                binding.rvtradeOffers.setLayoutManager(new LinearLayoutManager(mainActivityContext));
                binding.rvtradeOffers.setAdapter(tradeInOffersAdapter);
                tradeInOffersAdapter.addAll(tradedCars.getOfferDetails());
                tradeInOffersAdapter.notifyDataSetChanged();
                binding.llBidsInformation.setVisibility(View.VISIBLE);
            } else {
                binding.tvOfferTitle.setVisibility(View.GONE);
                binding.rvtradeOffers.setVisibility(View.GONE);
                if (tradedCars.isExpired()) {
                    binding.tvNoOffers.setText(mainActivityContext.getResources().getString(R.string.no_offer_available));
                } else {
                    binding.tvNoOffers.setText(mainActivityContext.getResources().getString(R.string.waiting_for_offer));
                }
                binding.tvNoOffers.setVisibility(View.VISIBLE);
                binding.llBidsInformation.setVisibility(View.VISIBLE);
            }
*/
        }
    }

    private void setMyCarDetail(TradeCar tradeCar) {
        if (tradeCar != null) {
            binding.tvMyCarName.setText(Utils.getCarNameByBrand(tradeCar, false));
            if (tradeCar.getMedia() != null && tradeCar.getMedia().size() > 0) {
                UIHelper.setImageWithGlide(mainActivityContext, binding.rivMyCarPic, tradeCar.getMedia().get(0).getFileUrl());
            }

            String yearChassis = "";

            yearChassis += mainActivityContext.getResources().getString(R.string.model) + " " + Utils.getCarModelName(tradeCar);

//            if (tradeCar.getYear() != null) {
//                yearChassis += mainActivityContext.getResources().getString(R.string.model_year) + " " + tradeCar.getYear();
//            }

            if (tradeCar.getChassis() != null) {
                yearChassis += ", " + mainActivityContext.getResources().getString(R.string.chassis) + " " + tradeCar.getChassis();
            }
            if (yearChassis.length() > 0) {
                binding.tvModelYearChassisNumber.setText(yearChassis);
                binding.tvModelYearChassisNumber.setVisibility(View.VISIBLE);
            }

            binding.llMyInformation.setVisibility(View.VISIBLE);

            binding.llMyCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TradeInYourCarFragment tradeInYourCarFragment = new TradeInYourCarFragment();
                    tradeInYourCarFragment.setProfile(true);
                    tradeInYourCarFragment.setEdit(true);
                    tradeInYourCarFragment.setTradeCar(tradeCar);
                    mainActivityContext.replaceFragment(tradeInYourCarFragment, TradeInYourCarFragment.class.getSimpleName(), true, false);
                }
            });

        }
    }

    private void setFeaturedNews(ArrayList<Media> media) {
        featuredArticleArrayList = new ArrayList<News>();
//        for (int i = 1; i < 4; i++) {
//            Media media = new Media();
//            media.setId(i);
//            media.setResourceId(R.drawable.image_3);
//            ArrayList<Media> mediaArrayList = new ArrayList<>();
//            mediaArrayList.add(media);
//
//            News news = new News();
//            news.setId(1);
//            news.setMedia(mediaArrayList);
//            featuredArticleArrayList.add(news);
//        }
        for (int i = 0; i < media.size(); i++) {
            News news = new News();
            ArrayList<Media> mediaArrayList = new ArrayList<>();
            mediaArrayList.add(media.get(i));
            news.setMedia(mediaArrayList);
            featuredArticleArrayList.add(news);
        }
        featureAdapter.addAll(featuredArticleArrayList);
        featureAdapter.notifyDataSetChanged();
        binding.circlePageIndicator.setViewPager(binding.viewpager);
    }

    public void visibleView() {
        binding.rvtradeOffers.setVisibility(View.VISIBLE);
//        binding.noDataLayout.setVisibility(View.GONE);
    }

    public void hideView() {
        binding.rvtradeOffers.setVisibility(View.GONE);
//        binding.noDataLayout.setVisibility(View.VISIBLE);
    }

    private void callConsultant(String name, final String number) {
        CallConsultantDialog callConsultantDialog = CallConsultantDialog.newInstance(mainActivityContext, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                IntrectionCall(currentTradeCar.getId(), AppConstants.Interaction.PHONE);
            }
        });
        callConsultantDialog.setConsultantNameAndPhone(mainActivityContext.getResources().getString(R.string.call) + " " + name, number);
        callConsultantDialog.show(mainActivityContext.getFragmentManager(), null);
    }

    public void setFromNotification(boolean fromNotification) {
        this.fromNotification = fromNotification;
    }

    public void setRefId(int refId) {
        this.refId = refId;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
}
