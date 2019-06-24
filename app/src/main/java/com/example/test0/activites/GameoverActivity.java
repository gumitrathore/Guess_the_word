package com.example.test0.activites;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.test0.MainActivity;
import com.example.test0.R;

public class GameoverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);

        Handler handler = new Handler();

               handler.postDelayed(new Runnable() {
                public void run() {
                    Popup();
                }
            }, 3000);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Popup();

    }
    public void Popup(){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(GameoverActivity.this);

        builder1.setMessage("Exit Game!");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "Play Again!",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(GameoverActivity.this, LandingActivity.class);
                        startActivity(intent);
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
