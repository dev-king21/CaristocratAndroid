package com.ingic.caristocrat.dialogs;

import android.app.DialogFragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.databinding.LayoutTradeRequestApprovalBinding;
import com.ingic.caristocrat.helpers.UIHelper;

/**
 */
public class TradeInRequestApprovalDialog extends DialogFragment implements View.OnClickListener {
    MainActivity context;
    LayoutTradeRequestApprovalBinding binding;
    private boolean edit;

    public static TradeInRequestApprovalDialog newInstance(MainActivity context) {
        TradeInRequestApprovalDialog fragment = new TradeInRequestApprovalDialog();
        fragment.context = context;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.DialogTheme);
        setCancelable(false);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_trade_request_approval, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (edit) {
            binding.tvCarSubmitMessage.setText(context.getResources().getString(R.string.car_updated_successful));
        }
        setListeners();
    }

    private void setListeners() {
        binding.tvClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        UIHelper.hideKeyboard(view, context);
        switch (view.getId()) {
            case R.id.tvClose:
                context.onBackPressed();
                dismiss();
                break;

        }
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }
}
