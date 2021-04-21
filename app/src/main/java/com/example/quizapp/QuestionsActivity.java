package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {

    private TextView questionsTV, qCountTV;
    private FloatingActionButton bookmarkFAB;
    private LinearLayout optionsContainer;
    private Button shareBtn, nextBtn;

    private int count = 0;
    private int position = 0;
    private int score = 0;
    List<QuestionsModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        questionsTV = findViewById(R.id.question_tv);
        qCountTV = findViewById(R.id.qcount_tv);
        bookmarkFAB = findViewById(R.id.bookmark_fab_btn);
        shareBtn = findViewById(R.id.share_btn);
        nextBtn = findViewById(R.id.next_btn);
        optionsContainer = findViewById(R.id.options_container);

        list = new ArrayList<>();
        list.add(new QuestionsModel("Question 1","a","b","c","d","a"));
        list.add(new QuestionsModel("Question 2","a","b","c","d","b"));
        list.add(new QuestionsModel("Question 3","a","b","c","d","c"));
        list.add(new QuestionsModel("Question 4","a","b","c","d","d"));

        //onClickListerner for all option buttons
        for(int i = 0; i< 4; i++)
        {
            optionsContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswer((Button) v);
                }
            });
        }

        //Setting 1st question

        playAnimation(questionsTV,0,list.get(position).getQuestion());

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextBtn.setEnabled(false);
                nextBtn.setAlpha(0.7f);
                enableOptions(true);
                position++;

                if(position == list.size()){
                    //ScoreActivity
                    return;
                }

                count = 0;
                playAnimation(questionsTV,0,list.get(position).getQuestion());
            }
        });

    }


    private void playAnimation(View view, int value,final String data){

        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if(value == 0 && count < 4){
                    String selectedOption = "";

                    if(count == 0){
                        selectedOption = list.get(position).getOptionA();

                    }else if(count == 1){
                        selectedOption = list.get(position).getOptionB();

                    }else if(count == 2){
                        selectedOption = list.get(position).getOptionC();

                    }else if(count == 3){
                        selectedOption = list.get(position).getOptionD();

                    }
                    playAnimation(optionsContainer.getChildAt(count),0,selectedOption); // value 0 => view is invisible
                    count++;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //changing the data (questions & options)
                if(value == 0){
                    try{
                        ((TextView)view).setText(data);
                        qCountTV.setText(position+1+"/"+list.size());
                    }catch (ClassCastException e){
                        ((Button)view).setText(data);
                    }
                    view.setTag(data);
                    playAnimation(view,1,data); //value 1 => view is visible
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void checkAnswer(Button selOption){
        enableOptions(false);
        //enabling NEXT Button
        nextBtn.setEnabled(true);
        nextBtn.setAlpha(1);

        if(selOption.getText().toString().equals(list.get(position).getCorrectAns())){
            //Correct Ans
            selOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
            score++;
        }else{
            //Incorrect Ans
            selOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));
            Button correctOption = (Button) optionsContainer.findViewWithTag(list.get(position).getCorrectAns());
            correctOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }
    }

    private void enableOptions(boolean enable){
        for(int i = 0; i< 4; i++)
        {
            optionsContainer.getChildAt(i).setEnabled(enable);
            if(enable){
                optionsContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));
            }
        }
    }

}