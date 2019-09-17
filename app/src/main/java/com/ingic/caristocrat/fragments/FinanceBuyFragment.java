package com.ingic.caristocrat.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.BankAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentFinanceBuyBinding;
import com.ingic.caristocrat.dialogs.CallConsultantDialog;
import com.ingic.caristocrat.dialogs.RequestConsultancyDialogFragment;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.OnRequestConsultancy;
import com.ingic.caristocrat.interfaces.SimpleDialogActionListener;
import com.ingic.caristocrat.models.BankInsuranceInformation;
import com.ingic.caristocrat.models.TradeCar;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FinanceBuyFragment extends BaseFragment implements View.OnClickListener, BankAdapter.BannkInterface, OnRequestConsultancy {
    FragmentFinanceBuyBinding binding;
    TradeCar tradeCar;
    BankAdapter bankAdapter, insuranceAdapter;
    ArrayList<BankInsuranceInformation> arrayList;
    double carAmount;
    DecimalFormat formatter;
    double regFee = AppConstants.REG_FEE;

    int bankID = 0;

    public FinanceBuyFragment() {
    }

    @SuppressLint("ValidFragment")
    public FinanceBuyFragment(TradeCar tradeCar) {
        this.tradeCar = tradeCar;
    }

    public static FinanceBuyFragment Instance() {
        return new FinanceBuyFragment();
    }

    public static FinanceBuyFragment Instance(TradeCar tradeCar) {
        return new FinanceBuyFragment(tradeCar);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_finance_buy, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        setCar();
        setListeners();
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
//        titlebar.resetTitlebar(mainActivityContext);
//        titlebar.setTitle(mainActivityContext.getResources().getString(R.string.virtual_buy));
//        titlebar.showBackButton(mainActivityContext, false);
//        mainActivityContext.getBinding().btnFilterAction.setVisibility(View.VISIBLE);
//        mainActivityContext.getBinding().btnFilterAction.setText(mainActivityContext.getResources().getString(R.string.enquire_now));
//        mainActivityContext.getBinding().btnFilterAction.setOnClickListener(this);
//        mainActivityContext.getBinding().llCommentsLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFilterAction:
                break;
        }
    }

    private void setListeners() {

        binding.rangeSeekbarPeriod.setMin(AppConstants.VIRTUAL_BUY_MIN_MONTHS);
        binding.rangeSeekbarPeriod.setMax(AppConstants.VIRTUAL_BUY_MAX_MONTHS);

        binding.rangeSeekbarDownPayment.setMin(AppConstants.VIRTUAL_BUY_MIN_PERCENTAGE);
        binding.rangeSeekbarDownPayment.setMax(AppConstants.VIRTUAL_BUY_75_PERCENTAGE);

        binding.rangeSeekbarInterestRate.setMin(AppConstants.VIRTUAL_BUY_MIN_PERCENTAGE);
        binding.rangeSeekbarInterestRate.setMax(AppConstants.VIRTUAL_BUY_10_PERCENTAGE);

        binding.rangeSeekbarInsuranceRate.setMin(AppConstants.VIRTUAL_BUY_MIN_PERCENTAGE);
        binding.rangeSeekbarInsuranceRate.setMax(AppConstants.VIRTUAL_BUY_10_PERCENTAGE);

        binding.rangeSeekbarPeriod.hideThumbText(true);
        binding.rangeSeekbarDownPayment.hideThumbText(true);
        binding.rangeSeekbarInterestRate.hideThumbText(true);
        binding.rangeSeekbarInterestRate.setDecimalScale(2);
        binding.rangeSeekbarInsuranceRate.hideThumbText(true);
        binding.rangeSeekbarInsuranceRate.setDecimalScale(2);

        binding.rangeSeekbarPeriod.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
//                setPeriod(seekParams.progress, Integer.valueOf((int) binding.rangeSeekbarPeriod.getMax()));
                calculateAmount();
//                if (seekParams.progress > binding.rangeSeekbarPeriod.getMin() && seekParams.progress < binding.rangeSeekbarPeriod.getMax())
//                    binding.rangeSeekbarPeriod.hideThumbText(false);
//                else
//                    binding.rangeSeekbarPeriod.hideThumbText(true);
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
//                if (seekBar.getProgress() > binding.rangeSeekbarPeriod.getMin() && seekBar.getProgress() < binding.rangeSeekbarPeriod.getMax())
//                    binding.rangeSeekbarPeriod.hideThumbText(false);
//                else
//                    binding.rangeSeekbarPeriod.hideThumbText(true);
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

                if (seekBar.getProgress() > binding.rangeSeekbarPeriod.getMin() && seekBar.getProgress() < binding.rangeSeekbarPeriod.getMax())
                    binding.rangeSeekbarPeriod.hideThumbText(false);
                else
                    binding.rangeSeekbarPeriod.hideThumbText(true);

            }
        });


        binding.rangeSeekbarDownPayment.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
