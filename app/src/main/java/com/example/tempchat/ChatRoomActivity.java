package com.example.tempchat;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ChatRoomActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference messagesRef;
    private String name, roomID;
    private EditText messageEditText;
    private Button sendButton;
    private RecyclerView messagesRecyclerView;
    private MessagesAdapter messagesAdapter;
    private List<Message> messagesList;
    private TextView titleEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chatroom);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        titleEditText = findViewById(R.id.title);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);

        // set Room ID
        roomID = getIntent().getStringExtra("ROOM_ID");
        titleEditText.setText("Room ID: "+roomID);

        // access to the messages
        messagesRef = db.collection("rooms").document(roomID).collection("messages");


        // set up recycler
        messagesList = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(messagesList);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesRecyclerView.setAdapter(messagesAdapter);

        // send btn
        sendButton.setOnClickListener(v -> sendMessage());

        // Listener for new messages
        messagesRef.orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("ChatRoom", "Listen failed.", e);
                    return;
                }

                List<Message> newMessages = new ArrayList<>();
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    Message message = doc.toObject(Message.class);
                    newMessages.add(message);
                }
                messagesAdapter.updateMessages(newMessages); // Update messages using the new method
                if(!messagesList.isEmpty())
                    messagesRecyclerView.smoothScrollToPosition(messagesList.size()-1);
            }
        });

    }

    private void sendMessage(){
        String messageText = messageEditText.getText().toString();
        if(!messageText.isEmpty()){
            name = getIntent().getStringExtra("USER");
            String username = name;
            long timestamp = System.currentTimeMillis();

            Message message = new Message(username, messageText, timestamp);
            messagesRef.add(message)
                    .addOnSuccessListener(documentReference -> {
                        Log.d("ChatRoom", "Message sent");
                        messageEditText.setText("");
                    })
                    .addOnFailureListener(e -> Log.w("ChatRoom", "Error sending message", e));
        }
    }
}