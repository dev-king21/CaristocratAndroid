package com.ingic.caristocrat.dialogs;

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
import com.ingic.caristocrat.databinding.LayoutContactDealerBinding;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.helpers.Utils;

/**
 */
public class CallConsultantDialog extends DialogFragment implements View.OnClickListener {
    MainActivity context;
    LayoutContactDealerBinding binding;
    private String name;
    private String phone;
    DialogInterface.OnClickListener onClickListener;

    public static CallConsultantDialog newInstance(MainActivity context, DialogInterface.OnClickListener onClickListener) {
        CallConsultantDialog fragment = new CallConsultantDialog();
        fragment.context = context;
        fragment.onClickListener = onClickListener;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.DialogTheme);
        setCancelable(false);
    }

    public void setConsultantNameAndPhone(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_contact_dealer, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvName.setText(name);
        binding.tvPhoneNumber.setText(phone);
        setListeners();


    }

    private void setListeners() {
        binding.tvCall.setOnClickListener(this);
        binding.tvCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        UIHelper.hideKeyboard(view, context);
        switch (view.getId()) {
            case R.id.tvCancel:
                dismiss();
                break;
            case R.id.tvCall:
                dismiss();
                Utils.openCallMaker(phone, context);
                if(onClickListener != null){
                    onClickListener.onClick(null, 0);
                }
                break;
        }
    }
}
