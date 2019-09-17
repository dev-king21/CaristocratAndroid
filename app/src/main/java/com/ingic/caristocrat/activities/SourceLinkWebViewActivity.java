package com.ingic.caristocrat.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.databinding.ActivitySourceLinkWebViewBinding;
import com.ingic.caristocrat.helpers.Titlebar;
import com.ingic.caristocrat.helpers.UIHelper;
import com.ingic.caristocrat.helpers.Utils;

public class SourceLinkWebViewActivity extends BaseActivity implements View.OnClickListener {
    ActivitySourceLinkWebViewBinding binding;
    Intent intent;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_source_link_web_view);

        intent = getIntent();
        if (intent != null) {
            url = intent.getStringExtra("link");
        }

        setTitlebar(binding.titlebar);

//        binding.webView.setBackgroundColor(Color.TRANSPARENT);
        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        binding.webView.clearCache(true);
        binding.webView.loadUrl(url);
        binding.webView.setWebChromeClient(new MyWebViewClient());
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibBackbtn:
                onBackPressed();
                break;
        }
    }

    private void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(this);
        titlebar.showBackButton(this, false).setOnClickListener(this);
        if (url != null) {
            titlebar.showLink(url);
        }
    }

    public boolean internetConnected() {
        UIHelper.hideSoftKeyboard(this);
        if (!Utils.isNetworkAvailable(this)) {
            UIHelper.showToast(this, getResources().getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG);
            return false;
        } else {
            return true;
        }
    }

    private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress >= 100) {
                binding.progressBarContainer.setVisibility(View.GONE);
//                pbLoadProgress.setVisibility(View.INVISIBLE);
//                wvSurveyView.setVisibility(View.VISIBLE);
            } else {
            }
            super.onProgressChanged(view, newProgress);
        }
    }
}
