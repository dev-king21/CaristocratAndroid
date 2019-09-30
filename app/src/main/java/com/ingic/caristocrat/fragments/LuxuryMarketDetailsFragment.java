package com.ingic.caristocrat.fragments;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.DetailedReviewAdapter;
import com.ingic.caristocrat.adapters.RegionalPriceAdapter;
import com.ingic.caristocrat.adapters.ShowroomDetailAdapter;
import com.ingic.caristocrat.adapters.SimilarListingsAdapter;
import com.ingic.caristocrat.adapters.SubcategoriesFeatureAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentLuxuryMarketDetailsBinding;
import com.ingic.caristocrat.dialogs.CallConsultantDialog;
import com.ingic.caristocrat.dialogs.ReportListingDialog;
import com.ingic.caristocrat.dialogs.RequestConsultancyDialogFragment;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.RecyclerTouchListener;
import com.ingic.caristocrat.helpers.SectionedExpandableLayoutHelper;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.helpers.Utils;
import com.ingic.caristocrat.interfaces.ClickListenerRecycler;
import com.ingic.caristocrat.interfaces.ItemClickListener;
import com.ingic.caristocrat.interfaces.OnCarSelectedForTradeListener;
import com.ingic.caristocrat.interfaces.OnRequestConsultancy;
import com.ingic.caristocrat.interfaces.SimpleDialogActionListener;
import com.ingic.caristocrat.models.MyCarAttributes;
import com.ingic.caristocrat.models.RatingAttribute;
import com.ingic.caristocrat.models.ReportListing;
import com.ingic.caristocrat.models.ReviewDetail;
import com.ingic.caristocrat.models.Section;
import com.ingic.caristocrat.models.ShowroomDetails;
import com.ingic.caristocrat.models.TradeCar;
import com.ingic.caristocrat.models.UserRating;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.models.Media;
import com.ingic.caristocrat.webhelpers.models.News;
import com.ingic.caristocrat.webhelpers.models.TradeInCar;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LuxuryMarketDetailsFragment extends BaseFragment implements View.OnClickListener, ItemClickListener, OnRequestConsultancy, OnCarSelectedForTradeListener {
    FragmentLuxuryMarketDetailsBinding binding;
    SubcategoriesFeatureAdapter featureAdapter;
    ArrayList<News> featuredArticleArrayList;
    SectionedExpandableLayoutHelper sectionedExpandableLayoutHelperDesc, sectionedExpandableLayoutHelperFuel, sectionedExpandableLayoutHelperEmission, sectionedExpandableLayoutHelperWarranty;
    SectionedExpandableLayoutHelper sectionedExpandableLayoutHelperSpecs, sectionedExpandableLayoutHelperTransmission, sectionedExpandableLayoutHelperBrakes, sectionedExpandableLayoutHelperSuspension, sectionedExpandableLayoutHelperWheelsTyres;
    SectionedExpandableLayoutHelper sectionedExpandableLayoutHelperDimensWeight, sectionedExpandableLayoutHelperSeatingCapacity, sectionedExpandableLayoutHelperDriveTrain, sectionedExpandableLayoutHelperEngine, sectionedExpandableLayoutHelperPerformance;
    RegionalPriceAdapter regionalPriceAdapter;
    ArrayList<TradeCar> similarListings;
    TradeCar currentTradeCar;
    int categoryId;
    RequestConsultancyDialogFragment requestConsultancyDialogFragment;
    PopupWindow popup;
    String categoryKey;
    private SimilarListingsAdapter similarListingsAdapter;
    private DetailedReviewAdapter detailedReviewAdapter;
    private ShowroomDetailAdapter showroomDetailAdapter;
    ArrayList<ReviewDetail> reviewDetails = new ArrayList<>();
    private double itemWidth;

    public LuxuryMarketDetailsFragment() {
    }

    public static LuxuryMarketDetailsFragment Instance() {
        return new LuxuryMarketDetailsFragment();
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_luxury_market_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        featureAdapter = new SubcategoriesFeatureAdapter(mainActivityContext, null, false);
        binding.viewpager.setAdapter(featureAdapter);
        if (categoryKey != null && categoryKey.equals(AppConstants.MainCategoriesType.LUXURY_NEW_CARS)) {
            initAdaptersForLuxuryNew();
        }

        itemWidth = UIHelper.screensize(mainActivityContext, "x") / 2.45;

        initSpecificationsAdapter();
        initDescriptionAdapter();
        initSimilarListingsAdapter();

        detailedReviewAdapter = new DetailedReviewAdapter(mainActivityContext);
        detailedReviewAdapter.setFromLuxuryDetails(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivityContext);
        binding.rvReviews.setLayoutManager(linearLayoutManager);
        binding.rvReviews.setNestedScrollingEnabled(false);
        binding.rvReviews.setAdapter(detailedReviewAdapter);

//        setValues(currentTradeCar);
        if (mainActivityContext.showLoader()) {
            if (currentTradeCar != null) {
                getDetail(currentTradeCar.getId());
            } else if (categoryId != 0) {
                getDetail(categoryId);
            }
        }
//        binding.swipeRefresh.setColorSchemeColors(ContextCompat.getColor(mainActivityContext,R.color.colorBlack));
//        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (mainActivityContext.showLoader()) {
//                    if (currentTradeCar != null)
//                        getDetail(currentTradeCar.getId());
//                }
//                binding.swipeRefresh.setRefreshing(false);
//            }
//        });
        setListeners();
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
//        titlebar.resetTitlebar(mainActivityContext);
//        titlebar.showBackButton(mainActivityContext, true);
//        titlebar.showTransparentTitlebar(mainActivityContext);
        mainActivityContext.getCollapsingToolBarLayout().setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRequestConsultancy:
                disableViewsForSomeSeconds(binding.btnRequestConsultancy);
                confirmPersonalShopper();
                break;

            case R.id.ibCallConsultant:
                disableViewsForSomeSeconds(binding.ibCallConsultant);
                IntrectionCall(currentTradeCar.getId(), AppConstants.Interaction.PHONE);
                if (currentTradeCar != null) {
                    if (currentTradeCar.getUser() != null) {
                        if (currentTradeCar.getUser().getShowroom_details() != null) {
                            if (currentTradeCar.getUser().getShowroom_details().getName() != null && currentTradeCar.getUser().getShowroom_details().getPhone() != null) {
                                callConsultant(currentTradeCar.getUser().getShowroom_details().getName(), currentTradeCar.getUser().getShowroom_details().getPhone());
                            }
                        }
                    }
                }
                break;
/*
            case R.id.btnReportThisListing:
                reportthisListing();
                break;
*/
            case R.id.btnVirtualBuy:
//                UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.implemented_later), Toast.LENGTH_SHORT);
                disableViewsForSomeSeconds(binding.btnVirtualBuy);
                virtualBuy();
                break;

            case R.id.btnTradeInYourCar:
                disableViewsForSomeSeconds(binding.btnTradeInYourCar);
                MyCarsDialogFragment myCarsDialogFragment = new MyCarsDialogFragment(mainActivityContext, currentTradeCar);
                mainActivityContext.replaceFragment(myCarsDialogFragment, MyCarsDialogFragment.class.getSimpleName(), true, false);
                /*
                if (!preferenceHelper.getLoginStatus()) {
                    launchSigninRequirement(mainActivityContext, mainActivityContext.getResources().getString(R.string.require_signin_trade));
                } else {
                    tradeInYourCar();
                }
                */
                break;

            case R.id.ibBackbtn:
                mainActivityContext.onBackPressed();
                break;

            case R.id.ibShare:
                share();
                break;

            case R.id.ibAddFav:
                if (!preferenceHelper.getLoginStatus()) {
                    launchSigninRequirement(mainActivityContext, mainActivityContext.getResources().getString(R.string.require_signin_message));
                } else {
                    showFavPopup(binding.ibAddFav);
                }
                break;

            case R.id.tvReadAllReviews:
            case R.id.tvAddReview:
                ReviewDetailsFragment reviewDetailsFragment = new ReviewDetailsFragment();
                reviewDetailsFragment.setTradeCar(currentTradeCar);
                mainActivityContext.replaceFragment(reviewDetailsFragment, ReviewDetailsFragment.class.getSimpleName(), true, false);
                break;
        }
    }

    @Override
    public void itemClicked(MyCarAttributes item) {

    }

    @Override
    public void itemClicked(Section section) {

    }

    @Override
    public void onRequested(String email, String name, String countryCode, String phone, int type, String message) {
        sendRequestConsultancy(email, name, countryCode, phone);
    }

    @Override
    public void onNewCarAdd() {

    }

    @Override
    public void onCarSelected(TradeCar tradeCar) {

    }

    @Override
    public void onCarAdded(TradeCar tradeCar) {
        confirmTradeIn(tradeCar);
    }

    private void initSimilarListingsAdapter() {
        similarListingsAdapter = new SimilarListingsAdapter(mainActivityContext);
        similarListingsAdapter.setWidth(itemWidth);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivityContext, LinearLayoutManager.HORIZONTAL, false);
        binding.rvSimilarListings.setLayoutManager(linearLayoutManager);
        binding.rvSimilarListings.setNestedScrollingEnabled(false);
        binding.rvSimilarListings.setAdapter(similarListingsAdapter);
        binding.rvSimilarListings.addOnItemTouchListener(new RecyclerTouchListener(mainActivityContext, binding.rvSimilarListings, new ClickListenerRecycler() {
            @Override
            public void onClick(View view, int position) {
                LuxuryMarketDetailsFragment luxuryMarketDetailsFragment = new LuxuryMarketDetailsFragment();
                luxuryMarketDetailsFragment.setCurrentTradeCar(similarListingsAdapter.getItem(position));
                luxuryMarketDetailsFragment.setSimilarListings(similarListings);
                if (similarListingsAdapter.getItem(position).getCategory() != null) {
                    luxuryMarketDetailsFragment.setCategoryKey(similarListingsAdapter.getItem(position).getCategory().getSlug());
                }
                mainActivityContext.replaceFragment(luxuryMarketDetailsFragment, LuxuryMarketDetailsFragment.class.getSimpleName(), true, true);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        if (currentTradeCar != null) {
            if (similarListings != null)
                similarListingsAdapter.addAll(similarListings, currentTradeCar.getId());
            if (similarListingsAdapter.getItemCount() > 0)
                visibleView();
            else {
                if (currentTradeCar.getCategory() != null) {
                    silarWebService(currentTradeCar.getCategory().getId());
                }
            }
        }
    }

    private void initSpecificationsAdapter() {
        sectionedExpandableLayoutHelperSpecs = new SectionedExpandableLayoutHelper(mainActivityContext, binding.rvSpecs, this, 2);
        sectionedExpandableLayoutHelperSpecs.addSection(mainActivityContext.getResources().getString(R.string.specifications), new ArrayList<MyCarAttributes>());
//        sectionedExpandableLayoutHelperSpecs.notifyDataSetChanged();
    }

    private void initDescriptionAdapter() {
        sectionedExpandableLayoutHelperDesc = new SectionedExpandableLayoutHelper(mainActivityContext, binding.rvDesc, this, 1);
//        ArrayList<MyCarAttributes> myCarAttributes=new ArrayList<>();
//        MyCarAttributes item=new MyCarAttributes();
//        item.setAttr_name();
//        item.setAttr_id(-1);
//        myCarAttributes.add(item);
        sectionedExpandableLayoutHelperDesc.addSection(mainActivityContext.getResources().getString(R.string.description), new ArrayList<MyCarAttributes>());
        sectionedExpandableLayoutHelperDesc.notifyDataSetChanged();

    }

    private void initAdaptersForLuxuryNew() {
        sectionedExpandableLayoutHelperDimensWeight = new SectionedExpandableLayoutHelper(mainActivityContext, binding.rvDimensionsWeight, this, 2);
        sectionedExpandableLayoutHelperDimensWeight.addSection(mainActivityContext.getResources().getString(R.string.dimensions_weight), new ArrayList<MyCarAttributes>());

        sectionedExpandableLayoutHelperSeatingCapacity = new SectionedExpandableLayoutHelper(mainActivityContext, binding.rvSeatingCapacity, this, 2);
        sectionedExpandableLayoutHelperSeatingCapacity.addSection(mainActivityContext.getResources().getString(R.string.seating_capacity), new ArrayList<MyCarAttributes>());

        sectionedExpandableLayoutHelperDriveTrain = new SectionedExpandableLayoutHelper(mainActivityContext, binding.rvDriveTrain, this, 2);
        sectionedExpandableLayoutHelperDriveTrain.addSection(mainActivityContext.getResources().getString(R.string.drive_train), new ArrayList<MyCarAttributes>());

        sectionedExpandableLayoutHelperEngine = new SectionedExpandableLayoutHelper(mainActivityContext, binding.rvEngine, this, 2);
        sectionedExpandableLayoutHelperEngine.addSection(mainActivityContext.getResources().getString(R.string.engine), new ArrayList<MyCarAttributes>());

        sectionedExpandableLayoutHelperPerformance = new SectionedExpandableLayoutHelper(mainActivityContext, binding.rvPerformance, this, 2);
        sectionedExpandableLayoutHelperPerformance.addSection(mainActivityContext.getResources().getString(R.string.performance), new ArrayList<MyCarAttributes>());

        sectionedExpandableLayoutHelperTransmission = new SectionedExpandableLayoutHelper(mainActivityContext, binding.rvTransmission, this, 2);
        sectionedExpandableLayoutHelperTransmission.addSection(mainActivityContext.getResources().getString(R.string.transmission), new ArrayList<MyCarAttributes>());

        /*sectionedExpandableLayoutHelperBrakes = new SectionedExpandableLayoutHelper(mainActivityContext, binding.rvBrakes, this, 2);
        sectionedExpandableLayoutHelperBrakes.addSection(mainActivityContext.getResources().getString(R.string.brakes), new ArrayList<MyCarAttributes>());

        sectionedExpandableLayoutHelperSuspension = new SectionedExpandableLayoutHelper(mainActivityContext, binding.rvSuspension, this, 2);
        sectionedExpandableLayoutHelperSuspension.addSection(mainActivityContext.getResources().getString(R.string.suspension), new ArrayList<MyCarAttributes>());*/

        sectionedExpandableLayoutHelperWheelsTyres = new SectionedExpandableLayoutHelper(mainActivityContext, binding.rvWheelTyres, this, 2);
        sectionedExpandableLayoutHelperWheelsTyres.addSection(mainActivityContext.getResources().getString(R.string.wheels_tyres), new ArrayList<MyCarAttributes>());

        sectionedExpandableLayoutHelperFuel = new SectionedExpandableLayoutHelper(mainActivityContext, binding.rvFuel, this, 2);
        sectionedExpandableLayoutHelperFuel.addSection(mainActivityContext.getResources().getString(R.string.fuel), new ArrayList<MyCarAttributes>());

        sectionedExpandableLayoutHelperEmission = new SectionedExpandableLayoutHelper(mainActivityContext, binding.rvEmission, this, 2);
        sectionedExpandableLayoutHelperEmission.addSection(mainActivityContext.getResources().getString(R.string.emission), new ArrayList<MyCarAttributes>());

        sectionedExpandableLayoutHelperWarranty = new SectionedExpandableLayoutHelper(mainActivityContext, binding.rvWarrantyMaintainence, this, 2);
        sectionedExpandableLayoutHelperWarranty.addSection(mainActivityContext.getResources().getString(R.string.warranty_maintenance), new ArrayList<MyCarAttributes>());

        regionalPriceAdapter = new RegionalPriceAdapter(mainActivityContext);
//        LinearLayoutManager regionalLayoutManager = new GridLayoutManager(mainActivityContext, 5);
        LinearLayoutManager regionalLayoutManager = new LinearLayoutManager(mainActivityContext, LinearLayoutManager.HORIZONTAL, false);
        binding.rvRegionalPrices.setLayoutManager(regionalLayoutManager);

        int spacingInPixels = mainActivityContext.getResources().getDimensionPixelSize(R.dimen.dp8);
        SpacesItemDecorationAllSideEqual spacesItemDecorationHome = new SpacesItemDecorationAllSideEqual(spacingInPixels);

        binding.rvRegionalPrices.addItemDecoration(spacesItemDecorationHome);
        binding.rvRegionalPrices.setAdapter(regionalPriceAdapter);
    }

    public void visibleView() {
        binding.rvSimilarListings.setVisibility(View.VISIBLE);
        binding.noDataLayout.setVisibility(View.GONE);
    }

    public void hideView() {
        binding.rvSimilarListings.setVisibility(View.GONE);
        binding.noDataLayout.setVisibility(View.VISIBLE);
    }

    private void tradeInYourCar() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("limit", 1);
        params.put("offset", 0);
        if (mainActivityContext.showLoader()) {
            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.MY_TRADE_INS, null, null, params, null, new WebApiRequest.WebServiceArrayResponse() {
                @Override
                public void onSuccess(ApiArrayResponse apiArrayResponse) {
                    ArrayList<TradeCar> tradeCars = (ArrayList<TradeCar>) JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), TradeCar.class);
                    /*
                    if (tradeCars.size() == 0) {
                        TradeInYourCarFragment tradeInYourCarFragment = new TradeInYourCarFragment();
                        tradeInYourCarFragment.setOnCarSelectedForTradeListener(LuxuryMarketDetailsFragment.this);
                        tradeInYourCarFragment.setTrading(true);
                        tradeInYourCarFragment.setTradeCar(currentTradeCar);
                        mainActivityContext.replaceFragment(tradeInYourCarFragment, TradeInYourCarFragment.class.getSimpleName(), true, false);
                    } else {
                        MyCarsDialogFragment myCarsDialogFragment = new MyCarsDialogFragment(mainActivityContext, tradeCars, currentTradeCar, 0);
                        mainActivityContext.replaceFragment(myCarsDialogFragment, MyCarsDialogFragment.class.getSimpleName(), true, false);
                    }
                    */
                    MyCarsDialogFragment myCarsDialogFragment = new MyCarsDialogFragment(mainActivityContext, currentTradeCar);
                    mainActivityContext.replaceFragment(myCarsDialogFragment, MyCarsDialogFragment.class.getSimpleName(), true, false);
                    mainActivityContext.hideLoader();
                }

                @Override
                public void onError() {
                    mainActivityContext.hideLoader();
                }
            });
        }
