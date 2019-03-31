package com.example.meeting_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;



public class CreateMeeting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);

        Button create = (Button) findViewById(R.id.create_button);

        final String meeting_id = generateMeetingID();
        TextView meeting_id1 = findViewById(R.id.display_meeting_id);
        meeting_id1.setText(meeting_id);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText) findViewById(R.id.get_name);
                String name2 = name.getText().toString();
                createMeeting(name2,meeting_id);

                Intent intent2 = new Intent(CreateMeeting.this,MeetingMain.class);
                CreateMeeting.this.startActivity(intent2);
                CreateMeeting.this.finish();

            }
        });

    }

    private String generateMeetingID() {
        String x = new String();
        Random r = new Random();

        for(int i=0 ; i<=4 ; i++) {
            int t = r.nextInt(26) + 97;
            x = x + (char) t;
        }
        return x;
    }

    private void createMeeting(String name, String meeting_id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference MeetingRef = database.getReference("Meeting_List");

        String key = MeetingRef.push().getKey();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + key + "/ID" , meeting_id);
        childUpdates.put("/" + key + "/Owner", name);
        //childUpdates.put("/" + key + "/Users/name", name);

        MeetingRef.updateChildren(childUpdates);

        DatabaseReference Users_List_Ref = MeetingRef.child(key + "/Users");
        String key2 = Users_List_Ref.push().getKey();

        childUpdates = new HashMap<>();
        childUpdates.put("/" + key2 + "/name", name);
        Users_List_Ref.updateChildren(childUpdates);

        MeetingDetails.meeting_id = meeting_id;
        MeetingDetails.name = name;
        MeetingDetails.owner = name;

        MeetingDetails.meeting_key = key;

        MeetingDetails.user_lists = new ArrayList<String>();
        MeetingDetails.user_keys = new ArrayList<String>();

        MeetingDetails.user_lists.add(name);
        MeetingDetails.user_keys.add(key2);
        MeetingDetails.logged_in = true;

    }

}