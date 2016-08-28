package com.myweather.user.myweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.myweather.user.myweather.R;
import com.myweather.user.myweather.db.WeatherDB;
import com.myweather.user.myweather.model.City;
import com.myweather.user.myweather.util.HttpUtil;
import com.myweather.user.myweather.util.HttpCallbackLister;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2016/8/27.
 */
public class ChooseAreaActivity extends Activity {
    private ProgressDialog progressDialog;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private WeatherDB weatherDB;
    private List<String> datalist = new ArrayList<String>();

    private List<City> cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datalist);
        listView.setAdapter(adapter);
        weatherDB = WeatherDB.getInstance(this);
        queryCities();
    }

    private void queryFromServer(){
        String address = "https://api.heweather.com/x3/citylist?search=allchina&key=84d5a64a3a8e481a91cde07c8bfe5fe1";
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackLister() {
            @Override
            public void onFinish(String response) {
                HttpUtil.handleCityResponse(weatherDB,ChooseAreaActivity.this,response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        queryCities();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void queryCities(){
        int a=1;
        String b="324";
        cityList = weatherDB.loadCities();
        if(cityList.size()>0){
            datalist.clear();
            for(City city:cityList){
                datalist.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
        }else {
            queryFromServer();
        }
    }

    private void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }
}