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
import com.ingic.caristocrat.databinding.LayoutSubscribeReportBinding;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.SubscribeListener;

public class SubscribeViewDialog extends DialogFragment implements View.OnClickListener {
    MainActivity context;
    LayoutSubscribeReportBinding binding;
    String message = "";

    SubscribeListener subscribeListener;

    public static SubscribeViewDialog newInstance(MainActivity context) {
        SubscribeViewDialog fragment = new SubscribeViewDialog();
        fragment.context = context;
        return fragment;
    }

    public void setSubscribeListener(SubscribeListener subscribeListener) {
        this.subscribeListener = subscribeListener;
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
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_subscribe_report, container, false);
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
        binding.btnSubscribe.setOnClickListener(this);
        binding.singleReport.setOnClickListener(this);
        binding.allReport.setOnClickListener(this);

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
            case R.id.singleReport:
                message = binding.singleReport.getText().toString();
                break;
            case R.id.allReport:
                message = binding.allReport.getText().toString();
                break;
            case R.id.btnSubscribe:
                if (binding.singleReport.isChecked()) {
                    subscribeListener.subscribe(true);
                } else {
                    subscribeListener.subscribe(false);
                }
                break;


        }
    }
}

