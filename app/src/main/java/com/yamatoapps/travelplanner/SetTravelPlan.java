package com.yamatoapps.travelplanner;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;

public class SetTravelPlan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_travel_plan);
        ListView planListView = findViewById(R.id.planListView);
        Button btnBack = findViewById(R.id.btnBack);
        TextView tvTitle = findViewById(R.id.tvTitle);
        Button btnAddPlan = findViewById(R.id.btnAddPlan);
        btnBack.setOnClickListener(view ->{
            finish();
        });
        Intent addTravelPlanIntent = new Intent(this, AddTravelPlan.class);
        btnAddPlan.setOnClickListener(view -> {
            startActivity(addTravelPlanIntent);
        });
        ArrayList<TravelModel> itemArrayList= new ArrayList<TravelModel>();
        TravelAdapter adapter = new TravelAdapter(this, itemArrayList);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Date currentDate = new Date(new Date().getYear(),new Date().getMonth(),new Date().getDate());

        if (this.getIntent().getStringExtra("plan") == null){
        db.collection("travel_plans").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                   TravelModel travelModel =  new TravelModel(document.getString("destination"),document.getDate("departure_date", DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                            ,document.getString("activity"));
                   travelModel.docId = document.getId();
                    adapter.add(travelModel);
                }
                planListView.setAdapter(adapter);
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
        }
        else{
            if (this.getIntent().getStringExtra("plan").contains("upcoming")){
                tvTitle.setText("UPCOMING TRAVELS");
                db.collection("travel_plans").where(Filter.or(Filter.greaterThan("departure_date",currentDate))).orderBy("departure_date", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            TravelModel travelModel =  new TravelModel(document.getString("destination"),document.getDate("departure_date", DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                                    ,document.getString("activity"));
                            travelModel.docId = document.getId();
                            adapter.add(travelModel);
                        }
                        planListView.setAdapter(adapter);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
            }
            else if(this.getIntent().getStringExtra("plan").contains("previous")){
                tvTitle.setText("PREVIOUS TRAVELS");
                db.collection("travel_plans").where(Filter.or(Filter.lessThan("departure_date", Timestamp.now()))).orderBy("departure_date", Query.Direction.ASCENDING).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            TravelModel travelModel =  new TravelModel(document.getString("destination"),document.getDate("departure_date", DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                                    ,document.getString("activity"));
                            travelModel.docId = document.getId();
                            adapter.add(travelModel);
                        }
                        planListView.setAdapter(adapter);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

            }else if(this.getIntent().getStringExtra("plan").contains("current")){
                tvTitle.setText("CURRENT TRAVEL");
                db.collection("travel_plans").where(Filter.or(Filter.lessThan("departure_date",Timestamp.now()),Filter.equalTo("departure_date",Timestamp.now()))).orderBy("departure_date", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            TravelModel travelModel =  new TravelModel(document.getString("destination"),document.getDate("departure_date", DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                                    ,document.getString("activity"));
                            travelModel.docId = document.getId();
                            adapter.add(travelModel);
                        }
                        planListView.setAdapter(adapter);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

            }
        }
    }
}