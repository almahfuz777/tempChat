package com.example.tempchat;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private String userType;
    EditText roomIDtextView, roomPassTextView;
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

         roomIDtextView = findViewById(R.id.roomID);
         roomPassTextView = findViewById(R.id.roomPass);
         submitBtn = findViewById(R.id.submitBtn);
         joinBtn = findViewById(R.id.selectJoinChat);
         createBtn = findViewById(R.id.selectCreateChat);
         backBtn = findViewById(R.id.backBtn);

    }

    public void selectJoinChat(View view) {
        userType = "join";

        roomIDtextView.setVisibility(View.VISIBLE);
        roomPassTextView.setVisibility(View.VISIBLE);
        submitBtn.setVisibility(View.VISIBLE);
        submitBtn.setVisibility(View.VISIBLE);
        backBtn.setVisibility(View.VISIBLE);

        //
        joinBtn.setVisibility(View.GONE);
        createBtn.setVisibility(View.GONE);

        roomIDtextView.setClickable(true);
        roomIDtextView.setInputType(InputType.TYPE_CLASS_TEXT);

        roomIDtextView.setHint("Enter Room ID");
        // append the room id;
        roomPassTextView.setHint("Enter Password");
        submitBtn.setText("JOIN ROOM");

    }

    public void selectCreateChat(View view) {
        userType = "create";

        roomIDtextView.setVisibility(View.VISIBLE);
        roomPassTextView.setVisibility(View.VISIBLE);
        submitBtn.setVisibility(View.VISIBLE);
        submitBtn.setVisibility(View.VISIBLE);
        backBtn.setVisibility(View.VISIBLE);

        //
        joinBtn.setVisibility(View.GONE);
        createBtn.setVisibility(View.GONE);

        roomIDtextView.setClickable(false);
        roomIDtextView.setInputType(0);

        //generate room id
        roomIDtextView.setHint("Room ID: ");
        // append the room id;
        roomPassTextView.setHint("Set Password");
        submitBtn.setText("CREATE ROOM");
    }

    public void submit(View view) {
        if(userType.equals("join")){

        }
        else if(userType.equals("create")){

        }
        else{
            //error
        }
    }

    public void backToMainMenu(View view) {
        roomIDtextView.setVisibility(View.GONE);
        roomPassTextView.setVisibility(View.GONE);
        submitBtn.setVisibility(View.GONE);

        //
        joinBtn.setVisibility(View.VISIBLE);
        createBtn.setVisibility(View.VISIBLE);
        backBtn.setVisibility(View.GONE);

    }
}