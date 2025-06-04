package com.yamatoapps.travelplanner;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddTravelPlan extends AppCompatActivity {

    Date departure_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel_plan);
        Button btnSelectDate = findViewById(R.id.btnSelectDate);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);
        final Calendar c = Calendar.getInstance();
        TextView tvDestination,tvActivity;
        tvDestination = findViewById(R.id.textInputDestination);
        tvActivity = findViewById(R.id.textInputActivity);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
            if (this.getIntent().getStringExtra("editing") != null){
                db.collection("travel_plans").document(getIntent().getStringExtra("docID")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                tvActivity.setText(task.getResult().get("activity").toString());
                tvDestination.setText(task.getResult().get("destination").toString());

                    }
                });
            }

        btnSave.setOnClickListener(view -> {

            if (this.getIntent().getStringExtra("editing") == null)
            {
                departure_date = c.getTime();
                Map<String, Object> booking = new HashMap<>();
                booking.put("destination",tvDestination.getText().toString());
                booking.put("departure_date", departure_date);
                booking.put("activity", tvActivity.getText().toString());
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Saving your travel");
                progressDialog.setMessage("Saving your travel...");
                progressDialog.show();
                db.collection("travel_plans")
                        .add(booking)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Congratulations! Your travel plan is set!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            }
            else {
                departure_date = c.getTime();
                Map<String, Object> booking = new HashMap<>();
                booking.put("destination",tvDestination.getText().toString());
                booking.put("departure_date", departure_date);
                booking.put("activity", tvActivity.getText().toString());
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Saving your travel");
                progressDialog.setMessage("Saving your travel...");
                progressDialog.show();
                db.collection("travel_plans").document(getIntent().getStringExtra("docID"))
                        .set(booking)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Congratulations! Your travel plan is updated!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            }

        });
        btnCancel.setOnClickListener(view -> {
        finish();
        });
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are getting
                // the instance of our calendar.

                // on below line we are getting
                // our day, month and year.
                int month1,date1,year1;
                month1 = c.get(Calendar.MONTH);
                date1 = c.get(Calendar.DAY_OF_MONTH);
                year1 = c.get(Calendar.YEAR);
                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        AddTravelPlan.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                c.set(Calendar.DATE,dayOfMonth);
                                c.set(Calendar.MONTH,monthOfYear);
                                c.set(Calendar.YEAR,year);
                                TimePickerDialog timePickerDialog = new TimePickerDialog(
                                        // on below line we are passing context.
                                        AddTravelPlan.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                                c.set(Calendar.HOUR_OF_DAY,i);
                                                c.set(Calendar.MINUTE,i1);
                                                c.set(Calendar.SECOND,0);
                                                c.set(Calendar.MILLISECOND,0);
                                                btnSelectDate.setText(c.getTime().toLocaleString());
                                            }
                                        },0,0,true);
                                // at last we are calling show to
                                // display our date picker dialog.
                                timePickerDialog.show();
                            }
                        },year1,month1,date1);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });

    }
}