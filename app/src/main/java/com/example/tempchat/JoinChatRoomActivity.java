package com.example.tempchat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class JoinChatRoomActivity extends AppCompatActivity {
    EditText roomIDtextView, roomPassTextView, usernameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_join_chatroom);
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
    }

    public void submit(View view){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String roomID = roomIDtextView.getText().toString();
        String passInput = roomPassTextView.getText().toString();
        String guestName = usernameTextView.getText().toString();

        if(roomID.isEmpty()||passInput.isEmpty()||guestName.isEmpty()){
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
        }
        else{
            // checking if the room exists
            DocumentReference docRef = db.collection("rooms").document(roomID);
            docRef.get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            // room found
                            if (document.exists()) {
                                String roomPass = document.getString("roomPass");   // roomPass

                                // password verification
                                if(roomPass != null && roomPass.equals(passInput)){
                                    Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(JoinChatRoomActivity.this, ChatRoomActivity.class );
                                    startActivity(intent);
                                }
                                // invalid password
                                else{
                                    Toast.makeText(this, "Invalid Pass", Toast.LENGTH_SHORT).show();
                                }

                            }
                            // room not found
                            else {
                                // Document doesn't exist
                                Log.d("not found", "Document "+roomID+" does not exist in the collection 'rooms'");
                                Toast.makeText(this, "Room not Found", Toast.LENGTH_SHORT).show();
                            }
                        }
                        // Error occurred while fetching document
                        else {
                            Log.d("TAG", "Error getting document: ", task.getException());
                        }
                    });
        }


    }
    public void backToMainMenu(View view) {
        Intent intent = new Intent(JoinChatRoomActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
