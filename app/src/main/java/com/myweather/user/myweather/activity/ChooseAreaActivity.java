package com.myweather.user.myweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    private Button button;
    private EditText search_text;

    private List<City> cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getBoolean("city_selected",false)){
            Intent intent = new Intent(this,WeatherActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datalist);
        listView.setAdapter(adapter);
        weatherDB = WeatherDB.getInstance(this);
        button = (Button) findViewById(R.id.button);
        search_text = (EditText) findViewById(R.id.search_text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = search_text.getText().toString();
                searchCity(text);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityCode = cityList.get(position).getCityCode();
                Intent intent = new Intent(ChooseAreaActivity.this,WeatherActivity.class);
                intent.putExtra("city_code",cityCode);
                startActivity(intent);
                finish();
            }
        });
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

    private void searchCity(String text){
        cityList = weatherDB.loadCities(text);
        datalist.clear();
        for(City city:cityList){
            datalist.add(city.getCityName());
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(0);
    }

    private void queryCities(){
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
