package com.example.mywether20;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class ImageButtonHandler  implements View.OnClickListener {
    private final MainActivity parentActivity;
    private ImageButton B_Message, B_Google, B_Setting;

    public ImageButtonHandler(MainActivity parentActivity) {
        this.parentActivity = parentActivity;
        B_Message = parentActivity.findViewById(R.id.B_Message);
        B_Google = parentActivity.findViewById(R.id.B_Google);
        B_Setting = parentActivity.findViewById(R.id.B_Setting);

        B_Message.setOnClickListener(this); // Додати обробник кліку до кнопки B_Message
        B_Google.setOnClickListener(this);
        B_Setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int clickedId = view.getId();

        if (clickedId == R.id.B_Message) {
            // При натисканні кнопки "Повідомлення" відобразити діалогове вікно для відправки повідомлення
            showSendMessageDialog();
        } else if (clickedId == R.id.B_Google) {
            // Відкрити веб-сайт за вказаною посиланням
            String websiteUrl = "https://openweathermap.org/";
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
            parentActivity.startActivity(websiteIntent);
        } else if (clickedId == R.id.B_Setting) {
            Toast.makeText(parentActivity, "Натиснули кнопку 'Налаштування'", Toast.LENGTH_SHORT).show();
        }
    }

    // Метод для відображення діалогового вікна для відправки повідомлення
    private void showSendMessageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        builder.setTitle(" Відправити повідомлення");
        builder.setMessage("Ведіть ваші побажання щодо покращення абож вкажіть недоліки програми:");

        final EditText input = new EditText(parentActivity);
        builder.setView(input);

        builder.setPositiveButton("Відправити", (dialogInterface, i) -> {
            String message = input.getText().toString();

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"leonardo20prof@gmail.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Повідомлення з додатку ");
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);

            try {
                parentActivity.startActivity(Intent.createChooser(emailIntent, "Виберіть поштовий клієнт"));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(parentActivity, "Поштовий клієнт не знайдено", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Скасувати", (dialogInterface, i) -> dialogInterface.cancel());

        builder.show();
    }
}
