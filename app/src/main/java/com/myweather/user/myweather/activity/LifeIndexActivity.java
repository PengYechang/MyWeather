package com.myweather.user.myweather.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.myweather.user.myweather.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2016/9/5.
 */
public class LifeIndexActivity extends Activity {
    private List<String> datalist = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        LifeIndex_init();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                LifeIndexActivity.this,android.R.layout.simple_list_item_1,datalist);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }

    void LifeIndex_init(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String comf = "生活指数: " + prefs.getString("comf_brf","")+"\n        "+prefs.getString("comf_txt","");
        datalist.add(comf);
        String drsg = "穿衣指数: " + prefs.getString("drsg_brf","")+"\n        "+prefs.getString("drsg_txt","");
        datalist.add(drsg);
        String uv = "紫外线指数: " + prefs.getString("uv_brf","")+"\n        "+prefs.getString("uv_txt","");
        datalist.add(uv);
        String cw = "洗车指数: " + prefs.getString("cw_brf","")+"\n        "+prefs.getString("cw_txt","");
        datalist.add(cw);
        String trav = "旅游指数: " + prefs.getString("trav_brf","")+"\n        "+prefs.getString("trav_txt","");
        datalist.add(trav);
        String flu = "感冒指数: " + prefs.getString("flu_brf","")+"\n        "+prefs.getString("flu_txt","");
        datalist.add(flu);
        String sport = "运动指数: " + prefs.getString("sport_brf","")+"\n        "+prefs.getString("sport_txt","");
        datalist.add(sport);
    }

}
