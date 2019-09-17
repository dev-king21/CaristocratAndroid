package com.ingic.caristocrat.telr;

import android.app.DialogFragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.databinding.TransactionDialogBinding;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.interfaces.ReportListener;

public class TransactionDialog extends DialogFragment implements View.OnClickListener {

    AppCompatActivity context;
    TransactionDialogBinding binding;
    String title,message;
    CloseDialog closeListener;

    int visibility;
    public static TransactionDialog newInstance(AppCompatActivity context) {
        TransactionDialog fragment = new TransactionDialog();
        fragment.context = context;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.DialogTheme);
        setCancelable(false);
    }

    public void setCloseListener(CloseDialog closeListener) {
        this.closeListener = closeListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.transaction_dialog, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (title != null) {
            binding.title.setText(title);
        }
        if (message != null) {
            binding.message.setText(message);
        }
        if (visibility != -1) {
            binding.tvClose.setVisibility(visibility);
        }
        setListeners();
    }

    private void setListeners() {
        binding.tvClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvClose:
                dismiss();
                closeListener.onClose();
                break;

        }
    }
    public void setTitle(String _title) {
        title = _title;

    }
    public void setMessage(String _message) {
        message = _message;
    }
    public void setVisibility(int _visibility)
    {
        visibility = _visibility;
    }


}
