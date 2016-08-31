package com.myweather.user.myweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;

import com.myweather.user.myweather.db.WeatherDB;
import com.myweather.user.myweather.model.City;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;


/**
 * Created by User on 2016/8/27.
 */
public class HttpUtil {

    public static void sendHttpRequest(final String address,final HttpCallbackLister lister){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine())!=null){
                        response.append(line);
                    }
                    if(lister !=  null){
                        lister.onFinish(response.toString());
                    }
                }catch (Exception e){
                    if(lister != null){
                        lister.onError(e);
                    }
                }finally {
                    if(connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static Bitmap getNetWorkBitmap(String urlString) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(urlString);
            // 使用HttpURLConnection打开连接
            HttpURLConnection urlConn = (HttpURLConnection) imgUrl
                    .openConnection();
            urlConn.setDoInput(true);
            urlConn.connect();
            // 将得到的数据转化成InputStream
            InputStream is = urlConn.getInputStream();
            // 将InputStream转换成Bitmap
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            System.out.println("[getNetWorkBitmap->]MalformedURLException");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[getNetWorkBitmap->]IOException");
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void handleCityResponse(WeatherDB weatherDB,Context context,String response){
        try {
            JSONObject cityInfo = new JSONObject(response);
            JSONArray jsonArray = cityInfo.getJSONArray("city_info");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                City city = new City();
                city.setCityCode(jsonObject.getString("id"));
                city.setProv(jsonObject.getString("prov"));
                city.setCityName(jsonObject.getString("city"));
                weatherDB.saveCity(city);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void handleWeatherResponse(Context context,String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray weather = jsonObject.getJSONArray("HeWeather data service 3.0");
            JSONObject weatherInfo = weather.getJSONObject(0);
            JSONObject basic = weatherInfo.getJSONObject("basic");
            String city_name = basic.getString("city");
            JSONObject aqi = weatherInfo.getJSONObject("aqi");
            JSONObject city = aqi.getJSONObject("city");
            String qlty = city.getString("qlty");
            JSONObject now = weatherInfo.getJSONObject("now");
            JSONObject cond = now.getJSONObject("cond");
            String cond_code = cond.getString("code");
            String cond_txt = cond.getString("txt");
            JSONObject wind = now.getJSONObject("wind");
            String dir = wind.getString("dir");
            String sc = wind.getString("sc");
            String fl = now.getString("fl");
            String hum = now.getString("hum");
            String tmp = now.getString("tmp");
            JSONArray daily_forecast = weatherInfo.getJSONArray("daily_forecast");
            JSONObject day0 = daily_forecast.getJSONObject(0);
            String date = day0.getString("date");
            JSONObject temp = day0.getJSONObject("tmp");
            String tmp_max = temp.getString("max");
            String tmp_min = temp.getString("min");
            JSONObject day1 = daily_forecast.getJSONObject(1);
            JSONObject day2 = daily_forecast.getJSONObject(2);
            JSONObject cond1 = day1.getJSONObject("cond");
            String cond1_code = cond1.getString("code_d");
            String cond1_text = cond1.getString("txt_n");
            JSONObject tmp1 = day1.getJSONObject("tmp");
            String tmp1_max = tmp1.getString("max");
            String tmp1_min = tmp1.getString("min");
            JSONObject cond2 = day2.getJSONObject("cond");
            String cond2_code = cond2.getString("code_d");
            String cond2_text = cond2.getString("txt_n");
            JSONObject tmp2 = day2.getJSONObject("tmp");
            String tmp2_max = tmp2.getString("max");
            String tmp2_min = tmp2.getString("min");
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putBoolean("city_selected",true);
            //明后天天气预报
            editor.putString("cond1_code",cond1_code);
            editor.putString("cond1_text",cond1_text);
            editor.putString("tmp1_max",tmp1_max);
            editor.putString("tmp1_min",tmp1_min);
            editor.putString("cond2_code",cond2_code);
            editor.putString("cond2_text",cond2_text);
            editor.putString("tmp2_max",tmp2_max);
            editor.putString("tmp2_min",tmp2_min);
            //今天天气情况
            editor.putString("city_name",city_name);
            editor.putString("tmp",tmp);
            editor.putString("tmp_max",tmp_max);
            editor.putString("tmp_min",tmp_min);
            editor.putString("date",date);
            editor.putString("fl",fl);
            editor.putString("hum",hum);
            editor.putString("qlty",qlty);
            editor.putString("cond_code",cond_code);
            editor.putString("cond_txt",cond_txt);
            editor.putString("sc",sc);
            editor.putString("dir",dir);
            editor.commit();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
