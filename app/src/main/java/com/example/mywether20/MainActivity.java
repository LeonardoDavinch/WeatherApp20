package com.example.mywether20;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText user_field;
    private Button main_button;
    private TextView res_temp;
    private TextView res_winter;
    private TextView res_Humidity;

    private TextView res_timeTemp;
    private TextView res_Show;
    private TextView res_Tis;

    private TextView res_data;
    private TextView dataday1;
    private TextView dataday2;
    private TextView dataday3;
    private TextView dataday4;
    private TextView dataday5;
    private TextView maxday1;
    private TextView maxday2;
    private TextView maxday3;
    private TextView maxday4;
    private TextView maxday5;
    private TextView minday01;
    private TextView minday02;
    private TextView minday03;
    private TextView minday04;
    private TextView minday05;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_field = findViewById(R.id.user_field);
        main_button = findViewById(R.id.main_button);
        res_temp = findViewById(R.id.res_temp);
        res_winter = findViewById(R.id.res_winter);
        res_Humidity = findViewById(R.id.res_Humidity);
        res_timeTemp = findViewById(R.id.res_timeTemp);
        res_Show = findViewById(R.id.res_Show);
        res_Tis = findViewById(R.id.res_Tis);
        res_data = findViewById(R.id.res_data);
        dataday1 = findViewById(R.id.dataday1);
        dataday2 = findViewById(R.id.dataday2);
        dataday3 = findViewById(R.id.dataday3);
        dataday4 = findViewById(R.id.dataday4);
        dataday5 = findViewById(R.id.dataday5);

        maxday1 = findViewById(R.id.maxday1);
        maxday2 = findViewById(R.id.maxday2);
        maxday3 = findViewById(R.id.maxday3);
        maxday4 = findViewById(R.id.maxday4);
        maxday5 = findViewById(R.id.maxday5);

        minday01 = findViewById(R.id.day01);
        minday02 = findViewById(R.id.day02);
        minday03 = findViewById(R.id.day03);
        minday04 = findViewById(R.id.day04);
        minday05 = findViewById(R.id.day05);


        main_button.setOnClickListener(view -> {
            String city = user_field.getText().toString().trim();
            if (!city.isEmpty()) {
                String apiKey = "6e8fe4fad218b24f3ca8504f36669203";

                // URL для прогнозу на п'ять днів
                String forecastUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" +
                        city + "&appid=" + apiKey + "&units=metric&lang=uk";

                // URL для поточної погоди
                String currentWeatherUrl = "https://api.openweathermap.org/data/2.5/weather?q=" +
                        city + "&appid=" + apiKey + "&units=metric&lang=uk";


                new GetWeatherDataTask().execute(currentWeatherUrl);
                new GetForecastDataTask().execute(forecastUrl);

            } else {
                Toast.makeText(MainActivity.this, "Введіть назву міста", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class GetForecastDataTask extends AsyncTask<String, Void, String> {
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
                    JSONArray list = jsonObject.getJSONArray("list");

                    // Очищаємо тексти перед оновленням даних
                    clearForecastData();

                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM", Locale.getDefault());

                    String currentDate = "";
                    double maxTemp = Double.MIN_VALUE;
                    double minTemp = Double.MAX_VALUE;

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject forecastItem = list.getJSONObject(i);
                        JSONObject main = forecastItem.getJSONObject("main");
                        double temp = main.getDouble("temp");

                        String dateTime = forecastItem.getString("dt_txt");
                        Date parsedDate = inputFormat.parse(dateTime);
                        String date = outputDateFormat.format(parsedDate);

                        // Пропускаємо поточний день в прогнозі
                        if (date.equals(outputDateFormat.format(new Date()))) {
                            continue;
                        }

                        // Якщо дата змінилася або досягнута остання година прогнозу
                        if (!date.equals(currentDate) || i == list.length() - 1) {
                            // Додати дані до TextView для поточного дня
                            addForecastData(currentDate, maxTemp, minTemp);

                            // Обнулити змінні для нового дня
                            currentDate = date;
                            maxTemp = Double.MIN_VALUE;
                            minTemp = Double.MAX_VALUE;
                        }

                        // Оновити максимальну і мінімальну температуру для дня
                        if (temp > maxTemp) {
                            maxTemp = temp;
                        }
                        if (temp < minTemp) {
                            minTemp = temp;
                        }
                    }

                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Помилка обробки даних прогнозу", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Помилка отримання даних прогнозу", Toast.LENGTH_SHORT).show();
            }
        }


        private void addForecastData(String date, double maxTemp, double minTemp) {
            if (dataday1.getText().toString().equals("")) {
                dataday1.setText(date);
                maxday1.setText(String.format(Locale.getDefault(), "%.1f °C", maxTemp));
                minday01.setText(String.format(Locale.getDefault(), "%.1f °C", minTemp));
            } else if (dataday2.getText().toString().equals("")) {
                dataday2.setText(date);
                maxday2.setText(String.format(Locale.getDefault(), "%.1f °C", maxTemp));
                minday02.setText(String.format(Locale.getDefault(), "%.1f °C", minTemp));
            } else if (dataday3.getText().toString().equals("")) {
                dataday3.setText(date);
                maxday3.setText(String.format(Locale.getDefault(), "%.1f °C", maxTemp));
                minday03.setText(String.format(Locale.getDefault(), "%.1f °C", minTemp));
            } else if (dataday4.getText().toString().equals("")) {
                dataday4.setText(date);
                maxday4.setText(String.format(Locale.getDefault(), "%.1f °C", maxTemp));
                minday04.setText(String.format(Locale.getDefault(), "%.1f °C", minTemp));
            } else if (dataday5.getText().toString().equals("")) {
                dataday5.setText(date);
                maxday5.setText(String.format(Locale.getDefault(), "%.1f °C", maxTemp));
                minday05.setText(String.format(Locale.getDefault(), "%.1f °C", minTemp));
            }
        }

        private void clearForecastData() {
            dataday1.setText("");
            dataday2.setText("");
            dataday3.setText("");
            dataday4.setText("");
            dataday5.setText("");

            maxday1.setText("");
            maxday2.setText("");
            maxday3.setText("");
            maxday4.setText("");
            maxday5.setText("");

            minday01.setText("");
            minday02.setText("");
            minday03.setText("");
            minday04.setText("");
            minday05.setText("");
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetWeatherDataTask extends AsyncTask<String, Void, String> {
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
                    JSONObject main = jsonObject.getJSONObject("main");
                    JSONObject wind = jsonObject.getJSONObject("wind");
                    JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);

                    Date currentDate = new Date();
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM");
                    String DataLocal = dateFormat.format(currentDate);

                    double temperature = main.getDouble("temp");
                    double windSpeed = wind.getDouble("speed");
                    int humidityValue = main.getInt("humidity");
                    double feelsLike = main.getDouble("feels_like");
                    double visibility = jsonObject.getDouble("visibility") / 1000.0;
                    String weatherDescription = weather.getString("description");


                    res_data.setText(String.format(DataLocal));

                    res_temp.setText(String.format(Locale.getDefault(), "%.1f °C", temperature));
                    res_winter.setText(String.format(Locale.getDefault(), "%.1f m/s", windSpeed));
                    res_Humidity.setText(String.format(Locale.getDefault(), "%d%%", humidityValue));
                    res_timeTemp.setText(String.format(Locale.getDefault(), "%.1f °C", feelsLike));
                    res_Show.setText(String.format(Locale.getDefault(), "%.2f км", visibility));
                    res_Tis.setText(weatherDescription);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Помилка обробки даних", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Помилка отримання даних", Toast.LENGTH_SHORT).show();
            }
        }

    }

}



