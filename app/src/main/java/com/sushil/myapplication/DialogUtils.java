package com.sushil.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

public class DialogUtils {

    public static void showRatingDialog(Activity activity) {
        View view = LayoutInflater.from(activity).inflate(R.layout.rating_dialog, null);
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(view)
                .setCancelable(true)
                .create();

        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        Button btnSubmit = view.findViewById(R.id.btnSubmitRating);

        btnSubmit.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            Toast.makeText(activity, "Thanks for rating " + rating + " stars!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }
}
