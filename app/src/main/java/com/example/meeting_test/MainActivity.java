package com.example.meeting_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Intent intent = new Intent(MainActivity.this,Login.class);
        MainActivity.this.startActivity(intent);
        MainActivity.this.finish();*/

        FirebaseApp.initializeApp(this);

        /*for(int i=0 ; i<10 ; i++) {
            Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_SHORT).show();
        }*/


        Button join = findViewById(R.id.join);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity.this,Login.class);
                MainActivity.this.startActivity(intent2);
                MainActivity.this.finish();
            }
        });

        Button create = findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, CreateMeeting.class);
                MainActivity.this.startActivity(intent2);
                MainActivity.this.finish();
            }
        });

        Button test = findViewById(R.id.test_button);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, TestTwo.class);
                MainActivity.this.startActivity(intent2);
                MainActivity.this.finish();
            }
        });

    }
}
