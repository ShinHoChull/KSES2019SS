package com.m2comm.kses2019s_con.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


import com.m2comm.kses2019s_con.R;
import com.m2comm.kses2019s_con.databinding.ActivityGlanceBinding;
import com.m2comm.kses2019s_con.modules.common.ChromeclientPower;
import com.m2comm.kses2019s_con.modules.common.CustomHandler;
import com.m2comm.kses2019s_con.modules.common.Custom_SharedPreferences;
import com.m2comm.kses2019s_con.modules.common.Download;
import com.m2comm.kses2019s_con.modules.common.Download_PDFViewerActivity;
import com.m2comm.kses2019s_con.modules.common.Globar;
import com.m2comm.kses2019s_con.views.PopWebviewActivity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class GlanceViewModel implements View.OnClickListener {

    private ActivityGlanceBinding binding;
    private Context c;
    private Activity activity;
    private Globar g;
    private Custom_SharedPreferences csp;
    private CustomHandler customHandler;
    private ChromeclientPower chromeclient;
    private boolean isSketch = false;
    private int defaultTab = 82;

    private void listenerRegister() {
        this.binding.defaultClickV.setOnClickListener(this);
    }


    public GlanceViewModel(ActivityGlanceBinding binding , Context c , Activity activity ) {
        this.binding = binding;
        this.c = c;
        this.activity = activity;
        this.init();
    }

    private void init() {
        this.g = new Globar(this.c);
        this.csp = new Custom_SharedPreferences(this.c);
        this.g.addFragment_Content_TopV(this.c , false);
        this.g.addFragment_Sub_TopV(this.c,"Program at a Glance");
        this.g.addFragment_BottomV(this.c);
        this.listenerRegister();

        this.chromeclient = new ChromeclientPower(activity,c,this.binding.glanceWv);

        this.binding.glanceWv.setWebViewClient(new WebviewCustomClient());
        this.binding.glanceWv.getSettings().setUseWideViewPort(true);
        this.binding.glanceWv.getSettings().setJavaScriptEnabled(true);
        this.binding.glanceWv.getSettings().setLoadWithOverviewMode(true);
        this.binding.glanceWv.getSettings().setDefaultTextEncodingName("utf-8");
        this.binding.glanceWv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.binding.glanceWv.getSettings().setSupportMultipleWindows(false);
        this.binding.glanceWv.getSettings().setDomStorageEnabled(true);
        this.binding.glanceWv.getSettings().setBuiltInZoomControls(true);
        this.binding.glanceWv.getSettings().setDisplayZoomControls(false);
        this.binding.glanceWv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        this.binding.glanceWv.getSettings().setTextZoom(90);

        this.changeGlance(defaultTab);
    }

    private void sendUrl() {
        this.binding.glanceWv.loadUrl(this.urlSetting(urlSetting("/session/glance.php?code="+this.g.code+"&tab="+this.defaultTab)));
    }

    public String urlSetting(String paramUrl) {
        String deviceid = csp.getValue("deviceid","");
        String url = this.g.baseUrl + paramUrl;

        if (paramUrl.startsWith("http") || paramUrl.startsWith("https")) {
            url = paramUrl;
        }
        if ( paramUrl.contains("?") )url += "&";
        else url += "?";
        url += "deviceid="+deviceid+"&device=android";

        return url;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.default_clickV:
                this.binding.defaultClickV.setVisibility(View.GONE);
                return;
        }
    }

    public void changeGlance(int tab) {
        this.sendUrl();
    }


    private class WebviewCustomClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            String[] urlCut = url.split("/");
            Log.d("NowUrl",url);
            if (url.startsWith(g.mainUrl) == false && url.startsWith(g.contentMainUrl) == false) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                c.startActivity(intent);
                return true;
            } else if ( g.extPDFSearch(urlCut[urlCut.length - 1]) ) {
                Intent content = new Intent(c, Download_PDFViewerActivity.class);
                content.putExtra("url", url);
                content.addFlags(FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(content);
                activity.overridePendingTransition(R.anim.anim_slide_in_bottom_login,0);
                // view.loadUrl(doc);
                return true;
            } else if ( g.extSearch(urlCut[urlCut.length - 1]) ) { //기타 문서 Search
                new Download(url,c,urlCut[urlCut.length - 1]);
                return true;
            } else if ( g.imgExtSearch(urlCut[urlCut.length - 1]) ) { //이미지 Search
                Intent content = new Intent(c, PopWebviewActivity.class);
                content.putExtra("paramUrl", url);
                content.addFlags(FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(content);
                activity.overridePendingTransition(R.anim.anim_slide_in_bottom_login,0);
                return true;
            } else if (url.contains("glance_sub.php")) {
                Intent content = new Intent(c,PopWebviewActivity.class);
                content.putExtra("paramUrl", url);
                content.addFlags(FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(content);
                activity.overridePendingTransition(R.anim.anim_slide_in_bottom_login,0);
                return true;
            } else if (urlCut[urlCut.length -1].equals("back.php")) {
                if (binding.glanceWv.canGoBack()) {
                    binding.glanceWv.goBack();
                } else {
                    activity.finish();
                    activity.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                }
                return true;
            }
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d("onPageStarted",url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            Log.d("onLoadResource",url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d("onPageFinished",url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Toast.makeText(c, "서버와 연결이 끊어졌습니다", Toast.LENGTH_SHORT ).show();
            view.loadUrl("about:blank");
        }
    }
}
