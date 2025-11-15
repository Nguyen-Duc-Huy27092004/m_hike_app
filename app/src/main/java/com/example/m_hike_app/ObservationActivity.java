package com.example.m_hike_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.m_hike_app.data.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ObservationActivity extends AppCompatActivity {

    EditText etObservation, etComment;
    Button btnSaveObservation;
    DatabaseHelper db;
    int hikeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation);

        etObservation = findViewById(R.id.etObservation);
        etComment = findViewById(R.id.etComment);
        btnSaveObservation = findViewById(R.id.btnSaveObservation);
        db = new DatabaseHelper(this);
        hikeId = getIntent().getIntExtra("hike_id", -1);

        btnSaveObservation.setOnClickListener(v -> {
            String obs = etObservation.getText().toString().trim();
            String comment = etComment.getText().toString().trim();
            if (obs.isEmpty()) {
                Toast.makeText(ObservationActivity.this, "Observation required", Toast.LENGTH_SHORT).show();
                return;
            }
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
            long id = db.insertObservation(hikeId, obs, time, comment);
            if (id > 0) {
                Toast.makeText(ObservationActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ObservationActivity.this, "Save failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
