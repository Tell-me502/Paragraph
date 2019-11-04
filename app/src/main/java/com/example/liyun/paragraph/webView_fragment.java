package com.example.liyun.paragraph;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class webView_fragment extends Fragment{
    private WebView mWebView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.webview_fragment,container,false);
        mWebView=view.findViewById(R.id.web_view);
        mWebView.loadUrl("https://www.baidu.com/");
        return view;
    }
}
