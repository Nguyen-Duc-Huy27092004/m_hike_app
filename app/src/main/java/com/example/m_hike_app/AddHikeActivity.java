package com.example.m_hike_app;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.m_hike_app.data.DatabaseHelper;
import com.example.m_hike_app.data.Hike;

import java.util.Calendar;
import java.util.Locale;

public class AddHikeActivity extends AppCompatActivity {

    EditText etName, etLocation, etDate, etLength, etDifficulty, etDescription;
    Switch swParking;
    Button btnSave;
    DatabaseHelper db;

    boolean isEdit = false;
    int hikeId = -1;
    Hike existingHike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hike);

        db = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etLocation = findViewById(R.id.etLocation);
        etDate = findViewById(R.id.etDate);
        etLength = findViewById(R.id.etLength);
        etDifficulty = findViewById(R.id.etDifficulty);
        etDescription = findViewById(R.id.etDescription);
        swParking = findViewById(R.id.swParking);
        btnSave = findViewById(R.id.btnSave);

        if (getIntent().hasExtra("hike_id")) {
            isEdit = true;
            hikeId = getIntent().getIntExtra("hike_id", -1);
            existingHike = db.getHikeById(hikeId);
            if (existingHike != null) loadExistingData();
            btnSave.setText("Update Hike");
            setTitle("Edit Hike");
        } else {
            btnSave.setText("Save Hike");
            setTitle("Add Hike");
        }

        etDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(AddHikeActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        int m = month + 1;
                        String s = String.format(Locale.US, "%04d-%02d-%02d", year, m, dayOfMonth);
                        etDate.setText(s);
                    },
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnSave.setOnClickListener(v -> {
            if (isEdit) updateHike();
            else saveNewHike();
        });
    }

    private void loadExistingData() {
        etName.setText(existingHike.getName());
        etLocation.setText(existingHike.getLocation());
        etDate.setText(existingHike.getDate());
        etLength.setText(String.valueOf(existingHike.getLength()));
        etDifficulty.setText(existingHike.getDifficulty());
        etDescription.setText(existingHike.getDescription());
        swParking.setChecked("Yes".equals(existingHike.getParking()));
    }

    private void saveNewHike() {
        String name = etName.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String lengthStr = etLength.getText().toString().trim();
        String difficulty = etDifficulty.getText().toString().trim();

        if (name.isEmpty() || location.isEmpty() || date.isEmpty() || lengthStr.isEmpty() || difficulty.isEmpty()) {
            Toast.makeText(AddHikeActivity.this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double length;
        try {
            length = Double.parseDouble(lengthStr);
        } catch (NumberFormatException e) {
            length = 0.0;
        }

        String parking = swParking.isChecked() ? "Yes" : "No";
        String description = etDescription.getText().toString().trim();

        long id = db.insertHike(name, location, date, parking, length, difficulty, description);
        if (id > 0) {
            Toast.makeText(AddHikeActivity.this, "Hike saved", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(AddHikeActivity.this, "Save failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateHike() {
        existingHike.setName(etName.getText().toString().trim());
        existingHike.setLocation(etLocation.getText().toString().trim());
        existingHike.setDate(etDate.getText().toString().trim());
        try {
            existingHike.setLength(Double.parseDouble(etLength.getText().toString().trim()));
        } catch (NumberFormatException e) {
            existingHike.setLength(0.0);
        }
        existingHike.setDifficulty(etDifficulty.getText().toString().trim());
        existingHike.setDescription(etDescription.getText().toString().trim());
        existingHike.setParking(swParking.isChecked() ? "Yes" : "No");

        int rows = db.updateHike(existingHike);
        if (rows > 0) {
            Toast.makeText(this, "Hike updated", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }
}
