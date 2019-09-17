package com.ingic.caristocrat.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.databinding.LayoutPaymentDialogBinding;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.PaymentListener;

public class PaymentDialog extends DialogFragment implements View.OnClickListener {
    MainActivity context;
    LayoutPaymentDialogBinding binding;

    PaymentListener paymentListener;

    public static PaymentDialog newInstance(MainActivity context) {
        PaymentDialog fragment = new PaymentDialog();
        fragment.context = context;
        return fragment;
    }

    public void setPaymentListener(PaymentListener paymentListener) {
        this.paymentListener = paymentListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.DialogTheme);
        setCancelable(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_payment_dialog, container, false);
        return binding.getRoot();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
    }

    private void setListeners() {
        binding.btnPay.setOnClickListener(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (context != null)
            context.setTheme(R.style.AppTheme);
    }

    @Override
    public void onClick(View view) {
        UIHelper.hideKeyboard(view, context);
        switch (view.getId()) {
            case R.id.btnPay:
                paymentListener.PaymentClick();
                break;
        }
    }
}