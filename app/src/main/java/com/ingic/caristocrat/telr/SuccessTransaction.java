package com.ingic.caristocrat.telr;

import android.content.Intent;
import android.view.View;

import com.ingic.caristocrat.activities.BaseActivity;
import com.ingic.caristocrat.constants.AppConstants;
import com.ingic.caristocrat.fragments.CompareSegmentMainWiseFragment;
import com.ingic.caristocrat.fragments.CompareSegmentSubWiseFragment;
import com.ingic.caristocrat.fragments.MainDetailPageFragment;
import com.ingic.caristocrat.helpers.DateFormatHelper;
import com.ingic.caristocrat.webhelpers.models.ApiResponse;
import com.ingic.caristocrat.webhelpers.services.WebApiRequest;
import com.telr.mobile.sdk.activty.WebviewActivity;
import com.telr.mobile.sdk.entity.response.status.StatusResponse;

import java.util.HashMap;
import java.util.Map;

public class SuccessTransaction extends BaseActivity {
    TransactionDialog transactionDialog;

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        StatusResponse status = intent.getParcelableExtra(WebviewActivity.PAYMENT_RESPONSE);
        transactionDialog = TransactionDialog.newInstance(SuccessTransaction.this);
        transactionDialog.show(SuccessTransaction.this.getFragmentManager(), null);
        transactionDialog.setCloseListener(new CloseDialog() {
            @Override
            public void onClose() {
                finish();
            }
        });


        if (status.getAuth() != null) {
            status.getAuth().getStatus();   // Authorisation status. A indicates an authorised transaction. H also indicates an authorised transaction, but where the transaction has been placed on hold. Any other value indicates that the request could not be processed.
            status.getAuth().getAvs();      /* Result of the AVS check:
                                            Y = AVS matched OK
                                            P = Partial match (for example, post-code only)
                                            N = AVS not matched
                                            X = AVS not checked
                                            E = Error, unable to check AVS */
            status.getAuth().getCode();     // If the transaction was authorised, this contains the authorisation code from the card issuer. Otherwise it contains a code indicating why the transaction could not be processed.
            status.getAuth().getMessage();  // The authorisation or processing error message.
            status.getAuth().getCa_valid();
            status.getAuth().getCardcode(); // Code to indicate the card type used in the transaction. See the code list at the end of the document for a list of card codes.
            status.getAuth().getCardlast4();// The last 4 digits of the card number used in the transaction. This is supplied for all payment types (including the Hosted Payment Page method) except for PayPal.
            status.getAuth().getCvv();      /* Result of the CVV check:
                                           Y = CVV matched OK
                                           N = CVV not matched
                                           X = CVV not checked
                                           E = Error, unable to check CVV */
            status.getAuth().getTranref(); //The payment gateway transaction reference allocated to this request.
            status.getAuth().getCardfirst6(); // The first 6 digits of the card number used in the transaction, only for version 2 is submitted in Tran -> Version

//            setTransactionDetails(status.getAuth().getTranref(), status.getAuth().getCardlast4());

            if (CompareSegmentMainWiseFragment.payment || CompareSegmentSubWiseFragment.payment) {
                ProComparisonSubs(status.getAuth().getTranref());
            } else {
                if (oneReport)
                    oneReportPayment(status.getAuth().getTranref());
                else
                    allReportPayment(status.getAuth().getTranref());
            }
        }

    }

    private void oneReportPayment(String transactionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", getPreferenceHelper().getUser().getId());
        params.put("news_id", newsId);
        params.put("to_date", DateFormatHelper.AddOneYearInCurrentDate());
        params.put("transaction_id", transactionId);
        WebApiRequest.Instance(SuccessTransaction.this).request(
                AppConstants.WebServicesKeys.ONE_REPORT_PAYMENT, null,
                null,
                params,
                new WebApiRequest.WebServiceObjectResponse() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        if (apiResponse.isSuccess()) {

                            if (apiResponse.getData().toString().contains("user_id")) {
                                MainDetailPageFragment.paid = true;
                                transactionDialog.setVisibility(View.VISIBLE);
                            } else
                                transactionDialog.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError() {
                        MainDetailPageFragment.paid = false;
                    }
                }, null);
    }

    private void allReportPayment(String transactionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", getPreferenceHelper().getUser().getId());
        params.put("to_date", DateFormatHelper.AddOneYearInCurrentDate());
        params.put("transaction_id", transactionId);
        WebApiRequest.Instance(SuccessTransaction.this).request(
                AppConstants.WebServicesKeys.ALL_REPORT_PAYMENT, null,
                null,
                params,
                new WebApiRequest.WebServiceObjectResponse() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        if (apiResponse.isSuccess()) {
                            if (apiResponse.getData().toString().contains("user_id")) {
                                MainDetailPageFragment.paid = true;
                                transactionDialog.setVisibility(View.VISIBLE);
                            } else
                                transactionDialog.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError() {
                        MainDetailPageFragment.paid = false;
                    }
                }, null);
    }

    private void ProComparisonSubs(String transactionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", getPreferenceHelper().getUser().getId());
        params.put("to_date", DateFormatHelper.AddOneYearInCurrentDate());
        params.put("transaction_id", transactionId);
        WebApiRequest.Instance(SuccessTransaction.this).request(
                AppConstants.WebServicesKeys.PRO_COMPARISION_SUBS, null,
                null,
                params,
                new WebApiRequest.WebServiceObjectResponse() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {
                        if (apiResponse.isSuccess()) {
                            if (apiResponse.getData().toString().contains("user_id")) {
                                CompareSegmentMainWiseFragment.payment = true;
                                CompareSegmentSubWiseFragment.payment = true;
                                transactionDialog.setVisibility(View.VISIBLE);
                            } else
                                transactionDialog.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError() {
                        CompareSegmentMainWiseFragment.payment = false;
                        CompareSegmentSubWiseFragment.payment = false;
                    }
                }, null);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}


