package com.m2comm.kses2019s_con.modules.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.m2comm.kses2019s_con.R;
import com.m2comm.kses2019s_con.modules.common.Custom_SharedPreferences;
import com.m2comm.kses2019s_con.modules.common.Globar;


public class MenuGridViewAdapter extends BaseAdapter {

    private Integer[] mainIcon = {
            R.drawable.menu_member1,
            R.drawable.menu_member2,
            R.drawable.menu_member3,
    };

    private String[] names = {
            "즐겨찾기",
            "행사리스트",
            "설정",
    };

    private Globar g;
    private Context c;
    private LayoutInflater inflater;
    private Custom_SharedPreferences csp;


    public MenuGridViewAdapter(Context c, LayoutInflater inflater) {
        this.c = c;
        this.g = new Globar(c);
        this.inflater = inflater;
        this.csp = new Custom_SharedPreferences(c);
    }

    @Override
    public int getCount() {
        return this.names.length;
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

        if (convertView == null) {

            convertView = this.inflater.inflate(R.layout.menu_gridview_child, parent, false);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.menu_gridview_img);
            TextView textView = (TextView) convertView.findViewById(R.id.menu_gridview_text);

            ViewGroup.LayoutParams param = convertView.getLayoutParams();
            if (param == null) {
                param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            param.width = this.g.w(120);
            param.height = this.g.h(100);
            convertView.setLayoutParams(param);


            textView.setText(this.names[position]);
            imageView.setImageResource(this.mainIcon[position]);

            //convertView.setBackgroundResource(R.color.main_color_darkblue);
        }

        return convertView;
    }
}
