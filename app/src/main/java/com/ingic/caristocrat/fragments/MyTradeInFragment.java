package com.ingic.caristocrat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.MyTradeInAdapter;
import com.ingic.caristocrat.adapters.TradedCarsAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentMyTradeInBinding;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.RecyclerTouchListener;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.interfaces.ClickListenerRecycler;
import com.ingic.caristocrat.models.TradeCar;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.models.TradedCars;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class MyTradeInFragment extends BaseFragment {
    FragmentMyTradeInBinding binding;
    TradedCarsAdapter adapter;
    MyTradeInAdapter myTradeInAdapter;
    ArrayList<TradeCar> tradeCars = new ArrayList<>();
    ArrayList<TradedCars> tradedCars = new ArrayList<>();
    String screenType;
    int LIMIT = 10;
    int OFFSET = 0;

    public MyTradeInFragment() {
    }

    //    public void setTradeCars(ArrayList<TradeCar> tradeCars) {
//        this.tradeCars=tradeCars;
//    }
    public static MyTradeInFragment Instance() {
        return new MyTradeInFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_trade_in, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeAdapter();
        if (tradedCars.size() == 0 && mainActivityContext.showLoader()) {
            getMyTradeIns();
        } else {
            adapter.addAll(tradedCars);
            adapter.notifyDataSetChanged();
        }
//        binding.swipeRefresh.setColorSchemeColors(ContextCompat.getColor(mainActivityContext,R.color.colorBlack));
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefresh.setRefreshing(false);
                if (mainActivityContext.showLoader()) {
                    LIMIT = 10;
                    OFFSET = 0;
                    getMyTradeIns();
                }
            }
        });
    }

    private void initializeAdapter() {
        myTradeInAdapter = new MyTradeInAdapter(mainActivityContext, new ArrayList<TradeCar>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivityContext);
        binding.recyclerViewMyTradeIn.setLayoutManager(linearLayoutManager);
        binding.recyclerViewMyTradeIn.setNestedScrollingEnabled(false);
//        binding.recyclerViewMyTradeIn.setAdapter(myTradeInAdapter);
//        if (tradeCars != null && tradeCars.size() > 0) {
//            myTradeInAdapter.addAll(tradeCars);
//            visibleView();
//        } else
//            hideView();
        binding.recyclerViewMyTradeIn.addOnItemTouchListener(new RecyclerTouchListener(mainActivityContext, binding.recyclerViewMyTradeIn, new ClickListenerRecycler() {
            @Override
            public void onClick(View view, int position) {
                TradeInDetailFragment tradeInDetailFragment = new TradeInDetailFragment();
//                tradeInDetailFragment.setCurrentTradeCar(tradedCars.get(position).getMyCar());
//                tradeInDetailFragment.setOfferedAmount(tradedCars.get(position).getAmount());
                tradeInDetailFragment.setScreenType(screenType);
                tradeInDetailFragment.setFromNotification(true);
                tradeInDetailFragment.setRefId(tradedCars.get(position).getId());
//                tradeInDetailFragment.setCurrentTradeCar(tradedCars.get(position).getTradeAgainstCar());
                mainActivityContext.replaceFragment(tradeInDetailFragment, TradeInDetailFragment.class.getSimpleName(), true, false);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        adapter = new TradedCarsAdapter(mainActivityContext, screenType);
        binding.recyclerViewMyTradeIn.setAdapter(adapter);
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(mainActivityContext);
        if (screenType != null) {
            if (screenType.equals(AppConstants.MyTradeInScreenTypes.TRADE_INS)) {
                titlebar.setTitle(mainActivityContext.getResources().getString(R.string.my_trades_in));
            } else if (screenType.equals(AppConstants.MyTradeInScreenTypes.EVALUATION)) {
                titlebar.setTitle(mainActivityContext.getResources().getString(R.string.my_evaluations));
            }
        }
        titlebar.showBackButton(mainActivityContext, false);
/*
        titlebar.showAddCarButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivityContext.replaceFragment(new TradeInYourCarFragment(), TradeInYourCarFragment.class.getSimpleName(), true, true);
            }
        });
*/
    }

    public void visibleView() {
        binding.recyclerViewMyTradeIn.setVisibility(View.VISIBLE);
        binding.noDataLayout.setVisibility(View.GONE);
    }

    public void hideView() {
        binding.recyclerViewMyTradeIn.setVisibility(View.GONE);
        binding.noDataLayout.setVisibility(View.VISIBLE);
    }

    private void getMyTradeIns() {
        Map<String, Object> params = new HashMap<>();
        if (screenType != null) {
            if (screenType.equals(AppConstants.MyTradeInScreenTypes.TRADE_INS)) {
                params.put("type", AppConstants.MyCarActions.TRADE);
            } else if (screenType.equals(AppConstants.MyTradeInScreenTypes.EVALUATION)) {
                params.put("type", AppConstants.MyCarActions.EVALUATE);
            }
        }

//        params.put("limit", LIMIT);
//        params.put("offset", OFFSET);
        WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.GET_TRADE_IN_CAR, binding.getRoot(), null, params, null, new WebApiRequest.WebServiceArrayResponse() {
            @Override
            public void onSuccess(ApiArrayResponse apiArrayResponse) {
                tradedCars = (ArrayList<TradedCars>) JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), TradedCars.class);
                if (tradedCars.size() > 0) {
                    adapter.addAll(tradedCars);
                    adapter.notifyDataSetChanged();
                    visibleView();
                } else {
                    hideView();
                }
                mainActivityContext.hideLoader();
            }

            @Override
            public void onError() {
                mainActivityContext.hideLoader();
                hideView();
            }
        });
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }
}