//        mainActivityContext.replaceFragment(new TradeInYourCarFragment(), TradeInYourCarFragment.class.getSimpleName(), true, true);
    }

    public void setCurrentTradeCar(TradeCar currentTradeCar) {
        this.currentTradeCar = currentTradeCar;
    }

    public void setSimilarListings(ArrayList<TradeCar> similarListings) {
        this.similarListings = similarListings;
    }

    private void setListeners() {
        binding.btnRequestConsultancy.setOnClickListener(this);
        binding.ibCallConsultant.setOnClickListener(this);
//        binding.btnReportThisListing.setOnClickListener(this);
        binding.btnVirtualBuy.setOnClickListener(this);
        binding.ibBackbtn.setOnClickListener(this);
        binding.btnTradeInYourCar.setOnClickListener(this);
        binding.ibShare.setOnClickListener(this);
        binding.ibAddFav.setOnClickListener(this);
        binding.tvAddReview.setOnClickListener(this);
        binding.tvReadAllReviews.setOnClickListener(this);
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

    private void requestConsultancy() {
        /*
        requestConsultancyDialogFragment = new RequestConsultancyDialogFragment(mainActivityContext, new OnRequestConsultancy() {
            @Override
            public void onRequested(String email, String name, String countryCode, String phone) {
                sendRequestConsultancy(email, name, countryCode, phone);
            }
        });
        requestConsultancyDialogFragment.show(mainActivityContext.getSupportFragmentManager(), RequestConsultancyDialogFragment.class.getSimpleName());
        */
        RequestConsultancyDialogFragment requestConsultancyDialogFragment = new RequestConsultancyDialogFragment(mainActivityContext, this);
        requestConsultancyDialogFragment.setTradeCar(currentTradeCar);
        requestConsultancyDialogFragment.setType(AppConstants.ContactType.MY_SHOPPER);
        mainActivityContext
                .getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(mainActivityContext.getMainFrameLayoutID(), requestConsultancyDialogFragment, RequestConsultancyDialogFragment.class.getSimpleName())
                .addToBackStack(mainActivityContext.getSupportFragmentManager().getBackStackEntryCount() == 0 ? "firstFrag" : null)
                .commit();
    }

    private void callConsultant(String name, final String number) {
        CallConsultantDialog callConsultantDialog = CallConsultantDialog.newInstance(mainActivityContext, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                IntrectionCall(currentTradeCar.getId(), AppConstants.Interaction.PHONE);
            }
        });
        callConsultantDialog.setConsultantNameAndPhone(mainActivityContext.getResources().getString(R.string.would_you_like) + " " + name, number);
        callConsultantDialog.show(mainActivityContext.getFragmentManager(), null);
    }

    private void reportthisListing() {
//        UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.implemented_later), Toast.LENGTH_SHORT);
        final ReportListingDialog reportListingDialog = ReportListingDialog.newInstance(mainActivityContext);
        reportListingDialog.show(mainActivityContext.getFragmentManager(), null);
        reportListingDialog.setReportListener((report) -> {
            if (currentTradeCar != null)
                reportReq(currentTradeCar.getId(), report, reportListingDialog);
        });

    }

    private void virtualBuy() {

        mainActivityContext.replaceFragment(VirtualBuyFragment.Instance(currentTradeCar), VirtualBuyFragment.class.getSimpleName(), true, false);

//        mainActivityContext.replaceFragment(FinanceBuyFragment.Instance(currentTradeCar), FinanceBuyFragment.class.getSimpleName(), true, false);
    }

    private void share() {
        if (currentTradeCar.getName() != null) {
            if (currentTradeCar.getMedia() != null) {
                if (currentTradeCar.getMedia().size() > 0) {
                    if (mainActivityContext.internetConnected()) {
                        if (currentTradeCar.getDeeplink() != null) {
                            Utils.share(mainActivityContext, currentTradeCar.getName(), currentTradeCar.getDeeplink(), currentTradeCar.getMedia().get(0).getFileUrl());
                        } else {
                            Utils.share(mainActivityContext, currentTradeCar.getName(), AppConstants.WEB_URL, currentTradeCar.getMedia().get(0).getFileUrl());
                        }
                    }
                } else {
                    UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.cannot_share_image), Toast.LENGTH_SHORT);
                }
            } else {
                UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.cannot_share_image), Toast.LENGTH_SHORT);
            }
        } else {
            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.cannot_share_name), Toast.LENGTH_SHORT);
        }
    }

    private void getDetail(int id) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.LUXURY_MARKET_CATEGORY_DETAIL, null, null, params, new WebApiRequest.WebServiceObjectResponse() {
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

    private void reportReq(int id, String message, final ReportListingDialog reportListingDialog) {
        final ReportListing reportListing = new ReportListing();
        reportListing.setCarId(id);
        reportListing.setMessage(message);
        WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.REPORT_REQUESTS, null, reportListing, null, new WebApiRequest.WebServiceObjectResponse() {
            @Override
            public void onSuccess(ApiResponse apiResponse) {
                mainActivityContext.hideLoader();
                UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.listing_reported), Toast.LENGTH_LONG);
                reportListingDialog.dismiss();
            }

            @Override
            public void onError() {
                mainActivityContext.hideLoader();
            }
        }, null);
    }

    private void setValues(TradeCar result) {
        if (result != null) {
            this.currentTradeCar = result;
//            binding.ibLikeWhite.setLiked(result.isIs_favorite());

            binding.tvName.setText(Utils.getCarName(result, false));

            if (result.getYear() != null) {
                binding.tvAttributes.setText(result.getYear() + "");
            }

            if (result.getKilometre() != null) {
                binding.rivSeparator.setVisibility(View.VISIBLE);
                binding.tvMileage.setText(NumberFormat.getNumberInstance(Locale.US).format(result.getKilometre()) + " KM");
            }

            if (result.getAmount() != null) {
                if (categoryKey != null) {
                    if (categoryKey.equals(AppConstants.MainCategoriesType.THE_OUTLET_MALL) || categoryKey.equals(AppConstants.MainCategoriesType.APPROVED_PRE_OWNED) || categoryKey.equals(AppConstants.MainCategoriesType.CLASSIC_CARS)) {
//                        binding.tvAmount.setText(mainActivityContext.getResources().getString(R.string.original_price) + " " + mainActivityContext.getResources().getString(R.string.AED) + " " + NumberFormat.getNumberInstance(Locale.US).format(result.getAmount()));
                        binding.llAttributes.setVisibility(View.GONE);
                        binding.llOutletMall.setVisibility(View.GONE);

                        if (result.getYear() != null) {
                            binding.tvApprovedYear.setText(result.getYear() + "");
                        }

                        if (result.getKilometre() != null) {
                            binding.rivSeparator.setVisibility(View.VISIBLE);
                            binding.tvApprovedMileage.setText(NumberFormat.getNumberInstance(Locale.US).format(result.getKilometre()) + " KM");
                        }

                        binding.tvApprovedPrice.setText((result.getCurrency() == null ? mainActivityContext.getCurrency() : result.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(result.getAmount()));
/*
                        if (result.getAmount_mkp() != null) {
                            binding.tvApprovedPrice.setText((result.getCurrency() == null ? mainActivityContext.getCurrency() : result.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(result.getAmount_mkp()));
                        } else {
                            binding.tvApprovedPrice.setText((result.getCurrency() == null ? mainActivityContext.getCurrency() : result.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(0));
                        }
*/
                        binding.llApprovedPreowned.setVisibility(View.VISIBLE);

                        if (categoryKey.equals(AppConstants.MainCategoriesType.THE_OUTLET_MALL)) {
                            if (result.getAmount() != null) {
                                binding.tvApprovedPrice.setText((result.getCurrency() == null ? mainActivityContext.getCurrency() : result.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(result.getAmount()));
                            } else {
                                binding.tvApprovedPrice.setText(mainActivityContext.getResources().getString(R.string.AED) + " " + NumberFormat.getNumberInstance(Locale.US).format(0));
                            }
                            binding.rivApprovedSeparator.setVisibility(View.GONE);
                            binding.tvApprovedMileage.setVisibility(View.GONE);
                        }

                    } else {
                        binding.tvAmount.setText((result.getCurrency() == null ? mainActivityContext.getCurrency() : result.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(result.getAmount()));
                    }
                }

                if (result.getUser() != null && result.getUser().getShowroom_details() != null) {
                    binding.tvConsultantName.setText(!UIHelper.isEmptyOrNull(result.getUser().getShowroom_details().getName()) ? result.getUser().getShowroom_details().getName() : "");
                    if (result.getUser().getShowroom_details().getAddress() != null && !result.getUser().getShowroom_details().getAddress().equals("")) {
                        binding.tvAddress.setText(result.getUser().getShowroom_details().getAddress());
                    } else {
                        binding.tvAddress.setVisibility(View.GONE);
                    }
                    if (result.getUser().getShowroom_details().getLogoUrl() != null) {
                        UIHelper.setImageWithGlide(mainActivityContext, binding.rivProfilePic, result.getUser().getShowroom_details().getLogoUrl());
                    }
                    binding.llShowroomDetails.setVisibility(View.VISIBLE);
                }

                if (result.getCategory().getSlug().equals(AppConstants.MainCategoriesType.LUXURY_NEW_CARS)) {

                    binding.tvReviewSection.setText(mainActivityContext.getResources().getString(R.string.consumer_reviews) + " (" + result.getReview_count() + ")");
                    binding.tvRating.setText(" (" + result.getAverage_rating() + ")");

                    binding.tvAmount.setVisibility(View.GONE);
                    binding.llShowroomDetails.setVisibility(View.GONE);
                    binding.tvLimitedAmount.setText((result.getCurrency() == null ? mainActivityContext.getCurrency() : result.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(result.getAmount()));
                    binding.tvLimitedAmount.setVisibility(View.VISIBLE);
                    binding.llLimitedAmount.setVisibility(View.VISIBLE);

                    showroomDetailAdapter = new ShowroomDetailAdapter(mainActivityContext, currentTradeCar);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivityContext);
                    binding.rvShowroomDetails.setLayoutManager(linearLayoutManager);
                    binding.rvShowroomDetails.setAdapter(showroomDetailAdapter);

                    if (result.getDealers() != null && result.getDealers().size() > 0) {
                        ArrayList<ShowroomDetails> showroomDetails = new ArrayList<>();
                        for (int pos = 0; pos < result.getDealers().size(); pos++) {
                            if (result.getDealers().get(pos).getShowroom_details() != null) {
                                ShowroomDetails showroomDetail = result.getDealers().get(pos).getShowroom_details();
                                showroomDetails.add(showroomDetail);
                            }
                        }
                        showroomDetailAdapter.addAll(showroomDetails);
                        binding.llShowroomDetails.setVisibility(View.GONE);
                        binding.llMultipleShowRoomDetails.setVisibility(View.VISIBLE);
                        showroomDetailAdapter.notifyDataSetChanged();
                    } else {
                        binding.btnTradeInYourCar.setAlpha((float) 0.5);
                        binding.btnTradeInYourCar.setEnabled(false);
//                        binding.viewActionButtonsSeparator.setVisibility(View.GONE);
                    }
                }
            }
/*
            if (result.getAmount_mkp() != null) {
                binding.tvAnotherAmount.setText(mainActivityContext.getResources().getString(R.string.AED) + " " + NumberFormat.getNumberInstance(Locale.US).format(result.getAmount_mkp()));
                binding.tvAnotherAmount.setVisibility(View.VISIBLE);
            }
*/

            if (result.getMedia() != null && result.getMedia().size() > 0) {
                setFeaturedNews(result.getMedia());
            }
            if (result.getSpecification() != null && result.getSpecification().size() > 0) {
                sectionedExpandableLayoutHelperSpecs.addAll(mainActivityContext.getResources().getString(R.string.specifications), result.getSpecification());
                sectionedExpandableLayoutHelperSpecs.notifyDataSetChanged();
            }
            setCarInteraction(AppConstants.CarInteractions.VIEW);

            if (!UIHelper.isEmptyOrNull(currentTradeCar.getNotes())) {
//                ArrayList<MyCarAttributes> myCarAttributes = new ArrayList<>();
                MyCarAttributes item = new MyCarAttributes();
                item.setAttr_name(currentTradeCar.getNotes());
                item.setAttr_id(-1);
//                myCarAttributes.add(item);
                sectionedExpandableLayoutHelperDesc.addItem(mainActivityContext.getResources().getString(R.string.description), item);
                sectionedExpandableLayoutHelperDesc.notifyDataSetChanged();
            }

            if (categoryKey != null && categoryKey.equals(AppConstants.MainCategoriesType.LUXURY_NEW_CARS)) {
                if (result.getIs_reviewed() == 1) {
                    binding.tvAddReview.setVisibility(View.GONE);
                }
                binding.llReviews.setVisibility(View.VISIBLE);
                binding.llSpecifications.setVisibility(View.GONE);
                if (result.getLimitedEditionSpecsArray() != null) {
                    if (result.getLimitedEditionSpecsArray().getDimensionsWeight() != null && result.getLimitedEditionSpecsArray().getDimensionsWeight().size() > 0) {
                        ArrayList<MyCarAttributes> convertedAttributes = new ArrayList<>();
                        for (int pos = 0; pos < result.getLimitedEditionSpecsArray().getDimensionsWeight().size(); pos++) {
                            MyCarAttributes myCarAttributes = new MyCarAttributes();
                            myCarAttributes.setAttr_name(result.getLimitedEditionSpecsArray().getDimensionsWeight().get(pos).getName());
                            if (result.getLimitedEditionSpecsArray().getDimensionsWeight().get(pos).getName().equals(AppConstants.CarSpecsLuxuryNew.HEIGHT) ||
                                    result.getLimitedEditionSpecsArray().getDimensionsWeight().get(pos).getName().equals(AppConstants.CarSpecsLuxuryNew.WIDTH) || result.getLimitedEditionSpecsArray().getDimensionsWeight().get(pos).getName().equals(AppConstants.CarSpecsLuxuryNew.LENGTH)) {
                                myCarAttributes.setAttr_option(result.getLimitedEditionSpecsArray().getDimensionsWeight().get(pos).getValue() + " MM");
                            } else if (result.getLimitedEditionSpecsArray().getDimensionsWeight().get(pos).getName().equals(AppConstants.CarSpecsLuxuryNew.TRUNK)) {
                                myCarAttributes.setAttr_option(result.getLimitedEditionSpecsArray().getDimensionsWeight().get(pos).getValue() + " L");
                            } else if (result.getLimitedEditionSpecsArray().getDimensionsWeight().get(pos).getName().equals(AppConstants.CarSpecsLuxuryNew.WEIGHT)) {
                                myCarAttributes.setAttr_option(result.getLimitedEditionSpecsArray().getDimensionsWeight().get(pos).getValue() + " KG");
                            } else {
                                myCarAttributes.setAttr_option(result.getLimitedEditionSpecsArray().getDimensionsWeight().get(pos).getValue());
                            }
                            myCarAttributes.setCategoryKey(categoryKey);
                            convertedAttributes.add(myCarAttributes);
                        }
                        sectionedExpandableLayoutHelperDimensWeight.addAll(mainActivityContext.getResources().getString(R.string.dimensions_weight), convertedAttributes);
                        sectionedExpandableLayoutHelperDimensWeight.notifyDataSetChanged();
                    }

                    if (result.getLimitedEditionSpecsArray().getSeatingCapacity() != null && result.getLimitedEditionSpecsArray().getSeatingCapacity().size() > 0) {
                        ArrayList<MyCarAttributes> convertedAttributes = new ArrayList<>();
                        for (int pos = 0; pos < result.getLimitedEditionSpecsArray().getSeatingCapacity().size(); pos++) {
                            MyCarAttributes myCarAttributes = new MyCarAttributes();
                            myCarAttributes.setAttr_name(result.getLimitedEditionSpecsArray().getSeatingCapacity().get(pos).getName());
                            myCarAttributes.setAttr_option(result.getLimitedEditionSpecsArray().getSeatingCapacity().get(pos).getValue());
                            myCarAttributes.setCategoryKey(categoryKey);
                            convertedAttributes.add(myCarAttributes);
                        }
                        sectionedExpandableLayoutHelperSeatingCapacity.addAll(mainActivityContext.getResources().getString(R.string.seating_capacity), convertedAttributes);
                        sectionedExpandableLayoutHelperSeatingCapacity.notifyDataSetChanged();
                    }

                    if (result.getLimitedEditionSpecsArray().getDrivetrain() != null && result.getLimitedEditionSpecsArray().getDrivetrain().size() > 0) {
                        ArrayList<MyCarAttributes> convertedAttributes = new ArrayList<>();
                        for (int pos = 0; pos < result.getLimitedEditionSpecsArray().getDrivetrain().size(); pos++) {
                            MyCarAttributes myCarAttributes = new MyCarAttributes();
                            myCarAttributes.setAttr_name(result.getLimitedEditionSpecsArray().getDrivetrain().get(pos).getName());
                            myCarAttributes.setAttr_option(result.getLimitedEditionSpecsArray().getDrivetrain().get(pos).getValue());
                            myCarAttributes.setCategoryKey(categoryKey);
                            convertedAttributes.add(myCarAttributes);
                        }
                        if (convertedAttributes.size() == 1) {
                            sectionedExpandableLayoutHelperDriveTrain = new SectionedExpandableLayoutHelper(mainActivityContext, binding.rvDriveTrain, this, 1);
                        } else if (convertedAttributes.size() > 1) {
                            sectionedExpandableLayoutHelperDriveTrain = new SectionedExpandableLayoutHelper(mainActivityContext, binding.rvDriveTrain, this, 2);
                        }
                        sectionedExpandableLayoutHelperDriveTrain.addSection(mainActivityContext.getResources().getString(R.string.drive_train), new ArrayList<MyCarAttributes>());
                        sectionedExpandableLayoutHelperDriveTrain.addAll(mainActivityContext.getResources().getString(R.string.drive_train), convertedAttributes);
                        sectionedExpandableLayoutHelperDriveTrain.notifyDataSetChanged();
                    }

                    if (result.getLimitedEditionSpecsArray().getEngine() != null && result.getLimitedEditionSpecsArray().getEngine().size() > 0) {
                        ArrayList<MyCarAttributes> convertedAttributes = new ArrayList<>();
                        for (int pos = 0; pos < result.getLimitedEditionSpecsArray().getEngine().size(); pos++) {
                            MyCarAttributes myCarAttributes = new MyCarAttributes();
                            myCarAttributes.setAttr_name(result.getLimitedEditionSpecsArray().getEngine().get(pos).getName());
                            if (result.getLimitedEditionSpecsArray().getEngine().get(pos).getName().equals(AppConstants.CarSpecsLuxuryNew.DISPLACEMENT)) {
                                myCarAttributes.setAttr_option(result.getLimitedEditionSpecsArray().getEngine().get(pos).getValue() + " CC");
                            } else {
                                myCarAttributes.setAttr_option(result.getLimitedEditionSpecsArray().getEngine().get(pos).getValue());
                            }
                            myCarAttributes.setCategoryKey(categoryKey);
                            convertedAttributes.add(myCarAttributes);
                        }
                        sectionedExpandableLayoutHelperEngine.addAll(mainActivityContext.getResources().getString(R.string.engine), convertedAttributes);
                        sectionedExpandableLayoutHelperEngine.notifyDataSetChanged();
                    }

                    if (result.getLimitedEditionSpecsArray().getPerformance() != null && result.getLimitedEditionSpecsArray().getPerformance().size() > 0) {
                        ArrayList<MyCarAttributes> convertedAttributes = new ArrayList<>();
                        for (int pos = 0; pos < result.getLimitedEditionSpecsArray().getPerformance().size(); pos++) {
                            MyCarAttributes myCarAttributes = new MyCarAttributes();
                            myCarAttributes.setAttr_name(result.getLimitedEditionSpecsArray().getPerformance().get(pos).getName());
                            if (result.getLimitedEditionSpecsArray().getPerformance().get(pos).getName().equals(AppConstants.CarSpecsLuxuryNew.MAX_SPEED)) {
                                myCarAttributes.setAttr_option(result.getLimitedEditionSpecsArray().getPerformance().get(pos).getValue() + " KM/H");
                            } else if (result.getLimitedEditionSpecsArray().getPerformance().get(pos).getName().equals(AppConstants.CarSpecsLuxuryNew.ACCELERATION)) {
                                myCarAttributes.setAttr_option(result.getLimitedEditionSpecsArray().getPerformance().get(pos).getValue() + " SEC");
                            } else if (result.getLimitedEditionSpecsArray().getPerformance().get(pos).getName().equals(AppConstants.CarSpecsLuxuryNew.TORQUE)) {
                                myCarAttributes.setAttr_option(result.getLimitedEditionSpecsArray().getPerformance().get(pos).getValue() + " NM");
                            } else {
                                myCarAttributes.setAttr_option(result.getLimitedEditionSpecsArray().getPerformance().get(pos).getValue());
                            }
                            myCarAttributes.setCategoryKey(categoryKey);
                            convertedAttributes.add(myCarAttributes);
                        }
                        sectionedExpandableLayoutHelperPerformance.addAll(mainActivityContext.getResources().getString(R.string.performance), convertedAttributes);
                        sectionedExpandableLayoutHelperPerformance.notifyDataSetChanged();
                    }

                    if (result.getLimitedEditionSpecsArray().getTransmission() != null && result.getLimitedEditionSpecsArray().getTransmission().size() > 0) {
                        ArrayList<MyCarAttributes> convertedAttributes = new ArrayList<>();
                        for (int pos = 0; pos < result.getLimitedEditionSpecsArray().getTransmission().size(); pos++) {
                            MyCarAttributes myCarAttributes = new MyCarAttributes();
                            myCarAttributes.setAttr_name(result.getLimitedEditionSpecsArray().getTransmission().get(pos).getName());
                            myCarAttributes.setAttr_option(result.getLimitedEditionSpecsArray().getTransmission().get(pos).getValue());
                            myCarAttributes.setCategoryKey(categoryKey);
                            convertedAttributes.add(myCarAttributes);
                        }
                        sectionedExpandableLayoutHelperTransmission.addAll(mainActivityContext.getResources().getString(R.string.transmission), convertedAttributes);
                        sectionedExpandableLayoutHelperTransmission.notifyDataSetChanged();
                    }

                    /*if (result.getLimitedEditionSpecsArray().getBrakes() != null && result.getLimitedEditionSpecsArray().getBrakes().size() > 0) {
                        ArrayList<MyCarAttributes> convertedAttributes = new ArrayList<>();
                        for (int pos = 0; pos < result.getLimitedEditionSpecsArray().getBrakes().size(); pos++) {
                            MyCarAttributes myCarAttributes = new MyCarAttributes();
                            myCarAttributes.setAttr_name(result.getLimitedEditionSpecsArray().getBrakes().get(pos).getName());
                            myCarAttributes.setAttr_option(result.getLimitedEditionSpecsArray().getBrakes().get(pos).getValue());
                            myCarAttributes.setCategoryKey(categoryKey);
                            convertedAttributes.add(myCarAttributes);
                        }
                        if (convertedAttributes.size() == 1) {
                            sectionedExpandableLayoutHelperBrakes = new SectionedExpandableLayoutHelper(mainActivityContext, binding.rvBrakes, this, 1);
                        } else if (convertedAttributes.size() > 1) {
                            sectionedExpandableLayoutHelperBrakes = new SectionedExpandableLayoutHelper(mainActivityContext, binding.rvBrakes, this, 2);
                        }
//                        sectionedExpandableLayoutHelperBrakes.addSection(mainActivityContext.getResources().getString(R.string.brakes), new ArrayList<MyCarAttributes>());
//                        sectionedExpandableLayoutHelperBrakes.addAll(mainActivityContext.getResources().getString(R.string.brakes), convertedAttributes);
//                        sectionedExpandableLayoutHelperBrakes.notifyDataSetChanged();
                    }

                    if (result.getLimitedEditionSpecsArray().getSuspension() != null && result.getLimitedEditionSpecsArray().getSuspension().size() > 0) {
                        ArrayList<MyCarAttributes> convertedAttributes = new ArrayList<>();
                        for (int pos = 0; pos < result.getLimitedEditionSpecsArray().getSuspension().size(); pos++) {
                            MyCarAttributes myCarAttributes = new MyCarAttributes();
                            myCarAttributes.setAttr_name(result.getLimitedEditionSpecsArray().getSuspension().get(pos).getName());
                            myCarAttributes.setAttr_option(result.getLimitedEditionSpecsArray().getSuspension().get(pos).getValue());
                            myCarAttributes.setCategoryKey(categoryKey);
                            convertedAttributes.add(myCarAttributes);
                        }
                        if (convertedAttributes.size() == 1) {
                            sectionedExpandableLayoutHelperSuspension = new SectionedExpandableLayoutHelper(mainActivityContext, binding.rvSuspension, this, 1);
                        } else if (convertedAttributes.size() > 1) {
                            sectionedExpandableLayoutHelperSuspension = new SectionedExpandableLayoutHelper(mainActivityContext, binding.rvSuspension, this, 2);
                        }
//                        sectionedExpandableLayoutHelperSuspension.addSection(mainActivityContext.getResources().getString(R.string.suspension), new ArrayList<MyCarAttributes>());
//                        sectionedExpandableLayoutHelperSuspension.addAll(mainActivityContext.getResources().getString(R.string.suspension), convertedAttributes);
//                        sectionedExpandableLayoutHelperSuspension.notifyDataSetChanged();
                    }*/

                    if (result.getLimitedEditionSpecsArray().getWheelsTyres() != null && result.getLimitedEditionSpecsArray().getWheelsTyres().size() > 0) {
                        ArrayList<MyCarAttributes> convertedAttributes = new ArrayList<>();
                        for (int pos = 0; pos < result.getLimitedEditionSpecsArray().getWheelsTyres().size(); pos++) {
                            MyCarAttributes myCarAttributes = new MyCarAttributes();
                            myCarAttributes.setAttr_name(result.getLimitedEditionSpecsArray().getWheelsTyres().get(pos).getName());
                            myCarAttributes.setAttr_option(result.getLimitedEditionSpecsArray().getWheelsTyres().get(pos).getValue());
                            myCarAttributes.setCategoryKey(categoryKey);
                            convertedAttributes.add(myCarAttributes);
                        }
                        sectionedExpandableLayoutHelperWheelsTyres.addAll(mainActivityContext.getResources().getString(R.string.wheels_tyres), convertedAttributes);
                        sectionedExpandableLayoutHelperWheelsTyres.notifyDataSetChanged();
                    }

                    if (result.getLimitedEditionSpecsArray().getFuel() != null && result.getLimitedEditionSpecsArray().getFuel().size() > 0) {
                        ArrayList<MyCarAttributes> convertedAttributes = new ArrayList<>();
                        for (int pos = 0; pos < result.getLimitedEditionSpecsArray().getFuel().size(); pos++) {
                            MyCarAttributes myCarAttributes = new MyCarAttributes();
                            myCarAttributes.setAttr_name(result.getLimitedEditionSpecsArray().getFuel().get(pos).getName());
                            if (result.getLimitedEditionSpecsArray().getFuel().get(pos).getName().equals(AppConstants.CarSpecsLuxuryNew.FUEL_CONSUMBSION)) {
                                myCarAttributes.setAttr_option(result.getLimitedEditionSpecsArray().getFuel().get(pos).getValue() + " L/100 KM");
                            } else {
                                myCarAttributes.setAttr_option(result.getLimitedEditionSpecsArray().getFuel().get(pos).getValue());
                            }
                            myCarAttributes.setCategoryKey(categoryKey);
                            convertedAttributes.add(myCarAttributes);
                        }
                        if (convertedAttributes.size() == 1) {
                            sectionedExpandableLayoutHelperFuel = new SectionedExpandableLayoutHelper(mainActivityContext, binding.rvFuel, this, 1);
                        } else if (convertedAttributes.size() > 1) {
                            sectionedExpandableLayoutHelperFuel = new SectionedExpandableLayoutHelper(mainActivityContext, binding.rvFuel, this, 2);
                        }
                        sectionedExpandableLayoutHelperFuel.addSection(mainActivityContext.getResources().getString(R.string.fuel), new ArrayList<MyCarAttributes>());
                        sectionedExpandableLayoutHelperFuel.addAll(mainActivityContext.getResources().getString(R.string.fuel), convertedAttributes);
                        sectionedExpandableLayoutHelperFuel.notifyDataSetChanged();
                    }

                    if (result.getLimitedEditionSpecsArray().getEmission() != null && result.getLimitedEditionSpecsArray().getEmission().size() > 0) {
                        ArrayList<MyCarAttributes> convertedAttributes = new ArrayList<>();
                        for (int pos = 0; pos < result.getLimitedEditionSpecsArray().getEmission().size(); pos++) {
                            MyCarAttributes myCarAttributes = new MyCarAttributes();
                            myCarAttributes.setAttr_name(result.getLimitedEditionSpecsArray().getEmission().get(pos).getName());
                            if (result.getLimitedEditionSpecsArray().getEmission().get(pos).getName().equals(AppConstants.CarSpecsLuxuryNew.EMISSION)) {
                                myCarAttributes.setAttr_option(result.getLimitedEditionSpecsArray().getEmission().get(pos).getValue() + " gmCO2/KM");
                            } else {
                                myCarAttributes.setAttr_option(result.getLimitedEditionSpecsArray().getEmission().get(pos).getValue());
                            }
                            myCarAttributes.setCategoryKey(categoryKey);
                            convertedAttributes.add(myCarAttributes);
                        }
                        if (convertedAttributes.size() == 1) {
                            sectionedExpandableLayoutHelperEmission = new SectionedExpandableLayoutHelper(mainActivityContext, binding.rvEmission, this, 1);
                        } else if (convertedAttributes.size() > 1) {
                            sectionedExpandableLayoutHelperEmission = new SectionedExpandableLayoutHelper(mainActivityContext, binding.rvEmission, this, 2);
                        }
                        sectionedExpandableLayoutHelperEmission.addSection(mainActivityContext.getResources().getString(R.string.emission), new ArrayList<MyCarAttributes>());
                        sectionedExpandableLayoutHelperEmission.addAll(mainActivityContext.getResources().getString(R.string.emission), convertedAttributes);
                        sectionedExpandableLayoutHelperEmission.notifyDataSetChanged();
                    }

                    if (result.getLimitedEditionSpecsArray().getWarrantyMaintenace() != null && result.getLimitedEditionSpecsArray().getWarrantyMaintenace().size() > 0) {
                        ArrayList<MyCarAttributes> convertedAttributes = new ArrayList<>();
                        for (int pos = 0; pos < result.getLimitedEditionSpecsArray().getWarrantyMaintenace().size(); pos++) {
                            MyCarAttributes myCarAttributes = new MyCarAttributes();
                            myCarAttributes.setAttr_name(result.getLimitedEditionSpecsArray().getWarrantyMaintenace().get(pos).getName());
                            if (result.getLimitedEditionSpecsArray().getWarrantyMaintenace().get(pos).getName().equals(AppConstants.CarSpecsLuxuryNew.WARRANTY)) {
                                myCarAttributes.setAttr_option(result.getLimitedEditionSpecsArray().getWarrantyMaintenace().get(pos).getValue());
                            } else if (result.getLimitedEditionSpecsArray().getWarrantyMaintenace().get(pos).getName().equals(AppConstants.CarSpecsLuxuryNew.MAINTENANCE_PROGRAM)) {
                                myCarAttributes.setAttr_option(result.getLimitedEditionSpecsArray().getWarrantyMaintenace().get(pos).getValue());
                            } else {
                                myCarAttributes.setAttr_option(result.getLimitedEditionSpecsArray().getWarrantyMaintenace().get(pos).getValue());
                            }
                            myCarAttributes.setCategoryKey(categoryKey);
                            convertedAttributes.add(myCarAttributes);
                        }
                        sectionedExpandableLayoutHelperWarranty.addAll(mainActivityContext.getResources().getString(R.string.warranty_maintenance), convertedAttributes);
                        sectionedExpandableLayoutHelperWarranty.notifyDataSetChanged();
                    }

                    if (result.getRegionalPrices() != null && result.getRegionalPrices().size() > 0) {
                        regionalPriceAdapter.addAll(result.getRegionalPrices());
                        regionalPriceAdapter.notifyDataSetChanged();
                    }

                    binding.llLuxuryNewCarsDetails.setVisibility(View.VISIBLE);
                    setProfileProgress();
                    setProfileDereciationProgress(true);

                    if (result.getDepricationAmount() != null && result.getDepricationAmount().size() >= 4) {
                        binding.tvYearOne.setText(result.getDepricationAmount().get(0).getYear_title() + "");
//                        binding.tvYearOnePrice.setText(mainActivityContext.getResources().getString(R.string.AED) + " " + new DecimalFormat("#0.00").format(result.getDepricationAmount().get(0).getAmount()) + "");
                        binding.tvYearOnePrice.setText((result.getCurrency() == null ? mainActivityContext.getCurrency() : result.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(result.getDepricationAmount().get(0).getAmount()) + "");
                        binding.tvYearOnePrice.setSelected(true);

                        binding.tvTwoOne.setText(result.getDepricationAmount().get(1).getYear_title() + "");
//                        binding.tvYearTwoPrice.setText(mainActivityContext.getResources().getString(R.string.AED) + " " + new DecimalFormat("#0.00").format(result.getDepricationAmount().get(1).getAmount()) + "");
                        binding.tvYearTwoPrice.setText((result.getCurrency() == null ? mainActivityContext.getCurrency() : result.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(result.getDepricationAmount().get(1).getAmount()) + "");
                        binding.tvYearTwoPrice.setSelected(true);
                        binding.tvPercentTwo.setText(result.getDepricationAmount().get(1).getPercentage() + " %");
                        binding.tvPercentTwo.setVisibility(View.VISIBLE);

                        binding.tvThreeOne.setText(result.getDepricationAmount().get(2).getYear_title() + "");
//                        binding.tvYearThreePrice.setText(mainActivityContext.getResources().getString(R.string.AED) + " " + new DecimalFormat("#0.00").format(result.getDepricationAmount().get(2).getAmount()) + "");
                        binding.tvYearThreePrice.setText((result.getCurrency() == null ? mainActivityContext.getCurrency() : result.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(result.getDepricationAmount().get(2).getAmount()) + "");
                        binding.tvYearThreePrice.setSelected(true);
                        binding.tvPercentThree.setText(result.getDepricationAmount().get(2).getPercentage() + " %");
                        binding.tvPercentThree.setVisibility(View.VISIBLE);

                        binding.tvYearFour.setText(result.getDepricationAmount().get(3).getYear_title() + "");
//                        binding.tvYearFourPrice.setText(mainActivityContext.getResources().getString(R.string.AED) + " " + new DecimalFormat("#0.00").format(result.getDepricationAmount().get(3).getAmount()) + "");
                        binding.tvYearFourPrice.setText((result.getCurrency() == null ? mainActivityContext.getCurrency() : result.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(result.getDepricationAmount().get(3).getAmount()) + "");
                        binding.tvYearFourPrice.setSelected(true);
                        binding.tvPercentFour.setText(result.getDepricationAmount().get(3).getPercentage() + " %");
                        binding.tvPercentFour.setVisibility(View.VISIBLE);

                        if (result.getDepricationAmount().size() >= 5) {
                            binding.tvYearFive.setText(result.getDepricationAmount().get(4).getYear_title() + "");
                            binding.tvYearFivePrice.setText((result.getCurrency() == null ? mainActivityContext.getCurrency() : result.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(result.getDepricationAmount().get(4).getAmount()) + "");
                            binding.tvYearFivePrice.setSelected(true);
                            binding.llFourthDepreciation.setGravity(Gravity.CENTER);
                            binding.llFifthDepreciation.setVisibility(View.VISIBLE);
                            binding.tvPercentFive.setText(result.getDepricationAmount().get(4).getPercentage() + " %");
                            binding.tvPercentFive.setVisibility(View.VISIBLE);
                            binding.tvPercentFour.setGravity(Gravity.CENTER_HORIZONTAL);
                            binding.tvPercentFive.setGravity(Gravity.END);
                        }

                        if (result.getDepricationAmount().size() >= 6) {
                            binding.tvYearSix.setText(result.getDepricationAmount().get(5).getYear_title() + "");
                            binding.tvYearSixPrice.setText((result.getCurrency() == null ? mainActivityContext.getCurrency() : result.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(result.getDepricationAmount().get(4).getAmount()) + "");
                            binding.tvYearSixPrice.setSelected(true);
                            binding.llFifthDepreciation.setGravity(Gravity.CENTER);
                            binding.llSixthDepreciation.setVisibility(View.VISIBLE);
                            binding.tvPercentSix.setText(result.getDepricationAmount().get(5).getPercentage() + " %");
                            binding.tvPercentSix.setVisibility(View.VISIBLE);
                            binding.tvPercentFive.setGravity(Gravity.CENTER_HORIZONTAL);
                            binding.tvPercentSix.setGravity(Gravity.END);
                        }

                        if (result.getDepricationAmount().size() >= 7) {
                            binding.tvYearSeven.setText(result.getDepricationAmount().get(6).getYear_title() + "");
                            binding.tvYearSevenPrice.setText((result.getCurrency() == null ? mainActivityContext.getCurrency() : result.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(result.getDepricationAmount().get(4).getAmount()) + "");
                            binding.tvYearSevenPrice.setSelected(true);
                            binding.llSixthDepreciation.setGravity(Gravity.CENTER);
                            binding.llSevenDepreciation.setVisibility(View.VISIBLE);
                            binding.tvPercentSeven.setText(result.getDepricationAmount().get(6).getPercentage() + " %");
                            binding.tvPercentSeven.setVisibility(View.VISIBLE);
                            binding.tvPercentSix.setGravity(Gravity.CENTER_HORIZONTAL);
                            binding.tvPercentSeven.setGravity(Gravity.END);
                        }

                        if (result.getDepricationAmount().size() >= 8) {
                            binding.tvYearEight.setText(result.getDepricationAmount().get(7).getYear_title() + "");
                            binding.tvYearEightPrice.setText((result.getCurrency() == null ? mainActivityContext.getCurrency() : result.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(result.getDepricationAmount().get(4).getAmount()) + "");
                            binding.tvYearEightPrice.setSelected(true);
                            binding.llSevenDepreciation.setGravity(Gravity.CENTER);
                            binding.llEightDepreciation.setVisibility(View.VISIBLE);
                            binding.tvPercentEight.setText(result.getDepricationAmount().get(7).getPercentage() + " %");
                            binding.tvPercentEight.setVisibility(View.VISIBLE);
                            binding.tvPercentSeven.setGravity(Gravity.CENTER_HORIZONTAL);
                            binding.tvPercentEight.setGravity(Gravity.END);
                        }
                    } else {
                        binding.llDepreciationTrend.setVisibility(View.GONE);
                    }

                    if (result.getLife_cycle() != null) {
                        if (result.getLife_cycle().equals("0-0")) {
                            binding.llLifeCycle.setVisibility(View.GONE);
                        } else {
                            binding.tvProdStartYear.setText(result.getLife_cycle().split("-")[0]);
                            binding.tvProdExpYear.setText(result.getLife_cycle().split("-")[1]);
                        }
                    } else {
                        binding.llLifeCycle.setVisibility(View.GONE);
                    }
                    /*
                    if (currentTradeCar.getReviewDetails() != null) {
                        if (currentTradeCar.getReviewDetails().size() > 0) {
                            ArrayList<UserRating> userRatings = new ArrayList<>();
                            for (int pos = 0; pos < currentTradeCar.getReviewDetails().size(); pos++) {
                                UserRating userRating = new UserRating();
                                userRating.setId(currentTradeCar.getReviewDetails().get(pos).getId());
                                userRating.setUser(currentTradeCar.getReviewDetails().get(pos).getUserDetails());
                                userRating.setOverAllRating(currentTradeCar.getReviewDetails().get(pos).getAverageRating());
                                userRating.setReviewDetails(currentTradeCar.getReviewDetails().get(pos).getReviewMessage());
                                ArrayList<RatingAttribute> rAttributes = new ArrayList<>();
                                for (int posi = 0; posi < currentTradeCar.getReviewDetails().get(pos).getDetails().size(); posi++) {
                                    RatingAttribute ratingAttribute = new RatingAttribute();
                                    ratingAttribute.setRating(currentTradeCar.getReviewDetails().get(pos).getDetails().get(posi).getRating());
                                    ratingAttribute.setTitle(currentTradeCar.getReviewDetails().get(pos).getDetails().get(posi).getAspectTitle());
                                    rAttributes.add(ratingAttribute);
                                }
                                userRating.setRatingAttributes(rAttributes);
                                userRatings.add(userRating);
                            }
                            if (userRatings.size() > 0) {
                                if (userRatings.size() <= 2) {
                                    detailedReviewAdapter.addAll(userRatings);
                                } else if (userRatings.size() > 2) {
                                    ArrayList<UserRating> subArray = new ArrayList<>();
                                    for (int pos = 0; pos < 2; pos++) {
                                        subArray.add(userRatings.get(pos));
                                    }
                                    detailedReviewAdapter.addAll(subArray);
                                }
//                                    binding.tvReadAllReviews.setOnClickListener(ReviewDetailsFragment.this);
                            }
                            detailedReviewAdapter.notifyDataSetChanged();
                            binding.tvNoReviews.setVisibility(View.GONE);
                        }
                    } else {
                        binding.llReviews.setVisibility(View.GONE);
                    }
                    */
                    getReviewDetails();
                } else {
                    binding.llReviews.setVisibility(View.GONE);
                }
            }
        }
    }

    private void setProfileProgress() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int difference = Integer.parseInt(binding.tvProdExpYear.getText().toString()) - Integer.parseInt(binding.tvProdStartYear.getText().toString());

        //If production start and end years are in future of current year (for e.g. prod start year is 2020 and end is 2022, where current year is 2019, that means production has not been started yet)
        if (currentYear < Integer.parseInt(binding.tvProdStartYear.getText().toString()) && currentYear < Integer.parseInt(binding.tvProdExpYear.getText().toString())) {
            binding.profileProgress.setProgress(0);
            return;
        }

        //If production end year is before current year (for e.g prod end year is 2015 that means obviously that start year would be before 2015 which means production has started as well as ended)
        if (currentYear > Integer.parseInt(binding.tvProdExpYear.getText().toString())) {
            binding.profileProgress.setProgress(100);
            return;
        }

        //If production start year equals to current or current year is greater than production start year but current year is smaller than ending year
        if (currentYear >= Integer.parseInt(binding.tvProdStartYear.getText().toString()) && currentYear < Integer.parseInt(binding.tvProdExpYear.getText().toString())) {
            ArrayList<Integer> years = new ArrayList<>();
            for (int year = Integer.parseInt(binding.tvProdExpYear.getText().toString()); year >= Integer.parseInt(binding.tvProdStartYear.getText().toString()); year--) {
                years.add(year);
            }
            if (years.size() > 0) {
                Collections.reverse(years);
                int position = Utils.binarySearch(years, 0, years.size() - 1, currentYear);
                float percent = 100 / difference;
                float monthlyProgress = (percent / 12) * currentMonth;
                float progress = position * percent;
                binding.profileProgress.setProgress((int) Math.ceil(progress + monthlyProgress));
            }
//            return;
        }
/*
        if (currentYear < Integer.parseInt(binding.tvProdExpYear.getText().toString()) && currentYear >= Integer.parseInt(binding.tvProdStartYear.getText().toString())) {
            int newDifference = currentYear - Integer.parseInt(binding.tvProdExpYear.getText().toString());
            float percent = 100 / difference;
            float monthlyProgress = (percent / 12) * currentMonth;
            float progress = newDifference * percent;
            binding.profileProgress.setProgress((int) Math.ceil(progress + monthlyProgress));
        }
*/
    }

    private void setProfileDereciationProgress(boolean depreciation) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        if (currentTradeCar.getDepricationAmount().size() > 2) {
            ArrayList<Integer> years = new ArrayList<>();
            for (int year = currentTradeCar.getDepricationAmount().get(0).getYear(); year <= currentTradeCar.getDepricationAmount().get(currentTradeCar.getDepricationAmount().size() - 1).getYear(); year++) {
                years.add(year);
            }
            if (years.size() > 0) {
                int position = Utils.binarySearch(years, 0, years.size() - 1, currentYear);
                float percent = 100 / (currentTradeCar.getDepricationAmount().get(currentTradeCar.getDepricationAmount().size() - 1).getYear() - currentTradeCar.getDepricationAmount().get(0).getYear());
                float monthlyProgress = (percent / 12) * currentMonth;
                float progress = position * percent;
                binding.profileDepreciationProgress.setProgress((int) Math.ceil(progress + monthlyProgress));
            }
        }
    }

    private void setCarInteraction(final int type) {
        if (preferenceHelper.getLoginStatus()) {
            Map<String, Object> params = new HashMap<>();
            params.put("car_id", currentTradeCar.getId());
            params.put("type", type);

            if (!Utils.isNetworkAvailable(mainActivityContext)) {
                UIHelper.showSnackbar(mainActivityContext.getMainFrameLayout(), mainActivityContext.getResources().getString(R.string.no_internet_connection), Snackbar.LENGTH_SHORT);
            } else {
                WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.CAR_INTERACTIONS, null, null, params, new WebApiRequest.WebServiceObjectResponse() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        if (type == AppConstants.CarInteractions.FAVORITE) {
                            if (!currentTradeCar.isIs_favorite()) {
                                UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.car_favorite), Toast.LENGTH_SHORT);
                                currentTradeCar.setIs_favorite(true);
                            } else {
                                UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.car_remove_favorite), Toast.LENGTH_SHORT);
                                currentTradeCar.setIs_favorite(false);
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
        }
    }

    private void showFavPopup(View parentView) {
        popup = new PopupWindow(mainActivityContext);
        View layout = getLayoutInflater().inflate(R.layout.layout_add_to_fav_popup, null);
        final TextView addFav = (TextView) layout.findViewById(R.id.tvAddFav);
        if (currentTradeCar != null) {
            if (currentTradeCar.isIs_favorite())
                addFav.setText(mainActivityContext.getResources().getString(R.string.remove_fav));
            else
                addFav.setText(mainActivityContext.getResources().getString(R.string.add_to_fav));
        }
        popup.setContentView(layout);
        // Set content width and height
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // Closes the popup window when touch outside of it - when looses focus
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        // Show anchored to button
        popup.setBackgroundDrawable(/*mainActivityContext.getResources().getDrawable(R.drawable.shape)*/new BitmapDrawable());
        popup.showAsDropDown(parentView);
        UIHelper.applyDim(mainActivityContext);

        addFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.dismiss();
                setCarInteraction(AppConstants.CarInteractions.FAVORITE);
            }
        });
        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                UIHelper.clearDim(mainActivityContext);
            }
        });
    }

    private void sendRequestConsultancy(String email, String name, String countryCode, String phone) {
        Map<String, Object> params = new HashMap<>();
        params.put("car_id", currentTradeCar.getId());
        params.put("email", email);
        params.put("name", name);
        if (countryCode != null) {
            params.put("country_code", countryCode);
        }
        if (phone != null) {
            params.put("phone", phone);
        }
        params.put("type", AppConstants.ContactType.MY_SHOPPER);

        if (mainActivityContext.showLoader()) {
            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.REQUEST_CONSULTANCY, null, null, params, new WebApiRequest.WebServiceObjectResponse() {
                @Override
                public void onSuccess(ApiResponse apiResponse) {
                    if (requestConsultancyDialogFragment != null) {
                        requestConsultancyDialogFragment.dismiss();
                    }
                    mainActivityContext.hideLoader();
                    UIHelper.showSimpleDialog(
                            mainActivityContext,
                            0,
                            mainActivityContext.getResources().getString(R.string.success_ex),
                            mainActivityContext.getResources().getString(R.string.received_your_details),
                            mainActivityContext.getResources().getString(R.string.ok_go_back),
                            null,
                            false,
                            false,
                            new SimpleDialogActionListener() {
                                @Override
                                public void onDialogActionListener(DialogInterface dialog, int which, boolean positive, boolean logout) {
                                    dialog.dismiss();
                                    mainActivityContext.onBackPressed();
                                }
                            }
                    );
                }

                @Override
                public void onError() {
                    mainActivityContext.hideLoader();
                }
            }, null);
        }
    }

    private void confirmTradeIn(TradeCar tradeCar) {
        UIHelper.showSimpleDialog(
                mainActivityContext,
                0,
                mainActivityContext.getResources().getString(R.string.trade_in_request),
                mainActivityContext.getResources().getString(R.string.submit_your) + " " + Utils.getCarNameByBrand(tradeCar, false) + " " + mainActivityContext.getResources().getString(R.string.for_tradein_q),
                mainActivityContext.getResources().getString(R.string.yes_trade_in),
                mainActivityContext.getResources().getString(R.string.cancel),
                false,
                false,
                new SimpleDialogActionListener() {
                    @Override
                    public void onDialogActionListener(DialogInterface dialog, int which, boolean positive, boolean logout) {
                        if (positive) {
                            if (mainActivityContext.showLoader()) {
                                tradeInCar(tradeCar.getId(), currentTradeCar.getId(), 0);
                            }
                        } else {

                        }
                        dialog.dismiss();
                    }
                }
        );
    }

    private void confirmPersonalShopper() {
        IntrectionCall(currentTradeCar.getId(), AppConstants.Interaction.REQUEST);
        UIHelper.showSimpleDialog(
                mainActivityContext,
                0,
                mainActivityContext.getResources().getString(R.string.personal_shopper),
                mainActivityContext.getResources().getString(R.string.write_to_us),
                mainActivityContext.getResources().getString(R.string.request_call),
                mainActivityContext.getResources().getString(R.string.call_now),
                true,
                false,
                new SimpleDialogActionListener() {
                    @Override
                    public void onDialogActionListener(DialogInterface dialog, int which, boolean positive, boolean logout) {
                        if (positive) {
                            requestConsultancy();
                        } else {
                            IntrectionCall(currentTradeCar.getId(), AppConstants.Interaction.PHONE);
                            if (currentTradeCar.getUser() != null && currentTradeCar.getUser().getDetails() != null)
                                callConsultant(!UIHelper.isEmptyOrNull(currentTradeCar.getUser().getDetails().getFirstName()) ? currentTradeCar.getUser().getDetails().getFirstName() : "", !UIHelper.isEmptyOrNull(currentTradeCar.getUser().getDetails().getPhone()) ? currentTradeCar.getUser().getDetails().getPhone() : "");
                        }
                        dialog.dismiss();
                    }
                }
        );
    }

    private void tradeInCar(int myCarId, int customerCarId, long amount) {
        TradeInCar tradeInCar = new TradeInCar();
        tradeInCar.setOwnerCarId(customerCarId);
        tradeInCar.setCustomerCarId(myCarId);
        tradeInCar.setAmount(amount);
        tradeInCar.setType(AppConstants.MyCarActions.TRADE);

        WebApiRequest.Instance(mainActivityContext).request(
                AppConstants.WebServicesKeys.POST_TRADE_IN_CAR,
                null,
                tradeInCar,
                null,
                new WebApiRequest.WebServiceObjectResponse() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        UIHelper.showToast(mainActivityContext, apiResponse.getMessage(), Toast.LENGTH_LONG);
//                        mainActivityContext.onBackPressed();
                        mainActivityContext.hideLoader();
                    }

                    @Override
                    public void onError() {
                        mainActivityContext.hideLoader();
                    }
                },
                null
        );
    }

    private void silarWebService(int id) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("category_id", id);
        WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.LUXURY_MARKET_CATEGORIES, null, null, params, null, new WebApiRequest.WebServiceArrayResponse() {
            @Override
            public void onSuccess(ApiArrayResponse apiArrayResponse) {
                similarListingsAdapter.addAll((ArrayList<TradeCar>) JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), TradeCar.class), currentTradeCar.getId());


                if (similarListingsAdapter.getItemCount() > 0)
                    visibleView();
                else
                    hideView();
                mainActivityContext.hideLoader();
            }

            @Override
            public void onError() {
                if (similarListingsAdapter.getItemCount() > 0)
                    visibleView();
                else
                    hideView();
                mainActivityContext.hideLoader();

            }
        });
    }

    private void getReviewDetails() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("car_id", currentTradeCar.getId());
        if (mainActivityContext.showLoader()) {
            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.GET_REVIEWS, null, null, params, null, new WebApiRequest.WebServiceArrayResponse() {
                @Override
                public void onSuccess(ApiArrayResponse apiArrayResponse) {
                    reviewDetails = JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), ReviewDetail.class);
                    if (reviewDetails.size() > 0) {
                        binding.ratingbar.setRating(currentTradeCar.getAverage_rating());
                        ArrayList<UserRating> userRatings = new ArrayList<>();
                        for (int pos = 0; pos < reviewDetails.size(); pos++) {
                            UserRating userRating = new UserRating();
                            userRating.setId(reviewDetails.get(pos).getId());
                            userRating.setUser(reviewDetails.get(pos).getUserDetails());
                            userRating.setOverAllRating(reviewDetails.get(pos).getAverageRating());
                            userRating.setReviewDetails(reviewDetails.get(pos).getReviewMessage());
                            ArrayList<RatingAttribute> rAttributes = new ArrayList<>();
                            for (int posi = 0; posi < reviewDetails.get(pos).getDetails().size(); posi++) {
                                RatingAttribute ratingAttribute = new RatingAttribute();
                                ratingAttribute.setRating(reviewDetails.get(pos).getDetails().get(posi).getRating());
                                ratingAttribute.setTitle(reviewDetails.get(pos).getDetails().get(posi).getAspectTitle());
                                rAttributes.add(ratingAttribute);
                            }
                            userRating.setRatingAttributes(rAttributes);
                            userRatings.add(userRating);
                        }
                        if (userRatings.size() > 0) {
                            if (userRatings.size() <= 2) {
                                detailedReviewAdapter.addAll(userRatings);
                                binding.tvReadAllReviews.setVisibility(View.GONE);
                            } else {
                                ArrayList<UserRating> subArray = new ArrayList<>();
                                for (int pos = 0; pos < 2; pos++) {
                                    subArray.add(userRatings.get(pos));
                                }
                                detailedReviewAdapter.addAll(subArray);
                                binding.tvReadAllReviews.setVisibility(View.VISIBLE);
                            }

//                            detailedReviewAdapter.addAll(userRatings);
//                            binding.tvReadAllReviews.setOnClickListener(ReviewDetailsFragment.this);
                            detailedReviewAdapter.notifyDataSetChanged();
                            binding.tvNoReviews.setVisibility(View.GONE);
                        }
                    } else {
//                        binding.tvReadAllReviews.setText(mainActivityContext.getResources().getString(R.string.no_review_to_show));
//                        binding.tvReadAllReviews.setOnClickListener(null);
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

    private void zoomImages() {
        if (currentTradeCar.getMedia().size() > 0) {
            FullImageFragment fullImageFragment = new FullImageFragment();
            fullImageFragment.setArrayList(currentTradeCar.getMedia());
            mainActivityContext.replaceFragment(fullImageFragment, FullImageFragment.class.getSimpleName(), true, false);
        }
    }

    public static class SpacesItemDecorationAllSideEqual extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecorationAllSideEqual(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.top = space;
            outRect.bottom = space;
            outRect.right = space;
        }
    }
}
