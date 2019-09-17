package com.ingic.caristocrat.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.adapters.MyTradeInAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentMyCarsDialogBinding;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.helpers.Utils;
import com.ingic.caristocrat.interfaces.LoadMoreListener;
import com.ingic.caristocrat.interfaces.OnCarSelectedForTradeListener;
import com.ingic.caristocrat.interfaces.SimpleDialogActionListener;
import com.ingic.caristocrat.models.TradeCar;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.models.TradeInCar;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("ValidFragment")
public class MyCarsDialogFragment extends BaseFragment implements View.OnClickListener, OnCarSelectedForTradeListener, LoadMoreListener {
    private FragmentMyCarsDialogBinding binding;
    private MainActivity activityContext;
    private MyTradeInAdapter adapter;
    private ArrayList<TradeCar> cars;
    private TradeCar customerTradeCar;
    private Titlebar titlebar;
    boolean evaluate;
    private String title;

    public MyCarsDialogFragment(MainActivity activityContext, TradeCar tradeCar) {
        this.activityContext = activityContext;
        this.cars = new ArrayList<>();
        this.customerTradeCar = tradeCar;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (cars != null) {
            cars.add(0, new TradeCar());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_cars_dialog, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (evaluate) {
            binding.llTrade.setVisibility(View.GONE);
            binding.llEvaluate.setVisibility(View.VISIBLE);
//            binding.tvHelperText.setText(mainActivityContext.getResources().getString(R.string.select_your_car_evaluate_or_add_car));
        }

        adapter = new MyTradeInAdapter(activityContext, new ArrayList<>(), true, this);
        binding.recyclerview.addItemDecoration(new DividerItemDecoration(activityContext, DividerItemDecoration.VERTICAL));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activityContext);
        binding.recyclerview.setLayoutManager(linearLayoutManager);
        binding.recyclerview.setAdapter(adapter);
        ViewCompat.setNestedScrollingEnabled(binding.recyclerview, false);

        if (cars != null && cars.size() > 0) {
            /*
            for(int i = 0; i < 15; i++){
                TradeCar car = new TradeCar();
                car.setChassis("Test " + i);
                car.setName("Test " + i);
                cars.add(car);
            }
            */
            adapter.addAllCars(cars);
            adapter.notifyDataSetChanged();
//            binding.scrollView.scrollTo(0, 0);

//            if (preferenceHelper.getLoginStatus()) {
//                tradeInYourCar();
//            }

        }

        /*
        binding.recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition == adapter.getItemCount() - 1) {
                    OFFSET += LIMIT;
                    tradeInYourCar();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        */
    }

    @Override
    public void onResume() {
        super.onResume();
        if (preferenceHelper.getLoginStatus()) {
            tradeInYourCar();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibBackbtn:
                activityContext.onBackPressed();
                break;
        }
    }

    @Override
    public void onNewCarAdd() {
        if (!preferenceHelper.getLoginStatus()) {


            launchSigninDailog(activityContext);

        }else {

            TradeInYourCarFragment tradeInYourCarFragment = new TradeInYourCarFragment();
            tradeInYourCarFragment.setOnCarSelectedForTradeListener(this);
            if (!evaluate) {
                tradeInYourCarFragment.setTrading(true);
                tradeInYourCarFragment.setTradeCar(customerTradeCar);
            } else {
                tradeInYourCarFragment.setTitle(title != null ? title : null);
                tradeInYourCarFragment.setEvaluate(true);
            }
            activityContext.replaceFragment(tradeInYourCarFragment, TradeInYourCarFragment.class.getSimpleName(), true, false);
        }
    }

    @Override
    public void onCarSelected(TradeCar tradeCar) {
        if (evaluate) {
            confirmEvaluate(tradeCar);
        } else {
            confirmTradeIn(tradeCar);
        }
    }

