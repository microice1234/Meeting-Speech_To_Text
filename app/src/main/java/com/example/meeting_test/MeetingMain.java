package com.example.meeting_test;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.hardware.camera2.TotalCaptureResult;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MeetingMain extends AppCompatActivity {

    // ![](giphy2.gif)     ![](giphy1.gif)
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private int i = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_main1);

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        Button view_trans = findViewById(R.id.view_transcript);

        final Button btnend = findViewById(R.id.end);

        btnend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MeetingMain.this, MainActivity.class);
                MeetingMain.this.startActivity(intent2);
                MeetingMain.this.finish();
            }
        });

        view_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MeetingMain.this, Transcript.class);
                MeetingMain.this.startActivity(intent2);
                MeetingMain.this.finish();
            }
        });

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                    speechToText();
                    btnend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent2 = new Intent(MeetingMain.this, MainActivity.class);
                            MeetingMain.this.startActivity(intent2);
                            MeetingMain.this.finish();
                        }
                    });
                }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference MeetingRef = database.getReference("Meeting_List");

        DatabaseReference currentMeetingRef = MeetingRef.child(MeetingDetails.meeting_key);
        DatabaseReference currentMeetingUsersRef = currentMeetingRef.child("Users");

        final TextView field = (TextView) findViewById(R.id.list_users);

        final ArrayList<String> users_list = new ArrayList<String>();

        if(MeetingDetails.owner.compareTo(MeetingDetails.name) != 0) {
            btnSpeak.setEnabled(false);
            btnSpeak.setAlpha(0.0f);
            try {
                Thread.sleep(3000);
            }
            catch (Exception e) { }
            textDisplay();

        }



        currentMeetingUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users_list.clear();
                for(DataSnapshot children : dataSnapshot.getChildren()) {
                    Users u = children.getValue(Users.class);

                    users_list.add(u.name);

                }
                String text = new String();
                text = text + "USERS\n";
                for(String e : users_list) {
                    text = text + e + "\n";
                }

                field.setText(text);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void speechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));

        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));

                    String text = new String(result.get(0));

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference MeetingRef = database.getReference("Meeting_List");

                    DatabaseReference currentMeetingRef = MeetingRef.child(MeetingDetails.meeting_key);

                    DatabaseReference dataRef = currentMeetingRef.child("Data");

                    Map<String, Object> childUpdates = new HashMap<>();

                    DatabaseReference dataPositionRef = currentMeetingRef.child("Position");
                    dataPositionRef.setValue(i);

                    childUpdates.put("/" + MeetingDetails.meeting_key + "/Data/Data" + i , text);
                    MeetingRef.updateChildren(childUpdates);
                    i++;

                    Toast.makeText(MeetingMain.this, text, Toast.LENGTH_LONG).show();


                }
                break;
            }
        }

        speechToText();
    }


    private void textDisplay() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference MeetingRef = database.getReference("Meeting_List");

        DatabaseReference currentMeetingRef = MeetingRef.child(MeetingDetails.meeting_key);
        DatabaseReference DataRef = currentMeetingRef.child("Data");
        DatabaseReference PositionRef = currentMeetingRef.child("Position");

        currentMeetingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot children : dataSnapshot.getChildren()) {
                    //Log.i("CHILD",children.getKey());
                    if(children.getKey().compareTo("Position") == 0) {
                        MeetingDetails.data_position = Integer.parseInt(children.getValue().toString());
                        //Log.i("INFO", "here1");
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot children : dataSnapshot.getChildren()) {
                    String key = children.getKey();
                    String data = new String();
                    //Log.i("KEY",key);
                    //Log.i("POS",Integer.toString(MeetingDetails.data_position));
                    if(key.compareTo("Data" + MeetingDetails.data_position) == 0) {
                        data = children.getValue().toString();
                        //Log.i("INFO", "here");

                        Toast.makeText(MeetingMain.this, data, Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

}

