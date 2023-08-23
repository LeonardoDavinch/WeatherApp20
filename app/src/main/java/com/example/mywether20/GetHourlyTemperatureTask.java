package com.example.mywether20;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
public class GetHourlyTemperatureTask extends AsyncTask<String, Void, String> {

    private TextView[] hourTemperatureViews;

    public GetHourlyTemperatureTask(TextView[] hourTemperatureViews) {
        this.hourTemperatureViews = hourTemperatureViews;
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder buffer = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            return buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray hourlyData = jsonObject.getJSONArray("list");

                for (int i = 0; i < 24; i++) {
                    JSONObject hourData = hourlyData.getJSONObject(i);
                    JSONObject main = hourData.getJSONObject("main");
                    int temperature = (int) Math.round(main.getDouble("temp"));

                    hourTemperatureViews[i].setText(String.format(Locale.getDefault(), "%d", temperature));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}