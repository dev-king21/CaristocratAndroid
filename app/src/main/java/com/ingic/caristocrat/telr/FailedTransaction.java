package com.ingic.caristocrat.telr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ingic.caristocrat.fragments.CompareSegmentMainWiseFragment;
import com.ingic.caristocrat.fragments.MainDetailPageFragment;
import com.telr.mobile.sdk.activty.WebviewActivity;
import com.telr.mobile.sdk.entity.response.status.StatusResponse;

public class FailedTransaction extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        Object object = intent.getParcelableExtra(WebviewActivity.PAYMENT_RESPONSE);
        CompareSegmentMainWiseFragment.payment = false;
        MainDetailPageFragment.paid = false;///doubet
        TransactionDialog transactionDialog = TransactionDialog.newInstance(FailedTransaction.this);

        transactionDialog.setTitle("Failed!");
        transactionDialog.setCloseListener(new CloseDialog() {
            @Override
            public void onClose() {
                finish();
            }
        });

        if (object instanceof StatusResponse) {
            StatusResponse status = (StatusResponse) object;
            transactionDialog.setMessage("Transaction Failed" + " : " + status.getTrace() + " \n" + status.getAuth().getMessage());
        } else if (object instanceof String) {
            String errorMessage = (String) object;
            transactionDialog.setMessage("Transaction Failed" + " : " + errorMessage);
        }
        transactionDialog.show(FailedTransaction.this.getFragmentManager(), null);
        transactionDialog.setVisibility(View.VISIBLE);
    }
}
