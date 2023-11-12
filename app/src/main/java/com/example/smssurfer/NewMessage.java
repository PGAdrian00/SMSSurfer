package com.example.smssurfer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.Manifest;
import android.widget.Toast;

public class NewMessage extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_SMS = 1;
    private EditText recipientEditText;
    private EditText messageEditText;
    private Button sendButton;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        dbManager = DBManagerSingleton.getDBManager(this);

        recipientEditText = findViewById(R.id.recipientEditText);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recipientPhoneNumber = recipientEditText.getText().toString();
                String messageText = messageEditText.getText().toString();
                sendSmsMessage(recipientPhoneNumber, messageText);
            }
        });
    }

    private void sendSmsMessage(String recipientPhoneNumber, String messageText) {
        // Check if SMS permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // If not, request the permission
            askForPermission();
        } else {
            Toast.makeText(NewMessage.this, "Permission granted!", Toast.LENGTH_SHORT).show();
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(recipientPhoneNumber, null, messageText, null, null);
            addSmsToDb(recipientPhoneNumber, messageText);
        }
    }

    private void askForPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_SMS);
    }

    // Callback for permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, send the SMS

                sendSmsMessage(recipientEditText.getText().toString(), messageEditText.getText().toString());
            } else {
                // Permission denied, inform the user
                Toast.makeText(NewMessage.this, "Permission to send denied!", Toast.LENGTH_SHORT).show();
                // Handle the case where the user denies the permission
            }
        }
    }

    private void addSmsToDb(String recipientPhoneNumber, String messageText) {
        long timestamp = System.currentTimeMillis();
        SimpleDateFormat sdfShort = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat sdfLong = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String shortDate = sdfShort.format(new Date(timestamp));
        String longDate = sdfLong.format(new Date(timestamp));

        int messageType = 2;

        long result = dbManager.addNewMessage(recipientPhoneNumber, messageText, messageType, String.valueOf(timestamp), shortDate, longDate);

        if (result == -1){
            Toast.makeText(NewMessage.this, "Add failed", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(NewMessage.this, "Add succeded", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(NewMessage.this, ListSMS.class);
        startActivity(intent);
        finish();
    }
}
