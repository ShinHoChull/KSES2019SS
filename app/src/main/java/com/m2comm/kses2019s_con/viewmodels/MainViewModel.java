package com.m2comm.kses2019s_con.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.m2comm.kses2019s_con.R;
import com.m2comm.kses2019s_con.databinding.ActivityMainBinding;
import com.m2comm.kses2019s_con.models.MessageDTO;
import com.m2comm.kses2019s_con.modules.adapters.CustomGridViewAdapter;
import com.m2comm.kses2019s_con.modules.common.CustomHandler;
import com.m2comm.kses2019s_con.modules.common.Custom_SharedPreferences;
import com.m2comm.kses2019s_con.modules.common.Globar;
import com.m2comm.kses2019s_con.views.ContentsActivity;
import com.m2comm.kses2019s_con.views.MenuActivity;
import com.m2comm.kses2019s_con.views.QuestionActivity;

import java.util.Arrays;

public class MainViewModel implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ActivityMainBinding binding;
    private Context c;
    private Activity activity;
    private Globar g;
    private Custom_SharedPreferences csp;
    private CustomHandler customHandler;

    private void listenerRegister() {
        this.binding.mainGridview.setOnItemClickListener(this);
        this.binding.mainMenu.setOnClickListener(this);
    }

    public MainViewModel(ActivityMainBinding binding, Context c, Activity activity) {
        this.binding = binding;
        this.c = c;
        this.activity = activity;
        this.init();
    }

    private void init() {

        this.listenerRegister();

        this.csp = new Custom_SharedPreferences(this.c);
        this.customHandler = new CustomHandler(this.c);
        this.g = new Globar(this.c);
        this.g.addFragment_BottomV(this.c);

        LayoutInflater inflater = (LayoutInflater) this.c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CustomGridViewAdapter cga = new CustomGridViewAdapter(this.c, inflater);
        this.binding.mainGridview.setAdapter(cga);

        FirebaseInstanceId.getInstance().getToken();
    }

    public void tokenRegister() {
        if (FirebaseInstanceId.getInstance().getToken() != null) {
            csp.put("token", FirebaseInstanceId.getInstance().getToken());
            new Thread() {
                @Override
                public void run() {
                    super.run();

                    Message msg = customHandler.obtainMessage();
                    try {
                        g.getParser(g.baseUrl + g.urls.get("setToken") + "?deviceid=" + csp.getValue("deviceid", "") +
                                "&device=android&code=" + g.code + "&token=" + FirebaseInstanceId.getInstance().getToken());

                    } catch (Exception e) {
                        msg.obj = new MessageDTO("Failed to fetch data.(Token Error)",
                                e.toString());
                        msg.what = CustomHandler.ALERT_WINDOW_CODE;
                        customHandler.sendMessage(msg);
                    }
                }
            }.start();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        int[] nativeArr = {0,2};

        if ( position == 5 ) {
            Intent question = new Intent(this.c , QuestionActivity.class);
            question.putExtra("sid","0");
            this.c.startActivity(question);
            this.activity.overridePendingTransition(R.anim.anim_slide_in_bottom_login, 0);
            return;
        }

        Intent content = new Intent(this.c, ContentsActivity.class);
        if (Arrays.binarySearch(nativeArr , position) >= 0 ) {
            content.putExtra("content",true);
        }
        content.putExtra("paramUrl",this.g.mainUrls[position]);
        c.startActivity(content);
        activity.overridePendingTransition(R.anim.anim_slide_in_right , R.anim.anim_slide_out_left);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.main_menu) {
            Intent menuCall = new Intent(this.c, MenuActivity.class);
            menuCall.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            activity.startActivity(menuCall);
            activity.overridePendingTransition(R.anim.anim_slide_in_left, 0);
        }
    }
}
