package com.example.realsimon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Game extends AppCompatActivity {

    List<Button> sequence = new ArrayList<Button>();
    Button[] lights;
    Integer pointer = 0; // Clicks per sequence run-through
    Integer seq_pointer = 0; // Overall how far we are in the sequence
    Integer score = 0; // Score I never implemented

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        int max_sequence = getIntent().getIntExtra("sequence", 4);

        lights = new Button[] {
                findViewById(R.id.button_light_one),
                findViewById(R.id.button_light_two),
                findViewById(R.id.button_light_three),
                findViewById(R.id.button_light_four)
        };

        // Attach click listener to buttons
        for (Button light : lights) {
            light.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Button light = findViewById(view.getId());

                    // Check if clicked light matches pointer
                    if (view.getId() == sequence.get(pointer).getId()) {
                        Log.i("Game/42", "Correctly clicked button");
                        Log.i("Game/44", "Pointer: " + pointer + "\nSeq: " + seq_pointer + "\nSeq_Size: " + sequence.size());

                        if (pointer >= sequence.size() - 1) { // Finished the sequence
                            youWin();
                        } else if (pointer.equals(seq_pointer)) { // Ready for the next flash sequence
                            pointer = 0; // reset the pointer to re-click through
                            score++;
                            seq_pointer++;
                            flashSequence();
                        } else {
                            pointer++;
                        }
                    } else {
                        youLose();
                    }

                    // 1s Timeout to change color back
                    flashButton(light, 1000);
                }
            });
        }

        // Generate the sequence
        int max = 4;
        for (int i = 0; i < max_sequence; i++) {
            int seq = (int) (Math.random() * (max - 1) + 1);
            Log.i("Game/57", "Chosen Seq - " + seq); // For debugging
            Button light = lights[seq - 1]; // Next light to click, -1 for 0 index offset
            sequence.add(light);
        }

        flashSequence();
    }

    private void flashSequence() {
        disableButtons();
        int delay = 1000;
        for (int i = 0; i < seq_pointer+1; i++) { // only show as far as the pointer goes
            Button light = sequence.get(i);
            flashButton(light, delay);
            delay += 1000;
        }

        // Grand delay to wait for ALL the flashes to re-enable buttons
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                enableButtons();
            }
        }, delay);
    }


    private void flashButton(final Button btn, int ms) {
        btn.setBackgroundColor(Color.RED);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                btn.setBackgroundColor(Color.LTGRAY);
            }
        }, ms);
    }

    private void youWin() {
        Log.i("Game", "You win!");
        new AlertDialog.Builder(this)
                .setTitle("You win!")
                .setPositiveButton(R.string.play_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        playAgain();
                    }
                }).show();
    }

    private void youLose() {
        Log.i("Game", "You lose! :(");
        new AlertDialog.Builder(this)
                .setTitle("You lost..")
                .setPositiveButton(R.string.play_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        playAgain();
                    }
                }).show();
    }

    private void playAgain() {
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        startActivityForResult(intent, 0);
    }

    private void enableButtons() {
        for (Button light : lights) {
            light.setEnabled(true);
        }
    }

    private void disableButtons() {
        for (Button light : lights) {
            light.setEnabled(false);
        }
    }
}