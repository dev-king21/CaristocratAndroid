package com.ingic.caristocrat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.FixedPagerAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentVirtualBuyBinding;
import com.ingic.caristocrat.helpers.JsonHelpers;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.models.BankInsuranceInformation;
import com.ingic.caristocrat.models.TradeCar;
import com.ingic.caristocrat.webhelpers.models.ApiArrayResponse;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;

import java.util.ArrayList;

public class VirtualBuyFragment extends BaseFragment implements View.OnClickListener {
    FragmentVirtualBuyBinding binding;
    FixedPagerAdapter adapter;
    TradeCar tradeCar;
    private boolean radioFinance = true;
    ArrayList<BankInsuranceInformation> arrayList;
    boolean onCashBuy;

    public VirtualBuyFragment() {
    }

    public static VirtualBuyFragment Instance(TradeCar tradeCar) {

        VirtualBuyFragment virtualBuyFragment = new VirtualBuyFragment();
        virtualBuyFragment.tradeCar = tradeCar;
        virtualBuyFragment.arrayList = new ArrayList<>();

        return virtualBuyFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_virtual_buy, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBankRates();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (onCashBuy) {
            onCashBuy = false;
            changeColor();
            radioFinance = false;
        }
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(mainActivityContext);
        titlebar.setTitle(mainActivityContext.getResources().getString(R.string.virtual_buy));
        titlebar.showBackButton(mainActivityContext, false).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivityContext.onBackPressed();
//                mainActivityContext.onBackPressed();
            }
        });
//        titlebar.showFilter().setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibBackbtn:
                break;

            case R.id.radiioFinance:
                if (!radioFinance) {
                    changeColor();
                    radioFinance = true;
                }

                break;

            case R.id.radioCashBuy:
                if (radioFinance) {
                    changeColor();
                    radioFinance = false;
                }
                break;
        }
    }

    private void initialize() {
        FinanceBuyFragment financeBuyFragment = new FinanceBuyFragment(tradeCar);
        financeBuyFragment.setArrayList(arrayList);
        mainActivityContext.replaceFragment(financeBuyFragment, FinanceBuyFragment.class.getSimpleName(), false, false, binding.fragframe.getId());
        binding.radiioFinance.setOnClickListener(this);
        binding.radioCashBuy.setOnClickListener(this);
//        adapter = new FixedPagerAdapter(getChildFragmentManager());
//        adapter.add(mainActivityContext.getResources().getString(R.string.finance_buy), FinanceBuyFragment.Instance(tradeCar));
//        adapter.add(mainActivityContext.getResources().getString(R.string.cash_buy), FinanceBuyFragment.Instance());
//        binding.viewpager.setAdapter(adapter);
//        binding.tabLayout.setupWithViewPager(binding.viewpager);
    }

    public void setTradeCar(TradeCar tradeCar) {
        this.tradeCar = tradeCar;
    }

    public void changeColor() {
        if (binding.radiioFinance.isChecked()) {
            binding.radiioFinance.setTextColor(getResources().getColor(R.color.white));
            binding.radioCashBuy.setTextColor(getResources().getColor(R.color.colorBlack));
            FinanceBuyFragment financeBuyFragment = new FinanceBuyFragment(tradeCar);
            financeBuyFragment.setArrayList(arrayList);
            mainActivityContext.replaceFragment(financeBuyFragment, FinanceBuyFragment.class.getSimpleName(), false, false, binding.fragframe.getId());
        } else {
            onCashBuy = true;
            binding.radioCashBuy.setTextColor(getResources().getColor(R.color.white));
            binding.radiioFinance.setTextColor(getResources().getColor(R.color.colorBlack));
            CashByFragment cashByFragment = new CashByFragment(tradeCar);
            cashByFragment.setArrayList(arrayList);
            mainActivityContext.replaceFragment(cashByFragment, CashByFragment.class.getSimpleName(), false, false, binding.fragframe.getId());
//            UIHelper.showToast(mainActivityContext, mainActivityContext.getResources().getString(R.string.implemented_later), Toast.LENGTH_LONG);
        }
    }

    private void getBankRates() {
        if (mainActivityContext.showLoader()) {
            WebApiRequest.Instance(mainActivityContext)
                    .request(AppConstants.WebServicesKeys.BANKS_RATES, binding.getRoot(), null, null, null, new WebApiRequest.WebServiceArrayResponse() {
                        @Override
                        public void onSuccess(ApiArrayResponse apiArrayResponse) {
                            arrayList.clear();
                            arrayList.addAll(JsonHelpers.convertToArrayModelClass(apiArrayResponse.getData(), BankInsuranceInformation.class));
                            initialize();
                            mainActivityContext.hideLoader();
                        }

                        @Override
                        public void onError() {
                            mainActivityContext.hideLoader();
                        }
                    });
        }
    }
}
