package com.example.smssurfer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;

import java.util.List;

public class Conversation extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SMSAdapter smsAdapter;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        String phoneNumber = getIntent().getStringExtra("phoneNumber");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbManager = new DBManager(this);

        // Query the database to get all the messages related to the phone number
        Cursor cursor = dbManager.getMessagesForPhoneNumber(phoneNumber);
        smsAdapter = new SMSAdapter(cursor, this);
        recyclerView.setAdapter(smsAdapter);
    }
}