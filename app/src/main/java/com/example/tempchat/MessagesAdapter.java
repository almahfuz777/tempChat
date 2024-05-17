package com.example.tempchat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {
    private List<Message> messagesList;

    public MessagesAdapter(List<Message> messagesList) {
        this.messagesList = messagesList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messagesList.get(position);
        holder.usernameTextView.setText(message.getUsername());
        holder.messageTextView.setText(message.getMessage());

        // Format timestamp to a readable date and time format
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm dd,MMM,yyyy", Locale.getDefault());
        String formattedTimestamp = sdf.format(new Date(message.getTimestamp()));
        holder.timestampTextView.setText(formattedTimestamp);
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    // Method to update the message list and notify the adapter of changes
    public void updateMessages(List<Message> newMessages) {
        messagesList.clear();
        messagesList.addAll(newMessages);
        notifyDataSetChanged();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView usernameTextView;
        public TextView messageTextView;
        public TextView timestampTextView; // Add timestamp TextView

        public MessageViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView); // Initialize timestamp TextView
        }
    }
}
