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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
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

    private  TextView res_data;



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
        res_data=findViewById(R.id.res_data);




        main_button.setOnClickListener(view -> {
            String city = user_field.getText().toString().trim();
            if (!city.isEmpty()) {
                String apiKey = "6e8fe4fad218b24f3ca8504f36669203";
                String url = "https://api.openweathermap.org/data/2.5/weather?q=" +
                        city + "&appid=" + apiKey + "&units=metric&lang=uk";
                new GetWeatherDataTask().execute(url);
            } else {
                Toast.makeText(MainActivity.this, "Введіть назву міста", Toast.LENGTH_SHORT).show();
            }
        });
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
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    String DataLocal=dateFormat.format(currentDate);


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
                    res_Tis.setText( weatherDescription);
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