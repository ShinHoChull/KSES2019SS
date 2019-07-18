package com.m2comm.kses2019s_con.views;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.m2comm.kses2019s_con.R;
import com.m2comm.kses2019s_con.databinding.ActivitySettingBinding;
import com.m2comm.kses2019s_con.modules.common.Globar;
import com.m2comm.kses2019s_con.viewmodels.SettingViewModel;


public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;
    SettingViewModel svm;
    private Globar g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        this.binding.setMain(this);
        this.svm = new SettingViewModel(this.binding , this , this);
    }

    public void settingChange(String v) {
        this.svm.settingChange(v);
    }


}
