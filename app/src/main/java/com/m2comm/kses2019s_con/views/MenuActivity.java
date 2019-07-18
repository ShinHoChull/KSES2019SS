package com.m2comm.kses2019s_con.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.m2comm.kses2019s_con.R;
import com.m2comm.kses2019s_con.models.BannerDTO;
import com.m2comm.kses2019s_con.models.MessageDTO;
import com.m2comm.kses2019s_con.modules.adapters.MenuGridViewAdapter;
import com.m2comm.kses2019s_con.modules.common.AnimatedExpandableListView;
import com.m2comm.kses2019s_con.modules.common.CustomHandler;
import com.m2comm.kses2019s_con.modules.common.Custom_SharedPreferences;
import com.m2comm.kses2019s_con.modules.common.Globar;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MenuActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private AnimatedExpandableListView listView;
    private ExampleAdapter adapter;
    private ArrayList<GroupItem> header;

    private GridView gridview;
    private Globar g;

    private LinearLayout boothBt;
    private ImageView closeBt, setting, menu_homeImg;
    private TextView menu_homeText;
    private HorizontalScrollView horizontalScrollView;

    private Custom_SharedPreferences csp;
    private CustomHandler customHandler;

    /*H Scroll Val*/
    private LinearLayout linearLayout;
    private int time = 20;

    private ArrayList<BannerDTO> bannerArray;

    Timer timer;
    TimerTask tt;

    private void listenerRegister() {
        this.gridview.setOnItemClickListener(this);
        this.closeBt.setOnClickListener(this);
        this.boothBt.setOnClickListener(this);
        this.menu_homeText.setOnClickListener(this);
        this.menu_homeImg.setOnClickListener(this);
    }

    private void init() {
        this.g = new Globar(this);
        this.csp = new Custom_SharedPreferences(this);
        this.customHandler = new CustomHandler( this );

        this.gridview = findViewById(R.id.menu_grid);
        this.closeBt = findViewById(R.id.menu_closeBt);
        this.boothBt = findViewById(R.id.boothBt);
        this.menu_homeImg = findViewById(R.id.menu_homeImg);
        this.menu_homeText = findViewById(R.id.menu_homeText);


        this.listenerRegister();

        this.header = new ArrayList<>();

        header.add(new GroupItem(R.drawable.icon01, "인사말",
                new ArrayList<ChildItem>(Arrays.asList(new ChildItem("- 인사말"), new ChildItem("- 학술대회 안내")))));

        header.add(new GroupItem(R.drawable.icon02, "프로그램",
                new ArrayList<ChildItem>(Arrays.asList(new ChildItem("- Program at a glance"), new ChildItem("- 8월 18일 (일)")
                        , new ChildItem("- Now")))));

        header.add(new GroupItem(R.drawable.icon03, "오시는길",
                new ArrayList<ChildItem>()));

        header.add(new GroupItem(R.drawable.icon04, "스폰서",
                new ArrayList<ChildItem>()));

        header.add(new GroupItem(R.drawable.icon05, "공지사항",
                new ArrayList<ChildItem>()));

        header.add(new GroupItem(R.drawable.icon06, "Q & A ",
                new ArrayList<ChildItem>()));

        this.linearLayout = new LinearLayout(getApplicationContext());
        this.linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        this.horizontalScrollView = findViewById(R.id.menu_Hscroll);
        this.horizontalScrollView.addView(this.linearLayout);

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.tt = new TimerTask() {
            @Override
            public void run() {
                hScroll();
            }
        };
        this.timer = new Timer();
        this.timer.schedule(tt, 0, time);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.timer.cancel();
    }

    private int xPoint = 0;
    private int maxPoint = 0;

    private void hScroll() {
        Log.d("zzzzz","zzz");
        this.xPoint += 1;

        this.horizontalScrollView.scrollTo(xPoint, 0);
        if (xPoint > this.horizontalScrollView.getScrollX()) {
            xPoint = 0;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        this.init();

        MenuGridViewAdapter mga = new MenuGridViewAdapter(this, getLayoutInflater());
        this.gridview.setAdapter(mga);

        this.adapter = new ExampleAdapter(this);
        this.adapter.setData(this.header);

        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            int lastClickedPosition = -1;
            GroupHolder oldHolder = null;

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, final int groupPosition, long id) {

                GroupHolder holder = (GroupHolder) v.getTag();

                if (header.get(groupPosition).items.size() <= 0) {
                    moveView(groupPosition, 0);
                    return true;
                }
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                    changefoldImg(R.string.plus, holder);
                } else {
                    changefoldImg(R.string.minus, holder);
                    listView.collapseGroupWithAnimation(lastClickedPosition);
                    if (oldHolder != null && groupPosition != lastClickedPosition) {
                        changefoldImg(R.string.plus, oldHolder);
                    }
                    listView.expandGroupWithAnimation(groupPosition);
                }

                lastClickedPosition = groupPosition;
                oldHolder = (GroupHolder) v.getTag();
                return true;
            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                moveView(groupPosition, childPosition);
                return false;
            }
        });


        new Thread() {
            @Override
            public void run() {
                super.run();
                Gson gson = new Gson();
                Message msg = customHandler.obtainMessage();
                try {
                    JsonElement je = g.getParser(g.baseUrl + g.urls.get("banner"));
                    Log.d("banner", "" + je);
                    Type listType = new TypeToken<ArrayList<BannerDTO>>() {
                    }.getType();
                    ArrayList<BannerDTO> bannerArr = gson.fromJson(je, listType);
                    bannerArray = bannerArr;

                    msg.what = CustomHandler.MAIN_PAGER_CODE;
                    customHandler.sendMessage(msg);
                } catch (Exception e) {
                    msg.obj = new MessageDTO("Failed to fetch data.(Banner Error)",
                            e.toString());
                    msg.what = CustomHandler.ALERT_WINDOW_CODE;
                    customHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    public void pagerSet() {
        if ( bannerArray == null ||  bannerArray.size() <= 0 ) {
            this.horizontalScrollView.setVisibility(View.GONE);
            this.timer.cancel();
            return;
        }
        this.horizontalScrollView.setVerticalScrollBarEnabled(false);

        for (int k = 0, l = 8; k < l; k++) {
            for (int i = 0, j = bannerArray.size(); i < j; i++) {
                ImageView im = new ImageView(getApplicationContext());
                final BannerDTO r = bannerArray.get(i);
                Picasso.get().load(this.g.mainUrl+r.getImgUrl()).into(im);
                this.maxPoint += 300;
                im.setLayoutParams(new LinearLayout.LayoutParams(this.g.w(100), this.g.h(60)));
                im.setScaleType(ImageView.ScaleType.FIT_CENTER);
                im.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( r.getLinkUrl().equals("") == false ) {
                            String url = r.getLinkUrl();
                            if ( url.contains("http") == false) url = "http://"+r.getLinkUrl();
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    }
                });

                this.linearLayout.addView(im);
            }
        }
    }

    private void changefoldImg(int text, GroupHolder holder) {
        Typeface fontAwsome = Typeface.createFromAsset(getAssets(), "fa_solid_900.ttf");
        holder.foldImg.setTypeface(fontAwsome);
        holder.foldImg.setText(text);
        holder.foldImg.setTextColor(getResources().getColor(R.color.main_color_black));
    }

    private static class GroupItem {
        int img;
        String title;
        List<ChildItem> items;

        public GroupItem(int img, String title, List<ChildItem> items) {
            this.img = img;
            this.title = title;
            this.items = items;
        }
    }

    private static class ChildItem {
        String title;

        public ChildItem(String title) {
            this.title = title;
        }
    }

    private static class ChildHolder {
        TextView title;
        TextView hint;
    }

    private static class GroupHolder {
        TextView title;
        ImageView headerIconImg;
        TextView foldImg;
    }

    /**
     * Adapter for our list of {@link GroupItem}s.
     */
    private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;
        private List<GroupItem> items;

        public ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.menu_list_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.menu_child_textTitle);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.title.setText(item.title);
            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);

            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.menu_group_item, parent, false);
                holder.headerIconImg = convertView.findViewById(R.id.menuImg);
                holder.foldImg = convertView.findViewById(R.id.foldImg);
                holder.title = convertView.findViewById(R.id.textTitle);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.headerIconImg.setImageResource(item.img);
            holder.title.setText(item.title);

            if (getRealChildrenCount(groupPosition) <= 0) {
                holder.foldImg.setVisibility(View.INVISIBLE);
            } else {
                holder.foldImg.setVisibility(View.VISIBLE);
            }

            if (listView.isGroupExpanded(groupPosition)) {
                changefoldImg(R.string.minus, holder);
            } else {
                changefoldImg(R.string.plus, holder);
            }

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_homeImg:
            case R.id.menu_homeText:
                Intent main = new Intent(this, MainActivity.class);
                main.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(main);
                finish();
                break;

            case R.id.menu_closeBt:
                finish();
                overridePendingTransition(0, R.anim.anim_slide_out_left);
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        switch (position) {
            case 0:
                //MyFav
                Intent content = new Intent(this, ContentsActivity.class);
                content.putExtra("paramUrl", this.g.urls.get("mySchedule"));
                startActivity(content);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                finish();
                break;
            case 1:
                //Event
                Intent eventList = new Intent(this,EventListActivity.class);
                startActivity(eventList);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                finish();
                break;
            case 2:
                //Setting
                Intent setting = new Intent(this, SettingActivity.class);
                startActivity(setting);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                finish();
                break;
        }

    }


    private void moveView(int groupPostion, int childPosition) {

        if (groupPostion == 5) {
            Intent question = new Intent(getApplicationContext(), QuestionActivity.class);
            question.putExtra("sid", "0");
            startActivity(question);
            overridePendingTransition(R.anim.anim_slide_in_bottom_login, 0);
            finish();
            return;
        } else if (groupPostion == 1 && childPosition == 0) {
            Intent glance = new Intent(this, GlanceActivity.class);
            startActivity(glance);
            finish();
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            return;
        }

        Intent content = new Intent(this, ContentsActivity.class);
        if (groupPostion == 0 || groupPostion == 2) {
            content.putExtra("content", true);
            content.putExtra("paramUrl", this.g.menuLink[groupPostion][0]);
        } else {
            content.putExtra("paramUrl", this.g.menuLink[groupPostion][childPosition]);
        }


        content.putExtra("title", this.header.get(groupPostion).title);
        startActivity(content);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        overridePendingTransition(0, R.anim.anim_slide_out_left);
    }
}
