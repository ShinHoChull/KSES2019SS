package com.m2comm.kses2019s_con.modules.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.m2comm.kses2019s_con.R;
import com.m2comm.kses2019s_con.modules.common.Globar;


public class CustomGridViewAdapter extends BaseAdapter {

    private Integer[] mainIcon = {
            R.drawable.icon01,
            R.drawable.icon02,
            R.drawable.icon03,
            R.drawable.icon04,
            R.drawable.icon05,
            R.drawable.icon06
    };

    private String[] names = {
            "인사말",
            "프로그램",
            "오시는길",
            "스폰서",
            "공지사항",
            "Q&A",
    };

    private Globar g;
    private Context c;
    private LayoutInflater inflater;


    public CustomGridViewAdapter(Context c, LayoutInflater inflater) {
        this.c = c;
        this.g = new Globar(c);
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return this.mainIcon.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if ( convertView == null ) {

            convertView = this.inflater.inflate(R.layout.main_gridview_child,parent,false);
            ImageView imageView = (ImageView)convertView.findViewById(R.id.main_gridview_img);
            TextView textView = (TextView)convertView.findViewById(R.id.main_gridview_text);
            ViewGroup.LayoutParams param = convertView.getLayoutParams();

            if(param == null) {
                param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            param.width = this.g.w(105);
            param.height = this.g.h(110);
            convertView.setLayoutParams(param);

            textView.setText(this.names[position]);
            imageView.setImageResource(this.mainIcon[position]);
            //convertView.setBackgroundResource(R.color.main_color_white);
        }

        return convertView;
    }
}
