package com.example.smssurfer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SMSAdapter extends RecyclerView.Adapter<SMSAdapter.SMSViewHolder> {

    private Cursor cursor;
    private Context context;

    public SMSAdapter(Cursor cursor, Context context) {
        this.cursor = cursor;
        this.context = context;
    }

    @NonNull
    @Override
    public SMSViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_view_message_row, parent, false);
        return new SMSViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SMSViewHolder holder, int position) {
        cursor.moveToPosition(position);
        SMS sms = SMS.fromCursor(cursor);
        holder.bind(sms);
    }

    @Override
    public int getItemCount() {
        if (cursor != null) {
            return cursor.getCount();
        } else {
            return 0;
        }
    }

    class SMSViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeStamp;
        View messageBubble;
        TextView phoneNumber;
        TextView messageType;

        SMSViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            timeStamp = itemView.findViewById(R.id.timeStamp);
            messageBubble = itemView.findViewById(R.id.messageBubble);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            messageType = itemView.findViewById(R.id.messageType);

            phoneNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle opening the Conversation activity here
                    openConversation(phoneNumber.getText().toString());
                }
            });


        }

    private void openConversation(String phoneNumber) {
        // Start the Conversation activity and pass the phone number to it
        Intent intent = new Intent(context, Conversation.class);
        intent.putExtra("phoneNumber", phoneNumber);
        context.startActivity(intent);
    }

        void bind(SMS sms) {
            messageText.setText(sms.getMessage());
            timeStamp.setText(sms.getTime());
            phoneNumber.setText(sms.getPhone());


            if (sms.getType() == 1) {
                // Received message
                messageBubble.setBackgroundResource(R.drawable.message_bubble);
                messageType.setText("Received");
            } else {
                // Sent message
                messageBubble.setBackgroundResource(R.drawable.message_bubble_sent);
                messageType.setText("Sent");
            }
        }
}


}