//                setDownPayment(seekParams.progress, Integer.valueOf((int) binding.rangeSeekbarDownPayment.getMax()));
                calculateAmount();
                if (seekParams.progress > binding.rangeSeekbarDownPayment.getMin() && seekParams.progress < binding.rangeSeekbarDownPayment.getMax())
                    binding.rangeSeekbarDownPayment.hideThumbText(false);
                else
                    binding.rangeSeekbarDownPayment.hideThumbText(true);
            }


            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
                if (seekBar.getProgress() > binding.rangeSeekbarDownPayment.getMin() && seekBar.getProgress() < binding.rangeSeekbarDownPayment.getMax())
                    binding.rangeSeekbarDownPayment.hideThumbText(false);
                else
                    binding.rangeSeekbarDownPayment.hideThumbText(true);
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                if (seekBar.getProgress() > binding.rangeSeekbarDownPayment.getMin() && seekBar.getProgress() < binding.rangeSeekbarDownPayment.getMax())
                    binding.rangeSeekbarDownPayment.hideThumbText(false);
                else
                    binding.rangeSeekbarDownPayment.hideThumbText(true);
            }
        });

        binding.rangeSeekbarInterestRate.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
//                setInterestRate(seekParams.progressFloat, Integer.valueOf((int) binding.rangeSeekbarInterestRate.getMax()));
                calculateAmount(true);
//                            binding.tvCalculatedInterestPayment.setText(seekParams.progressFloat + "");
                if(seekParams.progressFloat == 0){
                    binding.tvCalculatedInterestPayment.setText(mainActivityContext.getResources().getString(R.string.percentage_0));
                }
                if (seekParams.progress > binding.rangeSeekbarInterestRate.getMin() && seekParams.progress < binding.rangeSeekbarInterestRate.getMax())
                    binding.rangeSeekbarInterestRate.hideThumbText(false);
                else
                    binding.rangeSeekbarInterestRate.hideThumbText(true);
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
                if (seekBar.getProgress() > binding.rangeSeekbarInterestRate.getMin() && seekBar.getProgress() < binding.rangeSeekbarInterestRate.getMax())
                    binding.rangeSeekbarInterestRate.hideThumbText(false);
                else
                    binding.rangeSeekbarInterestRate.hideThumbText(true);
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                if (seekBar.getProgress() > binding.rangeSeekbarInterestRate.getMin() && seekBar.getProgress() < binding.rangeSeekbarInterestRate.getMax())
                    binding.rangeSeekbarInterestRate.hideThumbText(false);
                else
                    binding.rangeSeekbarInterestRate.hideThumbText(true);
            }
        });
        binding.rangeSeekbarInsuranceRate.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
//                setInsuranceRate(seekParams.progressFloat, Integer.valueOf((int) binding.rangeSeekbarInsuranceRate.getMax()));
                calculateAmount();
                if (seekParams.progress > binding.rangeSeekbarInsuranceRate.getMin() && seekParams.progress < binding.rangeSeekbarInsuranceRate.getMax())
                    binding.rangeSeekbarInsuranceRate.hideThumbText(false);
                else
                    binding.rangeSeekbarInsuranceRate.hideThumbText(true);
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
                calculateAmount();
                if (seekBar.getProgress() > binding.rangeSeekbarInsuranceRate.getMin() && seekBar.getProgress() < binding.rangeSeekbarInsuranceRate.getMax())
                    binding.rangeSeekbarInsuranceRate.hideThumbText(false);
                else
                    binding.rangeSeekbarInsuranceRate.hideThumbText(true);
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                if (seekBar.getProgress() > binding.rangeSeekbarInsuranceRate.getMin() && seekBar.getProgress() < binding.rangeSeekbarInsuranceRate.getMax())
                    binding.rangeSeekbarInsuranceRate.hideThumbText(false);
                else
                    binding.rangeSeekbarInsuranceRate.hideThumbText(true);
            }
        });


//        binding.rangeSeekbarPeriod.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
//            @Override
//            public void valueChanged(Number value) {
//                setPeriod(value,  Integer.valueOf((int)binding.rangeSeekbarPeriod.getMaxValue()));
//                calculateAmount();
//            }
//        });

