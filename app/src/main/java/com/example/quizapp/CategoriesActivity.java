package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Categories");

        //back button - finish activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        List<CategoryModel> list = new ArrayList<>();
        list.add(new CategoryModel("","Category 1"));
        list.add(new CategoryModel("","Category 2"));
        list.add(new CategoryModel("","Category 3"));
        list.add(new CategoryModel("","Category 4"));
        list.add(new CategoryModel("","Category 5"));
        list.add(new CategoryModel("","Category 6"));
        list.add(new CategoryModel("","Category 7"));
        list.add(new CategoryModel("","Category 8"));
        list.add(new CategoryModel("","Category 9"));
        list.add(new CategoryModel("","Category 10"));
        list.add(new CategoryModel("","Category 11"));
        list.add(new CategoryModel("","Category 12"));
        list.add(new CategoryModel("","Category 13"));
        list.add(new CategoryModel("","Category 14"));
        list.add(new CategoryModel("","Category 15"));
        list.add(new CategoryModel("","Category 16"));
        list.add(new CategoryModel("","Category 17"));
        list.add(new CategoryModel("","Category 18"));


        CategoryAdapter categoryAdapter = new CategoryAdapter(list);
        recyclerView.setAdapter(categoryAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}