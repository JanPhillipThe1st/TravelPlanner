package com.yamatoapps.travelplanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Button btnSeeTravelPlans = findViewById(R.id.btnSetTravelPlan);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        TextView tvUpcomingTravel,tvCurrentTravel,tvPreviousTravel;
        tvUpcomingTravel = findViewById(R.id.tvUpcomingTravel);
        tvPreviousTravel = findViewById(R.id.tvPreviousTravel);
        tvCurrentTravel = findViewById(R.id.tvCurrentTravel);
        Button btnViewUpcomingTravels, btnViewCurrentTravels, btnViewPreviousTravel;
        btnViewUpcomingTravels=findViewById(R.id.btnViewUpcomingTravels);
        btnViewCurrentTravels=findViewById(R.id.btnViewCurrentTravels);
        btnViewPreviousTravel=findViewById(R.id.btnViewPreviousTravel);
        Intent setTravelPlanIntent = new Intent(this, SetTravelPlan.class);
        btnSeeTravelPlans.setOnClickListener(view -> {
            setTravelPlanIntent.removeExtra("plan");
            startActivity(setTravelPlanIntent);
        });

        btnViewUpcomingTravels.setOnClickListener(view -> {
            setTravelPlanIntent.putExtra("plan","upcoming");
            startActivity(setTravelPlanIntent);
        });

        btnViewCurrentTravels.setOnClickListener(view -> {
            setTravelPlanIntent.putExtra("plan","current");
            startActivity(setTravelPlanIntent);
        });

        btnViewPreviousTravel.setOnClickListener(view -> {
            setTravelPlanIntent.putExtra("plan","previous");
            startActivity(setTravelPlanIntent);
        });

        db.collection("travel_plans").where(Filter.or( Filter.greaterThan("departure", Timestamp.now()),
                Filter.lessThanOrEqualTo("departure",  Timestamp.now()))).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                 if (task.isComplete()) {
                for(QueryDocumentSnapshot documentSnapshot:task.getResult()) {
                    tvPreviousTravel.setText(documentSnapshot.get("destination").toString());
                    Log.d("Data from firebase", documentSnapshot.get("destination").toString());
                }
                 }
            }
        });

        SimpleDateFormat dateFormatterEntry = new SimpleDateFormat("M/dd/yyyy");
        Date currentDate = new Date(new Date().getYear(),new Date().getMonth(),new Date().getDate());
        currentDate.setHours(8);
        db.collection("travel_plans").where(Filter.or(Filter.greaterThanOrEqualTo("departure_date",currentDate),Filter.equalTo("departure_date",currentDate))).orderBy("departure_date", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isComplete()) {
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()) {
                        tvUpcomingTravel.setText(documentSnapshot.get("destination").toString());

                    }
                }
            }
        });
        db.collection("travel_plans").where(Filter.or(Filter.lessThan("departure_date",Timestamp.now()),Filter.equalTo("departure_date",Timestamp.now()))).orderBy("departure_date", Query.Direction.ASCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                 if (task.isComplete()) {
                for(QueryDocumentSnapshot documentSnapshot:task.getResult()) {
                    tvPreviousTravel.setText(documentSnapshot.get("destination").toString());

                }
                 }
            }
        });
        db.collection("travel_plans").where(Filter.or(Filter.lessThan("departure_date",Timestamp.now()),Filter.equalTo("departure_date",Timestamp.now()))).orderBy("departure_date", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isComplete()) {
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()) {
                        tvCurrentTravel.setText(documentSnapshot.get("destination").toString());

                    }
                }
            }
        });

    }
}