//        binding.rangeSeekbarDownPayment.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
//            @Override
//            public void valueChanged(Number value) {
//                setDownPayment(value, Integer.valueOf((int) binding.rangeSeekbarDownPayment.getMaxValue()));
//                calculateAmount();
//            }
//        });


//        binding.rangeSeekbarInsuranceRate.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
//            @Override
//            public void valueChanged(Number value) {
//                setInsuranceRate(value,  Integer.valueOf((int)binding.rangeSeekbarInsuranceRate.getMaxValue()));
//                calculateAmount();
//            }
//        });

//        binding.rangeSeekbarInterestRate.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
//            @Override
//            public void valueChanged(Number value) {
//                setInterestRate(value,  Integer.valueOf((int)binding.rangeSeekbarInterestRate.getMaxValue()));
//                calculateAmount();
//            }
//        });
    }

    private void setPeriod(Number minValue, Number maxValue) {
        binding.tvMinPeriod.setText(minValue + " " + mainActivityContext.getResources().getString(R.string.months));
        binding.tvMaxPeriod.setText(maxValue + " " + mainActivityContext.getResources().getString(R.string.months));
    }

    private void setDownPayment(Number minValue, Number maxValue) {
        binding.tvMinDownPayment.setText(minValue + " " + AppConstants.VIRTUAL_PERCENTAGE);
        binding.tvMaxDownPayment.setText(maxValue + " " + AppConstants.VIRTUAL_PERCENTAGE);
    }

    private void setInsuranceRate(Number minValue, Number maxValue) {
        binding.tvMinInsuranceRate.setText(minValue + " " + AppConstants.VIRTUAL_PERCENTAGE);
        binding.tvMaxInsuranceRate.setText(maxValue + " " + AppConstants.VIRTUAL_PERCENTAGE);
    }

    private void setInterestRate(Number minValue, Number maxValue) {
        binding.tvMinInterestRate.setText(minValue + " " + AppConstants.VIRTUAL_PERCENTAGE);
        binding.tvMaxInterestRate.setText(maxValue + " " + AppConstants.VIRTUAL_PERCENTAGE);
    }

    private void setCar() {
        if (tradeCar != null) {
            binding.llTradingCarDetail.setVisibility(View.VISIBLE);

            if (tradeCar.getMedia() != null && tradeCar.getMedia().size() > 0) {
                UIHelper.setImageWithGlide(mainActivityContext, binding.ivCarImage, tradeCar.getMedia().get(0).getFileUrl());
            }

            if (tradeCar.getModel() != null) {
                binding.tvCarBrand.setText(tradeCar.getModel().getName());
                if (tradeCar.getModel().getBrand() != null) {
                    binding.tvCarName.setText(tradeCar.getModel().getBrand().getName());
                }
            }
            binding.tvCarYear.setText(tradeCar.getYear() + "");
            binding.tvCarPrice.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + "\t" + NumberFormat.getNumberInstance(Locale.US).format(tradeCar.getAmount()));

            binding.tvDownPayment.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + 0);
            binding.tvDriveDownPayment.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + 0);
            binding.tvInsuranceFirstYear.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + 0);

            binding.tvMonthlyInstallment.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round(tradeCar.getAmount() / AppConstants.VIRTUAL_BUY_MIN_MONTHS)) + "");
            binding.tvRegistrationFee.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round(regFee)));
            binding.tvTotalUpFrontPayment.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round(regFee)));

            binding.tvCalculatedDownPayment.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " 0");
            binding.tvCalculatedInterestPayment.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " 0");
            binding.tvCalculatedInsurance.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " 0");

            calculateAmount();
        }
    }

    private void init() {
        formatter = new DecimalFormat("#0.00");
        carAmount = tradeCar.getAmount();
//        carAmount = 100000;
        binding.tvTotalPrice.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(carAmount) + "");
        bankAdapter = new BankAdapter(mainActivityContext, this);

        ArrayList<BankInsuranceInformation> banks = new ArrayList<>();
        for (int pos = 0; pos < arrayList.size(); pos++) {
            if (arrayList.get(pos).getType() == AppConstants.BankRatesTypes.BANK) {
                banks.add(arrayList.get(pos));
            }
        }

        bankAdapter.addAll(banks);
        binding.recyclerViewBank.setLayoutManager(new LinearLayoutManager(mainActivityContext, LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerViewBank.setAdapter(bankAdapter);
        binding.IndicatorViewBank.setRecyclerView(binding.recyclerViewBank);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.recyclerViewBank);

        insuranceAdapter = new BankAdapter(mainActivityContext, this);

        ArrayList<BankInsuranceInformation> insuranceInformations = new ArrayList<>();
        for (int pos = 0; pos < arrayList.size(); pos++) {
            if (arrayList.get(pos).getType() == AppConstants.BankRatesTypes.INSURANCE) {
                insuranceInformations.add(arrayList.get(pos));
            }
        }

        insuranceAdapter.addAll(insuranceInformations);
        binding.recyclerViewInsurance.setLayoutManager(new LinearLayoutManager(mainActivityContext, LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerViewInsurance.setAdapter(insuranceAdapter);
        binding.indicatorInsurance.setRecyclerView(binding.recyclerViewInsurance);
        SnapHelper snapHelper1 = new PagerSnapHelper();
        snapHelper1.attachToRecyclerView(binding.recyclerViewInsurance);
    }

    private void calculateAmount() {
        double fv = carAmount - (carAmount * (Double.parseDouble(binding.rangeSeekbarDownPayment.getProgress() + "") / 100));
        double AIR = (fv) * (Double.parseDouble(binding.rangeSeekbarInterestRate.getProgressFloat() + "") / 100);
        double IMP = AIR / 12;
        double TIP = IMP * Double.parseDouble(binding.rangeSeekbarPeriod.getProgress() + "");
        binding.tvDownPayment.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round(carAmount - fv)));
        binding.tvCalculatedDownPayment.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round(carAmount - fv)));
        binding.tvDriveDownPayment.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round(carAmount - fv)));
        binding.tvMonthlyInstallment.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round((fv + TIP) / binding.rangeSeekbarPeriod.getProgress())) + "");
