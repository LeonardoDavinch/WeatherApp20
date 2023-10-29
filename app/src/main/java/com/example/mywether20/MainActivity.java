package com.example.mywether20;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import android.os.Bundle;

import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private EditText user_field;
    public TextView res_temp, res_winter, res_Humidity, res_timeTemp, res_Show, res_Tis, res_data;

    private TextView temperatureHoue0, temperatureHoue1, temperatureHoue2, temperatureHoue3,
            temperatureHoue4, temperatureHoue5, temperatureHoue6, temperatureHoue7,
            temperatureHoue8, temperatureHoue9, temperatureHoue10,
            temperatureHoue11, temperatureHoue12, temperatureHoue13,
            temperatureHoue14, temperatureHoue15, temperatureHoue16, temperatureHoue17,
            temperatureHoue18, temperatureHoue19, temperatureHoue20, temperatureHoue21,
            temperatureHoue22, temperatureHoue23;
    private TextView hourTemperature0, hourTemperature1, hourTemperature2, hourTemperature3, hourTemperature4,
            hourTemperature5, hourTemperature6, hourTemperature7, hourTemperature8, hourTemperature9, hourTemperature10,
            hourTemperature11, hourTemperature12, hourTemperature13, hourTemperature14, hourTemperature15, hourTemperature16,
            hourTemperature17, hourTemperature18, hourTemperature19, hourTemperature20, hourTemperature21, hourTemperature22,
            hourTemperature23;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.user_field);

        String lastCity = CityPreference.getCity(this);
        autoCompleteTextView.setText(lastCity);


        // Отримання списку міст з ресурсів
        String[] cityList = getResources().getStringArray(R.array.city_list);

        // Створення адаптера для автозаповнення
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                cityList
        );

        // Налаштування адаптера для AutoCompleteTextView
        autoCompleteTextView.setAdapter(adapter);

        // Додавання можливості ручного вводу
        autoCompleteTextView.setThreshold(1); // 1 символ


        //обробка подій вибора міста
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCity = parent.getItemAtPosition(position).toString();
            updateWeatherData(selectedCity);
        });

        //Прослуховування кнопок
        ImageButtonHandler imageButtonHandler = new ImageButtonHandler(this);

        ImageButton b_Message = findViewById(R.id.B_Message);
        ImageButton b_Google = findViewById(R.id.B_Google);
        ImageButton b_Setting = findViewById(R.id.B_InformationAuthor);

        b_Message.setOnClickListener(imageButtonHandler);
        b_Google.setOnClickListener(imageButtonHandler);
        b_Setting.setOnClickListener(imageButtonHandler);

        RelativeLayout relativeLayout = findViewById(R.id.Bac1);


        //вивід температури по годинам
        temperatureHoue0 = findViewById(R.id.temperatureHoue0);
        temperatureHoue1 = findViewById(R.id.temperatureHoue1);
        temperatureHoue2 = findViewById(R.id.temperatureHou2);
        temperatureHoue3 = findViewById(R.id.temperatureHou3);
        temperatureHoue4 = findViewById(R.id.temperatureHou4);
        temperatureHoue5 = findViewById(R.id.temperatureHou5);
        temperatureHoue6 = findViewById(R.id.temperatureHou6);
        temperatureHoue7 = findViewById(R.id.temperatureHou7);
        temperatureHoue8 = findViewById(R.id.temperatureHou8);
        temperatureHoue9 = findViewById(R.id.temperatureHou9);
        temperatureHoue10 = findViewById(R.id.temperatureHou10);
        temperatureHoue11 = findViewById(R.id.temperatureHou11);
        temperatureHoue12 = findViewById(R.id.temperatureHou12);
        temperatureHoue13 = findViewById(R.id.temperatureHou13);
        temperatureHoue14 = findViewById(R.id.temperatureHou14);
        temperatureHoue15 = findViewById(R.id.temperatureHou15);
        temperatureHoue16 = findViewById(R.id.temperatureHou16);
        temperatureHoue17 = findViewById(R.id.temperatureHou17);
        temperatureHoue18 = findViewById(R.id.temperatureHou18);
        temperatureHoue19 = findViewById(R.id.temperatureHou19);
        temperatureHoue20 = findViewById(R.id.temperatureHou20);
        temperatureHoue21 = findViewById(R.id.temperatureHou21);
        temperatureHoue22 = findViewById(R.id.temperatureHou22);
        temperatureHoue23 = findViewById(R.id.temperatureHou23);

        hourTemperature0 = findViewById(R.id.hourTemperature0);
        hourTemperature1 = findViewById(R.id.hourTemperature1);
        hourTemperature2 = findViewById(R.id.hourTemperature2);
        hourTemperature3 = findViewById(R.id.hourTemperature3);
        hourTemperature4 = findViewById(R.id.hourTemperature4);
        hourTemperature5 = findViewById(R.id.hourTemperature5);
        hourTemperature6 = findViewById(R.id.hourTemperature6);
        hourTemperature7 = findViewById(R.id.hourTemperature7);
        hourTemperature8 = findViewById(R.id.hourTemperature8);
        hourTemperature9 = findViewById(R.id.hourTemperature9);
        hourTemperature10 = findViewById(R.id.hourTemperature10);
        hourTemperature11 = findViewById(R.id.hourTemperature11);
        hourTemperature12 = findViewById(R.id.hourTemperature12);
        hourTemperature13 = findViewById(R.id.hourTemperature13);
        hourTemperature14 = findViewById(R.id.hourTemperature14);
        hourTemperature15 = findViewById(R.id.hourTemperature15);
        hourTemperature16 = findViewById(R.id.hourTemperature16);
        hourTemperature17 = findViewById(R.id.hourTemperature17);
        hourTemperature18 = findViewById(R.id.hourTemperature18);
        hourTemperature19 = findViewById(R.id.hourTemperature19);
        hourTemperature20 = findViewById(R.id.hourTemperature20);
        hourTemperature21 = findViewById(R.id.hourTemperature21);
        hourTemperature22 = findViewById(R.id.hourTemperature22);
        hourTemperature23 = findViewById(R.id.hourTemperature23);

        autoCompleteTextView = findViewById(R.id.user_field);
        // Отримати поточну годину
        Calendar cal = Calendar.getInstance();
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);

        // Визначити, чи є зараз день чи ніч
        boolean isDaytime = currentHour >= 6 && currentHour < 18;

        if (isDaytime) {
            relativeLayout.setBackgroundResource(R.drawable.day);
        } else {
            relativeLayout.setBackgroundResource(R.drawable.niaght);
        }


        user_field = findViewById(R.id.user_field);
        res_temp = findViewById(R.id.res_temp);
        res_winter = findViewById(R.id.res_winter);
        res_Humidity = findViewById(R.id.res_Humidity);
        res_timeTemp = findViewById(R.id.res_timeTemp);
        res_Show = findViewById(R.id.res_Show);
        res_Tis = findViewById(R.id.res_Tis);
        res_data = findViewById(R.id.res_data);
        updateWeatherData(lastCity);
    }

    private void updateWeatherData(String city) {
        CityPreference.setCity(this, city);
        String apiKey = "6e8fe4fad218b24f3ca8504f36669203";

        // URL для прогнозу на п'ять днів
        String forecastUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" +
                city + "&appid=" + apiKey + "&units=metric&lang=uk";

        // URL для поточної погоди
        String currentWeatherUrl = "https://api.openweathermap.org/data/2.5/weather?q=" +
                city + "&appid=" + apiKey + "&units=metric&lang=uk";

        TextView[] hourTemperatureViews = {
                temperatureHoue0, temperatureHoue1, temperatureHoue2, temperatureHoue3, temperatureHoue4,
                temperatureHoue5, temperatureHoue6, temperatureHoue7, temperatureHoue8, temperatureHoue9,
                temperatureHoue10, temperatureHoue11, temperatureHoue12, temperatureHoue13, temperatureHoue14,
                temperatureHoue15, temperatureHoue16, temperatureHoue17, temperatureHoue18, temperatureHoue19,
                temperatureHoue20, temperatureHoue21, temperatureHoue22, temperatureHoue23
        };
        TextView[] hourTimeViews = {
                hourTemperature0, hourTemperature1, hourTemperature2, hourTemperature3, hourTemperature4,
                hourTemperature5, hourTemperature6, hourTemperature7, hourTemperature8, hourTemperature9, hourTemperature10,
                hourTemperature11, hourTemperature12, hourTemperature13, hourTemperature14, hourTemperature15, hourTemperature16,
                hourTemperature17, hourTemperature18, hourTemperature19, hourTemperature20, hourTemperature21, hourTemperature22,
                hourTemperature23
        };

        String hourlyWeatherUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" +
                city + "&appid=" + apiKey + "&units=metric&lang=uk";

        new GetHourlyTemperatureTask(hourTemperatureViews, hourTimeViews).execute(hourlyWeatherUrl);

        new GetWeatherDataTask().execute(currentWeatherUrl);
        TextView[] dateViews = {
                findViewById(R.id.dataday1),
                findViewById(R.id.dataday2),
                findViewById(R.id.dataday3),
                findViewById(R.id.dataday4),
                findViewById(R.id.dataday5)
        };

        TextView[] maxTempViews = {
                findViewById(R.id.maxday1),
                findViewById(R.id.maxday2),
                findViewById(R.id.maxday3),
                findViewById(R.id.maxday4),
                findViewById(R.id.maxday5)
        };

        TextView[] minTempViews = {
                findViewById(R.id.day01),
                findViewById(R.id.day02),
                findViewById(R.id.day03),
                findViewById(R.id.day04),
                findViewById(R.id.day05)
        };

        ImageView[] maybeViews = {
                findViewById(R.id.meybe1),
                findViewById(R.id.meybe2),
                findViewById(R.id.meybe3),
                findViewById(R.id.meybe4),
                findViewById(R.id.meybe5)
        };
        new GetForecastDataTask(this, dateViews, maxTempViews, minTempViews, maybeViews).execute(forecastUrl);
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

        public class CityPreference {
            private static final String PREFS_NAME = "CityPrefs";
            private static final String KEY_CITY_NAME = "cityName";

            public String getCity(Context context) {
                SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                return prefs.getString(KEY_CITY_NAME, ""); // Повертаємо пустий рядок якщо місто не знайдено
            }

            public void setCity(Context context, String cityName) {
                SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
                editor.putString(KEY_CITY_NAME, cityName);
                editor.apply();
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
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EE.dd.MM.yyyy", new Locale("uk", "UA"));
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



