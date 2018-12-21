package com.zhongmei.yunfu.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.zhongmei.yunfu.ui.base.BaseActivity;

public class H5WebViewActivity extends BaseActivity {

    public static void start(Context context, String url) {
        context.startActivity(new Intent(context, H5WebViewActivity.class).putExtra("url", url));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setId(R.id.h5_webview);
        setContentView(frameLayout);
        Bundle bundle = new Bundle();
        bundle.putString("url", getIntent().getStringExtra("url"));
        H5WebViewFragment h5WebViewFragment = new H5WebViewFragment();
        h5WebViewFragment.setArguments(bundle);
        replaceFragment(frameLayout.getId(), h5WebViewFragment, "H5WebView");
    }
}
