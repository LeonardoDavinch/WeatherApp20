package com.example.mywether20;

import com.example.mywether20.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;
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
import java.util.Date;
import java.util.Locale;

@SuppressLint("StaticFieldLeak")
public class GetForecastDataTask extends AsyncTask<String, Void, String> {

    private final TextView dataday1, dataday2, dataday3, dataday4, dataday5;
    private final TextView maxday1, maxday2, maxday3, maxday4, maxday5;
    private final TextView minday01, minday02, minday03, minday04, minday05;
    private final ImageView meybe1, meybe2, meybe3, meybe4, meybe5;

    private final Context context;

    public GetForecastDataTask(Context context, TextView[] dateViews, TextView[] maxTempViews,
                               TextView[] minTempViews, ImageView[] maybeViews) {
        this.context = context;
        dataday1 = dateViews[0];
        dataday2 = dateViews[1];
        dataday3 = dateViews[2];
        dataday4 = dateViews[3];
        dataday5 = dateViews[4];

        maxday1 = maxTempViews[0];
        maxday2 = maxTempViews[1];
        maxday3 = maxTempViews[2];
        maxday4 = maxTempViews[3];
        maxday5 = maxTempViews[4];

        minday01 = minTempViews[0];
        minday02 = minTempViews[1];
        minday03 = minTempViews[2];
        minday04 = minTempViews[3];
        minday05 = minTempViews[4];

        meybe1 = maybeViews[0];
        meybe2 = maybeViews[1];
        meybe3 = maybeViews[2];
        meybe4 = maybeViews[3];
        meybe5 = maybeViews[4];
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
                JSONArray list = jsonObject.getJSONArray("list");

                // Очищаємо тексти перед оновленням даних
                clearForecastData();

                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputDateFormat = new SimpleDateFormat("EE.dd.MM", new Locale("uk", "UA"));

                String currentDate = "";
                double maxTemp = Double.MIN_VALUE;
                double minTemp = Double.MAX_VALUE;
                String weatherDescription = "";

                for (int i = 0; i < list.length(); i++) {
                    JSONObject forecastItem = list.getJSONObject(i);
                    JSONObject main = forecastItem.getJSONObject("main");
                    JSONArray weatherArray = forecastItem.getJSONArray("weather");
                    double temp = main.getDouble("temp");

                    String dateTime = forecastItem.getString("dt_txt");
                    Date parsedDate = inputFormat.parse(dateTime);
                    String date = outputDateFormat.format(parsedDate);

                    // Отримуємо опис погоди
                    if (weatherArray.length() > 0) {
                        JSONObject weatherObject = weatherArray.getJSONObject(0);
                        weatherDescription = weatherObject.getString("description");
                    }

                    // Пропускаємо поточний день в прогнозі
                    if (date.equals(outputDateFormat.format(new Date()))) {
                        continue;
                    }

                    // Якщо дата змінилася або досягнута остання година прогнозу
                    if (!date.equals(currentDate) || i == list.length() - 1) {
                        // Додати дані до TextView для поточного дня
                        addForecastData(currentDate, maxTemp, minTemp, weatherDescription);

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
                Toast.makeText(context, "Помилка отримання даних прогнозу", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Помилка отримання даних прогнозу", Toast.LENGTH_SHORT).show();
        }
    }

    private void addForecastData(String date, double maxTemp, double minTemp, String weatherDescription) {
        // Отримуємо ідентифікатор ресурсу зображення на основі умов погоди
        int weatherImageResource = getWeatherImageResource(weatherDescription);

        // Отримуємо ImageView для зображення погоди
        ImageView[] maybeViews = {meybe1, meybe2, meybe3, meybe4, meybe5};

        for (ImageView maybeView : maybeViews) {
            if (maybeView.getDrawable() == null) {
                maybeView.setImageResource(weatherImageResource);
                break;
            }
        }

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

    private int getWeatherImageResource(String weatherDescription) {
        switch (weatherDescription) {
            case "чисте небо":
                return R.drawable.sun;
            case "уривчасті хмари":
                return R.drawable.cloudy_3;
            case "легкий дощ":
                return R.drawable.rainy;
            case "сніг":
                return R.drawable.snowy;
            case "кілька хмар":
                return R.drawable.cloudy_3;
            case "рвані хмари":
                return R.drawable.cloudy_3;
            case "кілька хмари":
                return R.drawable.cloudy_3;
            case "гроза":
                return R.drawable.storm;
            case "туман":
                return R.drawable.cloudy_3;
            case "мряка":
                return R.drawable.droow;
            case "похмуро":
                return R.drawable.cloudy_3;
            case "ливень":
                return R.drawable.rainy;
            case "снігопад":
                return R.drawable.snowy;
            case "небезпечні умови":
                return R.drawable.droow;
            // Додайте інші умови погоди тут з відповідними ресурсами
            default:
                return R.drawable.humidity;
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