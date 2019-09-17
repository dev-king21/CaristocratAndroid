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
import android.widget.CompoundButton;
import android.widget.Toast;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.databinding.LayoutReportListingBinding;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.ReportListener;

/**
 */
public class ReportListingDialog extends DialogFragment implements View.OnClickListener {
    MainActivity context;
    LayoutReportListingBinding binding;
    ReportListener reportListener;
    String message = "";


    public static ReportListingDialog newInstance(MainActivity context) {
        ReportListingDialog fragment = new ReportListingDialog();
        fragment.context = context;
        return fragment;
    }


    public void setReportListener(ReportListener reportListener) {
        this.reportListener = reportListener;
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
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_report_listing, container, false);
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
        binding.rbFakeDetails.setOnClickListener(this);
        binding.rbSpam.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
        binding.rbOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    binding.etOther.setVisibility(View.VISIBLE);
                else
                    binding.etOther.setVisibility(View.GONE);
            }
        });
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
            case R.id.rbFakeDetails:
                message = binding.rbFakeDetails.getText().toString();
                break;
            case R.id.rbSpam:
                message = binding.rbSpam.getText().toString();
                break;
            case R.id.btnSubmit:
                if (binding.rbOther.isChecked()) {
                    if (UIHelper.isEmptyOrNull(binding.etOther.getText().toString())) {
                        UIHelper.showToast(context, getString(R.string.err_reason), Toast.LENGTH_SHORT);
                    } else {
                        message = binding.etOther.getText().toString();
                        reportListener.reportListing(message);
                    }
                } else {
                    if (!UIHelper.isEmptyOrNull(message))
                        reportListener.reportListing(message);
                    else
                        UIHelper.showToast(context, getString(R.string.err_report), Toast.LENGTH_SHORT);
                }

                break;


        }
    }


}
