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
 * Created by User on 2016/9/1.
 */
public class MoreinfoActivity extends Activity{

    private List<String> datalist = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        init_data();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            MoreinfoActivity.this,android.R.layout.simple_list_item_1,datalist);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }

    private  void init_data(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String aqi = "空气质量指数: "+prefs.getString("aqi","");
        datalist.add(aqi);
        String pm25 = "PM2.5 1小时平均值(ug/m³): "+prefs.getString("pm25","");
        datalist.add(pm25);
        String pm10 = "PM10 1小时平均值(ug/m³): "+prefs.getString("pm10","");
        datalist.add(pm10);
        String so2 = "二氧化硫1小时平均值(ug/m³): "+prefs.getString("so2","");
        datalist.add(so2);
        String no2 = "二氧化氮1小时平均值(ug/m³): "+prefs.getString("no2","");
        datalist.add(no2);
        String co = "一氧化碳1小时平均值(ug/m³): "+prefs.getString("co","");
        datalist.add(co);
        String o3 = "臭氧1小时平均值(ug/m³): "+prefs.getString("o3","");
        datalist.add(o3);
        String qlty = "空气质量类别: "+prefs.getString("qlty","");
        datalist.add(qlty);
    }
}
