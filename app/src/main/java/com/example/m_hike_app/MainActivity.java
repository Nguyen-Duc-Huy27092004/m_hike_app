package com.example.m_hike_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_hike_app.adapters.HikeAdapter;
import com.example.m_hike_app.data.DatabaseHelper;
import com.example.m_hike_app.data.Hike;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    HikeAdapter adapter;
    List<Hike> hikes = new ArrayList<>();
    DatabaseHelper db;
    MaterialButton btnReload;
    FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerView);
        btnReload = findViewById(R.id.btnReload);
        btnAdd = findViewById(R.id.btnAdd);

        adapter = new HikeAdapter(hikes, hike -> {
            Intent i = new Intent(MainActivity.this, HikeDetailActivity.class);
            i.putExtra("hike_id", hike.getId());
            startActivity(i);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AddHikeActivity.class))
        );

        btnReload.setOnClickListener(v -> loadData());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        hikes.clear();
        List<Hike> list = db.getAllHikes();
        hikes.addAll(list);
        adapter.notifyDataSetChanged();
    }
}
