package com.m2comm.kses2019s_con.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;


import com.m2comm.kses2019s_con.R;
import com.m2comm.kses2019s_con.databinding.ActivityEventListBinding;
import com.m2comm.kses2019s_con.modules.adapters.List_SpinnerAdapter;
import com.m2comm.kses2019s_con.modules.common.Globar;
import com.m2comm.kses2019s_con.views.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class EventListViewModel implements AdapterView.OnItemSelectedListener , View.OnClickListener {

    ActivityEventListBinding binding;
    private Context c;
    private Activity activity;
    private Globar g;
    private List_SpinnerAdapter adapterSpinner1;
    private List<String> data;
    private ArrayList<String> titleComparard;

    private void listenerRegister() {
        this.binding.listSpinner.setOnItemSelectedListener(this);
        this.binding.eventEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //바뀐 시점 전에
                //Log.d("beforeTextChanged", s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //바뀜 시점
                //Log.d("onTextChanged", s.toString());
                for ( int i = 0 , j = titleComparard.size() ; i < j ; i ++ ) {
                    if (titleComparard.get(i).toUpperCase().contains(s.toString().toUpperCase())) {
                        binding.event2019.setVisibility(View.VISIBLE);
                    } else {
                        binding.event2019.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //바뀐 이후
                //Log.d("afterTextChanged", s.toString());
            }
        });

        this.binding.eventClose.setOnClickListener(this);
        this.binding.event2019.setOnClickListener(this);
    }

    public EventListViewModel(ActivityEventListBinding binding, Context c, Activity activity) {
        this.binding = binding;
        this.c = c;
        this.activity = activity;
        this.init();
    }

    private void init() {
        this.listenerRegister();

        data = new ArrayList<>();
        data.add("전체   ▼");
        data.add("2019");
        //Adapter
        adapterSpinner1 = new List_SpinnerAdapter(this.c, data);
        //Adapter 적용
        this.binding.listSpinner.setAdapter(adapterSpinner1);
        titleComparard = new ArrayList<>();
        titleComparard.add("2019 주관절학회 대한정형외과의사회 합동 연수강좌 ");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            //전체
            binding.event2019.setVisibility(View.VISIBLE);
        } else if (position == 1) {
            //2019
            binding.event2019.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.event_close :
                this.activity.finish();
                activity.overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                break;
            case R.id.event_2019:
                Intent content = new Intent(c, MainActivity.class);
                content.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                activity.startActivity(content);
                break;
        }
    }
}
