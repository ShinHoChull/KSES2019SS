package com.m2comm.kses2019s_con.viewmodels;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.m2comm.kses2019s_con.R;
import com.m2comm.kses2019s_con.databinding.FragmentQuestionBinding;
import com.m2comm.kses2019s_con.models.QuestionModel;
import com.m2comm.kses2019s_con.models.TitleDTO;
import com.m2comm.kses2019s_con.modules.common.Custom_SharedPreferences;
import com.m2comm.kses2019s_con.modules.common.Globar;


public class QuestionViewModel implements View.OnClickListener{

    private FragmentQuestionBinding activity;
    private Custom_SharedPreferences csp;
    private TitleDTO titleDTO;
    private Context c;
    private Globar g;
    private QuestionModel model;


    public QuestionViewModel(FragmentQuestionBinding activity , Context c , TitleDTO titleDTO) {
        this.activity = activity;
        this.c = c;
        this.titleDTO = titleDTO;
        this.init();
    }

    private void init () {
        this.g = new Globar(this.c);
        this.csp = new Custom_SharedPreferences(this.c);
        this.model = new QuestionModel();
        this.activity.qaSubText.setText(this.titleDTO.getSubTitle());
        this.activity.questionSendBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.question_sendBt:

                if (this.activity.questionEdittext.getText().toString().equals("")) {
                    Toast.makeText(this.c, this.model.getQuestionMessage(), Toast.LENGTH_SHORT).show();
                } else {

                    AndroidNetworking.get(titleDTO.getUrl())
                            .addQueryParameter("question", this.activity.questionEdittext.getText().toString())
                            .addQueryParameter("code", this.g.code)
                            .addQueryParameter("id", csp.getValue("deviceid",""))
                            .addQueryParameter("office",csp.getValue("office",""))
                            .addQueryParameter("mobile","mobile")
                            .addQueryParameter("name",csp.getValue("name",""))
                            .setPriority(Priority.LOW)
                            .build().getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("Y")) {
                                g.baseAlertMessage("알림", model.getSuccessAlertMessage());
                                activity.questionEdittext.setText("");
                            } else {
                                g.baseAlertMessage("알림", model.getFailAlertMessage());
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            g.baseAlertMessage("알림", model.getFailAlertMessage());
                        }
                    });
                }
                break;
        }
    }
}