//        binding.tvCalculatedInterestPayment.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.ceil((fv + TIP) / binding.rangeSeekbarPeriod.getProgress())) + "");
        double insurance = carAmount * (Double.parseDouble(binding.rangeSeekbarInsuranceRate.getProgressFloat() + "") / 100);
        double insFirstyr = insurance;
        binding.tvInsuranceFirstYear.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round(insFirstyr)));
        binding.tvCalculatedInsurance.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round(insFirstyr)));
        regFee = AppConstants.REG_FEE;
        binding.tvRegistrationFee.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round(regFee)));
        binding.tvTotalUpFrontPayment.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round(carAmount - fv + insFirstyr + regFee)));
    }

    private void calculateAmount(boolean interest) {
        double fv = carAmount - (carAmount * (Double.parseDouble(binding.rangeSeekbarDownPayment.getProgress() + "") / 100));
        double AIR = (fv) * (Double.parseDouble(binding.rangeSeekbarInterestRate.getProgressFloat() + "") / 100);
        double IMP = AIR / 12;
        double TIP = IMP * Double.parseDouble(binding.rangeSeekbarPeriod.getProgress() + "");
        binding.tvDownPayment.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round(carAmount - fv)));
        binding.tvCalculatedDownPayment.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round(carAmount - fv)));
        binding.tvDriveDownPayment.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round(carAmount - fv)));
        binding.tvMonthlyInstallment.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round((fv + TIP) / binding.rangeSeekbarPeriod.getProgress())) + "");
        if(interest){
            binding.tvCalculatedInterestPayment.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round((AIR))) + "");
