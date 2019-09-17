package com.ingic.caristocrat.fragments;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.MainCategoriesAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentHomeBinding;
import com.ingic.caristocrat.helpers.DialogFactory;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.RecyclerTouchListener;
import com.ingic.caristocrat.helpers.SimpleItemTouchHelperCallback;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.CategoryPositionsChangedListener;
import com.ingic.caristocrat.interfaces.ClickListenerRecycler;
import com.ingic.caristocrat.interfaces.OnStartDragListener;
import com.ingic.caristocrat.models.Region;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.models.Category;
import com.ingic.caristocrat.webhelpers.models.User;
import com.ingic.caristocrat.webhelpers.models.UserWrapper;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, OnStartDragListener, CategoryPositionsChangedListener {
    FragmentHomeBinding binding;
    LinearLayoutManager linearLayoutManager;
    MainCategoriesAdapter adapter;
    ItemTouchHelper mItemTouchHelper;
    ArrayList<Category> arrayList = new ArrayList<>();
    User user;
    ArrayList<Region> regions;
    Region selectedRegion;

    public HomeFragment() {
    }

    public static HomeFragment Instance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = preferenceHelper.getUser();

        initAdapter();

        setListeners();

        if (mainActivityContext.showLoader()) {
            getCategories();
        }
//        binding.swipeRefresh.setColorSchemeColors(ContextCompat.getColor(mainActivityContext,R.color.colorBlack));
//        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (mainActivityContext.showLoader()) {
//                    getCategories();
//                }
//                binding.swipeRefresh.setRefreshing(false);
//            }
//        });
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(mainActivityContext);
        titlebar.showTitlebar(mainActivityContext);
        titlebar.setTitle(mainActivityContext.getResources().getString(R.string.app_name));
        if (user != null && user.getDetails() != null) {
            titlebar.setProfilepic(mainActivityContext, user.getDetails().getImageUrl());
//            if (!user.getDetails().getSocialLogin())
//            else
//                titlebar.setProfilepic(mainActivityContext, user.getDetails().getImage());
        }
        titlebar.showProfileButton().setOnClickListener(this);
        titlebar.showNotifications().setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llProfileButton:
                if (!preferenceHelper.getLoginStatus()) {
                    launchSigninRequirement(mainActivityContext, mainActivityContext.getResources().getString(R.string.require_signin_message_profile));
                } else {
                    mainActivityContext.replaceFragment(ProfileFragment.Instance(), ProfileFragment.class.getSimpleName(), true, false);
                }
                break;
            case R.id.rlNotification:
                if (!preferenceHelper.getLoginStatus()) {
                    launchSigninRequirement(mainActivityContext, mainActivityContext.getResources().getString(R.string.require_signin_message_notification));
                } else {
                    mainActivityContext.replaceFragment(NotificationsFragment.Instance(), NotificationsFragment.class.getSimpleName(), true, false);
                }
                break;
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onCategoryPositionChanged(int parentPosition, ArrayList<Category> categories) {
        ArrayList<Category> parentCategories = preferenceHelper.getCategoriesList();
        if (parentCategories != null && parentCategories.size() > 0) {
            parentCategories.get(parentPosition).setChild_category(categories);
        }
        preferenceHelper.putCategoriesList(parentCategories);
    }

    void setListeners() {
//        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                binding.swipeContainer.setRefreshing(false);
//            }
//        });
    }

    private void initAdapter() {
        adapter = new MainCategoriesAdapter(mainActivityContext, this, binding);
        linearLayoutManager = new LinearLayoutManager(mainActivityContext);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(binding.recyclerView);
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setAdapter(adapter);
        if (preferenceHelper.getCategoriesList() != null && preferenceHelper.getCategoriesList().size() > 0) {
            visibleView();
            adapter.addAll(preferenceHelper.getCategoriesList());
            adapter.notifyDataSetChanged();
        }
        binding.recyclerView.addOnItemTouchListener(new RecyclerTouchListener(mainActivityContext, binding.recyclerView, new ClickListenerRecycler() {
            @Override
            public void onClick(View view, int position) {
                if (mainActivityContext.internetConnected()) {

                    IntrectionCall(preferenceHelper.getCategoriesList().get(position).getId(), AppConstants.Interaction.MAIN_CAT);

                    if (preferenceHelper.getCategoriesList().get(position).getType() == AppConstants.MainCategoriesType.NEWS || preferenceHelper.getCategoriesList().get(position).getType() == AppConstants.MainCategoriesType.CONSULTANT || preferenceHelper.getCategoriesList().get(position).getType() == 0) {
                        if (preferenceHelper.getCategoriesList().get(position).getChild_category().size() > 0) {
                            AutoLifeFragment autoLifeFragment = new AutoLifeFragment();
                            autoLifeFragment.setChild_category(preferenceHelper.getCategoriesList().get(position).getChild_category());
                            autoLifeFragment.setTitle(preferenceHelper.getCategoriesList().get(position).getName());
                            autoLifeFragment.setParentPosition(position);
                            autoLifeFragment.setCategoryPositionsChangedListener(HomeFragment.this);
                            mainActivityContext.replaceFragment(autoLifeFragment, AutoLifeFragment.class.getSimpleName(), true, true);
                        } else {
                            SubcategoryDetailsFragment subcategoryDetailsFragment = new SubcategoryDetailsFragment();
                            subcategoryDetailsFragment.setTitle(preferenceHelper.getCategoriesList().get(position).getName());
                            subcategoryDetailsFragment.setCategoryId(preferenceHelper.getCategoriesList().get(position).getId());
                            mainActivityContext.replaceFragment(subcategoryDetailsFragment, SubcategoryDetailsFragment.class.getSimpleName(), true, true);
                        }
                    } else if (preferenceHelper.getCategoriesList().get(position).getType() == AppConstants.MainCategoriesType.LUXURY_MARKET) {
                        LuxuryMarketFragment luxuryMarketFragment = new LuxuryMarketFragment();
                        luxuryMarketFragment.setChild_category(preferenceHelper.getCategoriesList().get(position).getChild_category());
                        mainActivityContext.replaceFragment(luxuryMarketFragment, LuxuryMarketFragment.class.getSimpleName(), true, true);
                    }
                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    public void visibleView() {
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.noDataLayout.setVisibility(View.GONE);
    }

    public void hideView() {
        binding.recyclerView.setVisibility(View.GONE);
        binding.noDataLayout.setVisibility(View.VISIBLE);
    }

    private void getCategories() {
        Map<String, Object> params = new HashMap<>();
        params.put("parent_id", 0);

        WebApiRequest.Instance(mainActivityContext).request(
                AppConstants.WebServicesKeys.CATEGORIES, binding.getRoot(),
                null,
                params,
                null,
                new WebApiRequest.WebServiceArrayResponse() {
                    @Override
                    public void onSuccess(ApiArrayResponse apiArrayResponse) {
                        arrayList.clear();
                        arrayList = JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), Category.class);
                        mainActivityContext.setMainCategories(arrayList);
                        if (preferenceHelper.getCategoriesList() != null && mainActivityContext.getPreferenceHelper().getCategoriesList().size() > 0) {
                            ArrayList<Category> updatedList = updateCategories(preferenceHelper.getCategoriesList(), arrayList);
                            preferenceHelper.putCategoriesList(updatedList);
                            adapter.addAll(preferenceHelper.getCategoriesList());
                            adapter.notifyDataSetChanged();
                        } else {
                            preferenceHelper.putCategoriesList(arrayList);
                            adapter.addAll(preferenceHelper.getCategoriesList());
                            adapter.notifyDataSetChanged();
                        }
                        if (adapter.getItemCount() > 0) {
//                            binding.recyclerView.smoothScrollToPosition(0);
                            visibleView();
                        } else {
                            hideView();
                        }
                        mainActivityContext.hideLoader();

                        if (preferenceHelper.getLoginStatus()) {
                            if (preferenceHelper.getUser() != null && preferenceHelper.getUser().getDetails() != null && preferenceHelper.getUser().getDetails().getUserRegionDetail() == null) {
                                getRegions();
                            }
                        }
                    }

                    @Override
                    public void onError() {
                        if (adapter.getItemCount() > 0) {
                            visibleView();
                        } else {
                            hideView();
                        }
                        mainActivityContext.hideLoader();
                    }
                }
        );
    }

    private ArrayList<Category> updateCategories(ArrayList<Category> oldList, ArrayList<Category> newList) {
        //First Case: Replace Old Category Object from New Category Object without disordering the saved position
        for (int i = 0; i < oldList.size(); i++) {
            for (int j = 0; j < newList.size(); j++) {
                if (oldList.get(i).getId() == newList.get(j).getId()) {
                    Category newCategory = newList.get(j);
                    newCategory.setChild_category(updateCategories(oldList.get(i).getChild_category(), newList.get(j).getChild_category()));
                    oldList.set(i, newCategory);
                    break;
                }
            }
        }

        //Second Case: If element of new list is not found in old list
        boolean found = false;
        for (int i = 0; i < newList.size(); i++) {
            for (int j = 0; j < oldList.size(); j++) {
                if (newList.get(i).getId() == oldList.get(j).getId()) {
                    found = true;
                    break;
                } else {
                    found = false;
                }
            }
            if (!found) {
                oldList.add(newList.get(i));
            }
        }

        //Third Case: If element exists in saved list but removed from fetched list
        boolean exist = false;
        for (int i = 0; i < oldList.size(); i++) {
            for (int j = 0; j < newList.size(); j++) {
                if (oldList.get(i).getId() == newList.get(j).getId()) {
                    exist = true;
                    break;
                } else {
                    exist = false;
                }
            }
            if (!exist) {
                oldList.remove(i);
                break;
            }
        }

        ArrayList<Category> checkingCategories = oldList;
/*
        for (int i = 0; i < newList.size(); i++) {
            for (int j = 0; j < checkingCategories.size(); j++) {
                if (newList.get(i).getId() == checkingCategories.get(j).getId()) {
                    checkingCategories.get(j).setUnreadCount(newList.get(i).getUnreadCount());
                } else {
                    checkingCategories.get(j).setUnreadCount(0);
                }
            }
        }
*/
        oldList = checkingCategories;

        return oldList;
    }

    private void openRegionPicker() {
        if (regions != null && regions.size() > 0) {
            final ArrayList<String> regionsNames = new ArrayList<>();

            for (int i = 0; i < regions.size(); i++) {
                regionsNames.add(regions.get(i).getName());
            }
            DialogFactory.listDialog(mainActivityContext, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    selectedRegion = regions.get(i);
                    if (selectedRegion != null) {
                        updateProfileCall(selectedRegion.getId());
                    }
                }
            }, mainActivityContext.getResources().getString(R.string.select_your_region), regionsNames, false);
        }
    }

    private void getRegions() {
        if (mainActivityContext.showLoader()) {
            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.REGIONS, null, null, null, null, new WebApiRequest.WebServiceArrayResponse() {
                @Override
                public void onSuccess(ApiArrayResponse apiArrayResponse) {
                    regions = (ArrayList<Region>) JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), Region.class);
                    openRegionPicker();
                    mainActivityContext.hideLoader();
                }

                @Override
                public void onError() {
                    mainActivityContext.hideLoader();
                }
            });
        }
    }

    private void updateProfileCall(int id) {
        Map<String, Object> params = new HashMap<>();
        params.put("image", null);
        params.put("region_id", id);

        if (mainActivityContext.showLoader()) {
            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.UPDATE_PROFILE, null, null, params, new WebApiRequest.WebServiceObjectResponse() {
                @Override
                public void onSuccess(ApiResponse apiResponse) {
                    UserWrapper userWrapper = (UserWrapper) JsonHelpers.convertToModelClass(apiResponse.getData(), UserWrapper.class);
                    preferenceHelper.putUser(userWrapper.getUser());
                    UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.region_updated_successfully), Toast.LENGTH_SHORT);
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
