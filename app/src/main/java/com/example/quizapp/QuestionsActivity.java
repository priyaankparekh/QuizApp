package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private TextView questionsTV, qCountTV;
    private FloatingActionButton bookmarkFAB;
    private LinearLayout optionsContainer;
    private Button shareBtn, nextBtn;

    private int count = 0;
    private int position = 0;
    private int score = 0;
    List<QuestionsModel> list;
    private int setNo;
    private  String category;

    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        //laoding dialog
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dailog);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        questionsTV = findViewById(R.id.question_tv);
        qCountTV = findViewById(R.id.qcount_tv);
        bookmarkFAB = findViewById(R.id.bookmark_fab_btn);
        shareBtn = findViewById(R.id.share_btn);
        nextBtn = findViewById(R.id.next_btn);
        optionsContainer = findViewById(R.id.options_container);

        category = getIntent().getStringExtra("category");
        setNo = getIntent().getIntExtra("setNo",1);

        //Setting query to retrieve question
        list = new ArrayList<>();

        loadingDialog.show();

        myRef.child("Sets").child(category).child("Questions").orderByChild("setNo").equalTo(setNo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    list.add(dataSnapshot.getValue(QuestionsModel.class));
                }
                if(list.size() > 0){

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

                    playAnimation(questionsTV,0,list.get(position).getQuestion());

                    //nextBtn onClickListener
                    nextBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            nextBtn.setEnabled(false);
                            nextBtn.setAlpha(0.7f);
                            enableOptions(true);
                            position++;

                            if(position == list.size()){
                                //ScoreActivity

                                Intent scoreIntent = new Intent(QuestionsActivity.this,ScoreActivity.class);
                                scoreIntent.putExtra("score",score);
                                scoreIntent.putExtra("outof",list.size());
                                startActivity(scoreIntent);
                                finish();
                                return;
                            }

                            count = 0;
                            playAnimation(questionsTV,0,list.get(position).getQuestion());
                        }
                    });

                }//if listsize > 0 end
                else{
                    finish();
                    Toast.makeText(QuestionsActivity.this, "No Questions", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuestionsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                finish();
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
            //Marking the incorrect option with red border and highlighting the correct ans
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