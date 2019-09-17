package com.ingic.caristocrat.fragments;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.NotificationAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentNotificationsBinding;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.RecyclerTouchListener;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.ClickListenerRecycler;
import com.ingic.caristocrat.interfaces.SimpleDialogActionListener;
import com.ingic.caristocrat.models.NotificationWrapper;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class NotificationsFragment extends BaseFragment {
    FragmentNotificationsBinding binding;
    NotificationAdapter notificationAdapter;
    ArrayList<NotificationWrapper> notificationWrapperArrayList;
    boolean fromNotification;
    int actionID;
    String actionType;

    public NotificationsFragment() {
    }

    public static NotificationsFragment Instance() {
        return new NotificationsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifications, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeAdapter();
        if (mainActivityContext.showLoader())
            getNotifications();

//        binding.swipeRefresh.setColorSchemeColors(ContextCompat.getColor(mainActivityContext,R.color.colorBlack));
//        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (mainActivityContext.showLoader())
//                    getNotifications();
//                binding.swipeRefresh.setRefreshing(false);
//            }
//        });
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(mainActivityContext);
        titlebar.setTitle(mainActivityContext.getResources().getString(R.string.notifications));
        titlebar.showBackButton(mainActivityContext, false);
    }

    private void initializeAdapter() {
        notificationAdapter = new NotificationAdapter(mainActivityContext, new ArrayList<NotificationWrapper>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivityContext);
        binding.recyclerViewNotifications.setLayoutManager(linearLayoutManager);
        binding.recyclerViewNotifications.setNestedScrollingEnabled(false);
        binding.recyclerViewNotifications.setAdapter(notificationAdapter);
        binding.recyclerViewNotifications.addOnItemTouchListener(new RecyclerTouchListener(mainActivityContext, binding.recyclerViewNotifications, new ClickListenerRecycler() {
            @Override
            public void onClick(View view, int position) {
//                TradeCar tradeCar = new TradeCar();
//                tradeCar.setId(notificationWrapperArrayList.get(position).getRefId());
                TradeInDetailFragment tradeInDetailFragment = new TradeInDetailFragment();
                tradeInDetailFragment.setFromNotification(true);
                tradeInDetailFragment.setRefId(notificationWrapperArrayList.get(position).getRefId());
                if (notificationWrapperArrayList.get(position).getActionType() == AppConstants.MyCarActions.TRADE) {
                    tradeInDetailFragment.setScreenType(AppConstants.MyTradeInScreenTypes.TRADE_INS);
                } else if (notificationWrapperArrayList.get(position).getActionType() == AppConstants.MyCarActions.EVALUATE) {
                    tradeInDetailFragment.setScreenType(AppConstants.MyTradeInScreenTypes.EVALUATION);
                }
                mainActivityContext.replaceFragment(tradeInDetailFragment, TradeInDetailFragment.class.getSimpleName(), true, true);
//                UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.implemented_later), Toast.LENGTH_SHORT);
            }

            @Override
            public void onLongClick(View view, int position) {
                UIHelper.showSimpleDialog(
                        mainActivityContext,
                        0,
                        mainActivityContext.getResources().getString(R.string.delete_notification),
                        mainActivityContext.getResources().getString(R.string.delete_notification_confirmation),
                        mainActivityContext.getResources().getString(R.string.delete),
                        mainActivityContext.getResources().getString(R.string.cancel),
                        false,
                        false,
                        new SimpleDialogActionListener() {
                            @Override
                            public void onDialogActionListener(DialogInterface dialog, int which, boolean positive, boolean logout) {
                                Map<String, Object> params = new HashMap<>();
                                params.put("id", notificationWrapperArrayList.get(position).getId());
                                if (mainActivityContext.showLoader()) {
                                    WebApiRequest.Instance(mainActivityContext).request(
                                            AppConstants.WebServicesKeys.NOTIFICATION_DELETE,
                                            null,
                                            null,
                                            params,
                                            new WebApiRequest.WebServiceObjectResponse() {
                                                @Override
                                                public void onSuccess(ApiResponse apiResponse) {
                                                    notificationWrapperArrayList.remove(position);
                                                    notificationAdapter.remove(position);
                                                    notificationAdapter.notifyDataSetChanged();
                                                    UIHelper.showToast(mainActivityContext, apiResponse.getMessage(), Toast.LENGTH_SHORT);
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
                            }
                        }
                );
            }
        }));
    }

    public void visibleView() {
        binding.recyclerViewNotifications.setVisibility(View.VISIBLE);
//        binding.noDataLayout.setVisibility(View.GONE);
        binding.tvNoNotification.setVisibility(View.GONE);
    }

    public void hideView() {
        binding.recyclerViewNotifications.setVisibility(View.GONE);
        binding.tvNoNotification.setVisibility(View.VISIBLE);
//        binding.noDataLayout.setVisibility(View.VISIBLE);
    }

    private void getNotifications() {

        WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.GET_NOTIFICATIONS, binding.getRoot(), null, null, null, new WebApiRequest.WebServiceArrayResponse() {
            @Override
            public void onSuccess(ApiArrayResponse apiArrayResponse) {
                notificationWrapperArrayList = (ArrayList<NotificationWrapper>) JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), NotificationWrapper.class);
                notificationAdapter.addAll(notificationWrapperArrayList);
                mainActivityContext.hideLoader();
                if (notificationAdapter.getItemCount() > 0)
                    visibleView();
                else
                    hideView();

                if (fromNotification) {
                    fromNotification = false;
                    TradeInDetailFragment tradeInDetailFragment = new TradeInDetailFragment();
                    tradeInDetailFragment.setFromNotification(true);
                    tradeInDetailFragment.setRefId(actionID);
                    if (actionType != null) {
                        if (actionType.equals(String.valueOf(AppConstants.MyCarActions.TRADE))) {
                            tradeInDetailFragment.setScreenType(AppConstants.MyTradeInScreenTypes.TRADE_INS);
                        } else if (actionType.equals(String.valueOf(AppConstants.MyCarActions.EVALUATE))) {
                            tradeInDetailFragment.setScreenType(AppConstants.MyTradeInScreenTypes.EVALUATION);
                        }
                    }
                    mainActivityContext.replaceFragment(tradeInDetailFragment, TradeInDetailFragment.class.getSimpleName(), true, true);
                }
            }

            @Override
            public void onError() {
                if (notificationAdapter.getItemCount() > 0)
                    visibleView();
                else
                    hideView();
                mainActivityContext.hideLoader();
            }
        });
    }

    public void setFromNotification(boolean fromNotification) {
        this.fromNotification = fromNotification;
    }

    public void setActionID(int actionID) {
        this.actionID = actionID;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
}
