package com.proj.fab.estudemais;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class QuestionsActivity extends AppCompatActivity {
    private RecyclerView questionsView;
    private TextView tvQuesID,timerTV,catNameTV;
    private Button submitB, markB,clearSelB;
    private ImageButton prevQuesB, nextQuesB;
    private ImageView quesListB;
    private int quesID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);


        init();

        QuestionsAdapter quesAdapter = new QuestionsAdapter(DbQuery.g_quesList);
        questionsView.setAdapter(quesAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        questionsView.setLayoutManager(layoutManager);

        setSnapHelper();

        setClickListeners();

        startTimer();

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
        quesID = 0;
        tvQuesID.setText("1/"+String.valueOf(DbQuery.g_quesList.size()));
        catNameTV.setText(DbQuery.g_catList.get(DbQuery.g_selected_cat_index).getName());


    }

    private void setSnapHelper()
    {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(questionsView);

        questionsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());
                quesID= recyclerView.getLayoutManager().getPosition(view);

                tvQuesID.setText(String.valueOf(quesID +1 +"/"+ String.valueOf(DbQuery.g_quesList.size())));

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }
    private void setClickListeners()
    {
        prevQuesB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (quesID >0)
                {
                    questionsView.smoothScrollToPosition(quesID-1);
                }

            }
        });

        nextQuesB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (quesID < DbQuery.g_quesList.size()-1)
                {
                    questionsView.smoothScrollToPosition(quesID+1);

                }


            }
        });


    }
    public void startTimer()
    {
        long totalTime = DbQuery.g_testlist.get(DbQuery.g_selected_test_index).getTime()*60*1000;
        CountDownTimer timer = new CountDownTimer(totalTime + 1000,1000) {
            @Override
            public void onTick(long remainingTime) {

                String time =String.format("%02d:%02d min",
                        TimeUnit.MILLISECONDS.toMinutes(remainingTime),
                        TimeUnit.MILLISECONDS.toSeconds(remainingTime)-
                                TimeUnit.MINUTES.toSeconds( TimeUnit.MILLISECONDS.toMinutes(remainingTime))
                );

                timerTV.setText(time);

            }

            @Override
            public void onFinish() {

            }
        };
        timer.start();
    }

}