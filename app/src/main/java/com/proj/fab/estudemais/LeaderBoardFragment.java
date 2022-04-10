package com.proj.fab.estudemais;

import static com.proj.fab.estudemais.DbQuery.g_usersCount;
import static com.proj.fab.estudemais.DbQuery.g_usersList;
import static com.proj.fab.estudemais.DbQuery.myPerformace;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.proj.fab.estudemais.Adapters.RankAdapter;
import com.proj.fab.estudemais.DbQuery;


public class LeaderBoardFragment extends Fragment {

 private TextView totalUsersTV, myImgTextTV,myScoreTV,myRankTV;
 private RecyclerView usersView;
 private RankAdapter adapter;
 private Dialog progressDialog;
 private TextView dialogText;


        public LeaderBoardFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_leader_board, container, false);

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Classificação");


        initViews(view);

        progressDialog=new Dialog(getContext());
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText=progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText(" Carregando...");
        progressDialog.show();


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        usersView.setLayoutManager(layoutManager);

        adapter = new RankAdapter(DbQuery.g_usersList);

        usersView.setAdapter(adapter);

        DbQuery.getTopUsers(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                adapter.notifyDataSetChanged();

                if (DbQuery.myPerformace.getScore() !=0)
                {
                    if (! DbQuery.isMeOnTopList)
                    {
                        calculateRank();
                    }
                    myScoreTV.setText("Pontuação: "+ DbQuery.myPerformace.getScore());
                    myRankTV.setText("Classificação - "+DbQuery.myPerformace.getRank() );
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                Toast.makeText(getContext(),"Something went wrong! Please try again",Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();

            }
        });


        totalUsersTV.setText("Total de usuários: "+g_usersCount);
        myImgTextTV.setText(myPerformace.getName().toUpperCase().substring(0,1))    ;

        return view;

    }


    private void initViews(View view)
    {
        totalUsersTV=view.findViewById(R.id.total_users);
        myImgTextTV=view.findViewById(R.id.img_text);
        myScoreTV=view.findViewById(R.id.total_score);
        myRankTV=view.findViewById(R.id.rank);
        usersView=view.findViewById(R.id.users_view);

    }

    private void calculateRank()
    {
        int lowTopScore = g_usersList.get(g_usersList.size()-1).getScore();
        int remaining_slots = g_usersCount -20 ;
        int myslot = (myPerformace.getScore()*remaining_slots)/lowTopScore;
        int rank;

        if (lowTopScore != myPerformace.getScore())
        {
            rank = g_usersCount -myslot;
        }
        else
        {
            rank=21;
        }

        myPerformace.setRank(rank);
    }
}