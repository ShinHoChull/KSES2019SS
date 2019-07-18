package com.m2comm.kses2019s_con.modules.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;

import com.m2comm.kses2019s_con.R;
import com.m2comm.kses2019s_con.modules.common.Globar;


public class CustomImgView extends android.support.v7.widget.AppCompatImageView {

    private int x,y,w,h;
    private Context c;
    private Globar g;

    public CustomImgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context,attrs);
    }

    private void initView(Context c , AttributeSet a) {
        this.g = new Globar(c);
        this.c = c;
        TypedArray t = c.obtainStyledAttributes(a, R.styleable.CustomImgView);

        int color = t.getColor(R.styleable.CustomImgView_imgDefaultColor, 0);
        this.w = this.g.w(t.getInt(R.styleable.CustomImgView_w,0));
        this.h = this.g.h(t.getInt(R.styleable.CustomImgView_h,0));

        int width = g.w(t.getInt(R.styleable.CustomImgView_imgW,0));
        int height = g.h(t.getInt(R.styleable.CustomImgView_imgH,0));
        if ( width > 0 && height > 0) {
            Bitmap b = BitmapFactory.decodeResource(getResources(),t.getResourceId(R.styleable.CustomImgView_img,R.mipmap.ic_launcher));
            b = Bitmap.createScaledBitmap(b,width,height,true);
            this.setImageBitmap(b);
            this.setScaleType(ScaleType.CENTER_INSIDE);
        } else {
            this.setScaleType(ScaleType.CENTER_INSIDE);
            this.setImageResource(t.getResourceId(R.styleable.CustomImgView_img,R.mipmap.ic_launcher));
        }

        if(color != 0) {
            this.setColorFilter(color);
        }

        boolean touch = t.getBoolean(R.styleable.CustomImgView_touch,false);
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
