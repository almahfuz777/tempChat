package com.example.tempchat;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private String userType;
    private String roomID = "-1";
    EditText roomIDtextView, roomPassTextView, usernameTextView;
    Button submitBtn, joinBtn, createBtn, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
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
         submitBtn = findViewById(R.id.submitBtn);
         joinBtn = findViewById(R.id.selectJoinChat);
         createBtn = findViewById(R.id.selectCreateChat);
         backBtn = findViewById(R.id.backBtn);

    }

    public void selectJoinChat(View view) {
        userType = "join";

        updateVisibility();

        roomIDtextView.setClickable(true);
        roomIDtextView.setInputType(InputType.TYPE_CLASS_TEXT);

        roomIDtextView.setHint("Enter Room ID");
        roomPassTextView.setHint("Enter Password");
        submitBtn.setText("JOIN ROOM");

    }

    public void selectCreateChat(View view) {
        userType = "create";

        updateVisibility();

        roomIDtextView.setClickable(false);
        roomIDtextView.setInputType(0);

        roomIDtextView.setHint("Room ID: " + generateRoomID());
        roomPassTextView.setHint("Set Password");
        submitBtn.setText("CREATE ROOM");
    }
    private void updateVisibility(){
        roomIDtextView.setVisibility(View.VISIBLE);
        roomPassTextView.setVisibility(View.VISIBLE);
        usernameTextView.setVisibility(View.VISIBLE);
        submitBtn.setVisibility(View.VISIBLE);
        submitBtn.setVisibility(View.VISIBLE);
        backBtn.setVisibility(View.VISIBLE);

        //
        joinBtn.setVisibility(View.GONE);
        createBtn.setVisibility(View.GONE);

        //
        roomIDtextView.setText("");
        roomPassTextView.setText("");
        usernameTextView.setText("");
    }

    private String generateRoomID() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

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
    private void createRoom(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String username = usernameTextView.getText().toString();
        String roomPass = roomPassTextView.getText().toString();

        Map<String, Object> roomData = new HashMap<>();
        roomData.put("roomID", roomID);
        roomData.put("username", username);
        roomData.put("password", roomPass);

        db.collection("rooms").document(String.valueOf(roomID)).set(roomData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "Data added successfully!");

                        Intent intent = new Intent(MainActivity.this, ChatRoomActivity.class );
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
    private void joinRoom(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String roomID = roomIDtextView.getText().toString();

        DocumentReference docRef = db.collection("rooms").document(roomID);
        docRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Document exists
                            Log.d("found", "Joined room "+roomID);
                            Intent intent = new Intent(MainActivity.this, ChatRoomActivity.class );
                            startActivity(intent);
                        } else {
                            // Document doesn't exist
                            Log.d("not found", "Document "+roomID+" does not exist in the collection 'rooms'");
                            Toast.makeText(this, "Room not Found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Error occurred while fetching document
                        Log.d("TAG", "Error getting document: ", task.getException());
                    }
                });
    }

    public void submit(View view) {
        if(userType.equals("join")){
            joinRoom();
        }
        else if(userType.equals("create")){
            createRoom();
        }
        else{
            //error
        }
    }

    public void backToMainMenu(View view) {
        roomIDtextView.setVisibility(View.GONE);
        roomPassTextView.setVisibility(View.GONE);
        usernameTextView.setVisibility(View.GONE);
        submitBtn.setVisibility(View.GONE);

        //
        joinBtn.setVisibility(View.VISIBLE);
        createBtn.setVisibility(View.VISIBLE);
        backBtn.setVisibility(View.GONE);

        //
        roomPassTextView.setText("");
        usernameTextView.setText("");

    }
}