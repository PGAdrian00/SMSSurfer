package com.example.smssurfer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ListSMS extends AppCompatActivity {

    //cand apas pe un nr de telefon sa ma duca in conversation si sa imi afiseze cronologic mesajele cu nr ala.

    private RecyclerView recyclerView;
    private SMSAdapter smsAdapter;
    private DBManager dbManager;
    private FloatingActionButton faBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listsms);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        dbManager = DBManagerSingleton.getDBManager(this);
        Cursor cursor = dbManager.getMessages();

        smsAdapter = new SMSAdapter(cursor, ListSMS.this);
        recyclerView.setAdapter(smsAdapter);

        faBtn = findViewById(R.id.floatingActionButton);
        faBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                composeNewMessage();
            }
        });
    }


    private void composeNewMessage() {
        // Handle composing a new message
        Intent intent = new Intent(this, NewMessage.class);
        startActivity(intent);
    }


}