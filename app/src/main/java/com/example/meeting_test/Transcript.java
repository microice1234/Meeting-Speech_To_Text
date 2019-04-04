package com.example.meeting_test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.annotation.Documented;
import java.util.ArrayList;

public class Transcript extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transcript);
        FirebaseApp.initializeApp(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        final DatabaseReference MeetingRef = database.getReference("Meeting_List");

        DatabaseReference currentMeetingRef = MeetingRef.child(MeetingDetails.meeting_key);
        DatabaseReference dataReference = currentMeetingRef.child("Data");
        final ArrayList<String> text = new ArrayList<String>();

        final TextView transcript = findViewById(R.id.transcript);

        dataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    text.add(children.getValue().toString());
                }
                String trans = new String();
                trans = trans + "TRANSCRIPT\n";
                for (String e : text) {
                    trans = trans + e + "\n";
                }

                transcript.setText(trans);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }


}
