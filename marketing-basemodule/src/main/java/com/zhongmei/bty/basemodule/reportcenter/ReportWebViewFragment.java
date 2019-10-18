package com.zhongmei.bty.basemodule.reportcenter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.zhongmei.yunfu.ShopInfoManager;
import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.ui.base.BasicFragment;


public class ReportWebViewFragment extends BasicFragment {

    private View rootView;

    private WebView webView;

    private ProgressBar progressBar;

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
        rootView = inflater.inflate(R.layout.report_webview, container, false);
        webView = (WebView) rootView.findViewById(R.id.reportView);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressbar);
        Long brandId = ShopInfoManager.getInstance().getShopInfo().getBrandId();
        Long shopId = ShopInfoManager.getInstance().getShopInfo().getShopId();
        Long createId = Session.getAuthUser().getId();
        String createName = Session.getAuthUser().getName();

        String url = "http://mk.zhongmeiyunfu.com/marketing/internal/report/posReport?brandIdenty=" + brandId + "&shopIdenty=" + shopId + "&creatorId=" + createId + "&creatorName=" + createName;
        webView.loadUrl(url);//加载url


        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);

        return rootView;
    }

        private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
            progressBar.setVisibility(View.VISIBLE);
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onDestroy() {
        unregisterEventBus();
        super.onDestroy();
    }

}
