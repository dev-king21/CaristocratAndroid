package com.ingic.caristocrat.fragments;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.AutoLifeCategroriesAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentAutoLifeBinding;
import com.ingic.caristocrat.dialogs.RequestConsultancyDialogFragment;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.LuxuryMarketSearchFilter;
import com.ingic.caristocrat.helpers.RecyclerTouchListener;
import com.ingic.caristocrat.helpers.SimpleItemTouchHelperCallback;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.CategoryPositionsChangedListener;
import com.ingic.caristocrat.interfaces.ClickListenerRecycler;
import com.ingic.caristocrat.interfaces.OnRequestConsultancy;
import com.ingic.caristocrat.interfaces.OnStartDragListener;
import com.ingic.caristocrat.interfaces.SimpleDialogActionListener;
import com.ingic.caristocrat.models.TradeCar;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.models.Category;
import com.ingic.caristocrat.webhelpers.models.InteractionCar;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class AutoLifeFragment extends BaseFragment implements OnStartDragListener, OnRequestConsultancy, CategoryPositionsChangedListener {
    FragmentAutoLifeBinding binding;
    ArrayList<Category> autoLifeCategories;
    AutoLifeCategroriesAdapter autoLifeCategroriesAdapter;
    LinearLayoutManager linearLayoutManager;
    ItemTouchHelper mItemTouchHelper;
    private ArrayList<Category> child_category = new ArrayList<>();
    int clickedCategoryPosition = -1;
    String title;
    RequestConsultancyDialogFragment requestConsultancyDialogFragment;
    int parentPosition;
    CategoryPositionsChangedListener categoryPositionsChangedListener;

    public AutoLifeFragment() {

    }

    public static AutoLifeFragment Instance() {
        return new AutoLifeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_auto_life, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();
        setListeners();
        setValue();
//        binding.swipeRefresh.setColorSchemeColors(ContextCompat.getColor(mainActivityContext,R.color.colorBlack));
//        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                binding.swipeRefresh.setRefreshing(false);
//            }
//        });

    }

    @Override
    public void onResume() {
        super.onResume();
        /*
        if (autoLifeCategroriesAdapter != null) {
            if (clickedCategoryPosition > -1 && preferenceHelper.getLoginStatus()) {
                autoLifeCategroriesAdapter.markRead(clickedCategoryPosition);
                autoLifeCategroriesAdapter.notifyItemChanged(clickedCategoryPosition);
            }
        }
        */
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(mainActivityContext);
        titlebar.showTitlebar(mainActivityContext);
        if (title != null) {
            titlebar.setTitle(title);
        } else {
            titlebar.setTitle(mainActivityContext.getResources().getString(R.string.auto_life));
        }
        titlebar.showBackButton(mainActivityContext, false);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onRequested(String email, String name, String countryCode, String phone, int type, String message) {
        sendRequestConsultancy(email, name, countryCode, phone);
    }

    @Override
    public void onCategoryPositionChanged(int parentPosition, ArrayList<Category> categories) {
        this.child_category.clear();
        this.child_category.addAll(categories);
        if (categoryPositionsChangedListener != null) {
            categoryPositionsChangedListener.onCategoryPositionChanged(this.parentPosition, categories);
        }
    }

    void setListeners() {
    }

    public void visibleView() {
        binding.recyclerViewSubCategory.setVisibility(View.VISIBLE);
        binding.noDataLayout.setVisibility(View.GONE);
    }

    public void hideView() {
        binding.recyclerViewSubCategory.setVisibility(View.GONE);
        binding.noDataLayout.setVisibility(View.VISIBLE);
    }

    private void initAdapter() {
        autoLifeCategroriesAdapter = new AutoLifeCategroriesAdapter(mainActivityContext, binding, this, new ArrayList<Category>(), this);
        linearLayoutManager = new LinearLayoutManager(mainActivityContext);
        binding.recyclerViewSubCategory.setLayoutManager(linearLayoutManager);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(autoLifeCategroriesAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(binding.recyclerViewSubCategory);
        binding.recyclerViewSubCategory.setAdapter(autoLifeCategroriesAdapter);
        binding.recyclerViewSubCategory.setNestedScrollingEnabled(false);
        if (preferenceHelper.getSubCategoriesList() != null && preferenceHelper.getSubCategoriesList().size() > 0) {
            autoLifeCategroriesAdapter.addAll(preferenceHelper.getSubCategoriesList());
            visibleView();
        } else {
            hideView();
        }
        binding.recyclerViewSubCategory.addOnItemTouchListener(new RecyclerTouchListener(mainActivityContext, binding.recyclerViewSubCategory, new ClickListenerRecycler() {
            @Override
            public void onClick(View view, int position) {
                clickedCategoryPosition = position;
                if (preferenceHelper.getLoginStatus()) {
                    IntrectionCall(child_category.get(position).getId(), AppConstants.Interaction.MAIN_CAT, position);
                } else {
                    onClicke(position);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
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
        requestConsultancyDialogFragment = new RequestConsultancyDialogFragment(mainActivityContext, this);
        requestConsultancyDialogFragment.setType(AppConstants.ContactType.CONSULTANCY);
        mainActivityContext
                .getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(mainActivityContext.getMainFrameLayoutID(), requestConsultancyDialogFragment, RequestConsultancyDialogFragment.class.getSimpleName())
                .addToBackStack(mainActivityContext.getSupportFragmentManager().getBackStackEntryCount() == 0 ? "firstFrag" : null)
                .commit();
    }

    private void setValue() {
        autoLifeCategories = child_category;
        updateOrdering(autoLifeCategories);
        autoLifeCategroriesAdapter.addAll(autoLifeCategories);
//        autoLifeCategroriesAdapter.notifyDataSetChanged();
//        autoLifeCategroriesAdapter.addAll(autoLifeCategories);
        if (autoLifeCategroriesAdapter.getItemCount() > 0) {
            visibleView();
        } else {
            hideView();
        }

    }

    private void updateOrdering(ArrayList<Category> arrayList) {

        if (preferenceHelper.getSubCategoriesList().size() != arrayList.size()) {
            if (preferenceHelper.getCategoriesList() != null && preferenceHelper.getSubCategoriesList().size() > 0) {
                ArrayList<Category> categories = new ArrayList<>();
                ArrayList<Category> unmatchedList = new ArrayList<>();
                categories.addAll(preferenceHelper.getSubCategoriesList());
                int sameItemindex = -1;
                for (int i = 0; i < arrayList.size(); i++) {
                    boolean isExist = false;
                    for (int j = 0; j < categories.size(); j++) {
                        if (arrayList.get(i).getId() == categories.get(j).getId()) {
                            sameItemindex = j;
                            isExist = true;
                            break;
                        }
//                        else if (j != sameItemindex && arrayList.get(i).getId() != categories.get(j).getId()) {
//                            categories.add(arrayList.get(i));
//                        }
                    }

                    if (!isExist) {
                        categories.add(arrayList.get(i));

                    }
                }
                preferenceHelper.putSubCategoriesList(null);
                preferenceHelper.putSubCategoriesList(categories);
            } else {
                preferenceHelper.putSubCategoriesList(null);
                preferenceHelper.putSubCategoriesList(arrayList);
            }

        }
    }

    private void sendRequestConsultancy(String email, String name, String countryCode, String phone) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("name", name);
        if (countryCode != null) {
            params.put("country_code", countryCode);
        }
        if (phone != null) {
            params.put("phone", phone);
        }
        params.put("type", AppConstants.ContactType.CONSULTANCY);

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

    public void IntrectionCall(int id, int type, int pos) {
        InteractionCar interactionCar = new InteractionCar();
        interactionCar.setCar_id(id);
        interactionCar.setType(type);
        if (mainActivityContext.showLoader()) {
            WebApiRequest.Instance(mainActivityContext).request(
                    AppConstants.WebServicesKeys.CAR_INTRECTION, null,
                    interactionCar,
                    null,
                    new WebApiRequest.WebServiceObjectResponse() {
                        @Override
                        public void onSuccess(ApiResponse apiResponse) {
                            onClicke(pos);
                            mainActivityContext.hideLoader();
                        }

                        @Override
                        public void onError() {
                            mainActivityContext.hideLoader();
                        }
                    },
                    null);
        }
    }

    private void onClicke(int position) {
        if (child_category.get(position).getSlug().equals(AppConstants.MainCategoriesType.REVIEWS)) {
            OutletMallFragment outletMallFragment = new OutletMallFragment();
            outletMallFragment.setItem_id(AppConstants.WebServicesKeys.LUXURY_MARKET_CATEGORIES);
            outletMallFragment.setFromLuxuryMarket(false);
            outletMallFragment.setTitle(mainActivityContext.getResources().getString(R.string.reviews));
            outletMallFragment.setCategoryKey(AppConstants.MainCategoriesType.REVIEWS);
            LuxuryMarketSearchFilter.getInstance().resetFilter(false);
            mainActivityContext.replaceFragment(outletMallFragment, OutletMallFragment.class.getSimpleName(), true, true);
        } else if (child_category.get(position).getSlug().equals(AppConstants.MainCategoriesType.COMPARE)) {
            CompareFragment compareFragment = new CompareFragment();
            mainActivityContext.replaceFragment(compareFragment, CompareFragment.class.getSimpleName(), true, true);
        } else if (child_category.get(position).getSlug().equals(AppConstants.MainCategoriesType.EVALUATE_MY_CAR)) {
            if (!preferenceHelper.getLoginStatus()) {
//                launchSigninRequirement(mainActivityContext, mainActivityContext.getResources().getString(R.string.require_signin_evaluate));
                MyCarsDialogFragment myCarsDialogFragment = new MyCarsDialogFragment(mainActivityContext, null);
                myCarsDialogFragment.setEvaluate(true);
                myCarsDialogFragment.setTitle(child_category.get(position).getName());
                mainActivityContext.replaceFragment(myCarsDialogFragment, MyCarsDialogFragment.class.getSimpleName(), true, false);
            } else {
                if (mainActivityContext.showLoader()) {
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("limit", 1);
                    params.put("offset", 0);
                    WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.MY_TRADE_INS, null, null, params, null, new WebApiRequest.WebServiceArrayResponse() {
                        @Override
                        public void onSuccess(ApiArrayResponse apiArrayResponse) {
                            ArrayList<TradeCar> tradeCars = (ArrayList<TradeCar>) JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), TradeCar.class);
                            /*
                            if (tradeCars.size() == 0) {
                                TradeInYourCarFragment tradeInYourCarFragment = new TradeInYourCarFragment();
                                tradeInYourCarFragment.setEvaluate(true);
                                mainActivityContext.replaceFragment(tradeInYourCarFragment, TradeInYourCarFragment.class.getSimpleName(), true, false);
                            } else if (tradeCars.size() > 0) {
                                MyCarsDialogFragment myCarsDialogFragment = new MyCarsDialogFragment(mainActivityContext, tradeCars, null, 0);
                                myCarsDialogFragment.setEvaluate(true);
                                mainActivityContext.replaceFragment(myCarsDialogFragment, MyCarsDialogFragment.class.getSimpleName(), true, false);
                            }
                            */
                            MyCarsDialogFragment myCarsDialogFragment = new MyCarsDialogFragment(mainActivityContext, null);
                            myCarsDialogFragment.setEvaluate(true);
                            mainActivityContext.replaceFragment(myCarsDialogFragment, MyCarsDialogFragment.class.getSimpleName(), true, false);
                            mainActivityContext.hideLoader();
                        }

                        @Override
                        public void onError() {
                            mainActivityContext.hideLoader();
                        }
                    });
                }
            }
        } else if (child_category.get(position).getSlug().equals(AppConstants.MainCategoriesType.ASK_FOR_CONSULTANCY)) {
            requestConsultancy();
        } else if (child_category.get(position).getSlug().equals(AppConstants.MainCategoriesType.VIN_CHECK)) {
            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.coming_soon), Toast.LENGTH_SHORT);
        } else {
            SubcategoryDetailsFragment subcategoryDetailsFragment = new SubcategoryDetailsFragment();
            subcategoryDetailsFragment.setTitle(child_category.get(position).getName());
            subcategoryDetailsFragment.setCategoryId(child_category.get(position).getId());
            mainActivityContext.replaceFragment(subcategoryDetailsFragment, SubcategoryDetailsFragment.class.getSimpleName(), true, true);
        }
    }

    public void setChild_category(ArrayList<Category> child_category) {
        this.child_category = child_category;
    }

    public void setCategoryPositionsChangedListener(CategoryPositionsChangedListener categoryPositionsChangedListener) {
        this.categoryPositionsChangedListener = categoryPositionsChangedListener;
    }

    public void setParentPosition(int parentPosition) {
        this.parentPosition = parentPosition;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
