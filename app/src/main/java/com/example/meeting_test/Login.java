package com.example.meeting_test;

import android.content.Intent;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button join = findViewById(R.id.join_button);
        FirebaseApp.initializeApp(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText name1 = (EditText) findViewById(R.id.person_name);
                String name2 = name1.getText().toString();

                EditText meeting_id1 = (EditText) findViewById(R.id.meeting_id);
                String meeting_id2 = meeting_id1.getText().toString();
                int validate[] = authenticate(name2, meeting_id2);

                try {
                    Thread.sleep(10000);
                }
                catch(Exception e) { }
                if(validate[0] == 1 && validate[1] == 1) {
                    Intent intent2 = new Intent(Login.this, MeetingMain.class);
                    Login.this.startActivity(intent2);
                    Login.this.finish();
                }
            }
        });
    }

    public int[] authenticate(final String name, String id) {
        final String temp = new String(id);
        final String temp2 = new String(name);
        final int login[] = new int[2];
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference MeetingRef = database.getReference("Meeting_List");

        MeetingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int logged_in = 0;
                int valid_name = 0;
                for(DataSnapshot children : dataSnapshot.getChildren()) {

                    String owner = children.child("Owner").getValue().toString();
                    MeetingDetails.owner = owner;
                    if(owner.compareTo(temp2) == 0) {
                        valid_name = 0;
                        Toast.makeText(Login.this, "NAME INVALID", Toast.LENGTH_LONG).show();
                        break;
                    }
                    else valid_name = 1;

                    String new_id = children.child("ID").getValue().toString();
                    if(new_id.compareTo(temp) == 0) {
                        // Toast.makeText(Login.this, "Logged IN", Toast.LENGTH_SHORT).show();
                        String key = children.getKey();
                        DatabaseReference Users_List_Ref = MeetingRef.child(key + "/Users");
                        String key2 = Users_List_Ref.push().getKey();

                        Map<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("/" + key2 + "/name", temp2);
                        Users_List_Ref.updateChildren(childUpdates);

                        MeetingDetails.meeting_id = new_id;
                        MeetingDetails.meeting_key = key;
                        MeetingDetails.name = name;

                        MeetingDetails.user_lists = new ArrayList<String>();
                        MeetingDetails.user_keys = new ArrayList<String>();

                        MeetingDetails.user_lists.add(name);
                        MeetingDetails.user_keys.add(key2);

                        MeetingDetails.logged_in = true;

                        Toast.makeText(Login.this, "LOGGED IN", Toast.LENGTH_LONG).show();
                        logged_in = 1;
                        break;

                    }
                }
                if(logged_in == 0) {
                    Toast.makeText(Login.this, "LOGIN FAILED", Toast.LENGTH_LONG).show();
                    login[0] = 0;
                }
                else {
                    login[0] = 1;
                    Log.i("INFO","HERE1");
                }
                if(valid_name == 0) {
                    login[1] = 0;

                }
                else {
                    login[1] = 1;
                    Log.i("INFO","HERE2");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        /*if(login[1] == 1) {
            Log.i("INFO", "HERE3");
        }*/
        Log.i("INFO", "HERE3");
        return login;

    }

}
