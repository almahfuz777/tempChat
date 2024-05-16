package com.example.tempchat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CreateChatRoomActivity extends AppCompatActivity {
    private String roomID = "-1";
    private EditText roomIDtextView, roomPassTextView, usernameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_chatroom);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Initialize Firebase
        FirebaseApp.initializeApp(this);

        // find views by id
        roomIDtextView = findViewById(R.id.roomID);
        roomPassTextView = findViewById(R.id.roomPass);
        usernameTextView = findViewById(R.id.username);

        roomIDtextView.setHint("Room ID: " + generateRoomID());

    }

    private String generateRoomID() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // generate random id between (1000,9999)
        int rand = new Random().nextInt(9000) + 1000;
        String x = String.valueOf(rand);

        DocumentReference docRef = db.collection("rooms").document(x);
        docRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Document exists
                            Log.d("found", "Document "+ x +" exists in the collection 'rooms'");
                            generateRoomID();
                        } else {
                            // Document doesn't exist
                            Log.d("not found", "Document "+ x + " does not exist in the collection 'rooms'");
                            roomID = x;
                        }
                    } else {
                        // Error occurred while fetching document
                        Log.d("TAG", "Error getting document: ", task.getException());
                    }
                });

        return x;
    }

    public void submit(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String host = usernameTextView.getText().toString();
        String roomPass = roomPassTextView.getText().toString();

        if (host.isEmpty() || roomPass.isEmpty()) {
            Toast.makeText(this, "Enter a username and password", Toast.LENGTH_SHORT).show();
        }
        else {
            // create map
            Map<String, Object> roomData = new HashMap<>();
            roomData.put("roomID", roomID);
            roomData.put("hostName", host);
            roomData.put("roomPass", roomPass);

            // adding to database
            db.collection("rooms").document(String.valueOf(roomID)).set(roomData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("TAG", "Data added successfully!");
                            Toast.makeText(CreateChatRoomActivity.this, "Chat Room Created", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CreateChatRoomActivity.this, ChatRoomActivity.class);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error adding data", e);
                        }
                    });
        }

    }

    public void backToMainMenu(View view) {
        Intent intent = new Intent(CreateChatRoomActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
