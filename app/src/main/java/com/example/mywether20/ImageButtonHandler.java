package com.example.mywether20;


import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ImageButtonHandler  implements View.OnClickListener {
    private final MainActivity parentActivity;
    private ImageButton B_Message, B_Google, InformationAurhor;

    public ImageButtonHandler(MainActivity parentActivity) {
        this.parentActivity = parentActivity;
        B_Message = parentActivity.findViewById(R.id.B_Message);
        B_Google = parentActivity.findViewById(R.id.B_Google);
        InformationAurhor = parentActivity.findViewById(R.id.B_InformationAuthor);

        B_Message.setOnClickListener(this);
        B_Google.setOnClickListener(this);
        InformationAurhor.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int clickedId = view.getId();

        if (clickedId == R.id.B_Message) {
            showSendMessageDialog();
        } else if (clickedId == R.id.B_Google) {
            String websiteUrl = "https://openweathermap.org/";
            // Відкриваємо веб-сайт за вказаною посиланням
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
            parentActivity.startActivity(websiteIntent);
        } else if (clickedId == R.id.B_InformationAuthor) {
            // При натисканні кнопки "Налаштування" відображаємо вікно з інформацією про розробника
            showDeveloperInfoDialog();
        }
    }

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

    private void showDeveloperInfoDialog() {
        // Створюємо діалогове вікно з інформацією про розробника
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        builder.setTitle("Інформація про розробника");
        builder.setMessage("Розроблено: Ганусяк В.С\n" +
                "Електронна пошта: leonardo20prof@gmail.com\n" +
                "Версія додатку: 1.0");

        builder.setPositiveButton("Закрити", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });

        builder.show();
    }
}
