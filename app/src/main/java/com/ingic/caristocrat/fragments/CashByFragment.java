package com.ingic.caristocrat.fragments;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.adapters.BankAdapter;
import com.ingic.caristocrat.adapters.FixedPagerAdapter;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.databinding.FragmentCashByBinding;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class CashByFragment extends BaseFragment implements View.OnClickListener, BankAdapter.BannkInterface, OnRequestConsultancy {
    FragmentCashByBinding binding;
    TradeCar tradeCar;
    FixedPagerAdapter adapter;
    ArrayList<BankInsuranceInformation> arrayList;

    BankAdapter bankAdapter, insuranceAdapter;

    double regFee = AppConstants.REG_FEE;
    int bankID = 0;

    public CashByFragment() {

    }

    @SuppressLint("ValidFragment")
    public CashByFragment(TradeCar tradeCar) {
        this.tradeCar = tradeCar;
    }

    public static CashByFragment Instance(TradeCar tradeCar) {
        CashByFragment cashByFragment = new CashByFragment();
        cashByFragment.tradeCar = tradeCar;
        return (cashByFragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cash_by, container, false);
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
//        binding.rangeSeekbarPeriod.setMinValue(AppConstants.VIRTUAL_BUY_MIN_MONTHS);
//        binding.rangeSeekbarPeriod.setMaxValue(AppConstants.VIRTUAL_BUY_MAX_MONTHS);
//
//        binding.rangeSeekbarDownPayment.setMinValue(AppConstants.VIRTUAL_BUY_MIN_PERCENTAGE);
//        binding.rangeSeekbarDownPayment.setMaxValue(AppConstants.VIRTUAL_BUY_75_PERCENTAGE);
//
//        binding.rangeSeekbarInterestRate.setMinValue(AppConstants.VIRTUAL_BUY_MIN_PERCENTAGE);
//        binding.rangeSeekbarInterestRate.setMaxValue(AppConstants.VIRTUAL_BUY_30_PERCENTAGE);
        binding.rangeSeekbarInsuranceRate.setMin(AppConstants.VIRTUAL_BUY_MIN_PERCENTAGE);
        binding.rangeSeekbarInsuranceRate.setMax(AppConstants.VIRTUAL_BUY_10_PERCENTAGE);
        binding.rangeSeekbarInsuranceRate.hideThumbText(true);
        binding.rangeSeekbarInsuranceRate.setDecimalScale(2);
//        binding.rangeSeekbarPeriod.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
//            @Override
//            public void valueChanged(Number value) {
//                setPeriod(value, binding.rangeSeekbarPeriod.getMaxValue());
//                calculateAmount();
//            }
//        });

//        binding.rangeSeekbarDownPayment.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
//            @Override
//            public void valueChanged(Number value) {
//                setDownPayment(value, binding.rangeSeekbarDownPayment.getMaxValue());
//                calculateAmount();
//            }
//        });
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
                calculateAmount();
                if (seekBar.getProgress() > binding.rangeSeekbarInsuranceRate.getMin() && seekBar.getProgress() < binding.rangeSeekbarInsuranceRate.getMax())
                    binding.rangeSeekbarInsuranceRate.hideThumbText(false);
                else
                    binding.rangeSeekbarInsuranceRate.hideThumbText(true);
            }
        });

//        binding.rangeSeekbarInsuranceRate.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
//            @Override
//            public void valueChanged(Number value) {
//                setInsuranceRate(value, Integer.valueOf((int) binding.rangeSeekbarInsuranceRate.getMaxValue()));
//                calculateAmount();
//            }
//        });

