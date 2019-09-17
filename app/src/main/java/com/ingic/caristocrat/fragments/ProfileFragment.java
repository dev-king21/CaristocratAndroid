package com.ingic.caristocrat.fragments;

import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.ArticlesAdapter;
import com.ingic.caristocrat.adapters.MainCategoriesTitleAdapter;
import com.ingic.caristocrat.adapters.MyCarsAdapter;
import com.ingic.caristocrat.adapters.ProfileOptionsAdatpter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentProfileBinding;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.RecyclerTouchListener;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.helpers.Utils;
import com.ingic.caristocrat.interfaces.ClickListenerRecycler;
import com.ingic.caristocrat.interfaces.OnItemClickListener;
import com.ingic.caristocrat.models.Age;
import com.ingic.caristocrat.models.Car;
import com.ingic.caristocrat.models.ProfileOptions;
import com.ingic.caristocrat.models.TradeCar;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.models.Category;
import com.ingic.caristocrat.webhelpers.models.News;
import com.ingic.caristocrat.webhelpers.models.User;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class ProfileFragment extends BaseFragment implements View.OnClickListener, OnItemClickListener {
    FragmentProfileBinding binding;
    LinearLayoutManager linearLayoutManager;
    MyCarsAdapter myCarsAdapter;
    ProfileOptionsAdatpter profileOptionsAdatpter;
    MainCategoriesTitleAdapter categoriesTitleAdapter;
    ArticlesAdapter adapter;
    ArrayList<Category> favoriteCategories = new ArrayList<Category>();
    User user;
    ArrayList<TradeCar> favoritesCars = new ArrayList<>();

    private int LIMIT = 1000;
    private int OFFSET = 0;

    public ProfileFragment() {
    }

    public static ProfileFragment Instance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
/*
        TextView tvNoData = binding.noDataLayout.findViewById(R.id.noDataText);
        tvNoData.setText(mainActivityContext.getResources().getString(R.string.no_favourite));
*/
        initArticlesAdapter();
        initCategoriesTitleAdapter();
        initCarsAdapter();
        initProfileOptionsAdapter();
        binding.profileProgress.getProgressDrawable().mutate().setColorFilter(ContextCompat.getColor(mainActivityContext, R.color.colorBlack), PorterDuff.Mode.SRC_IN);
        //  getMyTradeIns();
        setListeners();
        getProfile();
        getFavorites();
//        binding.swipeRefresh.setColorSchemeColors(ContextCompat.getColor(mainActivityContext,R.color.colorBlack));
//        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getMyTradeIns();
//                setListeners();
//                getProfile();
//                getFavorites();
//                binding.swipeRefresh.setRefreshing(false);
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setProfile();
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
        mainActivityContext.getIvSubCategoryItem().setImageResource(R.drawable.car_prof_bg);
        mainActivityContext.getIvSubCategoryItem().setScaleType(ImageView.ScaleType.FIT_XY);
        mainActivityContext.getIvSubCategoryItem().requestLayout();
        mainActivityContext.getCollapsingToolBarLayout().setVisibility(View.GONE);
        mainActivityContext.getRvSubCategoryItem().setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibBackbtn:
                mainActivityContext.onBackPressed();
                break;
            case R.id.ibSettings:
                mainActivityContext.replaceFragment(SettingsFragment.Instance(), SettingsFragment.class.getName(), true, false);
                break;
//            case R.id.btnCompleteNow:
//                mainActivityContext.replaceFragment(ProfileEditFragment.Instance(), ProfileEditFragment.class.getName(), true, false);
//                break;
//            case R.id.btnJoinClub:
//                UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.implemented_later), Toast.LENGTH_LONG);
            //  break;
        }
    }

    @Override
    public void onItemClick(int position, String name, boolean clicked) {
        if (categoriesTitleAdapter.getItemCount() > 0) {
            adapter = new ArticlesAdapter(mainActivityContext, null, true);
            if (name.equals(mainActivityContext.getResources().getString(R.string.cars))) {
                if (favoritesCars.size() == 0) {
                    noArticleFound();
                } else {
                    adapter.addAllCars(favoritesCars);
                    binding.recyclerViewArticles.setAdapter(adapter);
//                    binding.recyclerViewArticles.smoothScrollToPosition(adapter.getItemCount() - 1);
                    if (clicked) {
                        binding.recyclerViewArticles.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.nestedscroll.fullScroll(View.FOCUS_DOWN);
                            }
                        });
                    }
                    articleFound();
                }
                adapter.setCars(true);
            } else {
                int newPosition = position - 1;
                if (newPosition < 0) {
                    newPosition = 0;
                }
                if (favoriteCategories.get(newPosition).getNews().size() == 0) {
                    noArticleFound();
                } else {
                    adapter.addAll(favoriteCategories.get(newPosition).getNews());
                    binding.recyclerViewArticles.setAdapter(adapter);
//                    binding.recyclerViewArticles.smoothScrollToPosition(adapter.getItemCount() - 1);
                    if (clicked) {
                        binding.recyclerViewArticles.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.nestedscroll.fullScroll(View.FOCUS_DOWN);
                            }
                        });
                    }
                    adapter.setCars(false);
                    articleFound();
                }
            }
        }
        /*
        else if (categoriesTitleAdapter.getItemCount() == 0) {
            hideView();
        }
        */
    }

    private void setListeners() {
        binding.ibBackbtn.setOnClickListener(this);
        binding.ibSettings.setOnClickListener(this);
        /*
        binding.recyclerViewMyCars.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastvisibleitemposition = linearLayoutManager.findLastVisibleItemPosition();

                if (lastvisibleitemposition == myCarsAdapter.getItemCount() - 1) {
                    OFFSET += LIMIT;
                    getMyCars();
                }
            }
        });
        */
    }

    private void initProfileOptionsAdapter() {
        final ArrayList<ProfileOptions> profileOptions = new ArrayList<>();
        profileOptions.add(new ProfileOptions(AppConstants.ProfileOptions.COMPLETE_NOW, mainActivityContext.getResources().getString(R.string.complete_now)));
        profileOptions.add(new ProfileOptions(AppConstants.ProfileOptions.JOIN_THE_CLUB, mainActivityContext.getResources().getString(R.string.join_the_club)));
        profileOptions.add(new ProfileOptions(AppConstants.ProfileOptions.MY_TRADE_INS, mainActivityContext.getResources().getString(R.string.my_trades_in)));
        profileOptions.add(new ProfileOptions(AppConstants.ProfileOptions.MY_EVALUATIONS, mainActivityContext.getResources().getString(R.string.my_evaluations)));
        profileOptionsAdatpter = new ProfileOptionsAdatpter(mainActivityContext, profileOptions);
        linearLayoutManager = new LinearLayoutManager(mainActivityContext, LinearLayoutManager.HORIZONTAL, false);
        binding.rvProfileOptions.setLayoutManager(linearLayoutManager);
        binding.rvProfileOptions.setNestedScrollingEnabled(false);
        binding.rvProfileOptions.setAdapter(profileOptionsAdatpter);
        binding.rvProfileOptions.scrollToPosition(0);
        binding.rvProfileOptions.addOnItemTouchListener(new RecyclerTouchListener(mainActivityContext, binding.rvProfileOptions, new ClickListenerRecycler() {
            @Override
            public void onClick(View view, int position) {
                switch (profileOptions.get(position).getTag()) {
                    case AppConstants.ProfileOptions.COMPLETE_NOW:
                        if (binding.profileProgress.getProgress() < 100)
                            mainActivityContext.replaceFragment(ProfileEditFragment.Instance(), ProfileEditFragment.class.getName(), true, false);
                        else {
                            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.completed), Toast.LENGTH_LONG);
                        }
                        break;
                    case AppConstants.ProfileOptions.JOIN_THE_CLUB:
                        UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.coming_soon), Toast.LENGTH_LONG);
                        break;
                    case AppConstants.ProfileOptions.MY_TRADE_INS:
                        MyTradeInFragment myTradeInFragment = new MyTradeInFragment();
                        myTradeInFragment.setScreenType(AppConstants.MyTradeInScreenTypes.TRADE_INS);
//                        myTradeInFragment.setTradeCars(myTradeIns);
                        //  myTradeInFragment.setTradeCars(tradeCars);
                        mainActivityContext.replaceFragment(myTradeInFragment, MyTradeInFragment.class.getName(), true, false);
                        break;

                    case AppConstants.ProfileOptions.MY_EVALUATIONS:
                        MyTradeInFragment myEvaluations = new MyTradeInFragment();
                        myEvaluations.setScreenType(AppConstants.MyTradeInScreenTypes.EVALUATION);
//                        myTradeInFragment.setTradeCars(myTradeIns);
                        //  myTradeInFragment.setTradeCars(tradeCars);
                        mainActivityContext.replaceFragment(myEvaluations, MyTradeInFragment.class.getName(), true, false);
                        break;

                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void initCarsAdapter() {
        myCarsAdapter = new MyCarsAdapter(mainActivityContext, new ArrayList<TradeCar>());
        linearLayoutManager = new LinearLayoutManager(mainActivityContext, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerViewMyCars.setLayoutManager(linearLayoutManager);
        binding.recyclerViewMyCars.setNestedScrollingEnabled(false);
        binding.recyclerViewMyCars.setAdapter(myCarsAdapter);
        myCarsAdapter.addAll(new ArrayList<TradeCar>());
        binding.recyclerViewMyCars.addOnItemTouchListener(new RecyclerTouchListener(mainActivityContext, binding.recyclerViewMyCars, new ClickListenerRecycler() {
            @Override
            public void onClick(View view, int position) {
//                UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.implemented_later), Toast.LENGTH_LONG);
                if (position == 0) {
                    TradeInYourCarFragment tradeInYourCarFragment = new TradeInYourCarFragment();
                    tradeInYourCarFragment.setProfile(true);
                    mainActivityContext.replaceFragment(tradeInYourCarFragment, TradeInYourCarFragment.class.getSimpleName(), true, false);
                } else {

                    TradeInYourCarFragment tradeInYourCarFragment = new TradeInYourCarFragment();
                    tradeInYourCarFragment.setProfile(true);
                    tradeInYourCarFragment.setEdit(true);
                    tradeInYourCarFragment.setTradeCar((TradeCar) myCarsAdapter.getItem(position));
                    mainActivityContext.replaceFragment(tradeInYourCarFragment, TradeInYourCarFragment.class.getSimpleName(), true, false);

                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void initCategoriesTitleAdapter() {
        categoriesTitleAdapter = new MainCategoriesTitleAdapter(mainActivityContext, this);
        linearLayoutManager = new LinearLayoutManager(mainActivityContext, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerViewTabs.setLayoutManager(linearLayoutManager);
        binding.recyclerViewTabs.setNestedScrollingEnabled(false);
        binding.recyclerViewTabs.setAdapter(categoriesTitleAdapter);
    }

    private void initArticlesAdapter() {
        adapter = new ArticlesAdapter(mainActivityContext, null, true);
        linearLayoutManager = new LinearLayoutManager(mainActivityContext, LinearLayoutManager.VERTICAL, false);
        binding.recyclerViewArticles.setLayoutManager(linearLayoutManager);
        binding.recyclerViewArticles.setNestedScrollingEnabled(false);
        binding.recyclerViewArticles.setAdapter(adapter);
        /*
        binding.recyclerViewArticles.addOnItemTouchListener(new RecyclerTouchListener(mainActivityContext, binding.recyclerViewArticles, new ClickListenerRecycler() {
            @Override
            public void onClick(View view, int position) {
                UIHelper.hideSoftKeyboard(mainActivityContext, view);
                MainDetailPageFragment mainDetailPageFragment = new MainDetailPageFragment();
                mainDetailPageFragment.setCategoryId(favoriteCategories.get(position).getId());
                mainDetailPageFragment.setImageUrl(favoriteCategories.get(position).getMedia().get(0).getFileUrl());
                mainActivityContext.replaceFragment(mainDetailPageFragment, MainDetailPageFragment.class.getSimpleName(), true, false);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        */
    }

    private void setProfile() {
        int progress = 0;
        user = preferenceHelper.getUser();
        if (user != null) {
            if (user.getDetails() != null) {
                if (user.getDetails().getFirstName() != null) {
                    progress += 10;
                }
                if (user.getEmail() != null) {
                    progress += 10;
                }
                if (user.getDetails().getPhone() != null && !user.getDetails().getPhone().equals("")) {
                    progress += 20;
                }
                if (user.getDetails().getAbout() != null && !user.getDetails().getAbout().equals("")) {
                    progress += 10;
                }
                if (user.getDetails().getImageUrl() != null && !user.getDetails().getImageUrl().equals("")) {
                    progress += 10;
                }
                if (user.getDetails().getGender() == AppConstants.Gender.MALE || user.getDetails().getGender() == AppConstants.Gender.FEMALE) {
                    progress += 10;
                }
                if (user.getDetails().getDob() != null && !user.getDetails().getDob().equals("")) {
                    progress += 10;
                }
                if (user.getDetails().getProfession() != null && !user.getDetails().getProfession().equals("")) {
                    progress += 10;
                }
                if (user.getDetails().getNationality() != null && !user.getDetails().getNationality().equals("")) {
                    progress += 10;
                }
                binding.profileProgress.setProgress(progress);
                binding.tvProfileCompletionStatus.setText(progress + " % " + mainActivityContext.getResources().getString(R.string.complete));
                if (progress >= 100) {
                    profileOptionsAdatpter.set(0, new ProfileOptions(AppConstants.ProfileOptions.COMPLETE_NOW, mainActivityContext.getResources().getString(R.string.completed)));
                }
            }
        }
        if (user != null && user.getDetails() != null) {
            binding.tvName.setText((user.getDetails().getFirstName() == null) ? "" : user.getDetails().getFirstName());
            binding.tvEmail.setText((user.getEmail() == null) ? "" : user.getEmail());
            if (user.getDetails().getAbout() != null) {
                binding.tvAbout.setText(user.getDetails().getAbout());
                binding.tvAbout.setVisibility(View.VISIBLE);
                binding.llAboutMe.setVisibility(View.VISIBLE);
                binding.noAbout.setVisibility(View.GONE);
            } else {
                binding.tvAbout.setVisibility(View.GONE);
                binding.llAboutMe.setVisibility(View.GONE);
                binding.noAbout.setVisibility(View.VISIBLE);
            }
            String code = user.getDetails() != null ? user.getDetails().getCountryCode() : "";
            String phone = user.getDetails() != null ? user.getDetails().getPhone() : "";
            if (code != null && phone != null) {
                binding.tvPhoneNumber.setText(code + " " + phone);
            }
            if (user.getDetails().getImageUrl() != null)
                UIHelper.setUserImageWithGlide(mainActivityContext, binding.ivProfile, user.getDetails().getImageUrl());
//            if (!user.getDetails().getSocialLogin())
//
//            else
//                UIHelper.setUserImageWithGlide(mainActivityContext, binding.ivProfile, user.getDetails().getImage() == null ? "" : user.getDetails().getImage());

            if (user.getDetails().getNationality() != null) {
                binding.tvNationality.setText(user.getDetails().getNationality());
            } else {
                binding.tvNationality.setVisibility(View.GONE);
            }

            if (user.getDetails().getGenderLabel() != null && user.getDetails().getProfession() != null) {
                binding.tvGenderProfession.setText(user.getDetails().getGenderLabel() + ", " + user.getDetails().getProfession());
            } else if (user.getDetails().getGenderLabel() != null) {
                binding.tvGenderProfession.setText(user.getDetails().getGenderLabel());
            } else if (user.getDetails().getProfession() != null) {
                binding.tvGenderProfession.setText(user.getDetails().getProfession());
            } else {
                binding.tvGenderProfession.setVisibility(View.GONE);
            }

            if (user.getDetails().getDob() != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppConstants.DOB_FORMAT);
                try {
                    Date date = simpleDateFormat.parse(user.getDetails().getDob());
                    Age age = Utils.calculateAge(date);
                    if (age != null) {
                        if (age.getYears() == 1) {
                            binding.tvAge.setText(age.getYears() + " " + mainActivityContext.getResources().getString(R.string.year));
                        } else {
                            binding.tvAge.setText(age.getYears() + " " + mainActivityContext.getResources().getString(R.string.years));
                        }
                    }
                } catch (ParseException ex) {
                }
            } else {
                binding.tvAge.setVisibility(View.GONE);
            }
        }
    }

    private void getFavorites() {

        final Category category = new Category();
        Map<String, Object> favoritesCarsParams = new HashMap<>();
        favoritesCarsParams.put("favorite", 1);
        Map<String, Object> params = new HashMap<>();
        params.put("category_id", 0);
        if (mainActivityContext.showLoader()) {
            WebApiRequest.Instance(mainActivityContext).request(
                    AppConstants.WebServicesKeys.FAVORITE_CARS, null,
                    null,
                    favoritesCarsParams,
                    null,
                    new WebApiRequest.WebServiceArrayResponse() {
                        @Override
                        public void onSuccess(ApiArrayResponse apiArrayResponse) {
                            favoritesCars = JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), TradeCar.class);

                            category.setName(mainActivityContext.getResources().getString(R.string.cars));
                            categoriesTitleAdapter.add(category);
                            categoriesTitleAdapter.notifyDataSetChanged();
                            if (favoritesCars.size() > 0) {
                                if (categoriesTitleAdapter.getItemCount() > 0) {
                                    categoriesTitleAdapter.setArticlesAdapter(adapter);
                                    categoriesTitleAdapter.notifyDataSetChanged();

                                    binding.llEmptyLayout.setVisibility(View.GONE);
                                    binding.recyclerViewTabs.setVisibility(View.VISIBLE);
                                    binding.recyclerViewArticles.setVisibility(View.VISIBLE);
                                }
                            }

                            WebApiRequest.Instance(mainActivityContext).request(
                                    AppConstants.WebServicesKeys.FAVORITE_NEWS, null,
                                    null,
                                    params,
                                    null,
                                    new WebApiRequest.WebServiceArrayResponse() {
                                        @Override
                                        public void onSuccess(ApiArrayResponse apiArrayResponse) {
                                            favoriteCategories = JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), Category.class);
                                            if (favoriteCategories.size() > 0) {
                                                for (int position = 0; position < favoriteCategories.size(); position++) {
                                                    if (
                                                            favoriteCategories.get(position).getSlug().equals(AppConstants.MainCategoriesType.AUTO_LIFE) ||
                                                                    favoriteCategories.get(position).getSlug().equals(AppConstants.MainCategoriesType.CAREDUCATION) ||
                                                                    favoriteCategories.get(position).getSlug().equals(AppConstants.MainCategoriesType.FOR_WOMEN_ONLY) ||
                                                                    favoriteCategories.get(position).getSlug().equals(AppConstants.MainCategoriesType.EVENTS)
                                                    ) {
                                                        categoriesTitleAdapter.add(favoriteCategories.get(position));
                                                    }
                                                }
                                                if (categoriesTitleAdapter.getItemCount() > 0) {
                                                    categoriesTitleAdapter.setArticlesAdapter(adapter);
                                                    categoriesTitleAdapter.notifyDataSetChanged();

                                                    binding.llEmptyLayout.setVisibility(View.GONE);
                                                    binding.recyclerViewTabs.setVisibility(View.VISIBLE);
                                                    binding.recyclerViewArticles.setVisibility(View.VISIBLE);
                                                }
                                                mainActivityContext.hideLoader();
                                            }
                                        }

                                        @Override
                                        public void onError() {
                                            mainActivityContext.hideLoader();
                                        }
                                    }
                            );
                        }

                        @Override
                        public void onError() {
                            mainActivityContext.hideLoader();
                        }
                    }
            );
/*
            WebApiRequest.Instance(mainActivityContext).request(
                    AppConstants.WebServicesKeys.FAVORITE_NEWS, binding.getRoot(),
                    null,
                    params,
                    null,
                    new WebApiRequest.WebServiceArrayResponse() {
                        @Override
                        public void onSuccess(ApiArrayResponse apiArrayResponse) {
                            favoriteCategories = JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), Category.class);
                            if (favoriteCategories.size() > 0) {
                                for (int position = 0; position < favoriteCategories.size(); position++) {
                                    categoriesTitleAdapter.add(categoriesTitleAdapter.getItemCount(), favoriteCategories.get(position));
                                }
                                if (categoriesTitleAdapter.getItemCount() > 0) {
                                    categoriesTitleAdapter.setArticlesAdapter(adapter);
                                    categoriesTitleAdapter.notifyDataSetChanged();
                                    visibleView();
                                } else {
                                    hideView();
                                }
                                mainActivityContext.hideLoader();
                            }
                        }

                        @Override
                        public void onError() {
                            mainActivityContext.hideLoader();
                        }
                    }
            );
*/
        }
    }

    private void getProfile() {
        WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.PROFILE, binding.getRoot(), null, null, new WebApiRequest.WebServiceObjectResponse() {
            @Override
            public void onSuccess(ApiResponse apiResponse) {
                User user = (User) JsonHelpers.convertToModelClass(apiResponse.getData(), User.class);
                preferenceHelper.putUser(user);
                setProfile();
                getMyCars();
                mainActivityContext.hideLoader();
            }

            @Override
            public void onError() {
                mainActivityContext.hideLoader();
            }
        }, null);
    }

    public void visibleView() {
//        binding.recyclerViewArticles.setVisibility(View.VISIBLE);
//        binding.noDataLayout.setVisibility(View.GONE);
    }

    public void hideView() {
//        binding.recyclerViewArticles.setVisibility(View.GONE);
//        binding.noDataLayout.setVisibility(View.VISIBLE);
//        TextView noData = binding.noDataLayout.findViewById(R.id.noDataText);
//        noData.setTextSize(14);
    }

    public void noArticleFound() {
        binding.recyclerViewArticles.setVisibility(View.GONE);
        binding.tvNoFavorite.setVisibility(View.VISIBLE);
    }

    public void articleFound() {
        binding.tvNoFavorite.setVisibility(View.GONE);
        binding.recyclerViewArticles.setVisibility(View.VISIBLE);
    }

    boolean stopCall = false;

    private void getMyCars() {
        if (!stopCall) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("limit", LIMIT);
            params.put("offset", OFFSET);
            if (mainActivityContext.showLoader()) {
                WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.MY_TRADE_INS, null, null, params, null, new WebApiRequest.WebServiceArrayResponse() {
                    @Override
                    public void onSuccess(ApiArrayResponse apiArrayResponse) {
                        ArrayList<TradeCar> tradeCars = (ArrayList<TradeCar>) JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), TradeCar.class);
                        myCarsAdapter.addAll(tradeCars);
                        mainActivityContext.hideLoader();
                        if (myCarsAdapter.getItemCount() > 0 && tradeCars.size() == 0) {
                            stopCall = true;
                        }
                    }

                    @Override
                    public void onError() {
                        mainActivityContext.hideLoader();
                    }
                });
            }
        }
//        mainActivityContext.replaceFragment(new TradeInYourCarFragment(), TradeInYourCarFragment.class.getSimpleName(), true, true);
    }
//    private void getMyTradeIns() {
//
//        WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.MY_TRADE_INS, binding.getRoot(), null, null, null, new WebApiRequest.WebServiceArrayResponse() {
//            @Override
//            public void onSuccess(ApiArrayResponse apiArrayResponse) {
//                tradeCars = (ArrayList<TradeCar>) JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), TradeCar.class);
////                myTradeIns = new ArrayList<>();
////                for (int i = 0; i < tradeCars.size(); i++) {
////                    if (tradeCars.get(i).getTop_bids() != null && tradeCars.get(i).getTop_bids().size() > 0)
////                        myTradeIns.add(tradeCars.get(i));
////                }
//                if (tradeCars.size() > 0)
//                    visibleView();
//                else
//                    hideView();
//                myCarsAdapter.addAll(tradeCars);
//                mainActivityContext.hideLoader();
//
//            }
//
//            @Override
//            public void onError() {
//                if (tradeCars.size() > 0)
//                    visibleView();
//                else
//                    hideView();
//                mainActivityContext.hideLoader();
//            }
//        });
//    }

}
