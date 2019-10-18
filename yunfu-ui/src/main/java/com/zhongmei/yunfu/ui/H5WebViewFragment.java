package com.zhongmei.yunfu.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhongmei.yunfu.ui.base.BasicFragment;


public class H5WebViewFragment extends BasicFragment {

    protected View rootView;
    private WebView webView;
    private ProgressBar progressBar;
    private TextView txTitle;
    private String webTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return rootView = inflater.inflate(R.layout.h5_webview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView = (WebView) findViewById(R.id.reportView);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);        txTitle = (TextView) findViewById(R.id.actionbar_title);
        findViewById(R.id.actionbar_back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        String url = null;
        Bundle arguments = getArguments();
        if (arguments != null) {
            url = arguments.getString("url");
            webTitle = arguments.getString("title");
        }

        txTitle.setText(webTitle);
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);        }

        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

                webSettings.setJavaScriptEnabled(true);
                webSettings.setSupportZoom(true);
                webSettings.setBuiltInZoomControls(false);
                webSettings.setUseWideViewPort(true);
                webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
    }

        private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {            progressBar.setVisibility(View.VISIBLE);
        }


    };

        private WebChromeClient webChromeClient = new WebChromeClient() {
                @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
            localBuilder.setMessage(message).setPositiveButton("确定", null);
            localBuilder.setCancelable(false);
            localBuilder.create().show();

                                                            result.confirm();
            return true;
        }

                @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (webTitle == null) {
                txTitle.setText(title);
            }
        }

                @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        webView.destroy();
        webView = null;
    }


    @Override
    public void onDestroy() {
        unregisterEventBus();
        super.onDestroy();
    }
}
