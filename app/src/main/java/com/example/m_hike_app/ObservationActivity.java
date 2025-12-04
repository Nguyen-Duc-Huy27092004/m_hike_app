package com.example.m_hike_app;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.m_hike_app.data.DatabaseHelper;
import com.example.m_hike_app.data.Observation;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ObservationActivity extends AppCompatActivity {

    EditText etObsText, etObsDate, etObsComment;
    Button btnSaveObservation;
    DatabaseHelper db;

    int hikeId = -1;
    int obsId = -1;
    boolean isEdit = false;
    Observation existingObs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation);

        db = new DatabaseHelper(this);

        etObsText = findViewById(R.id.etObsText);
        etObsDate = findViewById(R.id.etObsDate);
        etObsComment = findViewById(R.id.etObsComment);
        btnSaveObservation = findViewById(R.id.btnSaveObservation);

        hikeId = getIntent().getIntExtra("hike_id", -1);

        if (getIntent().hasExtra("obs_id")) {
            isEdit = true;
            obsId = getIntent().getIntExtra("obs_id", -1);
            existingObs = db.getObservationById(obsId);
            if (existingObs != null) loadExistingData();
            btnSaveObservation.setText("Update Observation");
            setTitle("Edit Observation");
        } else {
            btnSaveObservation.setText("Save Observation");
            setTitle("Add Observation");

            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
            etObsDate.setText(time);
        }

        etObsDate.setOnClickListener(v -> showDatePicker());

        btnSaveObservation.setOnClickListener(v -> {
            if (isEdit) updateObservation();
            else saveObservation();
        });
    }

    private void loadExistingData() {
        etObsText.setText(existingObs.getText());
        etObsDate.setText(existingObs.getTimestamp());
        etObsComment.setText(existingObs.getComment());
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    int m = month + 1;
                    String formattedDate = String.format(Locale.US, "%04d-%02d-%02d", year, m, dayOfMonth);

                    String prev = etObsDate.getText().toString();
                    String timePart = "";
                    if (prev.contains(" ")) timePart = prev.substring(prev.indexOf(" "));
                    etObsDate.setText(formattedDate + timePart);
                },
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveObservation() {
        String text = etObsText.getText().toString().trim();
        String date = etObsDate.getText().toString().trim();
        String comment = etObsComment.getText().toString().trim();

        if (text.isEmpty()) {
            Toast.makeText(this, "Observation required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (date.isEmpty()) {
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
            date = time;
        }

        long id = db.insertObservation(hikeId, text, date, comment);
        if (id > 0) {
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateObservation() {
        existingObs.setText(etObsText.getText().toString().trim());
        existingObs.setTimestamp(etObsDate.getText().toString().trim());
        existingObs.setComment(etObsComment.getText().toString().trim());

        int rows = db.updateObservation(existingObs);
        if (rows > 0) {
            Toast.makeText(this, "Observation updated", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }
}
