package com.proj.fab.estudemais;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;


public class CategoryFragment extends Fragment {


    public CategoryFragment() {

    }

    private GridView catView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);
        catView = view.findViewById(R.id.cat_Grid);

       // loadCategories();
        CategoryAdapter adapter = new CategoryAdapter(DbQuery.g_catList);
        catView.setAdapter(adapter);


        return view;
    }


    }

