package com.proj.fab.estudemais;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class QuestionsActivity extends AppCompatActivity {
    private RecyclerView questionsView;
    private TextView tvQuesID,timerTV,catNameTV;
    private Button submitB, markB,clearSelB;
    private ImageButton prevQuesB, nextQuesB;
    private ImageView quesListB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        init();

        QuestionsAdapter quesAdapter = new QuestionsAdapter(DbQuery.g_quesList);
        questionsView.setAdapter(quesAdapter);

    }
    public void init()
    {
        questionsView=findViewById(R.id.questions_view);
        tvQuesID=findViewById(R.id.tv_quesID);
        timerTV=findViewById(R.id.tv_timer);
        catNameTV=findViewById(R.id.qa_catName);
        submitB=findViewById(R.id.submitB);
        markB=findViewById(R.id.markB);
        clearSelB=findViewById(R.id.clear_selB);
        prevQuesB=findViewById(R.id.prev_quesB);
        nextQuesB=findViewById(R.id.next_quesB);
        quesListB=findViewById(R.id.ques_list_gridB);

    }

}