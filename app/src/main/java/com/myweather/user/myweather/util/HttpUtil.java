package com.myweather.user.myweather.util;

import android.content.Context;

import com.myweather.user.myweather.db.WeatherDB;
import com.myweather.user.myweather.model.City;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


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
                    }if(lister !=  null){
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


}
