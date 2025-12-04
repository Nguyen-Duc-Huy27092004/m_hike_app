package com.example.m_hike_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

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
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerView);
        btnReload = findViewById(R.id.btnReload);
        btnAdd = findViewById(R.id.btnAdd);
        searchView = findViewById(R.id.searchView);

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

        // ⭐ THÊM LISTENER CHO SEARCHVIEW TẠI ĐÚNG VỊ TRÍ ⭐
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        hikes.clear();
        hikes.addAll(db.getAllHikes());
        adapter.notifyDataSetChanged();
    }

    private void performSearch(String keyword) {
        if (keyword.trim().isEmpty()) {
            loadData();
            return;
        }

        hikes.clear();
        hikes.addAll(db.searchHikes(keyword));
        adapter.notifyDataSetChanged();
    }
}
