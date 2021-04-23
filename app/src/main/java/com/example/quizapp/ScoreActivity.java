package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    private TextView scoredTv,outOfTv;
    private Button doneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        scoredTv = findViewById(R.id.scored_tv);
        outOfTv = findViewById(R.id.outof_tv);
        doneBtn = findViewById(R.id.done_btn);

        scoredTv.setText(String.valueOf(getIntent().getIntExtra("score",0 )));
        outOfTv.setText("OUT OF "+String.valueOf(getIntent().getIntExtra("outof",0 )));


        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}