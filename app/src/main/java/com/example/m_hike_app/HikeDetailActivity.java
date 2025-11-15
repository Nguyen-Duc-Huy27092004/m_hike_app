package com.example.m_hike_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.m_hike_app.adapters.ObservationAdapter;
import com.example.m_hike_app.data.DatabaseHelper;
import com.example.m_hike_app.data.Hike;
import com.example.m_hike_app.data.Observation;

import java.util.ArrayList;
import java.util.List;

public class HikeDetailActivity extends AppCompatActivity {

    TextView tvName, tvLocation, tvDate, tvParking, tvLength, tvDifficulty, tvDescription;
    Button btnAddObservation, btnEdit, btnDelete;
    RecyclerView recyclerObservations;
    ObservationAdapter obsAdapter;
    List<Observation> observations = new ArrayList<>();
    DatabaseHelper db;
    int hikeId;
    Hike hike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_detail);

        db = new DatabaseHelper(this);
        hikeId = getIntent().getIntExtra("hike_id", -1);

        tvName = findViewById(R.id.tvName);
        tvLocation = findViewById(R.id.tvLocation);
        tvDate = findViewById(R.id.tvDate);
        tvParking = findViewById(R.id.tvParking);
        tvLength = findViewById(R.id.tvLength);
        tvDifficulty = findViewById(R.id.tvDifficulty);
        tvDescription = findViewById(R.id.tvDescription);
        btnAddObservation = findViewById(R.id.btnAddObservation);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        recyclerObservations = findViewById(R.id.recyclerObservations);

        obsAdapter = new ObservationAdapter(observations, new ObservationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Observation observation) {
                // could implement edit/delete per observation
            }
        });
        recyclerObservations.setLayoutManager(new LinearLayoutManager(this));
        recyclerObservations.setAdapter(obsAdapter);

        btnAddObservation.setOnClickListener(v -> {
            Intent i = new Intent(HikeDetailActivity.this, ObservationActivity.class);
            i.putExtra("hike_id", hikeId);
            startActivity(i);
        });

        btnEdit.setOnClickListener(v -> {
            Toast.makeText(HikeDetailActivity.this, "Edit functionality can be added (open AddHike in edit mode).", Toast.LENGTH_SHORT).show();
        });

        btnDelete.setOnClickListener(v -> new AlertDialog.Builder(HikeDetailActivity.this)
                .setTitle("Delete Hike")
                .setMessage("Delete this hike?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    db.deleteHike(hikeId);
                    Toast.makeText(HikeDetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("No", null)
                .show());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHike();
        loadObservations();
    }

    private void loadHike() {
        hike = db.getHikeById(hikeId);
        if (hike != null) {
            tvName.setText(hike.getName());
            tvLocation.setText(hike.getLocation());
            tvDate.setText(hike.getDate());
            tvParking.setText(hike.getParking());
            tvLength.setText(String.format("%s km", hike.getLength()));
            tvDifficulty.setText(hike.getDifficulty());
            tvDescription.setText(hike.getDescription());
        }
    }

    private void loadObservations() {
        observations.clear();
        observations.addAll(db.getObservationsForHike(hikeId));
        obsAdapter.notifyDataSetChanged();
    }
}
