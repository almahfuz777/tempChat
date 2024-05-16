package com.example.tempchat;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
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
    }

    public void selectJoinChat(View view) {
        // load activity
        Intent intent = new Intent(MainActivity.this, JoinChatRoomActivity.class );
        startActivity(intent);
    }

    public void selectCreateChat(View view) {
        // load activity
        Intent intent = new Intent(MainActivity.this, CreateChatRoomActivity.class );
        startActivity(intent);
    }

}