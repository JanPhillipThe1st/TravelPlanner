package com.yamatoapps.travelplanner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class TravelAdapter extends ArrayAdapter<TravelModel> {
    public TravelAdapter(@NonNull Context context, ArrayList<TravelModel> travelModels) {
        super(context, 0, travelModels);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TravelModel item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.plan_item, parent, false);
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        TextView tvDestination = (TextView)convertView.findViewById(R.id.tvDestination);
        TextView tvDate = (TextView)convertView.findViewById(R.id.tvDate);
        TextView tvActivity = (TextView)convertView.findViewById(R.id.tvActivity);
        Button btnManage = (Button) convertView.findViewById(R.id.btnManage);
        View finalConvertView = convertView;
        btnManage.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(finalConvertView.getContext());
                builder.setMessage("What would you like to do?");
                builder.setTitle("Manage travel to "+String.valueOf(item.destination));
                builder.setCancelable(false);
                builder.setPositiveButton("Edit", (DialogInterface.OnClickListener) (dialog, which) -> {
                    Intent editIntent = new Intent(finalConvertView.getContext(), AddTravelPlan.class);
                    editIntent.putExtra("editing","true");
                    editIntent.putExtra("docID", item.docId);
                    finalConvertView.getContext().startActivity(editIntent);
                });
                builder.setNegativeButton("Delete", (DialogInterface.OnClickListener) (dialog, which) -> {
                    db.collection("travel_plans").document(item.docId).delete();
                    Toast.makeText(getContext(), "Entry Deleted Successfully!", Toast.LENGTH_SHORT).show();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return false;
            }
        });
        tvDestination.setText("Destination: "+ String.valueOf(item.destination));
        tvDate.setText("Date: "+ String.valueOf(item.departure.toString()));
        tvActivity.setText("Activity: "+ String.valueOf(item.activity.toString()));
        return convertView;
    }
}