//        binding.rangeSeekbarInterestRate.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
//            @Override
//            public void valueChanged(Number value) {
//                setInterestRate(value, binding.rangeSeekbarInterestRate.getMaxValue());
//                calculateAmount();
//            }
//        });
    }

    private void setPeriod(Number minValue, Number maxValue) {
//        binding.tvMinPeriod.setText(minValue + " " + mainActivityContext.getResources().getString(R.string.months));
//        binding.tvMaxPeriod.setText(maxValue + " " + mainActivityContext.getResources().getString(R.string.months));
    }

    private void setDownPayment(Number minValue, Number maxValue) {
//        binding.tvMinDownPayment.setText(minValue + " " + AppConstants.VIRTUAL_PERCENTAGE);
//        binding.tvMaxDownPayment.setText(maxValue + " " + AppConstants.VIRTUAL_PERCENTAGE);
    }

    private void setInsuranceRate(Number minValue, Number maxValue) {
        binding.tvMinInsuranceRate.setText(minValue + " " + AppConstants.VIRTUAL_PERCENTAGE);
        binding.tvMaxInsuranceRate.setText(maxValue + " " + AppConstants.VIRTUAL_PERCENTAGE);
    }

    private void setInterestRate(Number minValue, Number maxValue) {
//        binding.tvMinInterestRate.setText(minValue + " " + AppConstants.VIRTUAL_PERCENTAGE);
//        binding.tvMaxInterestRate.setText(maxValue + " " + AppConstants.VIRTUAL_PERCENTAGE);
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

            binding.tvInsuranceFirstYear.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + 0);
            binding.tvRegistrationFee.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + 0);

            binding.tvDriveDownPayment.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round(tradeCar.getAmount())));
            binding.tvTotalUpFrontPayment.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round(tradeCar.getAmount() + regFee)));

            binding.tvCalculatedInsurance.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " 0");
        }
    }

    private void init() {
//        binding.tvTotalPrice.setText(tradeCar.getAmount() + "");
//        binding.radiioFinance.setOnClickListener(this);
//        binding.radioCashBuy.setOnClickListener(this);

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
        SnapHelper snapHelper1 = new PagerSnapHelper();
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
        snapHelper1.attachToRecyclerView(binding.recyclerViewInsurance);

    }

    private void calculateAmount() {
        DecimalFormat formatter = new DecimalFormat("#0.00");
//        double fv = tradeCar.getAmount() * (Double.parseDouble(binding.rangeSeekbarDownPayment.getSelectedMinValue() + "") / 100);
//        double AIR = (tradeCar.getAmount() - fv) * (Double.parseDouble(binding.rangeSeekbarInterestRate.getSelectedMinValue() + "") / 100);
//        double IMP = AIR / 12;
//        double TIP = IMP * Double.parseDouble(binding.rangeSeekbarPeriod.getSelectedMinValue() + "");
//        binding.tvDownPayment.setText(getString(R.string.aed) + formatter.format( fv));
        binding.tvDriveDownPayment.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round(tradeCar.getAmount())));
//        binding.tvMonthlyInstallment.setText(getString(R.string.aed) + formatter.format((fv + TIP) / Double.parseDouble(binding.rangeSeekbarPeriod.getSelectedMinValue() + "")));
        double insurance = tradeCar.getAmount() * (Double.parseDouble(binding.rangeSeekbarInsuranceRate.getProgressFloat() + "") / 100);
//        double insFirstyr = tradeCar.getAmount() * insurance;
        binding.tvInsuranceFirstYear.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round(insurance)));
        binding.tvCalculatedInsurance.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round(insurance)));
        double regFee = AppConstants.REG_FEE;

        binding.tvRegistrationFee.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + Math.round(regFee));

        binding.tvTotalUpFrontPayment.setText((tradeCar.getCurrency() == null ? mainActivityContext.getCurrency() : tradeCar.getCurrency()) + " " + NumberFormat.getNumberInstance(Locale.US).format(Math.round(tradeCar.getAmount() + insurance + regFee)));

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
        sendRequestConsultancy(email, name, countryCode, phone);
    }

    private void sendRequestConsultancy(String email, String name, String countryCode, String phone) {
        Map<String, Object> params = new HashMap<>();
        params.put("car_id", tradeCar.getId());
        params.put("bank_id", this.bankID);
        params.put("email", email);
        params.put("name", name);
        if (countryCode != null) {
            params.put("country_code", countryCode);
        }
        if (phone != null) {
            params.put("phone", phone);
        }
        params.put("type", AppConstants.ContactType.VIRTUAL_BUY);

        if (mainActivityContext.showLoader()) {
            WebApiRequest.Instance(mainActivityContext).request(AppConstants.WebServicesKeys.REQUEST_CONSULTANCY, null, null, params, new WebApiRequest.WebServiceObjectResponse() {
                @Override
                public void onSuccess(ApiResponse apiResponse) {
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

    @Override
    public void onBankItemRequestCallClick(BankInsuranceInformation bankInsuranceInformation) {
        IntrectionCall(tradeCar.getId(), AppConstants.Interaction.REQUEST);
        if (!preferenceHelper.getLoginStatus()) {
            if (bankInsuranceInformation.getType() == AppConstants.BankRatesTypes.BANK) {
                launchSigninRequirement(mainActivityContext, mainActivityContext.getResources().getString(R.string.require_signin_bank));
            } else if (bankInsuranceInformation.getType() == AppConstants.BankRatesTypes.INSURANCE) {
                launchSigninRequirement(mainActivityContext, mainActivityContext.getResources().getString(R.string.the_insurance_received));
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

    public void setArrayList(ArrayList<BankInsuranceInformation> arrayList) {
        this.arrayList = arrayList;
    }
}
