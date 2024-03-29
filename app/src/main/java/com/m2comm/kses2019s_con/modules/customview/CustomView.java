package com.m2comm.kses2019s_con.modules.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.m2comm.kses2019s_con.R;
import com.m2comm.kses2019s_con.modules.common.Globar;

public class CustomView extends LinearLayout {

    private int x,y,w,h;
    private Globar g;

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context,attrs);
    }

    private void initView(Context c , AttributeSet a) {
        this.g = new Globar(c);
        TypedArray t = c.obtainStyledAttributes(a, R.styleable.CustomView);

        this.w = this.g.w(t.getInt(R.styleable.CustomView_w,0));
        this.h = this.g.h(t.getInt(R.styleable.CustomView_h,0));
        boolean isSchedule = (boolean)t.getBoolean(R.styleable.CustomView_isSchedule,false);
        if ( isSchedule == true ) {
            this.h = this.g.w(t.getInt(R.styleable.CustomView_w,0));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        if(this.w>0)
            widthMeasureSpec=w;
        if(this.h>0)
            heightMeasureSpec=h;
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }
}
