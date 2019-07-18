package com.m2comm.kses2019s_con.modules.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.m2comm.kses2019s_con.R;

public class CustomTextView extends AppCompatTextView {

    private int x,y,w,h;
    private double default_W = 360;
    private double default_H = 640;
    public int deviceW = 0;
    public int deviceH = 0;

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context,attrs);
    }

    private void initView(Context c , AttributeSet a) {
        this.wh_setting(c);
        TypedArray t = c.obtainStyledAttributes(a, R.styleable.CustomTextView);

        this.w = this.w(t.getInt(R.styleable.CustomTextView_w,0));
        this.h = this.h(t.getInt(R.styleable.CustomTextView_h,0));
        int fontSize =(int)this.setTextSize(t.getInt(R.styleable.CustomTextView_fontSize,14));
        boolean isPhoto = (boolean) t.getBoolean(R.styleable.CustomTextView_isPhoto,false);//this.g.setTextSize();
        String fontStyle = t.getString(R.styleable.CustomTextView_fontType) == null ? "":t.getString(R.styleable.CustomTextView_fontType);

        String font = "NotoSansCJKkr-Medium.otf";
        if ( fontStyle.equals("regular") ) {
            font = "NotoSansCJKkr-Regular.otf";
        } else if ( fontStyle.equals("bold") ) {
            font = "NotoSansCJKkr-Bold.otf";
        }

        if (!fontStyle.equals("")) {
            Typeface fontAwsome = Typeface.createFromAsset(c.getAssets(), font);
            this.setTypeface(fontAwsome);
        }

        if ( isPhoto == true ) {
            this.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
        } else {
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX ,fontSize * 3 - 5);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        if(this.w>0) {
            widthMeasureSpec = this.w;
            getLayoutParams().width = this.w;
        }

        if(this.h > 0) {
            heightMeasureSpec = this.h;
            getLayoutParams().height = this.h;
        }
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }
    public double setTextSize(int fontize) {
        double size_formatter = (double) fontize / this.deviceW;
        return (int) this.deviceW * size_formatter;
    }

    private void wh_setting(Context c) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(displayMetrics);

        int statusBarHeight = 0;

        int resId = c.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            statusBarHeight = c.getResources().getDimensionPixelSize(resId);
        }

        WindowManager windowManager = (WindowManager)c.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if ( Build.MODEL.equals("SM-G973N") ) {
            statusBarHeight = 0;
        }
        this.deviceW = size.x;
        this.deviceH = size.y - statusBarHeight;
    }


    public int x(int x) {
        return (int) (this.deviceW * (x / default_W));
    }

    public int y(int x) {
        return (int) (this.deviceH * (x / default_H));
    }

    public int w(int x) {
        return (int) (this.deviceW * (x / default_W));
    }

    public int h(int x) {
        return (int) (this.deviceH * (x / default_H));
    }
}
