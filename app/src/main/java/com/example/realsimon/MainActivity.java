package com.example.realsimon;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startGame(View view) {
        Intent intent = new Intent(view.getContext(), Game.class);
        EditText sequence = findViewById(R.id.input_sequence);
        int seq = Integer.parseInt(sequence.getText().toString());
        intent.putExtra("sequence", seq);
        startActivityForResult(intent, 0);
    }
}