package com.m2comm.kses2019s_con.views;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.m2comm.kses2019s_con.R;
import com.m2comm.kses2019s_con.databinding.ActivityGlanceBinding;
import com.m2comm.kses2019s_con.viewmodels.GlanceViewModel;


public class GlanceActivity extends AppCompatActivity {

    ActivityGlanceBinding binding;
    GlanceViewModel gvm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_glance);
        this.binding.setGlance(this);
        this.gvm = new GlanceViewModel(this.binding , this , this);
    }


}
