package com.m2comm.kses2019s_con.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.m2comm.kses2019s_con.R;
import com.m2comm.kses2019s_con.modules.common.Alarm;
import com.m2comm.kses2019s_con.modules.common.ChromeclientPower;
import com.m2comm.kses2019s_con.modules.common.Custom_SharedPreferences;
import com.m2comm.kses2019s_con.modules.common.Download;
import com.m2comm.kses2019s_con.modules.common.Download_PDFViewerActivity;
import com.m2comm.kses2019s_con.modules.common.Globar;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ContentsActivity extends AppCompatActivity  {

    private ChromeclientPower chromeclient;
    private boolean isPdf = false;
    private Custom_SharedPreferences csp;
    public Globar g;
    private Context longCLickContext = this;

    private String paramUrl;
    private WebView wv;
    private FrameLayout fr;
    public boolean isContent;

    public boolean isBottom = false;

    //Content
    public void viewReset() {

        this.wv.setLongClickable(true);
        this.wv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("contentLongClick","long");
                WebView.HitTestResult hitTestResult = wv.getHitTestResult();
                switch (hitTestResult.getType()) {
                    case WebView.HitTestResult.IMAGE_TYPE:
                        final String typeUrl = hitTestResult.getExtra();
                        String[] temps = typeUrl.split("/");
                        final String fileNames = temps[temps.length - 1];
                        new MaterialDialog.Builder(longCLickContext).title("Image")
                                .content("Do you want to download the image?")
                                .positiveText("OK").negativeText("Cancel").theme(Theme.LIGHT).onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                new Download(typeUrl,longCLickContext,fileNames);
                            }
                        }).show();
                        break;
                }
                return false;
            }
        });
    }

    public String urlSetting(String paramUrl) {
        String deviceid = csp.getValue("deviceid","");
        String url = this.g.baseUrl + paramUrl;
        if ( this.isContent == true ) {
            url = this.g.contentUrl + paramUrl;
        }
        if (paramUrl.startsWith("http") || paramUrl.startsWith("https")) {
            url = paramUrl;
        }
        if ( paramUrl.contains("?") )url += "&";
        else url += "?";
        url += "deviceid="+deviceid+"&device=android&id=android&login="+csp.getValue("isLogin",false);

        return url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);
        getWindow().setWindowAnimations(0);

        Intent intent = new Intent(this.getIntent());
        this.paramUrl = intent.getStringExtra("paramUrl");
        this.isBottom = intent.getBooleanExtra("isBottom",false);
        this.isContent = intent.getBooleanExtra("content",false);
        this.wv = findViewById(R.id.content_Webview);

        this.init();
        this.viewReset();

    }

    private void init() {
        this.csp = new Custom_SharedPreferences(this);
        this.g = new Globar(this);
        this.g.addFragment_Content_TopV(this , false);

        this.fr = findViewById(R.id.fragment_Bottom);
        this.g.addFragment_BottomV(this);

        this.chromeclient = new ChromeclientPower(this,this,this.wv);
        this.wv.setWebViewClient(new WebviewCustomClient());
        this.wv.setWebChromeClient(this.chromeclient);
        this.wv.getSettings().setUseWideViewPort(true);
        this.wv.getSettings().setJavaScriptEnabled(true);
        this.wv.getSettings().setLoadWithOverviewMode(true);
        this.wv.getSettings().setDefaultTextEncodingName("utf-8");
        this.wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.wv.getSettings().setSupportMultipleWindows(false);
        this.wv.getSettings().setDomStorageEnabled(true);
        this.wv.getSettings().setBuiltInZoomControls(true);
        this.wv.getSettings().setDisplayZoomControls(false);
        this.wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        this.wv.loadUrl(this.urlSetting(this.paramUrl));
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
                startActivity(intent);
                return true;
            } else if ( g.extPDFSearch(urlCut[urlCut.length - 1]) ) {
                isPdf = true;
                Intent content = new Intent(getApplicationContext(), Download_PDFViewerActivity.class);
                content.putExtra("url", url);
                content.addFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(content);
                overridePendingTransition(R.anim.anim_slide_in_bottom_login,0);
                // view.loadUrl(doc);
                return true;
            } else if ( g.extSearch(urlCut[urlCut.length - 1]) ) { //기타 문서 Search
                new Download(url,getApplicationContext(),urlCut[urlCut.length - 1]);
                return true;
            } else if ( g.imgExtSearch(urlCut[urlCut.length - 1]) ) { //이미지 Search
                Intent content = new Intent(getApplicationContext(), PopWebviewActivity.class);
                content.putExtra("paramUrl", url);
                content.addFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(content);
                overridePendingTransition(R.anim.anim_slide_in_bottom_login,0);
                return true;
            } else if (url.contains("add_alarm.php")) {
                String[] temp= url.split("&");
                final String sid = temp[0].split("=")[1];
                final String tab = temp[1].split("=")[1];
                String time2 = temp[2].split("=")[1];
                final String time = time2.split("-")[0];
                final String subject = temp[3].split("=")[1];

                Alarm alarm = new Alarm(ContentsActivity.this);
                int day = 18;

                if(tab.equals("82")) {
                    day=18;
                }

                try {
                    Log.d("subject =",URLDecoder.decode(subject,"UTF-8") + "");
                    if(Integer.parseInt(time.split(":")[1])<10) {
                        alarm.InsertAlarm(2019, 8-1, day, Integer.parseInt(time.split(":")[0])-1, Integer.parseInt(time.split(":")[1])+50, Integer.parseInt(sid), URLDecoder.decode(subject,"UTF-8") + " 10분 후에 시작합니다.");
                    } else {
                        alarm.InsertAlarm(2019, 8-1, day, Integer.parseInt(time.split(":")[0]), Integer.parseInt(time.split(":")[1])-10, Integer.parseInt(sid), URLDecoder.decode(subject,"UTF-8") + " 10분 후에 시작합니다.");
                    }
                    Toast.makeText(ContentsActivity.this,"Add Alram complete.",Toast.LENGTH_SHORT).show();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return true
                        ;
            } else if(url.contains("remove_alarm.php")){
                String[] temp= url.split("&");
                final String sid = temp[0].split("=")[1];
                Alarm alarm = new Alarm(ContentsActivity.this);
                alarm.DelAlarm(Integer.parseInt(sid));
                Toast.makeText(ContentsActivity.this,"Remove Alram complete.",Toast.LENGTH_SHORT).show();
                return true;
            } else if ( url.contains("glance.php")) {
//                Intent setting = new Intent(getApplicationContext(), GlanceActivity.class);
//                startActivity(setting);
                return true;
            } else if (url.contains("app_question.php")){

                String[] temp= url.split("sid=");
                Log.d("questionSid",temp[1]+"");
                Intent question = new Intent(getApplicationContext() , QuestionActivity.class);
                question.putExtra("sid",temp[1]+"");
                startActivity(question);
                overridePendingTransition(R.anim.anim_slide_in_bottom_login, 0);
                return true;

            }  else if (urlCut[urlCut.length -1].equals("back.php")) {
                if (wv.canGoBack()) {
                    wv.goBack();
                } else {
                    finish();
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
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
            Toast.makeText(getApplicationContext(), "서버와 연결이 끊어졌습니다", Toast.LENGTH_SHORT ).show();
            view.loadUrl("about:blank");
        }


    }


    //Intent 초기화
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        this.wv.onResume();
//        if (this.isPdf == false) {
//            Intent intent = new Intent(this.getIntent());
//            //this.tv.setText(intent.getStringExtra("title"));
//            this.choiceNum = intent.getStringExtra("num");
//            if (this.choiceNum == null || this.choiceNum.equals("")) {
//              //  this.titlebarOption.setVisibility(View.GONE);
//            }
//
//            this.paramUrl = intent.getStringExtra("paramUrl");
//
//            //this.wv.loadUrl(this.urlSetting(this.paramUrl));
//        }
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        this.wv.onPause();
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (this.wv.canGoBack()) {
//            this.wv.goBack();
//        } else {
//            finish();
//            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
//        }
//    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void api21Code() {

        //21레벨 api에서 꼭필요한 코드.
        // https -> http 로 전송할때 cancle되지 않도록..
        wv.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(this.wv, true);
    }

    public void onBackPressed() {
        //super.onBackPressed();
        if ( this.wv.canGoBack() ) {
            this.wv.goBack();
        } else {
            finish();
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        }
    }
}
