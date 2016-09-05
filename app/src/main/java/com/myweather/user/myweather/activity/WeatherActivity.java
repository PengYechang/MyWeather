package com.myweather.user.myweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.myweather.user.myweather.R;
import com.myweather.user.myweather.service.AutoUpdateService;
import com.myweather.user.myweather.util.HttpCallbackLister;
import com.myweather.user.myweather.util.HttpUtil;

/**
 * Created by User on 2016/8/31.
 */
public class WeatherActivity extends Activity implements View.OnClickListener{
    private RelativeLayout weather_info_layout;
    private LinearLayout weather_info_layout2;

    private TextView  city_name;
    private TextView now_tem;
    private TextView temp1;
    private TextView temp2;
    private TextView current_date;
    private ImageView cond_pic;
    private TextView cond_text;
    private TextView fl;
    private TextView hum;
    private TextView qlty;
    private TextView dir;
    private TextView sc;

    private ImageView cond_pic1;
    private TextView cond_text1;
    private TextView temp1_1;
    private TextView temp2_1;
    private ImageView cond_pic2;
    private TextView cond_text2;
    private TextView temp1_2;
    private TextView temp2_2;

    private Bitmap mDownloadImage = null;
    private downloadImageTask task;
    private downloadImageTask task1;
    private downloadImageTask task2;

    private Button switchCity;
    private Button more;
    private Button refreshweather;
    private Button lifeIndex;

    private String cityCode;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.switch_city:
                Intent intent = new Intent(this,ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity",true);
                startActivity(intent);
                finish();
                break;
            case R.id.more:
                Intent intent1 =new Intent(this,MoreinfoActivity.class);
                startActivity(intent1);
                break;
            case R.id.refresh_weather:
                city_name .setText("同步中...");
                if(!TextUtils.isEmpty(cityCode)){
                    queryWeatherInfo(cityCode);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                    city_name.setText(prefs.getString("city_name",""));
                }
                break;
            case R.id.Life_index:
                Intent intent2 = new Intent(this,LifeIndexActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);

        switchCity = (Button) findViewById(R.id.switch_city);
        switchCity.setOnClickListener(this);
        more = (Button) findViewById(R.id.more);
        more.setOnClickListener(this);
        refreshweather = (Button) findViewById(R.id.refresh_weather);
        refreshweather.setOnClickListener(this);
        lifeIndex = (Button) findViewById(R.id.Life_index);
        lifeIndex.setOnClickListener(this);

        weather_info_layout = (RelativeLayout) findViewById(R.id.weather_info);
        weather_info_layout2 = (LinearLayout) findViewById(R.id.weather_info1);
        city_name = (TextView) findViewById(R.id.city_name);
        now_tem = (TextView) findViewById(R.id.now_tem);
        temp1 = (TextView) findViewById(R.id.temp1);
        temp2 = (TextView) findViewById(R.id.temp2);
        current_date = (TextView) findViewById(R.id.current_date);
        cond_pic = (ImageView) findViewById(R.id.cond_pic);
        cond_text = (TextView) findViewById(R.id.cond_text);
        fl = (TextView) findViewById(R.id.fl);
        hum = (TextView) findViewById(R.id.hum);
        qlty = (TextView) findViewById(R.id.qlty);
        dir = (TextView) findViewById(R.id.dir);
        sc = (TextView) findViewById(R.id.sc);
        cond_pic1 = (ImageView) findViewById(R.id.cond_pic1);
        cond_text1 = (TextView) findViewById(R.id.cond_text1);
        temp1_1 = (TextView) findViewById(R.id.temp1_1);
        temp2_1 = (TextView) findViewById(R.id.temp2_1);
        cond_pic2 = (ImageView) findViewById(R.id.cond_pic2);
        cond_text2 = (TextView) findViewById(R.id.cond_text2);
        temp1_2 = (TextView) findViewById(R.id.temp1_2);
        temp2_2 = (TextView) findViewById(R.id.temp2_2);

        cityCode = getIntent().getStringExtra("city_code");
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("cityCode",cityCode);
        editor.commit();
        task = new downloadImageTask();
        task1 = new downloadImageTask();
        task2 = new downloadImageTask();

        if(!TextUtils.isEmpty(cityCode)){
            city_name.setText("同步中...");
            city_name.setVisibility(View.VISIBLE);
            weather_info_layout.setVisibility(View.INVISIBLE);
            weather_info_layout2.setVisibility(View.INVISIBLE);
            queryWeatherInfo(cityCode);
        }else{
            showWeather();
        }
    }

    private void queryWeatherInfo(String cityCode){
        String address = "https://api.heweather.com/x3/weather?cityid="+cityCode+"&key=84d5a64a3a8e481a91cde07c8bfe5fe1";
        HttpUtil.sendHttpRequest(address, new HttpCallbackLister() {
            @Override
            public void onFinish(String response) {
                HttpUtil.handleWeatherResponse(WeatherActivity.this,response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                city_name.setText("同步失败");
            }
        });
    }

    private void showWeather(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        city_name.setText(prefs.getString("city_name",""));
        now_tem.setText(prefs.getString("tmp","")+"℃");
        temp1.setText(prefs.getString("tmp_min",""));
        temp2.setText(prefs.getString("tmp_max","")+"°");
        current_date.setText(prefs.getString("date",""));
        cond_text.setText(prefs.getString("cond_txt",""));
        fl.setText(prefs.getString("fl","")+"°");
        hum.setText(prefs.getString("hum","")+"%");
        qlty.setText(prefs.getString("qlty",""));
        dir.setText(prefs.getString("dir",""));
        sc.setText(prefs.getString("sc",""));

        cond_text1.setText(prefs.getString("cond1_text",""));
        temp1_1.setText(prefs.getString("tmp1_min",""));
        temp2_1.setText(prefs.getString("tmp1_max","")+"°");
        cond_text2.setText(prefs.getString("cond2_text",""));
        temp1_2.setText(prefs.getString("tmp2_min",""));
        temp2_2.setText(prefs.getString("tmp2_max","")+"°");

        String cond_code = prefs.getString("cond_code","");
        String cond1_code = prefs.getString("cond1_code","");
        String cond2_code = prefs.getString("cond2_code","");
        task.setImageView(cond_pic);
        task.execute("http://files.heweather.com/cond_icon/"+cond_code+".png"); // 执行异步操作
        task1.setImageView(cond_pic1);
        task1.execute("http://files.heweather.com/cond_icon/" + cond1_code + ".png"); // 执行异步操作
        task2.setImageView(cond_pic2);
        task2.execute("http://files.heweather.com/cond_icon/"+cond2_code+".png"); // 执行异步操作

        weather_info_layout.setVisibility(View.VISIBLE);
        weather_info_layout2.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

    class downloadImageTask extends AsyncTask<String, Integer, Boolean> {
        private ImageView imageView;

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            System.out.println("[downloadImageTask->]doInBackground "
                    + params[0]);
            mDownloadImage = HttpUtil.getNetWorkBitmap(params[0]);
            return true;
        }

        // 下载完成回调
        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            imageView.setImageBitmap(mDownloadImage);
            System.out.println("result = " + result);
            super.onPostExecute(result);
        }

        // 更新进度回调
        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }


    }
}

