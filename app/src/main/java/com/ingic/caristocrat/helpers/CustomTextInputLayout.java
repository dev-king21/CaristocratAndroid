package com.ingic.caristocrat.helpers;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ingic.caristocrat.R;


/**
 */

public class CustomTextInputLayout extends LinearLayout {
    TextView tvError;
    Context ctx;

    public CustomTextInputLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public CustomTextInputLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        this.setOrientation(VERTICAL);
        ctx = context;
    }

    public void setErrorEnabled() {
        if(this.tvError==null) {
            this.tvError = new AppCompatTextView(ctx);
            this.removeView(this.tvError);
            this.tvError.setTextColor(ContextCompat.getColor(ctx, R.color.colorError));
            this.tvError.setTextSize(10.0f);
            this.tvError.setVisibility(INVISIBLE);
            this.tvError.setPadding(14, -1, 0, 0);
            this.addView(this.tvError);
        }
    }

    public void setError(@Nullable final CharSequence error) {
        this.tvError.setText(error);
        errorEnable(true);
    }

    public void setErrorMessage(@Nullable final CharSequence error) {
        this.tvError.setText(error);
    }

    public void errorEnable(boolean isErrorShow) {
        if (isErrorShow) {
            tvError.setVisibility(VISIBLE);
        } else {
            if (tvError != null) {
                tvError.setVisibility(GONE);
                tvError.setText("");
            }
        }
    }

    public boolean isErrorEnable() {
        if (tvError.getVisibility() == VISIBLE) {
            return true;
        } else {
            return false;
        }
    }

}
