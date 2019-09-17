package com.ingic.caristocrat.dialogs;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ingic.caristocrat.R;
import com.ingic.caristocrat.activities.MainActivity;
import com.ingic.caristocrat.databinding.LayoutPdfDialogBinding;
import com.ingic.caristocrat.fragments.BaseFragment;
import com.ingic.caristocrat.helpers.Titlebar;

public class PDFViewDialog extends BaseFragment {
    String request_url;
    MainActivity mainActivityContext;
    LayoutPdfDialogBinding binding;

    public static PDFViewDialog newInstance(MainActivity context, String request_url) {
        PDFViewDialog fragment = new PDFViewDialog();
        fragment.mainActivityContext = context;
        fragment.request_url = request_url;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_pdf_dialog, container, false);
        return binding.getRoot();
    }

    @Override
    public void setTitlebar(Titlebar titlebar) {
        titlebar.resetTitlebar(mainActivityContext);
        titlebar.setTitle("Report");
        titlebar.showBackButton(mainActivityContext,false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeWebView();
    }

    private void initializeWebView() {
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" +request_url);
        binding.webView.setWebViewClient(webViewClient);
        binding.webView.getSettings().setLoadWithOverviewMode(true);
        binding.webView.getSettings().setUseWideViewPort(true);
        binding.webView.getSettings().setDomStorageEnabled(true);
    }

    WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            mainActivityContext.showLoader();


        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            binding.webView.loadUrl("javascript:(function() { " +
                    "document.querySelector('[role=\"toolbar\"]').remove();})()");
            mainActivityContext.hideLoader();


        }
    };
}
