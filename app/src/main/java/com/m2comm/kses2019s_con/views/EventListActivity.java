package com.m2comm.kses2019s_con.views;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;

import com.m2comm.kses2019s_con.R;
import com.m2comm.kses2019s_con.databinding.ActivityEventListBinding;
import com.m2comm.kses2019s_con.modules.adapters.List_SpinnerAdapter;
import com.m2comm.kses2019s_con.viewmodels.EventListViewModel;


public class EventListActivity extends AppCompatActivity {

    ActivityEventListBinding binding;
    private EventListViewModel evm;

    Spinner spinner1;
    List_SpinnerAdapter adapterSpinner1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        this.binding = DataBindingUtil.setContentView(this,R.layout.activity_event_list);
        this.binding.setEvent(this);
        this.evm = new EventListViewModel(this.binding , this , this);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v == null) return super.dispatchTouchEvent(event);
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(event);
    }
}
