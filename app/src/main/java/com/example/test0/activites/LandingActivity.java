package com.example.test0.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

import com.example.test0.MainActivity;
import com.example.test0.R;
import com.example.test0.models.Level;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class LandingActivity  extends AppCompatActivity {
    ArrayList<Level> levels = new ArrayList<>();
    ProgressDialog pd;
    boolean viewClick = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        pd = new ProgressDialog(this);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("levels");
        pd.setMessage("Please Wait....");
        pd.setCancelable(false);
        pd.show();

// Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<Level>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<Level>>() {};
                ArrayList<Level> post = dataSnapshot.getValue(genericTypeIndicator);
                levels.addAll(post);
                pd.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pd.dismiss();
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        CardView Easy = (CardView) findViewById(R.id.cvEasy);
        Easy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!viewClick) {
                    viewClick = true;
                    Intent intent = new Intent(LandingActivity.this, MainActivity.class);
                    intent.putExtra("levels", new Gson().toJson(levels));
                    intent.putExtra("type", "easy");
                    startActivity(intent);
                }
            }
        });
        CardView Medium = (CardView) findViewById(R.id.cvMedium);
        Medium.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!viewClick) {
                    viewClick = true;
                    Intent intent = new Intent(LandingActivity.this, MainActivity.class);
                    intent.putExtra("levels", new Gson().toJson(levels));
                    intent.putExtra("type", "medium");
                    startActivity(intent);
                }
            }
        });
        CardView Hard = (CardView) findViewById(R.id.cvHard);
        Hard.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!viewClick) {
                    viewClick = true;
                    Intent intent = new Intent(LandingActivity.this, MainActivity.class);
                    intent.putExtra("levels", new Gson().toJson(levels));
                    intent.putExtra("type", "hard");
                    startActivity(intent);
                }
            }
        });
        CardView Genius = (CardView) findViewById(R.id.cvGenius);
        Genius.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!viewClick) {
                    viewClick = true;
                    Intent intent = new Intent(LandingActivity.this, MainActivity.class);
                    intent.putExtra("levels", new Gson().toJson(levels));
                    intent.putExtra("type", "genius");
                    startActivity(intent);
                }
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
         viewClick = false;
    }
}