    @Override
    public void onCarAdded(TradeCar tradeCar) {
        tradeCar.setNewlyAdded(true);
//        cars.set(0, customerTradeCar);
//        cars.add(0, new TradeCar());
        if (adapter.getItemCount() > 0) {
            adapter.add(1, tradeCar);
            adapter.notifyDataSetChanged();
        } else if (adapter.getItemCount() == 0) {
        }
        if (evaluate) {
            confirmEvaluate(tradeCar);
        } else {
            confirmTradeIn(tradeCar);
        }
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        AppBarLayout.LayoutParams params = new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.WRAP_CONTENT);
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED | AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);
        mainActivityContext.getCollapsingToolBarLayout().setLayoutParams(params);
        mainActivityContext.getCollapsingToolBarLayout().requestLayout();
        mainActivityContext.getRvSubCategoryItem().setVisibility(View.GONE);
        mainActivityContext.getCollapsingToolBarLayout().setVisibility(View.VISIBLE);

        this.titlebar = titlebar;
        titlebar.resetTitlebar(mainActivityContext);
        titlebar.setTitle(mainActivityContext.getResources().getString(R.string.select_a_car));
        titlebar.showBackButton(mainActivityContext, false);
    }

    @Override
    public void onDestroyView() {
        if (!evaluate) {
            mainActivityContext.getIvSubCategoryItem().setImageResource(R.drawable.car_prof_bg);
            mainActivityContext.getIvSubCategoryItem().setScaleType(ImageView.ScaleType.FIT_XY);
            mainActivityContext.getIvSubCategoryItem().requestLayout();
            mainActivityContext.getCollapsingToolBarLayout().setVisibility(View.GONE);
            mainActivityContext.getRvSubCategoryItem().setVisibility(View.VISIBLE);
        }
        super.onDestroyView();
    }

    @Override
    public void onLoadMore() {
        if (preferenceHelper.getLoginStatus()) {
            tradeInYourCar();
        }
    }

    boolean stopCall = false;

    private void tradeInYourCar() {
        if (!stopCall) {
            HashMap<String, Object> params = new HashMap<>();
            if (mainActivityContext.internetConnected()) {
                WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.MY_TRADE_INS, null, null, params, null, new WebApiRequest.WebServiceArrayResponse() {
                    @Override
                    public void onSuccess(ApiArrayResponse apiArrayResponse) {
                        ArrayList<TradeCar> tradeCars = (ArrayList<TradeCar>) JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), TradeCar.class);
                        if (tradeCars.size() > 0) {
                            if (preferenceHelper.getLoginStatus()) {
                                cars.clear();
                                cars.add(0, new TradeCar());
                            }
                            cars.addAll(tradeCars);
                            adapter.addAll(cars);
                            adapter.notifyDataSetChanged();
                        }
//                        if (adapter.getItemCount() > 0 && tradeCars.size() == 0) {
//                            stopCall = true;
//                        }
                        mainActivityContext.hideLoader();
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

    private void confirmTradeIn(TradeCar tradeCar) {
        UIHelper.showSimpleDialog(
                activityContext,
                0,
                activityContext.getResources().getString(R.string.trade_in_request),
                activityContext.getResources().getString(R.string.submit_your) + " \"" + Utils.getCarNameByBrand(tradeCar, false) + "\" " + activityContext.getResources().getString(R.string.for_tradein_q),
                activityContext.getResources().getString(R.string.yes_trade_in),
                activityContext.getResources().getString(R.string.cancel),
                false,
                false,
                new SimpleDialogActionListener() {
                    @Override
                    public void onDialogActionListener(DialogInterface dialog, int which, boolean positive, boolean logout) {
                        if (positive) {
                            if (mainActivityContext.showLoader()) {
                                tradeInCar(tradeCar.getId(), customerTradeCar.getId(), 0, AppConstants.MyCarActions.TRADE);
                            }
                        } else {
                        }
                        dialog.dismiss();
                    }
                }
        );
    }

    private void confirmEvaluate(TradeCar tradeCar) {
        UIHelper.showSimpleDialog(
                activityContext,
                0,
                activityContext.getResources().getString(R.string.car_evaluation_from_dealers),
                activityContext.getResources().getString(R.string.submit_your) + " \"" + Utils.getCarNameByBrand(tradeCar, false) + "\" " + activityContext.getResources().getString(R.string.for_evaluation_q),
                activityContext.getResources().getString(R.string.yes),
                activityContext.getResources().getString(R.string.cancel),
                false,
                false,
                new SimpleDialogActionListener() {
                    @Override
                    public void onDialogActionListener(DialogInterface dialog, int which, boolean positive, boolean logout) {
                        if (positive) {
                            if (mainActivityContext.showLoader()) {
                                evaluateCar(tradeCar.getId(), AppConstants.MyCarActions.EVALUATE);
                            }
                        } else {
                        }
                        dialog.dismiss();
                    }
                }
        );
    }

    private void createconfirmTradeInDialog(TradeCar tradeCar) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivityContext);
        final View view = LayoutInflater.from(mainActivityContext).inflate(R.layout.layout_trade_in_dialog, null);
        builder.setView(view);
        final Dialog dialog;
        TextView tvTradingMessage = view.findViewById(R.id.tvTradingMessage);
        EditText etAmount = view.findViewById(R.id.etAmount);
        tvTradingMessage.setText(activityContext.getResources().getString(R.string.trade_in_confirm) + " " + tradeCar.getName() + " ?");
        builder.setTitle(activityContext.getResources().getString(R.string.trade_in_car));
        builder.setPositiveButton(activityContext.getResources().getString(R.string.yes_trade_in), new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (etAmount.getText().toString().length() == 0) {
                    UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.enter_trading_amount), Toast.LENGTH_SHORT);
                    return;
                }

                if (mainActivityContext.showLoader()) {
                    tradeInCar(tradeCar.getId(), customerTradeCar.getId(), Long.parseLong(etAmount.getText().toString()), 0);
                }
            }
        });
        builder.setNegativeButton(activityContext.getResources().getString(R.string.cancel), null);
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void tradeInCar(int myCarId, int customerCarId, long amount, int type) {
        TradeInCar tradeInCar = new TradeInCar();
        tradeInCar.setOwnerCarId(customerCarId);
        tradeInCar.setCustomerCarId(myCarId);
        tradeInCar.setAmount(amount);
        tradeInCar.setType(type);

        WebApiRequest.Instance(mainActivityContext).request(
                AppConstants.WebServicesKeys.POST_TRADE_IN_CAR,
                null,
                tradeInCar,
                null,
                new WebApiRequest.WebServiceObjectResponse() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        UIHelper.showToast(mainActivityContext, apiResponse.getMessage(), Toast.LENGTH_LONG);
                        mainActivityContext.onBackPressed();
                        mainActivityContext.hideLoader();
                    }

                    @Override
                    public void onError() {
//                        UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.car_already_traded), Toast.LENGTH_LONG);
                        mainActivityContext.hideLoader();
                    }
                },
                null
        );
    }

    private void evaluateCar(int myCarId, int type) {
        TradeInCar tradeInCar = new TradeInCar();
        tradeInCar.setCustomerCarId(myCarId);
        tradeInCar.setType(type);

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
                        UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.car_already_evaluated), Toast.LENGTH_LONG);
                        mainActivityContext.hideLoader();
                    }
                },
                null
        );
    }

    public void setEvaluate(boolean evaluate) {
        this.evaluate = evaluate;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