//            binding.tvCalculatedInterestPayment.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.ceil((fv + TIP) / binding.rangeSeekbarPeriod.getProgress())) + "");
        }
        double insurance = carAmount * (Double.parseDouble(binding.rangeSeekbarInsuranceRate.getProgress() + "") / 100);
        double insFirstyr = insurance;
        binding.tvInsuranceFirstYear.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round(insFirstyr)));
        binding.tvCalculatedInsurance.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round(insFirstyr)));
        regFee = AppConstants.REG_FEE;
        binding.tvRegistrationFee.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round(regFee)));
        binding.tvTotalUpFrontPayment.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round(carAmount - fv + insFirstyr + regFee)));
    }

    private void requestConsultancy(BankInsuranceInformation bankInsuranceInformation) {
        this.bankID = bankInsuranceInformation.getId();
        RequestConsultancyDialogFragment requestConsultancyDialogFragment = new RequestConsultancyDialogFragment(mainActivityContext, this);
        requestConsultancyDialogFragment.setType(AppConstants.ContactType.VIRTUAL_BUY);
        requestConsultancyDialogFragment.setTradeCar(tradeCar);
        requestConsultancyDialogFragment.setBankId(bankID);
        mainActivityContext
                .getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(mainActivityContext.getMainFrameLayoutID(), requestConsultancyDialogFragment, RequestConsultancyDialogFragment.class.getSimpleName())
                .addToBackStack(mainActivityContext.getSupportFragmentManager().getBackStackEntryCount() == 0 ? "firstFrag" : null)
                .commit();
    }

    @Override
    public void onRequested(String email, String name, String countryCode, String phone, int type, String message) {
//        sendRequestConsultancy(email, name, countryCode, phone);
    }

    int companyTypeId;

    private void sendRequestConsultancy(int companyId, int companyTypeId, String email, String name, String countryCode, String phone) {
        this.companyTypeId = companyTypeId;
        Map<String, Object> params = new HashMap<>();
        params.put("car_id", tradeCar.getId());
        params.put("bank_id", companyId);
        params.put("email", email);
        params.put("name", name);
        if (countryCode != null) {
            params.put("country_code", countryCode);
        }
        if (phone != null) {
            params.put("phone", phone);
        }
        params.put("type", companyTypeId);

        if (mainActivityContext.showLoader()) {
            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.REQUEST_CONSULTANCY, null, null, params, new WebApiRequest.WebServiceObjectResponse() {
                @Override
                public void onSuccess(ApiResponse apiResponse) {
                    mainActivityContext.hideLoader();
                    String message = "";
                    if (companyTypeId == AppConstants.BankRatesTypes.BANK) {
                        message = mainActivityContext.getResources().getString(R.string.the_bank_received);
                    } else if (companyTypeId == AppConstants.BankRatesTypes.INSURANCE) {
                        message = mainActivityContext.getResources().getString(R.string.the_insurance_received);
                    }
                    UIHelper.showSimpleDialog(
                            mainActivityContext,
                            0,
                            mainActivityContext.getResources().getString(R.string.success_ex),
                            message,
                            mainActivityContext.getResources().getString(R.string.ok_go_back),
                            null,
                            false,
                            false,
                            new SimpleDialogActionListener() {
                                @Override
                                public void onDialogActionListener(DialogInterface dialog, int which, boolean positive, boolean logout) {
                                    dialog.dismiss();
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

    @Override
    public void onBankItemRequestCallClick(BankInsuranceInformation bankInsuranceInformation) {
        IntrectionCall(tradeCar.getId(), AppConstants.Interaction.REQUEST);
//        requestConsultancy(bankInsuranceInformation);
        if (!preferenceHelper.getLoginStatus()) {
            if (bankInsuranceInformation.getType() == AppConstants.BankRatesTypes.BANK) {
                launchSigninRequirement(mainActivityContext, mainActivityContext.getResources().getString(R.string.require_signin_bank));
            } else if (bankInsuranceInformation.getType() == AppConstants.BankRatesTypes.INSURANCE) {
                launchSigninRequirement(mainActivityContext, mainActivityContext.getResources().getString(R.string.require_signin_insurance));
            }
        } else {
            sendRequestConsultancy(bankInsuranceInformation.getId(), bankInsuranceInformation.getType(), preferenceHelper.getUser().getEmail(), preferenceHelper.getUser().getName(), preferenceHelper.getUser().getDetails().getCountryCode(), preferenceHelper.getUser().getDetails().getPhone());
        }
    }

    @Override
    public void onBankItemCallNowClick(BankInsuranceInformation bankInsuranceInformation) {
//        IntrectionCall(1, AppConstants.Interaction.PHONE);
//        if (currentTradeCar.getUser() != null && currentTradeCar.getUser().getDetails() != null)
        if (bankInsuranceInformation.getTitle() != null && bankInsuranceInformation.getPhoneNo() != null) {
            callConsultant(bankInsuranceInformation.getTitle(), bankInsuranceInformation.getPhoneNo());
        }
    }

    private void callConsultant(String name, final String number) {
        CallConsultantDialog callConsultantDialog = CallConsultantDialog.newInstance(mainActivityContext, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                IntrectionCall(1, AppConstants.Interaction.PHONE);
            }
        });
        callConsultantDialog.setConsultantNameAndPhone(mainActivityContext.getResources().getString(R.string.would_you_like) + " " + name, number);
        callConsultantDialog.show(mainActivityContext.getFragmentManager(), null);
    }

    public void setArrayList(ArrayList<BankInsuranceInformation> arrayList) {
        this.arrayList = arrayList;
    }
}
